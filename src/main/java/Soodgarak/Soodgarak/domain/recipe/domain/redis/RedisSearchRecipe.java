package Soodgarak.Soodgarak.domain.recipe.domain.redis;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.redis.core.RedisHash;

import java.io.Serializable;

@RedisHash("Recipe_search")
@NoArgsConstructor
@AllArgsConstructor
@Getter
public class RedisSearchRecipe implements Serializable {
    @Id
    private Long id;
    private Long recipeId;

    public static RedisSearchRecipe of(Long id) {
        return new RedisSearchRecipe(id, id);
    }
}
