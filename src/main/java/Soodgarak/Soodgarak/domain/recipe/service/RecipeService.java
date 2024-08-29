package Soodgarak.Soodgarak.domain.recipe.service;

import Soodgarak.Soodgarak.domain.recipe.controller.model.RecipeResponse;
import Soodgarak.Soodgarak.domain.recipe.controller.model.RecipeWithCountResponse;
import Soodgarak.Soodgarak.domain.recipe.domain.Recipe;
import Soodgarak.Soodgarak.domain.recipe.domain.RecipeGroup;
import Soodgarak.Soodgarak.domain.recipe.domain.RequestType;
import Soodgarak.Soodgarak.domain.recipe.domain.redis.RedisCategoryRecipe;
import Soodgarak.Soodgarak.domain.recipe.domain.redis.RedisRecipe;
import Soodgarak.Soodgarak.domain.recipe.domain.redis.RedisSearchRecipe;
import Soodgarak.Soodgarak.domain.recipe.repository.RecipeQueryRepository;
import Soodgarak.Soodgarak.domain.recipe.repository.RecipeRepository;
import Soodgarak.Soodgarak.domain.recipe.repository.redis.CategoryRecipeRedis;
import Soodgarak.Soodgarak.domain.recipe.repository.redis.RecipeRedis;
import Soodgarak.Soodgarak.domain.recipe.repository.redis.SearchRecipeRedis;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class RecipeService {
    private final RecipeRepository recipeRepository;
    private final RecipeQueryRepository recipeQueryRepository;
    private final RecipeRedis recipeRedis;
    private final CategoryRecipeRedis categoryRecipeRedis;
    private final SearchRecipeRedis searchRecipeRedis;
    private final Long initCheckValue = 0L;
    private final Long countCheckValue = -1L;

    private void initRedis(RecipeGroup group) {
        if (group.equals(RecipeGroup.ALL) && recipeRedis.existsById(initCheckValue)) {
            recipeRedis.deleteAll();
            recipeRedis.save(RedisRecipe.of(initCheckValue, initCheckValue));
        } else if (group.equals(RecipeGroup.CATEGORY) && categoryRecipeRedis.existsById(initCheckValue)) {
            categoryRecipeRedis.deleteAll();
            categoryRecipeRedis.save(RedisCategoryRecipe.of(initCheckValue, initCheckValue));
        } else if (group.equals(RecipeGroup.SEARCH) && searchRecipeRedis.existsById(initCheckValue)) {
            searchRecipeRedis.deleteAll();
            searchRecipeRedis.save(RedisSearchRecipe.of(initCheckValue, initCheckValue));
        }
    }

    private Long countData(String keyword) {
        Long totalCount;
        if (keyword.isBlank()) {
            if (recipeRedis.existsById(countCheckValue)) {
                return recipeRedis.findById(countCheckValue).orElse(null).getRecipeId();
            }
            totalCount = recipeRepository.count();
            recipeRedis.save(RedisRecipe.of(countCheckValue, totalCount));

        } else if (keyword.equals("M") || keyword.equals("S") || keyword.equals("V")) {
            if (categoryRecipeRedis.existsById(countCheckValue)) {
                return categoryRecipeRedis.findById(countCheckValue).orElse(null).getRecipeId();
            }
            totalCount = recipeRepository.countByMbtiStartingWith(keyword);
            categoryRecipeRedis.save(RedisCategoryRecipe.of(countCheckValue, totalCount));

        } else if (keyword.equals("H") || keyword.equals("N")) {
            if (categoryRecipeRedis.existsById(countCheckValue)) {
                return categoryRecipeRedis.findById(countCheckValue).orElse(null).getRecipeId();
            }
            totalCount = recipeRepository.countByMbtiEndingWith(keyword);
            categoryRecipeRedis.save(RedisCategoryRecipe.of(countCheckValue, totalCount));
        } else {
            if (searchRecipeRedis.existsById(countCheckValue)) {
                return searchRecipeRedis.findById((countCheckValue)).orElse(null).getRecipeId();
            }
            totalCount = recipeRepository.countByMenuOrIngredientContaining(keyword, keyword);
            searchRecipeRedis.save(RedisSearchRecipe.of(countCheckValue, totalCount));
        }

        return totalCount;
    }

    private boolean checkNextData(RecipeGroup group, Long totalCount) {
        if (group.equals(RecipeGroup.ALL)) {
            if (recipeRedis.count() != totalCount) {
                return true;
            }
        } else if (group.equals(RecipeGroup.CATEGORY)) {
            if (categoryRecipeRedis.count() != totalCount) {
                return true;
            }
        } else {
            if (searchRecipeRedis.count() != totalCount) {
                return true;
            }
        }

        return false;
    }

    public RecipeWithCountResponse getResponse(String keyword, RequestType requestType) {
        Long totalCount;
        boolean hasNextData;
        List<RecipeResponse> recipeResponseList;

        if (keyword.isBlank()) {
            if (requestType.equals(RequestType.INIT)) { recipeResponseList = getInitAllRecipeList(); }
            else { recipeResponseList = addFromAllRecipeList(); }
            totalCount = countData(keyword);
            hasNextData = checkNextData(RecipeGroup.ALL, totalCount);
        } else if (keyword.equals("M") || keyword.equals("S")
                || keyword.equals("V") || keyword.equals("H") || keyword.equals("N")) {
            if (requestType.equals(RequestType.INIT)) { recipeResponseList = getInitCategoryRecipeList(keyword); }
            else { recipeResponseList = addFromCategoryRecipeList(keyword); }
            totalCount = countData(keyword);
            hasNextData = checkNextData(RecipeGroup.CATEGORY, totalCount);
        } else {
            if (requestType.equals(RequestType.INIT)) { recipeResponseList = getInitSearchRecipeList(keyword); }
            else { recipeResponseList = addFromSearchRecipeList(keyword); }
            totalCount = countData(keyword);
            hasNextData = checkNextData(RecipeGroup.SEARCH, totalCount);
        }


        return RecipeWithCountResponse.of(
                totalCount,
                hasNextData,
                recipeResponseList
        );
    }

    private List<RecipeResponse> getInitAllRecipeList() {
        initRedis(RecipeGroup.ALL);

        List<RecipeResponse> recipeResponseList = new ArrayList<>();
        List<Recipe> recipeList = recipeQueryRepository.getInitAllRecipeList();

        for (Recipe recipe : recipeList) {
            Long id = recipe.getId();
            recipeRedis.save(RedisRecipe.of(id, id));
            recipeResponseList.add(RecipeResponse.of(
                    recipe.getId(),
                    recipe.getMenu(),
                    recipe.getMainImage(),
                    recipe.getMbti(),
                    recipe.getWay(),
                    recipe.getCategory()
            ));
        }
        return recipeResponseList;
    }

    private List<RecipeResponse> getInitCategoryRecipeList(String category) {
        initRedis(RecipeGroup.CATEGORY);

        List<RecipeResponse> recipeResponseList = new ArrayList<>();
        List<Recipe> recipeList = recipeQueryRepository.getInitCategoryRecipeList(category);

        for (Recipe recipe : recipeList) {
            Long id = recipe.getId();
            categoryRecipeRedis.save(RedisCategoryRecipe.of(id, id));
            recipeResponseList.add(RecipeResponse.of(
                    recipe.getId(),
                    recipe.getMenu(),
                    recipe.getMainImage(),
                    recipe.getMbti(),
                    recipe.getWay(),
                    recipe.getCategory()
            ));
        }

        return recipeResponseList;
    }

    private List<RecipeResponse> getInitSearchRecipeList(String keyword) {
        initRedis(RecipeGroup.SEARCH);

        List<RecipeResponse> recipeResponseList = new ArrayList<>();
        List<Recipe> recipeList = recipeQueryRepository.getInitSearchRecipeList(keyword);

        for (Recipe recipe : recipeList) {
            Long id = recipe.getId();
            searchRecipeRedis.save(RedisSearchRecipe.of(id, id));
            recipeResponseList.add(RecipeResponse.of(
                    recipe.getId(),
                    recipe.getMenu(),
                    recipe.getMainImage(),
                    recipe.getMbti(),
                    recipe.getWay(),
                    recipe.getCategory()
            ));
        }

        return recipeResponseList;
    }

    private List<RecipeResponse> addFromAllRecipeList() {
        List<RecipeResponse> recipeResponseList = new ArrayList<>();
        List<Recipe> recipeList = recipeQueryRepository.addFromAllRecipeList();

        for (int index = 0; index < recipeList.size(); index++) {
            if (recipeRedis.existsById(recipeList.get(index).getId())) {
                recipeList.remove(index--);
                recipeList.add(recipeQueryRepository.addOneFromAllRecipeList());
            }
        }

        for (Recipe recipe : recipeList) {
            Long id = recipe.getId();
            recipeRedis.save(RedisRecipe.of(id, id));
            recipeResponseList.add(RecipeResponse.of(
                    recipe.getId(),
                    recipe.getMenu(),
                    recipe.getMainImage(),
                    recipe.getMbti(),
                    recipe.getWay(),
                    recipe.getCategory()
            ));
        }

        return recipeResponseList;
    }

    private List<RecipeResponse> addFromCategoryRecipeList(String category) {
        List<RecipeResponse> recipeResponseList = new ArrayList<>();
        List<Recipe> recipeList = recipeQueryRepository.addFromCategoryRecipeList(category);

        for (int index = 0; index < recipeList.size(); index++) {
            if (categoryRecipeRedis.existsById(recipeList.get(index).getId())) {
                recipeList.remove(index--);
                recipeList.add(recipeQueryRepository.addOneFromCategoryRecipeList(category));
            }
        }

        for (Recipe recipe : recipeList) {
            Long id = recipe.getId();
            categoryRecipeRedis.save(RedisCategoryRecipe.of(id, id));
            recipeResponseList.add(RecipeResponse.of(
                    recipe.getId(),
                    recipe.getMenu(),
                    recipe.getMainImage(),
                    recipe.getMbti(),
                    recipe.getWay(),
                    recipe.getCategory()
            ));
        }

        return recipeResponseList;
    }

    private List<RecipeResponse> addFromSearchRecipeList(String keyword) {
        List<RecipeResponse> recipeResponseList = new ArrayList<>();
        List<Recipe> recipeList = recipeQueryRepository.addFromSearchRecipeList(keyword);

        for (int index = 0; index < recipeList.size(); index++) {
            if (searchRecipeRedis.existsById(recipeList.get(index).getId())) {
                recipeList.remove(index--);
                recipeList.add(recipeQueryRepository.addOneFromSearchRecipeList(keyword));
            }
        }

        for (Recipe recipe : recipeList) {
            Long id = recipe.getId();
            searchRecipeRedis.save(RedisSearchRecipe.of(id, id));
            recipeResponseList.add(RecipeResponse.of(
                    recipe.getId(),
                    recipe.getMenu(),
                    recipe.getMainImage(),
                    recipe.getMbti(),
                    recipe.getWay(),
                    recipe.getCategory()
            ));
        }

        return recipeResponseList;
    }
}
