package com.pommala.arka.email.transport.javamail.internal;

import com.pommala.arka.email.model.FinalEmailMessage;
import com.pommala.arka.email.spi.EmailTransport;
import com.pommala.arka.email.support.code.EmailTransportCode;
import com.pommala.arka.email.support.exception.EmailTransportException;
import com.pommala.arka.email.support.exception.EmailTransportTimeoutException;
import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import java.net.ConnectException;
import java.net.SocketTimeoutException;
import java.net.UnknownHostException;
import java.util.Objects;
import javax.net.ssl.SSLException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.mail.MailAuthenticationException;
import org.springframework.mail.MailSendException;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;

/**
 * JavaMail SMTP transport with typed exception translation.
 *
 * <p>All low-level transport exceptions must be translated to typed
 * {@link EmailTransportException} subtypes before leaving this class.
 * Raw framework exceptions must never escape.
 *
 * <p>Transport rule: every exit returns normally or throws a typed exception.
 * Never a raw {@link RuntimeException}.
 */
public class JavaMailEmailTransport implements EmailTransport {

    private static final String LOG_PREFIX = "[arka-email-transport-javamail]";
    private static final Logger log = LoggerFactory.getLogger(JavaMailEmailTransport.class);

    private final JavaMailSender mailSender;

    public JavaMailEmailTransport(JavaMailSender mailSender) {
        this.mailSender = Objects.requireNonNull(mailSender, "mailSender must not be null");
    }

    @Override
    public void send(FinalEmailMessage message) {
        Objects.requireNonNull(message, "message must not be null");
        log.debug("{} Sending email [flowKey={}, correlationId={}]",
                LOG_PREFIX, message.flowKey(), message.correlationId());
        try {
            var mime = buildMimeMessage(message);
            mailSender.send(mime);
            log.debug("{} Email delivered [flowKey={}, correlationId={}]",
                    LOG_PREFIX, message.flowKey(), message.correlationId());
        } catch (MailAuthenticationException ex) {
            // Transport exception translation rule: all low-level exceptions translated.
            throw new EmailTransportException(EmailTransportCode.SMTP_AUTH_FAILED,
                    "SMTP authentication failed for flow '" + message.flowKey() + "'", ex);
        } catch (MailSendException ex) {
            throw translateMailSendException(ex, message.flowKey());
        } catch (MessagingException ex) {
            throw new EmailTransportException(EmailTransportCode.SMTP_SEND_FAILED,
                    "SMTP messaging exception for flow '" + message.flowKey() + "'", ex);
        } catch (Exception ex) {
            throw new EmailTransportException(EmailTransportCode.SMTP_SEND_FAILED,
                    "Unexpected transport failure for flow '" + message.flowKey() + "'", ex);
        }
    }

    private MimeMessage buildMimeMessage(FinalEmailMessage msg) throws MessagingException {
        var mime   = mailSender.createMimeMessage();
        var helper = new MimeMessageHelper(mime, !msg.attachmentPaths().isEmpty(), "UTF-8");

        helper.setFrom(msg.from());
        helper.setTo(msg.to().toArray(new String[0]));
        if (!msg.cc().isEmpty())  helper.setCc(msg.cc().toArray(new String[0]));
        if (!msg.bcc().isEmpty()) helper.setBcc(msg.bcc().toArray(new String[0]));
        helper.setSubject(msg.subject());
        helper.setText(msg.htmlBody(), true);

        if (msg.replyTo() != null && !msg.replyTo().isBlank()) {
            helper.setReplyTo(msg.replyTo());
        }

        for (var entry : msg.attachmentPaths().entrySet()) {
            helper.addAttachment(entry.getKey(), new java.io.File(entry.getValue()));
        }
        return mime;
    }

    private EmailTransportException translateMailSendException(MailSendException ex, String flowKey) {
        // Transport exception translation: walk cause chain for specific conditions.
        var cause = ex.getCause();
        while (cause != null) {
            if (cause instanceof SocketTimeoutException) {
                // EMAIL-TRN-5040: single code for connect/read/write timeouts.
                return new EmailTransportTimeoutException(EmailTransportCode.SMTP_TIMEOUT,
                        "SMTP timeout for flow '" + flowKey + "'", ex);
            }
            if (cause instanceof SSLException) {
                return new EmailTransportException(EmailTransportCode.SMTP_TLS_FAILED,
                        "SMTP TLS failure for flow '" + flowKey + "'", ex);
            }
            if (cause instanceof ConnectException || cause instanceof UnknownHostException) {
                return new EmailTransportException(EmailTransportCode.SMTP_CONNECTION_REFUSED,
                        "SMTP connection refused for flow '" + flowKey + "'", ex);
            }
            cause = cause.getCause();
        }
        return new EmailTransportException(EmailTransportCode.SMTP_SEND_FAILED,
                "SMTP send failed for flow '" + flowKey + "'", ex);
    }
}
