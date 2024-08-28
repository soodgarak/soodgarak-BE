package Soodgarak.Soodgarak.domain.recipe.repository.redis;


import Soodgarak.Soodgarak.domain.recipe.domain.redis.RedisCategoryRecipe;
import org.springframework.data.repository.CrudRepository;

public interface CategoryRecipeRedis extends CrudRepository<RedisCategoryRecipe, Long> {
}
