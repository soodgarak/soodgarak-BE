package Soodgarak.Soodgarak.domain.recipe.controller;

import Soodgarak.Soodgarak.domain.recipe.controller.model.RecipeDetailResponse;
import Soodgarak.Soodgarak.domain.recipe.controller.model.RecipeRequest;
import Soodgarak.Soodgarak.domain.recipe.controller.model.RecipeWithCountResponse;
import Soodgarak.Soodgarak.domain.recipe.domain.RequestType;
import Soodgarak.Soodgarak.domain.recipe.service.RecipeService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.Parameters;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@Tag(name = "Recipe", description = "레시피 조회 API")
@RequestMapping("/recipe")
public class RecipeController {
    private final RecipeService recipeService;

    @GetMapping
    @Operation(summary = "레시피 조회",
            description = "조건(전체, 카테고리, 검색)에 해당하는 레시피 List를 조회합니다.")
    @Parameters({
            @Parameter(
                    name = "category",
                    required = false),
            @Parameter(
                    name = "keyword",
                    required = false),
            @Parameter(
                    name = "mbti",
                    required = false),
            @Parameter(
                    name = "page",
                    required = false)
    })
    public ResponseEntity<RecipeWithCountResponse> getRecipeList(@ModelAttribute RecipeRequest recipeRequest) {
        RecipeWithCountResponse recipeResponse;

        if (recipeRequest.mbti() != null) {
            recipeResponse = recipeService.getResponse(recipeRequest.mbti(), null);
        } else if (recipeRequest.page() == 1) {
            if (recipeRequest.keyword() != null) {
                recipeResponse = recipeService.getResponse(recipeRequest.keyword(), RequestType.INIT);
            } else if (recipeRequest.category() != null) {
                recipeResponse = recipeService.getResponse(recipeRequest.category(), RequestType.INIT);
            } else {
                recipeResponse = recipeService.getResponse("", RequestType.INIT);
            }
        } else {
            if (recipeRequest.keyword() != null) {
                recipeResponse = recipeService.getResponse(recipeRequest.keyword(), RequestType.ADD);
            } else if (recipeRequest.category() != null) {
                recipeResponse = recipeService.getResponse(recipeRequest.category(), RequestType.ADD);
            } else {
                recipeResponse = recipeService.getResponse("", RequestType.ADD);
            }
        }

        return ResponseEntity.ok(recipeResponse);
    }

    @GetMapping("/{recipeId}")
    @Operation(summary = "레시피 상세 조회", description = "특정 레시피의 상세 정보를 조회합니다.")
    public ResponseEntity<RecipeDetailResponse> getRecipeDetail(@RequestParam Long recipeId) {
        return ResponseEntity.ok(recipeService.getRecipeDetail(recipeId));
    }
}
