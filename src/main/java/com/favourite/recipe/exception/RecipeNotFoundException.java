package com.favourite.recipe.exception;

public class RecipeNotFoundException extends RecipeException {

	private static final long serialVersionUID = 1L;
	private final String error;

	public RecipeNotFoundException(String error, String message) {
        super(error, message);
		this.error = error;
	}

    @Override
	public String getError() {
		return error;
	}

}
