package com.nibado.example.microserviceversioning.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(value = HttpStatus.BAD_REQUEST)
public class NoProtocolVersionException extends RuntimeException {
    public NoProtocolVersionException() {
        super("No protocol header (X-Protocol-Version) specified");
    }
}
