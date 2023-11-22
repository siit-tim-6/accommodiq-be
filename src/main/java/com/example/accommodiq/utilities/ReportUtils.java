package com.example.accommodiq.utilities;

import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.http.HttpStatus;
import org.springframework.web.server.ResponseStatusException;

import java.util.ResourceBundle;

public class ReportUtils {

    private static final ResourceBundle bundle = ResourceBundle.getBundle("ValidationMessages", LocaleContextHolder.getLocale());

    public static void throwBadRequest(String messageKey) {
        String value = bundle.getString(messageKey);
        throw new ResponseStatusException(HttpStatus.BAD_REQUEST, value);
    }

    public static void throwNotFound(String messageKey) {
        String value = bundle.getString(messageKey);
        throw new ResponseStatusException(HttpStatus.NOT_FOUND, value);
    }
}
