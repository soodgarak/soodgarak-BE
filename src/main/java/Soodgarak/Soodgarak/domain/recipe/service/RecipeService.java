package Soodgarak.Soodgarak.domain.recipe.service;

import Soodgarak.Soodgarak.domain.recipe.repository.RecipeRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class RecipeService {
    private final RecipeRepository recipeRepository;

    private Long countData(String keyword) {
        if (keyword.isBlank()) {
            return recipeRepository.count();
        } else if (keyword.equals("M") || keyword.equals("S") || keyword.equals("V")) {
            return recipeRepository.countByMbtiStartingWith(keyword);
        } else if (keyword.equals("H") || keyword.equals("N")) {
            return recipeRepository.countByMbtiEndingWith(keyword);
        } else {
            return recipeRepository.countByMenuContaining(keyword) + recipeRepository.countByIngredientContaining(keyword);
        }
    }
}
