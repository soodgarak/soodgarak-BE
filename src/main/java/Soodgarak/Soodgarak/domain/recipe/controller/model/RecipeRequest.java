package Soodgarak.Soodgarak.domain.recipe.controller.model;

public record RecipeRequest(
        String category,
        String keyword,
        Integer page
) {
    public RecipeRequest(String category, String keyword, Integer page) {
        this.category = category;
        this.keyword = keyword != null ? keyword.toLowerCase() : null;
        this.page = page;
    }
}
