package com.talentpath.favRecipEzSpringBt.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(code = HttpStatus.INTERNAL_SERVER_ERROR, reason = "direction to add is null")
public class NullDirectionException extends Exception{
    public NullDirectionException(String message){super(message);};
    public NullDirectionException(String message, Throwable innerException){super(message,innerException);};

}
