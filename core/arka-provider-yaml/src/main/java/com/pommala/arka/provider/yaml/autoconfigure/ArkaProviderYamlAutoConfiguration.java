package com.pommala.arka.provider.yaml.autoconfigure;

import com.pommala.arka.provider.yaml.internal.YamlFlowProperties;
import org.springframework.boot.autoconfigure.AutoConfiguration;
import org.springframework.boot.context.properties.EnableConfigurationProperties;

/**
 * Auto-configuration for YAML flow property binding.
 *
 * <p>Exposes {@link YamlFlowProperties} as a bean so channel autoconfiguration
 * classes can inject and use the raw flow and sender maps.
 */
@AutoConfiguration
@EnableConfigurationProperties(YamlFlowProperties.class)
public class ArkaProviderYamlAutoConfiguration {}
