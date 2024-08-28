package Soodgarak.Soodgarak.domain.recipe.service;

import Soodgarak.Soodgarak.domain.recipe.domain.RecipeGroup;
import Soodgarak.Soodgarak.domain.recipe.domain.redis.RedisCategoryRecipe;
import Soodgarak.Soodgarak.domain.recipe.domain.redis.RedisRecipe;
import Soodgarak.Soodgarak.domain.recipe.domain.redis.RedisSearchRecipe;
import Soodgarak.Soodgarak.domain.recipe.repository.RecipeRepository;
import Soodgarak.Soodgarak.domain.recipe.repository.redis.CategoryRecipeRedis;
import Soodgarak.Soodgarak.domain.recipe.repository.redis.RecipeRedis;
import Soodgarak.Soodgarak.domain.recipe.repository.redis.SearchRecipeRedis;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class RecipeService {
    private final RecipeRepository recipeRepository;
    private final RecipeRedis recipeRedis;
    private final CategoryRecipeRedis categoryRecipeRedis;
    private final SearchRecipeRedis searchRecipeRedis;
    private final Long initCheckValue = 0L;

    private Long countData(String keyword) {
        if (keyword.isBlank()) {
            return recipeRepository.count();
        } else if (keyword.equals("M") || keyword.equals("S") || keyword.equals("V")) {
            return recipeRepository.countByMbtiStartingWith(keyword);
        } else if (keyword.equals("H") || keyword.equals("N")) {
            return recipeRepository.countByMbtiEndingWith(keyword);
        } else {
            return recipeRepository.countByMenuContaining(keyword) + recipeRepository.countByIngredientContaining(keyword);
        }
    }

    private void initRedis(RecipeGroup group) {
        if (group.equals(RecipeGroup.ALL) && recipeRedis.existsById(initCheckValue)) {
            recipeRedis.deleteAll();
            recipeRedis.save(RedisRecipe.of(initCheckValue));
        } else if (group.equals(RecipeGroup.CATEGORY) && categoryRecipeRedis.existsById(initCheckValue)) {
            categoryRecipeRedis.deleteAll();
            categoryRecipeRedis.save(RedisCategoryRecipe.of(initCheckValue));
        } else if (group.equals(RecipeGroup.SEARCH) && searchRecipeRedis.existsById(initCheckValue)) {
            searchRecipeRedis.deleteAll();
            searchRecipeRedis.save(RedisSearchRecipe.of(initCheckValue));
        }
    }

}
