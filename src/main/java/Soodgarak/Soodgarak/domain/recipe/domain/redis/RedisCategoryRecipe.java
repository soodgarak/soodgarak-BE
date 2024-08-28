package Soodgarak.Soodgarak.domain.recipe.domain.redis;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.redis.core.RedisHash;

import java.io.Serializable;

@RedisHash("Recipe_Category")
@NoArgsConstructor
@AllArgsConstructor
@Getter
public class RedisCategoryRecipe implements Serializable {
    @Id
    private Long id;
    private Long recipeId;

    public static RedisCategoryRecipe of(Long id) {
        return new RedisCategoryRecipe(id, id);
    }
}
