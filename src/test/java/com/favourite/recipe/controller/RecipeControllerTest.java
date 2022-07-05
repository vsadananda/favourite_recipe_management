package com.favourite.recipe.controller;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

import java.util.ArrayList;
import java.util.List;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.ActiveProfiles;

import com.favourite.recipe.exception.RecipeBadRequestException;
import com.favourite.recipe.exception.RecipeInternalException;
import com.favourite.recipe.model.Recipe;
import com.favourite.recipe.model.RecipeType;
import com.favourite.recipe.model.UpdateRecipe;

@SpringBootTest
@ActiveProfiles("test")
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
class RecipeControllerTest extends EmbeddedMongoDbSetup {


    @Autowired
    private RecipeController recipeController;

    private static Recipe savedRecipe;

    @Test
    @DisplayName("Add Recipe")
    @Order(1)
    void testAddFavouriteRecipe() {
        ResponseEntity<Recipe> response = recipeController.addFavouriteRecipe(createRecipe());
        savedRecipe = response.getBody();
        assertNotNull(savedRecipe);
        assertEquals(createRecipe().getRecipeName(), savedRecipe.getRecipeName());
    }

    @Test
    @DisplayName("Recipe with Same Recipe Name should not insert")
    @Order(2)
    void testAddFavouriteRecipeDuplicateRecipeName() {
        Assertions.assertThrows(RecipeBadRequestException.class, () -> {
            recipeController.addFavouriteRecipe(createRecipe());
        });
    }

    @Test
    @DisplayName("Search Favourite Recipe Type")
    @Order(3)
    void testSearchFavouriteRecipeType() {
        ResponseEntity<List<Recipe>> response = recipeController.searchFavouriteRecipe("recipeType:NON_VEG");
        List<Recipe> recipeList = response.getBody();
        assertNotNull(recipeList);
        assertEquals(1, recipeList.size());
    }

    @Test
    @DisplayName("Search Number of Servings")
    @Order(4)
    void testSearchNumberOfServings() {
        ResponseEntity<List<Recipe>> response = recipeController.searchFavouriteRecipe("servings:5");
        List<Recipe> recipeList = response.getBody();
        assertEquals(0, recipeList.size());
    }

    @Test
    @DisplayName("Search Number of Servings Exception")
    @Order(5)
    void testSearchNumberOfServingsException() {
        Assertions.assertThrows(RecipeBadRequestException.class, () -> {
            recipeController.searchFavouriteRecipe("servings:five");
        });
    }

    @Test
    @DisplayName("Search Number of Servings and RecipeType")
    @Order(6)
    void testSearchNumberOfServingsAndRecipeType() {
        ResponseEntity<List<Recipe>> response = recipeController.searchFavouriteRecipe("recipeType:NON_VEG;servings:4");
        List<Recipe> recipeList = response.getBody();
        assertNotNull(recipeList);
        assertEquals(1, recipeList.size());
    }

    @Test
    @DisplayName("Search Ingredients in Recipe")
    @Order(7)
    void testSearchIngredients() {
        ResponseEntity<List<Recipe>> response = recipeController.searchFavouriteRecipe("ingredients:potato,tomato");
        List<Recipe> recipeList = response.getBody();
        assertNotNull(recipeList);
        assertEquals(1, recipeList.size());
    }

    @Test
    @DisplayName("Search Instructions")
    @Order(8)
    void testSearchInstructions() {
        ResponseEntity<List<Recipe>> response = recipeController.searchFavouriteRecipe("instructions:oven");
        List<Recipe> recipeList = response.getBody();
        assertNotNull(recipeList);
        assertEquals(1, recipeList.size());
    }

    @Test
    @DisplayName("Search Nothing")
    @Order(9)
    void testSearchNotPresentException() {
        Assertions.assertThrows(RecipeBadRequestException.class, () -> {
            recipeController.searchFavouriteRecipe("");
        });
    }

    @Test
    @DisplayName("Update Recipe")
    @Order(10)
    void testUpdateFavouriteRecipe() {
        ResponseEntity<Recipe> response = recipeController.updateFavouriteRecipe(savedRecipe.getRecipeId(),
                updateRecipe());
        Recipe recipe = response.getBody();
        assertNotNull(recipe);

    }

    @Test
    @DisplayName("No Update")
    @Order(11)
    void testNoUpdateException() {
        Assertions.assertThrows(RecipeBadRequestException.class, () -> {
            recipeController.updateFavouriteRecipe(savedRecipe.getRecipeId(), null);
        });
    }

    @Test
    @DisplayName("Delete Recipe")
    @Order(12)
    void testDeleteFavouriteRecipe() {
        recipeController.deleteFavouriteRecipe(savedRecipe.getRecipeId());
        ResponseEntity<List<Recipe>> response = recipeController.searchFavouriteRecipe("recipeType:NON_VEG");
        List<Recipe> recipeList = response.getBody();
        assertNotNull(recipeList);
        assertEquals(0, recipeList.size());

    }

    @Test
    @DisplayName("Update Recipe with wrong Id")
    @Order(13)
    void testUpdateRecipeWithWrongIdException() {
        Assertions.assertThrows(RecipeInternalException.class, () -> {
            recipeController.updateFavouriteRecipe("62c390fb20f9e72af4f3bac0", updateRecipe());
        });
    }

    private Recipe createRecipe() {
        Recipe recipe = new Recipe();
        recipe.setRecipeName("BIRYANI");
        recipe.setRecipetype(RecipeType.NON_VEG);
        recipe.setNumberOfServings(4);
        recipe.setInstructions("Oven");
        List<String> ingredients = new ArrayList<>();
        ingredients.add("potato");
        ingredients.add("tomato");
        recipe.setIngredients(ingredients);
        return recipe;
    }

    private UpdateRecipe updateRecipe() {
        UpdateRecipe updateRecipe = new UpdateRecipe();
        updateRecipe.setRecipeName("DUM BIRYANI");
        updateRecipe.setNumberOfServings(5);
        updateRecipe.setRecipetype(RecipeType.VEG);
        List<String> ingredientsList = new ArrayList<>();
        ingredientsList.add("onion");
        updateRecipe.setIngredients(ingredientsList);
        updateRecipe.setInstructions("onion");
        return updateRecipe;
    }
}
