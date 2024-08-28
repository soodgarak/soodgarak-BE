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
    private String mbti;
    private String way;
    private String category;
    private String ingredient;

    private Recipe (String menu, String mainImage, String mbti, String way, String category, String ingredient) {
        this.menu = menu;
        this.mainImage = mainImage;
        this.mbti = mbti;
        this.way = way;
        this.category = category;
        this.ingredient = ingredient;
    }
}
