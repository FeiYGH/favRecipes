package com.talentpath.favRecipEzSpringBt.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(code= HttpStatus.INTERNAL_SERVER_ERROR, reason="Ingredient obejct cannot be null")
public class NullIngredientException extends Exception{
    public NullIngredientException(String message){super(message);}
    public NullIngredientException(String message, Throwable innerException){super(message, innerException);}
}
