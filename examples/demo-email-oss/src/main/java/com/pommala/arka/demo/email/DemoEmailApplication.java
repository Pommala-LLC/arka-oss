package com.pommala.arka.demo.email;

import com.pommala.arka.email.api.EmailSendCommand;
import com.pommala.arka.api.MessageFlowService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

/**
 * Demo application showing minimal Arka Email OSS usage.
 *
 * <p>Not published as a library artifact — demo code only.
 * Lives in examples/, never inside any starter source tree.
 */
@SpringBootApplication
public class DemoEmailApplication {

    private static final Logger log = LoggerFactory.getLogger(DemoEmailApplication.class);

    public static void main(String[] args) {
        SpringApplication.run(DemoEmailApplication.class, args);
    }

    @Bean
    CommandLineRunner sendDemo(MessageFlowService flowService) {
        return args -> {
            log.info("Sending demo email via Arka Email OSS...");

            var command = EmailSendCommand.builder("welcome-email")
                    .to("recipient@example.com")
                    .model("name", "JayaKumar")
                    .model("product", "Arka")
                    .build();

            var result = flowService.send(command);
            log.info("Result: success={}, flowKey={}", result.success(), result.flowKey());
        };
    }
}
