package com.favourite.recipe.controller;

import java.util.List;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.favourite.recipe.RestURIConstants;
import com.favourite.recipe.exception.ErrorDetail;
import com.favourite.recipe.model.Recipe;
import com.favourite.recipe.model.UpdateRecipe;
import com.favourite.recipe.service.RecipeService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.ExampleObject;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;

@RestController
public class RecipeController extends BaseController {

    @Autowired
    private RecipeService recipeService;

    /*
     * ============================== ADD RECIPE ============================================
     */

    @Operation(summary = "Add Recipe", description = "Add Favourite Recipe", tags = { "Manage Favourite Recipe" })
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "CREATED", content = @Content(schema = @Schema(implementation = Recipe.class), mediaType = MediaType.APPLICATION_JSON_VALUE)),
            @ApiResponse(responseCode = "400", description = "Bad Request [E0001]", content = @Content(schema = @Schema(implementation = ErrorDetail.class), mediaType = MediaType.APPLICATION_JSON_VALUE)) })
    @PostMapping(RestURIConstants.RECIPE)

    public ResponseEntity<Recipe> addFavouriteRecipe(
            @Parameter(name = "Recipe", description = "Recipe Details", required = true) @RequestBody @Valid Recipe recipe) {
        return new ResponseEntity<>(recipeService.addFavouriteRecipe(recipe), HttpStatus.CREATED);
    }

    /*
     * ============================= DELETE RECIPE =======================================
     */

    @Operation(summary = "Delete Recipe By Id", description = "Delete Favourite Recipe By Id", tags = {
            "Manage Favourite Recipe" })
    @ApiResponses(value = { @ApiResponse(responseCode = "200", description = "OK") })
    @DeleteMapping(RestURIConstants.RECIPE + RestURIConstants.ID)

    public ResponseEntity<Void> deleteFavouriteRecipe(
            @Parameter(name = "id", description = "Recipe Details Id", required = true, example = "62c390fb20f9e72af4f3bac0") @PathVariable(required = true) String id) {
        recipeService.deleteFavouriteRecipe(id);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    /*
     * ============================= UPDATE RECIPE ============================================
     */

    @Operation(summary = "Update Recipe", description = "Update Favourite Recipe", tags = { "Manage Favourite Recipe" })
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "OK", content = @Content(schema = @Schema(implementation = Recipe.class), mediaType = MediaType.APPLICATION_JSON_VALUE)),
            @ApiResponse(responseCode = "500", description = "Internal Exception [E0002]", content = @Content(schema = @Schema(implementation = ErrorDetail.class), mediaType = MediaType.APPLICATION_JSON_VALUE)),
            @ApiResponse(responseCode = "400", description = "Bad Request [E0003]", content = @Content(schema = @Schema(implementation = ErrorDetail.class), mediaType = MediaType.APPLICATION_JSON_VALUE)) })
    @PutMapping(RestURIConstants.RECIPE + RestURIConstants.ID)

    public ResponseEntity<Recipe> updateFavouriteRecipe(
            @Parameter(name = "id", description = "Recipe Details Id", required = true, example = "62c390fb20f9e72af4f3bac0") @PathVariable(required = true) String id,
            @Parameter(name = "Recipe", description = "Recipe Details", required = true) @RequestBody UpdateRecipe recipe) {
        return new ResponseEntity<>(recipeService.updateFavouriteRecipe(id, recipe), HttpStatus.OK);
    }

    /*
     * ==============================SEARCH RECIPE =========================================
     */

    @Operation(summary = "Search Recipe", description = "Search Favourite Recipe", tags = { "Manage Favourite Recipe" })
    @ApiResponses(value = { @ApiResponse(responseCode = "200", description = "OK"),
            @ApiResponse(responseCode = "400", description = "Bad Request [E0004]", content = @Content(schema = @Schema(implementation = ErrorDetail.class), mediaType = MediaType.APPLICATION_JSON_VALUE)) })
    @GetMapping(RestURIConstants.RECIPE)

    public ResponseEntity<List<Recipe>> searchFavouriteRecipe(@Parameter(examples = {
            @ExampleObject(name = "recipeType", value = "VEG"),
            @ExampleObject(name = "servings", value = "servings:4;"),
            @ExampleObject(name = "ingredients", value = "ingredients:potato,tomato"),
            @ExampleObject(name = "instruction", value = "instruction:salmon"),
            @ExampleObject(name = "recipeType,servings", value = "recipeType:VEG;servings:4"),
            @ExampleObject(name = "recipeType,servings,ingredients", value = "recipeType:VEG;servings:4,ingredients:potato,tomato"),
            @ExampleObject(name = "recipeType,servings,ingredients,instruction", value = "recipeType:VEG;servings:4,ingredients:potato,tomato;instruction:salmon"), }) 
    @RequestParam(value = RestURIConstants.SEARCH, required = true) String search) {

        return new ResponseEntity<>(recipeService.searchFavouriteRecipe(search), HttpStatus.OK);
    }
}
