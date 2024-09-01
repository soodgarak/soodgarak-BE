package Soodgarak.Soodgarak.domain.recipe.repository.redis;

import Soodgarak.Soodgarak.domain.recipe.domain.redis.RedisMbtiRecipe;
import org.springframework.data.repository.CrudRepository;

public interface MbtiRecipeRedis extends CrudRepository<RedisMbtiRecipe, Long> {
}
