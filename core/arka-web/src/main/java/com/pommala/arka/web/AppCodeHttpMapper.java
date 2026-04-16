package com.pommala.arka.web;

import com.pommala.arka.support.code.ApplicationCode;
import org.springframework.http.HttpStatus;

/**
 * SPI for mapping internal application codes to HTTP statuses.
 *
 * <p>Channel exception handlers implement this interface to drive
 * the HTTP status selection without hard-coding switch statements
 * in the base handler.
 */
public interface AppCodeHttpMapper {
    HttpStatus map(ApplicationCode code);
}
