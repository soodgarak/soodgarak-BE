package Soodgarak.Soodgarak.domain.recipe.repository.redis;


import Soodgarak.Soodgarak.domain.recipe.domain.redis.RedisRecipe;
import org.springframework.data.repository.CrudRepository;

public interface RecipeRedis extends CrudRepository<RedisRecipe, Long> {
}
