package com.nibado.example.microserviceversioning.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

import java.util.Locale;
import java.util.function.Supplier;

@ResponseStatus(value = HttpStatus.NOT_FOUND)
public class NotFoundException extends RuntimeException {
    public NotFoundException(String message) {
        super(message);
    }

    public static Supplier<NotFoundException> supplierForOrder(int id) {
        return () -> new NotFoundException(String.format(Locale.ROOT, "Order with id %s not found.", id));
    }
}
