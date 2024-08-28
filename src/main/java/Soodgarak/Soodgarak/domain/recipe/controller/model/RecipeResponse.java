package Soodgarak.Soodgarak.domain.recipe.controller.model;

public record RecipeResponse(
        Long id,
        String menu,
        String mainImage,
        String way,
        String category
) {
    public static RecipeResponse of(
            Long id,
            String menu,
            String mainImage,
            String way,
            String category
    ) {
        return new RecipeResponse(id, menu, mainImage, way, category);
    }
}
