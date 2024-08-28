package Soodgarak.Soodgarak.domain.recipe.controller;

import Soodgarak.Soodgarak.domain.recipe.controller.model.RecipeRequest;
import Soodgarak.Soodgarak.domain.recipe.controller.model.RecipeResponse;
import Soodgarak.Soodgarak.domain.recipe.service.RecipeService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.List;

@RestController
@RequiredArgsConstructor
@Tag(name = "Recipe", description = "레시피 조회 API")
@RequestMapping("/recipe")
public class RecipeController {
    private final RecipeService recipeService;

    @GetMapping
    @Operation(summary = "레시피 조회",
            description = "조건(전체, 카테고리, 검색)에 해당하는 레시피 List를 조회합니다. 각 Query Parameter는 생략이 가능합니다.")
    public ResponseEntity<List<RecipeResponse>> getRecipeList(@ModelAttribute RecipeRequest recipeRequest) {
        List<RecipeResponse> recipeResponseList = new ArrayList<>();

        if (recipeRequest.page() == null) {
            if (recipeRequest.keyword() != null) {
                recipeResponseList = recipeService.getInitSearchRecipeList(recipeRequest.keyword());
            } else if (recipeRequest.category() != null) {
                recipeResponseList = recipeService.getInitCategoryRecipeList(recipeRequest.category());
            } else {
                recipeResponseList = recipeService.getInitAllRecipeList();
            }
        } else {
            if (recipeRequest.keyword() != null) {
                recipeResponseList = recipeService.addFromSearchRecipeList(recipeRequest.keyword());
            } else if (recipeRequest.category() != null) {
                recipeResponseList = recipeService.addFromCategoryRecipeList(recipeRequest.category());
            } else {
                recipeResponseList = recipeService.addFromAllRecipeList();
            }
        }

        return ResponseEntity.ok(recipeResponseList);
    }
}
