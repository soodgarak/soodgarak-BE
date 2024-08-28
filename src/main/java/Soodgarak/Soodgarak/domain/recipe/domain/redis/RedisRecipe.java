package Soodgarak.Soodgarak.domain.recipe.domain.redis;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.redis.core.RedisHash;

import java.io.Serializable;

@RedisHash("Recipe_All")
@NoArgsConstructor
@AllArgsConstructor
@Getter
public class RedisRecipe implements Serializable {
    @Id
    private Long id;
    private Long recipeId;

    public static RedisRecipe of(Long id) {
        return new RedisRecipe(id, id);
    }
}
