package Soodgarak.Soodgarak.domain.recipe.repository;

import Soodgarak.Soodgarak.domain.recipe.domain.Recipe;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface RecipeRepository extends JpaRepository<Recipe, Long> {
    Long countByMbtiStartingWith(String keyword);
    Long countByMbtiEndingWith(String keyword);
    Long countByMenuOrIngredientContaining(String menuKeyword, String ingredientKeyword);
    Long countByMbti(String keyword);
}
