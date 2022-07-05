package com.favourite.recipe.model;

import java.util.ArrayList;
import java.util.List;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class UpdateRecipe {

    @Schema(description = "Unique identifier of the Recipe", example = "938082fc71ef8dbc0171f25e5b480006")
    private String recipeId;

    @Schema(description = "Recipe Name", example = "Biryani")
    private String recipeName;

    @Schema(description = "Recipe Type", example = "VEG")
    private RecipeType recipetype;

    @Schema(description = "Number of Servings", example = "4")
    private Integer numberOfServings;

    @Schema(description = "Ingredients in Dishes", example = "[\"potato\", \"tomato\"]")
    private List<String> ingredients = new ArrayList<>();

    @Schema(description = "Instructions for Dishes", example = "oven")
    private String instructions;
}
