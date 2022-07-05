package com.favourite.recipe.service;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.bson.types.ObjectId;
import org.modelmapper.ModelMapper;
import org.modelmapper.TypeToken;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;

import com.favourite.recipe.entity.ColumnNameConstansts;
import com.favourite.recipe.entity.RecipeEntity;
import com.favourite.recipe.exception.RecipeBadRequestException;
import com.favourite.recipe.exception.RecipeInternalException;
import com.favourite.recipe.model.Recipe;
import com.favourite.recipe.model.UpdateRecipe;

@Service
public class RecipeService {

    @Autowired
    private MongoTemplate mongoTemplate;

    @Autowired
    private ModelMapper modelMapper;

    /**
     * Add Favourite Recipe
     * 
     * @param recipe Recipe
     * @return Returns Inserted Recipe with Id
     */
    public Recipe addFavouriteRecipe(Recipe recipe) {
        Query query = new Query();
        query.addCriteria(Criteria.where("recipeName").is(recipe.getRecipeName()));
        RecipeEntity recipeExits = mongoTemplate.findOne(query, RecipeEntity.class);
        if (recipeExits == null) {
            RecipeEntity recipeEntity = modelMapper.map(recipe, RecipeEntity.class);
            RecipeEntity saveRecipe = mongoTemplate.save(recipeEntity);
            return modelMapper.map(saveRecipe, Recipe.class);
        } else {
            throw new RecipeBadRequestException("E0001", "Favourite Recipe with Same Name Exists");
        }
    }


    /**
     * Delete Favourite Recipe
     * 
     * @param id Recipe Id
     */
    @Transactional
    public void deleteFavouriteRecipe(String id) {
        Query query = new Query();
        query.addCriteria(Criteria.where(ColumnNameConstansts.ID).is(new ObjectId(id)));
        mongoTemplate.findAndRemove(query, RecipeEntity.class);
    }

    /**
     * Update Favourite Recipe
     * 
     * @param id Recipe Id
     * @param recipe Recipe
     * @return Returns Updated Recipe
     */
    @Transactional
    public Recipe updateFavouriteRecipe(String id, UpdateRecipe recipe) {
        if (recipe != null) {
            Query query = new Query();
            query.addCriteria(Criteria.where(ColumnNameConstansts.ID).is(new ObjectId(id)));
            Update update = updateRecipe(recipe);
            RecipeEntity updatedRecipe = mongoTemplate.findAndModify(query, update, RecipeEntity.class);
            if (updatedRecipe == null) {
                throw new RecipeInternalException("E0002", "Error occurred while updating the Favourite Recipe");
            }
            return modelMapper.map(updatedRecipe, Recipe.class);
        }else {
            throw new RecipeBadRequestException("E0003", "Bad Request Nothing to Update");
        }
    }

    /**
     * Update Recipe used to find and update the Recipe
     * 
     * @param recipe
     * @return Returns the Update object which need to be updated
     */
    private Update updateRecipe(UpdateRecipe recipe) {
        Update update = new Update();

        if (StringUtils.hasLength(recipe.getRecipeName())) {
            update.set(ColumnNameConstansts.RECIPENAME, recipe.getRecipeName());
        }

        if (recipe.getRecipetype() != null) {
            update.set(ColumnNameConstansts.RECIPETYPE, recipe.getRecipetype());
        }

        if (recipe.getNumberOfServings() != null && recipe.getNumberOfServings() > 0) {
            update.set(ColumnNameConstansts.NUMBER_OF_SERVINGS, recipe.getNumberOfServings());
        }

        if (!CollectionUtils.isEmpty(recipe.getIngredients())) {
            update.set(ColumnNameConstansts.INGREDIENTS, recipe.getIngredients());
        }

        if (StringUtils.hasLength(recipe.getInstructions())) {
            update.set(ColumnNameConstansts.INSTRUCTIONS, recipe.getInstructions());
        }
        return update;

    }

    /**
     * Search Favourite Recipe
     * 
     * @param search
     * @return Returns the Recipe List
     */
    public List<Recipe> searchFavouriteRecipe(String search) {
        Map<String, String> reqParams = filterMaps(search);
        if (CollectionUtils.isEmpty(reqParams)) {
            throw new RecipeBadRequestException("E0004", "Bad Request Nothing to Search");
        }
        String recipeType = reqParams.get(SearchConstants.RECIPETYPE);
        String servings = reqParams.get(SearchConstants.SERVINGS);
        String ingredients = reqParams.get(SearchConstants.INGREDIENTS);
        String instructions = reqParams.get(SearchConstants.INSTRUCTIONS);
        Query query = new Query();
        if (StringUtils.hasLength(recipeType)) {
            query.addCriteria(Criteria.where(ColumnNameConstansts.RECIPETYPE).is(recipeType));
        }
        if (StringUtils.hasLength(servings)) {
            try {
                Integer servingNumber = Integer.valueOf(servings);
                query.addCriteria(Criteria.where(ColumnNameConstansts.NUMBER_OF_SERVINGS).is(servingNumber));
            } catch (NumberFormatException ex) {
                throw new RecipeBadRequestException("E0005", "Number of Servings should be number");
            }
        }
        if (StringUtils.hasLength(ingredients)) {
            query.addCriteria(
                    Criteria.where(ColumnNameConstansts.INGREDIENTS).in(Arrays.asList(ingredients.split(","))));
        }
        if (StringUtils.hasLength(instructions)) {
            query.addCriteria(
                    Criteria.where(ColumnNameConstansts.INSTRUCTIONS).regex(instructions, "i"));
        }
        List<RecipeEntity> recipeList = mongoTemplate.find(query, RecipeEntity.class);
        return modelMapper.map(recipeList, new TypeToken<List<Recipe>>() {}.getType());

    }

    /**
     * Filter Map method is used to extract the searching values
     * 
     * @param filterAttr
     * @return map object containing the key and value
     */
    private Map<String, String> filterMaps(String filterAttr) {
        Map<String, String> mapFilter = new HashMap<>();
        if (StringUtils.hasLength(filterAttr)) {
            String[] attributes = filterAttr.split(";");
            Arrays.asList(attributes).stream().forEach(attribute -> {
                String[] attributeSplit = attribute.split(":");
                mapFilter.put(attributeSplit[0], attributeSplit.length > 1 ? attributeSplit[1] : "");
            });
        }
        return mapFilter;
    }
}
