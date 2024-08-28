package Soodgarak.Soodgarak.domain.recipe.repository;

import Soodgarak.Soodgarak.domain.recipe.domain.Recipe;
import com.querydsl.core.types.Projections;
import com.querydsl.core.types.dsl.NumberExpression;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.List;

import static Soodgarak.Soodgarak.domain.recipe.domain.QRecipe.recipe;

@Repository
@RequiredArgsConstructor
public class RecipeQueryRepository {
    private final JPAQueryFactory queryFactory;

    public List<Recipe> getInitAllRecipeList() {
        return queryFactory.select(Projections.bean(Recipe.class,
                        recipe.id,
                        recipe.menu,
                        recipe.mainImage,
                        recipe.mbti,
                        recipe.way,
                        recipe.category))
                .from(recipe)
                .orderBy(NumberExpression.random().asc())
                .limit(30)
                .fetch();
    }
}
