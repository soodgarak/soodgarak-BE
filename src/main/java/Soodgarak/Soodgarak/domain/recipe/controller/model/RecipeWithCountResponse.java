package Soodgarak.Soodgarak.domain.recipe.controller.model;

import java.util.List;

public record RecipeWithCountResponse(
        Long totalCount,
        boolean hasNextData,
        List<RecipeResponse> recipeResponse
) {
    public static RecipeWithCountResponse of(
            Long totalCount,
            boolean hasNextData,
            List<RecipeResponse> recipeResponse
    ) {
        return new RecipeWithCountResponse(totalCount, hasNextData, recipeResponse);
    }
}
