package Soodgarak.Soodgarak.domain.recipe.repository.redis;


import Soodgarak.Soodgarak.domain.recipe.domain.redis.RedisSearchRecipe;
import org.springframework.data.repository.CrudRepository;

public interface SearchRecipeRedis extends CrudRepository<RedisSearchRecipe, Long> {
}
