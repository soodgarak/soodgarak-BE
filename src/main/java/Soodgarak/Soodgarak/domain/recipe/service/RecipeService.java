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
    private final Long countCheckValue = -1L;

    private void initRedis(RecipeGroup group) {
        if (group.equals(RecipeGroup.ALL)) {
            recipeRedis.deleteAll();
        } else if (group.equals(RecipeGroup.CATEGORY)) {
            categoryRecipeRedis.deleteAll();
        } else if (group.equals(RecipeGroup.SEARCH)) {
            searchRecipeRedis.deleteAll();
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

        } else if (keyword.equals("m") || keyword.equals("s") || keyword.equals("v")) {
            if (categoryRecipeRedis.existsById(countCheckValue)) {
                return categoryRecipeRedis.findById(countCheckValue).orElse(null).getRecipeId();
            }
            totalCount = recipeRepository.countByMbtiStartingWith(keyword);
            categoryRecipeRedis.save(RedisCategoryRecipe.of(countCheckValue, totalCount));

        } else if (keyword.equals("h") || keyword.equals("n")) {
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
            if (recipeRedis.count() < totalCount) {
                return true;
            }
        } else if (group.equals(RecipeGroup.CATEGORY)) {
            if (categoryRecipeRedis.count() < totalCount) {
                return true;
            }
        } else {
            if (searchRecipeRedis.count() < totalCount) {
                return true;
            }
        }

        return false;
    }

    private Long countNextData(RecipeGroup group, Long totalCount) {
        Long count;

        if (group.equals(RecipeGroup.ALL)) {
            count = totalCount - recipeRedis.count();
        } else if (group.equals(RecipeGroup.CATEGORY)) {
            count = totalCount - categoryRecipeRedis.count();
        } else {
            count = totalCount - searchRecipeRedis.count();
        }

        return count >= 10L ? 10L : count;
    }

    public RecipeWithCountResponse getResponse(String keyword, RequestType requestType) {
        Long totalCount;
        boolean hasNextData;
        List<RecipeResponse> recipeResponseList;

        if (keyword.isBlank()) {
            if (requestType.equals(RequestType.INIT)) {
                recipeResponseList = getInitAllRecipeList();
                totalCount = countData(keyword);
            }
            else {
                totalCount = countData(keyword);
                recipeResponseList = addFromAllRecipeList(totalCount);
            }
            hasNextData = checkNextData(RecipeGroup.ALL, totalCount);
        } else if (keyword.equals("m") || keyword.equals("s")
                || keyword.equals("v") || keyword.equals("h") || keyword.equals("n")) {
            if (requestType.equals(RequestType.INIT)) {
                recipeResponseList = getInitCategoryRecipeList(keyword);
                totalCount = countData(keyword);
            }
            else {
                totalCount = countData(keyword);
                recipeResponseList = addFromCategoryRecipeList(keyword, totalCount);
            }
            hasNextData = checkNextData(RecipeGroup.CATEGORY, totalCount);
        } else {
            if (requestType.equals(RequestType.INIT)) {
                recipeResponseList = getInitSearchRecipeList(keyword);
                totalCount = countData(keyword);
            }
            else {
                totalCount = countData(keyword);
                recipeResponseList = addFromSearchRecipeList(keyword, totalCount);
            }
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

    private List<RecipeResponse> addFromAllRecipeList(Long totalCount) {
        List<RecipeResponse> recipeResponseList = new ArrayList<>();
        List<Recipe> recipeList = recipeQueryRepository.addFromAllRecipeList(countNextData(RecipeGroup.ALL, totalCount));

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

    private List<RecipeResponse> addFromCategoryRecipeList(String category, Long totalCount) {
        List<RecipeResponse> recipeResponseList = new ArrayList<>();
        List<Recipe> recipeList = recipeQueryRepository.addFromCategoryRecipeList(category, countNextData(RecipeGroup.CATEGORY, totalCount));

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

    private List<RecipeResponse> addFromSearchRecipeList(String keyword, Long totalCount) {
        List<RecipeResponse> recipeResponseList = new ArrayList<>();
        List<Recipe> recipeList = recipeQueryRepository.addFromSearchRecipeList(keyword, countNextData(RecipeGroup.SEARCH, totalCount));

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
