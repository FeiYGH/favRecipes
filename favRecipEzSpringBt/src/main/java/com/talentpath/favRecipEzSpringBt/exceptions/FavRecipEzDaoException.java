package com.talentpath.favRecipEzSpringBt.exceptions;

import com.talentpath.favRecipEzSpringBt.daos.FavRecipEzDao;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(code = HttpStatus.INTERNAL_SERVER_ERROR, reason ="Error in processing request to Postgres")
public class FavRecipEzDaoException extends Exception {
    public FavRecipEzDaoException(String message){
        super(message);
    }
    public FavRecipEzDaoException(String message, Throwable innerException){
        super(message, innerException);
    }
}
