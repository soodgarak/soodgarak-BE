package Soodgarak.Soodgarak.domain.recipe.domain.redis;

import lombok.*;
import org.springframework.data.annotation.Id;
import org.springframework.data.redis.core.RedisHash;

import java.io.Serializable;

@RedisHash("Recipe_search")
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class RedisSearchRecipe implements Serializable {
    @Id
    private Long id;
    private Long recipeId;
}
