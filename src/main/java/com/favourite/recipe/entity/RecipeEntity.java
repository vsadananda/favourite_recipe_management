package com.favourite.recipe.entity;

import java.util.ArrayList;
import java.util.List;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

import com.favourite.recipe.model.RecipeType;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Document(collection = "recipe")
public class RecipeEntity {

    @Id
    private String recipeId;

    @Field(name = ColumnNameConstansts.RECIPENAME)
    private String recipeName;

    @Field(name = ColumnNameConstansts.RECIPETYPE)
    private RecipeType recipetype;

    @Field(name = ColumnNameConstansts.NUMBER_OF_SERVINGS)
    private Integer numberOfServings;

    @Field(name = ColumnNameConstansts.INGREDIENTS)
    private List<String> ingredients = new ArrayList<>();

    @Field(name = ColumnNameConstansts.INSTRUCTIONS)
    private String instructions;
}
