package Soodgarak.Soodgarak.domain.recipe.controller.model;

public record RecipeRequest(
        String category,
        String keyword,
        String mbti,
        Integer page
) {
    public RecipeRequest(String category, String keyword, String mbti, Integer page) {
        this.category = category;
        this.keyword = keyword != null ? keyword.toLowerCase() : null;
        this.mbti = mbti;
        this.page = page;
    }
}
