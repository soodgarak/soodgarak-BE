package Soodgarak.Soodgarak.domain.recipe.domain;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@NoArgsConstructor
@AllArgsConstructor
@Setter
@Getter
public class Recipe {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String menu;
    private String mainImage;

    private Recipe (String menu, String mainImage) {
        this.menu = menu;
        this.mainImage = mainImage;
    }

    public static Recipe of(String menu, String mainImage) {
        return new Recipe(menu, mainImage);
    }
}
