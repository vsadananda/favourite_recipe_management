package com.favourite.recipe.exception;

public class RecipeInternalException extends RecipeException {
	private static final long serialVersionUID = -2803012023737273834L;
	private final String error;

	public RecipeInternalException(String error, String message) {
        super(error, message);
		this.error = error;
	}

    @Override
	public String getError() {
		return error;
	}
}
