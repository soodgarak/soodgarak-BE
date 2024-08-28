package Soodgarak.Soodgarak.domain.recipe.domain.redis;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.data.annotation.Id;
import org.springframework.data.redis.core.RedisHash;

import java.io.Serializable;

@RedisHash("Recipe_All")
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class RedisRecipe implements Serializable {
    @Id
    private Long id;
    private Long recipeId;
}
