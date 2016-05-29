/**
 * (c) 2003-2016 MuleSoft, Inc. The software in this package is
 * published under the terms of the CPAL v1.0 license, a copy of which
 * has been included with this distribution in the LICENSE.md file.
 */
package org.mule.modules.cookbook.automation.functional;

import com.cookbook.tutorial.service.Description;
import org.hamcrest.Matchers;
import org.junit.Test;
import org.mule.modules.cookbook.exception.CookbookException;
import org.mule.modules.cookbook.utils.EntityType;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.Matchers.notNullValue;

public class DescribeEntityTestCases extends AbstractTestCases {

    @Test
    public void testDescribeIngredient() throws CookbookException {
        final Description description = getConnector().describeEntity(EntityType.INGREDIENT.name());
        assertThat(description, notNullValue());
        assertThat(description.getName(), equalTo("Ingredient"));
        assertThat(description.getInnerFields(), hasSize(6));
        assertThat(description.getInnerFields(), Matchers.<Description> hasItems(Matchers.hasProperty("name", equalTo("id")), Matchers.hasProperty("name", equalTo("created")),
                Matchers.hasProperty("name", equalTo("lastModified")), Matchers.hasProperty("name", equalTo("name")), Matchers.hasProperty("name", equalTo("quantity")),
                Matchers.hasProperty("name", equalTo("unit"))));
    }

    @Test
    public void testDescribeRecipe() throws CookbookException {
        final Description description = getConnector().describeEntity(EntityType.RECIPE.name());
        assertThat(description, notNullValue());
        assertThat(description.getName(), equalTo("Recipe"));
        assertThat(description.getInnerFields(), hasSize(8));
        assertThat(description.getInnerFields(), Matchers.<Description> hasItems(Matchers.hasProperty("name", equalTo("id")), Matchers.hasProperty("name", equalTo("created")),
                Matchers.hasProperty("name", equalTo("lastModified")), Matchers.hasProperty("name", equalTo("name")), Matchers.hasProperty("name", equalTo("ingredients")),
                Matchers.hasProperty("name", equalTo("prepTime")), Matchers.hasProperty("name", equalTo("cookTime")), Matchers.hasProperty("name", equalTo("directions"))));
    }

}
