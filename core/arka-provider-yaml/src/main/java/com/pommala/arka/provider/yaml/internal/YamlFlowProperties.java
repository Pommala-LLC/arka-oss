package com.pommala.arka.provider.yaml.internal;

import java.util.Map;
import org.springframework.boot.context.properties.ConfigurationProperties;

/**
 * Shared YAML flow property binding.
 *
 * <p>Binds raw flow and sender maps from YAML configuration. Channel-specific
 * autoconfiguration uses these maps via {@link YamlFlowLoader} to build typed
 * resolved flow objects.
 *
 * <h3>Configuration shape:</h3>
 * <pre>{@code
 * arka:
 *   flows:
 *     my-flow-key:
 *       template: my/template
 *       subject: My Subject
 *       sender-ref: default-sender
 *   senders:
 *     default-sender:
 *       from: noreply@example.com
 *       display-name: "My App"
 * }</pre>
 */
@ConfigurationProperties(prefix = "arka")
public class YamlFlowProperties {

    private Map<String, Map<String, Object>> flows = Map.of();
    private Map<String, Map<String, Object>> senders = Map.of();

    public Map<String, Map<String, Object>> getFlows()  { return flows; }
    public Map<String, Map<String, Object>> getSenders() { return senders; }

    public void setFlows(Map<String, Map<String, Object>> flows)   { this.flows = flows; }
    public void setSenders(Map<String, Map<String, Object>> senders) { this.senders = senders; }
}
