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

import java.util.List;

@RestController
@RequiredArgsConstructor
@Tag(name = "Recipe", description = "레시피 조회 API")
@RequestMapping("/recipe")
public class RecipeController {
    private final RecipeService recipeService;

    @GetMapping
    @Operation(summary = "레시피 조회", description = "조건에 해당하는 레시피 List를 조회합니다.")
    public ResponseEntity<List<RecipeResponse>> getRecipeList(@ModelAttribute RecipeRequest recipeRequest) {
        if (recipeRequest.page() == null) {
            if (recipeRequest.keyword() != null) {
                return ResponseEntity.ok(recipeService.getInitSearchRecipeList(recipeRequest.keyword()));
            } else if (recipeRequest.category() != null) {
                return ResponseEntity.ok(recipeService.getInitCategoryRecipeList(recipeRequest.category()));
            } else {
                return ResponseEntity.ok(recipeService.getInitAllRecipeList());
            }
        } else {
            if (recipeRequest.keyword() != null) {

            } else if (recipeRequest.category() != null) {

            } else {

            }
        }
        return ResponseEntity.ok(null);
    }
}
