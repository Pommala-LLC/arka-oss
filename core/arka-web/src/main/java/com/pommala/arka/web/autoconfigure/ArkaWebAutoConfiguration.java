package com.pommala.arka.web.autoconfigure;

import org.springframework.boot.autoconfigure.AutoConfiguration;
import org.springframework.boot.autoconfigure.condition.ConditionalOnWebApplication;

/**
 * Auto-configuration for the shared Arka web scaffolding.
 *
 * <p>Provides base types: {@link com.pommala.arka.web.ErrorResponse},
 * {@link com.pommala.arka.web.AppCodeHttpMapper}.
 * Channel web modules extend these for their specific HTTP surfaces.
 */
@AutoConfiguration
@ConditionalOnWebApplication
public class ArkaWebAutoConfiguration {}
