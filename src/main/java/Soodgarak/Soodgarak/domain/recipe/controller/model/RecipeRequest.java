package Soodgarak.Soodgarak.domain.recipe.controller.model;

public record RecipeRequest(
        String category,
        String keyword,
        String mbti,
        Integer page
) {
    public RecipeRequest(String category, String keyword, String mbti, Integer page) {
        this.category = category != null ? category.toLowerCase() : null;
        this.keyword = keyword != null ? keyword.toLowerCase() : null;
        this.mbti = mbti != null ? keyword.toLowerCase() : null;
        this.page = page;
    }
}
