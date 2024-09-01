package Soodgarak.Soodgarak.domain.recipe.controller;

import Soodgarak.Soodgarak.domain.recipe.controller.model.RecipeRequest;
import Soodgarak.Soodgarak.domain.recipe.controller.model.RecipeWithCountResponse;
import Soodgarak.Soodgarak.domain.recipe.domain.RequestType;
import Soodgarak.Soodgarak.domain.recipe.service.RecipeService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@Tag(name = "Recipe", description = "레시피 조회 API")
@RequestMapping("/recipe")
public class RecipeController {
    private final RecipeService recipeService;

    @GetMapping
    @Operation(summary = "레시피 조회",
            description = "조건(전체, 카테고리, 검색)에 해당하는 레시피 List를 조회합니다. 각 Query Parameter는 생략이 가능합니다.")
    public ResponseEntity<RecipeWithCountResponse> getRecipeList(@ModelAttribute RecipeRequest recipeRequest) {
        RecipeWithCountResponse recipeResponse;

        if (recipeRequest.page() == null) {
            if (recipeRequest.keyword() != null) {
                recipeResponse = recipeService.getResponse(recipeRequest.keyword(), RequestType.INIT);
            } else if (recipeRequest.category() != null) {
                recipeResponse = recipeService.getResponse(recipeRequest.category(), RequestType.INIT);
            } else if (recipeRequest.mbti() != null) {
                recipeResponse = recipeService.getResponse(recipeRequest.mbti(), RequestType.INIT);
            } else {
                recipeResponse = recipeService.getResponse("", RequestType.INIT);
            }
        } else {
            if (recipeRequest.keyword() != null) {
                recipeResponse = recipeService.getResponse(recipeRequest.keyword(), RequestType.ADD);
            } else if (recipeRequest.category() != null) {
                recipeResponse = recipeService.getResponse(recipeRequest.category(), RequestType.ADD);
            } else if (recipeRequest.mbti() != null) {
                recipeResponse = recipeService.getResponse(recipeRequest.mbti(), RequestType.ADD);
            } else {
                recipeResponse = recipeService.getResponse("", RequestType.ADD);
            }
        }

        return ResponseEntity.ok(recipeResponse);
    }
}
