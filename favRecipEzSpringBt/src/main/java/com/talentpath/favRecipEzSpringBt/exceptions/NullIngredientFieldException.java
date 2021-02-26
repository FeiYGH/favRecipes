package com.talentpath.favRecipEzSpringBt.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(code= HttpStatus.INTERNAL_SERVER_ERROR, reason="None of the fields in Ingredient object can be null.")
public class NullIngredientFieldException extends Exception{
    public NullIngredientFieldException(String message){super(message);}
    public NullIngredientFieldException(String message, Throwable innerException){super(message, innerException);}

}
