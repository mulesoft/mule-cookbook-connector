/**
 * (c) 2003-2016 MuleSoft, Inc. The software in this package is
 * published under the terms of the CPAL v1.0 license, a copy of which
 * has been included with this distribution in the LICENSE.md file.
 */
package org.mule.modules.cookbook.automation.functional;

import com.cookbook.tutorial.service.CookBookEntity;
import com.google.common.collect.Lists;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.mule.modules.cookbook.exception.CookbookException;
import org.mule.modules.cookbook.utils.EntityType;

import java.util.List;
import java.util.Map;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;

public class TransformToMapTestCases extends AbstractTestCases {

    private List<Integer> entityIds = Lists.newArrayList();
    private CookBookEntity createdIngredient;
    private CookBookEntity createdRecipe;

    @Before
    public void setUp() throws CookbookException {
        createdIngredient = getConnector().create(EntityType.INGREDIENT.name(), TestDataBuilder.createIngredientData());
        createdRecipe = getConnector().create(EntityType.RECIPE.name(), TestDataBuilder.getRecentlyAddedRecipeData());
        entityIds.add(createdIngredient.getId());
        entityIds.add(createdRecipe.getId());
    }

    @After
    public void tearDown() throws CookbookException {
        silentlyDelete(entityIds);
    }

    @Test
    public void testTransformIngredient() throws CookbookException {
        Map<String, Object> ingredient = getConnector().transformToMap(createdIngredient);
        assertThat(ingredient, notNullValue());
        assertThat(ingredient.size(), is(6));
        assertThat(ingredient.keySet(), containsInAnyOrder(TestDataBuilder.INGREDIENT_FIELDS.toArray()));
    }

    @Test
    public void testTransformRecipe() throws CookbookException {
        Map<String, Object> recipe = getConnector().transformToMap(createdRecipe);
        assertThat(recipe, notNullValue());
        assertThat(recipe.size(), is(8));
        assertThat(recipe.keySet(), containsInAnyOrder(TestDataBuilder.RECIPE_FIELDS.toArray()));
    }

}
