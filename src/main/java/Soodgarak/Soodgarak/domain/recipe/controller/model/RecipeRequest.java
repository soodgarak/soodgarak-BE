package Soodgarak.Soodgarak.domain.recipe.controller.model;

import lombok.Getter;

@Getter
public record RecipeRequest(
        String category,
        String keyword,
        Integer index
) {
}
