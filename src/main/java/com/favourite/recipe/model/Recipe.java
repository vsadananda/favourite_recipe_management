package com.favourite.recipe.model;

import java.util.ArrayList;
import java.util.List;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotEmpty;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class Recipe {

    @Schema(description = "Unique identifier of the Recipe", example = "62c390fb20f9e72af4f3bac0")
    private String recipeId;

    @Schema(description = "Recipe Name", example = "Biryani", required = true, maxLength = 100, minLength = 5)
    @NotBlank(message = "Recipe Name cannot be Empty")
    private String recipeName;

    @Schema(description = "Recipe Type", example = "VEG", required = true)
    @NotBlank(message = "Recipe Type cannot be Empty")
    private RecipeType recipetype;

    @Schema(description = "Number of Servings", example = "4", required = true)
    @NotBlank(message = "Number of servings cannot be Empty")
    private Integer numberOfServings;

    @Schema(description = "Ingredients in Dishes", example = "[\"potato\", \"tomato\"]", required = true)
    @NotEmpty(message = "Ingredients in Dishes cannot be Empty")
    private List<String> ingredients = new ArrayList<>();

    @Schema(description = "Instructions for Dishes", example = "oven")
    private String instructions;

}
