package Soodgarak.Soodgarak.domain.recipe.repository;

import Soodgarak.Soodgarak.domain.recipe.domain.Recipe;
import com.querydsl.core.types.Projections;
import com.querydsl.core.types.dsl.Expressions;
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
                .orderBy(Expressions.numberTemplate(Double.class, "RAND()").asc())
                .limit(30)
                .fetch();
    }

    public List<Recipe> getInitCategoryRecipeList(String category) {
        return queryFactory.select(Projections.bean(Recipe.class,
                        recipe.id,
                        recipe.menu,
                        recipe.mainImage,
                        recipe.mbti,
                        recipe.way,
                        recipe.category))
                .from(recipe)
                .where(recipe.mbti.like(category + "%")
                        .or(recipe.mbti.like( "%" + category)))
                .orderBy(Expressions.numberTemplate(Double.class, "RAND()").asc())
                .limit(30)
                .fetch();
    }

    public List<Recipe> getInitSearchRecipeList(String keyword) {
        return queryFactory.select(Projections.bean(Recipe.class,
                        recipe.id,
                        recipe.menu,
                        recipe.mainImage,
                        recipe.mbti,
                        recipe.way,
                        recipe.category))
                .from(recipe)
                .where(recipe.menu.contains(keyword)
                        .or(recipe.ingredient.contains(keyword)))
                .orderBy(Expressions.numberTemplate(Double.class, "RAND()").asc())
                .limit(30)
                .fetch();
    }

    public List<Recipe> addFromAllRecipeList(Long count, List<Long> list) {
        return queryFactory.select(Projections.bean(Recipe.class,
                        recipe.id,
                        recipe.menu,
                        recipe.mainImage,
                        recipe.mbti,
                        recipe.way,
                        recipe.category))
                .from(recipe)
                .where(recipe.id.notIn(list))
                .orderBy(Expressions.numberTemplate(Double.class, "RAND()").asc())
                .limit(count)
                .fetch();
    }

    public List<Recipe> addFromCategoryRecipeList(String category, Long count, List<Long> list) {
        return queryFactory.select(Projections.bean(Recipe.class,
                        recipe.id,
                        recipe.menu,
                        recipe.mainImage,
                        recipe.mbti,
                        recipe.way,
                        recipe.category))
                .from(recipe)
                .where((recipe.mbti.like(category + "%")
                        .or(recipe.mbti.like( "%" + category)))
                        .and(recipe.id.notIn(list)))
                .orderBy(Expressions.numberTemplate(Double.class, "RAND()").asc())
                .limit(count)
                .fetch();
    }

    public List<Recipe> addFromSearchRecipeList(String keyword, Long count, List<Long> list) {
        return queryFactory.select(Projections.bean(Recipe.class,
                        recipe.id,
                        recipe.menu,
                        recipe.mainImage,
                        recipe.mbti,
                        recipe.way,
                        recipe.category))
                .from(recipe)
                .where((recipe.menu.contains(keyword)
                        .or(recipe.ingredient.contains(keyword)))
                        .and(recipe.id.notIn(list)))
                .orderBy(Expressions.numberTemplate(Double.class, "RAND()").asc())
                .limit(count)
                .fetch();
    }
}
