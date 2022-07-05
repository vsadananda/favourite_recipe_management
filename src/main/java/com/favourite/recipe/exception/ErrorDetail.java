package com.favourite.recipe.exception;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ErrorDetail {

	private int status;

	private String error;

	private String message;

}