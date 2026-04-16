package com.pommala.arka.provider.yaml.internal;

import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Objects;
import org.yaml.snakeyaml.LoaderOptions;
import org.yaml.snakeyaml.Yaml;
import org.yaml.snakeyaml.constructor.SafeConstructor;

/**
 * Shared YAML loading infrastructure.
 *
 * <p>Provides raw property maps from YAML content. Channel-specific mappers
 * convert these raw maps to typed resolved flow objects.
 *
 * <p>YAML safe construction (mandatory for all YAML parsing):
 * prevents deserialization attacks via {@link SafeConstructor}.
 */
public final class YamlFlowLoader {

    private YamlFlowLoader() {}

    /**
     * Parses a YAML string into a raw map. Always uses safe construction.
     *
     * @param yamlContent raw YAML content, never null
     * @return parsed map, never null
     */
    @SuppressWarnings("unchecked")
    public static Map<String, Object> parse(String yamlContent) {
        Objects.requireNonNull(yamlContent, "yamlContent must not be null");
        // YAML safe construction (mandatory): prevents deserialization attacks.
        var yaml = new Yaml(new SafeConstructor(new LoaderOptions()));
        var result = yaml.<Map<String, Object>>load(yamlContent);
        return result != null ? result : new LinkedHashMap<>();
    }
}
