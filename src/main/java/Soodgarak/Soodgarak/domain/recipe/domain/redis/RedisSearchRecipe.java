package Soodgarak.Soodgarak.domain.recipe.domain.redis;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.redis.core.RedisHash;

import java.io.Serializable;

@RedisHash(value = "Recipe_Search", timeToLive = 300)
@NoArgsConstructor
@AllArgsConstructor
@Getter
public class RedisSearchRecipe implements Serializable {
    @Id
    private Long id;
    private Long recipeId;

    public static RedisSearchRecipe of(Long id, Long recipeId) {
        return new RedisSearchRecipe(id, recipeId);
    }
}
