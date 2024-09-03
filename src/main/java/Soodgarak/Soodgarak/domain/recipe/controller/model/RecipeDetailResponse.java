package Soodgarak.Soodgarak.domain.recipe.controller.model;

import java.util.List;

public record RecipeDetailResponse(
        Long recipeId,
        String menu,
        String ingredient,
        String mainImage,
        String tip,
        List<ManualResponse> manualList
) {
    public static RecipeDetailResponse of(
            Long recipeId,
            String menu,
            String ingredient,
            String mainImage,
            String tip,
            List<ManualResponse> manualList
    ) {
        return new RecipeDetailResponse(recipeId, menu, ingredient, mainImage, tip, manualList);
    }
}
