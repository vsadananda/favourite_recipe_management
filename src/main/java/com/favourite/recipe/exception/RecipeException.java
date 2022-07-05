package com.favourite.recipe.exception;

public class RecipeException extends RuntimeException
{
    private static final long serialVersionUID = -7031726492968709193L;

    private final String error;

    public RecipeException(String error, String message)
    {
        super(message);
        this.error = error;
    }

    public String getError()
    {
        return error;
    }
}
