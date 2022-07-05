package com.favourite.recipe.repository;

import org.springframework.data.mongodb.repository.MongoRepository;

import com.favourite.recipe.entity.RecipeEntity;

public interface RecipeRepository extends MongoRepository<RecipeEntity, String> {

    RecipeEntity findByRecipeNameIgnoreCase(String recipeName);

    void deleteByRecipeNameIgnoreCase(String recipeName);

}
