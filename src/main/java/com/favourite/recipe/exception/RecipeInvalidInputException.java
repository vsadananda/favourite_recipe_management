/*Copyright © 2019 Robert Bosch Engineering and Business Solutions Private Limited. All Rights Reserved.

NOTICE:  All information contained herein is, and remains the property of Robert Bosch Engineering and Business Solutions Private Limited.
Dissemination of this information or reproduction of this material is strictly forbidden unless prior written permission is obtained from 
Robert Bosch Engineering and Business Solutions Private Limited.

*/
package com.favourite.recipe.exception;

public class RecipeInvalidInputException extends RecipeException
{
	private static final long serialVersionUID = 1L;
	private final String error;

	public RecipeInvalidInputException(String error, String message) {
        super(error, message);
		this.error = error;
	}

    @Override
	public String getError() {
		return error;
	}
}
