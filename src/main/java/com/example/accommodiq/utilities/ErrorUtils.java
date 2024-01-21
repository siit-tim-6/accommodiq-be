package com.example.accommodiq.utilities;

import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.http.HttpStatus;
import org.springframework.web.server.ResponseStatusException;

import java.util.ResourceBundle;

public class ErrorUtils {

    private static final ResourceBundle bundle = ResourceBundle.getBundle("ValidationMessages", LocaleContextHolder.getLocale());

    public static ResponseStatusException generateBadRequest(String messageKey) {
        String value = bundle.getString(messageKey);
        return new ResponseStatusException(HttpStatus.BAD_REQUEST, value);
    }

    public static ResponseStatusException generateNotFound(String messageKey) {
        String value = bundle.getString(messageKey);
        return new ResponseStatusException(HttpStatus.NOT_FOUND, value);
    }

    public static ResponseStatusException generateException(HttpStatus status, String messageKey) {
        String value = bundle.getString(messageKey);
        return new ResponseStatusException(status, value);
    }
}
