package Soodgarak.Soodgarak.domain.recipe.domain.redis;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.redis.core.RedisHash;

import java.io.Serializable;

@RedisHash(value = "Recipe_Category", timeToLive = 300)
@NoArgsConstructor
@AllArgsConstructor
@Getter
public class RedisCategoryRecipe implements Serializable {
    @Id
    private Long id;
    private Long recipeId;

    public static RedisCategoryRecipe of(Long id, Long recipeId) {
        return new RedisCategoryRecipe(id, recipeId);
    }
}
