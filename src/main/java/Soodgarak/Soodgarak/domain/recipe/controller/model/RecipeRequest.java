package Soodgarak.Soodgarak.domain.recipe.controller.model;

public record RecipeRequest(
        Long recipeId,
        String category,
        String keyword,
        Integer index
) {
}
