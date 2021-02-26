package com.talentpath.favRecipEzSpringBt.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(code= HttpStatus.INTERNAL_SERVER_ERROR, reason="none of the fields inside Instruction object can be null")
public class NullDirectionFieldException extends Exception{
    public NullDirectionFieldException(String message){super(message);}
    public NullDirectionFieldException(String message, Throwable innerException){super(message, innerException);}

}
