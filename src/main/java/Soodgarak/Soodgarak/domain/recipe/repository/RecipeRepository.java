package Soodgarak.Soodgarak.domain.recipe.repository;

import Soodgarak.Soodgarak.domain.recipe.domain.Recipe;
import org.springframework.data.jpa.repository.JpaRepository;

public interface RecipeRepository extends JpaRepository<Recipe, Long> {
}
