package Soodgarak.Soodgarak.domain.recipe.domain;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import lombok.*;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Manual {
    @Id
    private Long manualId;
    private Long recipeId;
    private String manual;
    private String manualImgUrl;

    public static Manual of(Long manualId, Long recipeId, String manual, String manualImgUrl) {
        return new Manual(manualId, recipeId, manual, manualImgUrl);
    }
}
