/**
 * (c) 2003-2016 MuleSoft, Inc. The software in this package is
 * published under the terms of the CPAL v1.0 license, a copy of which
 * has been included with this distribution in the LICENSE.md file.
 */
package org.mule.modules.cookbook.automation.functional;

import com.cookbook.tutorial.service.Ingredient;
import com.cookbook.tutorial.service.NoSuchEntityException;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.mule.modules.cookbook.exception.CookbookException;
import org.mule.modules.cookbook.utils.EntityType;

import java.util.Map;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.containsString;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.instanceOf;

public class GetTestCases extends AbstractTestCases {

    private Map<String, Object> testData;
    private Integer entityId;

    @Before
    public void setUp() throws CookbookException {
        testData = TestDataBuilder.createIngredientData();
        entityId = getConnector().create(EntityType.INGREDIENT.name(), testData).getId();
    }

    @After
    public void tearDown() {
        silentlyDelete(entityId);
    }

    @Test
    public void testGetIngredient() throws CookbookException {
        final Ingredient entity = (Ingredient) getConnector().get(EntityType.INGREDIENT.name(), entityId);
        assertThat(entity.getName(), equalTo(testData.get("name")));
        assertThat(entity.getQuantity(), equalTo(Double.valueOf((String) testData.get("quantity"))));
        assertThat(entity.getUnit().name(), equalTo(testData.get("unit")));
    }

    @Test
    public void testGetNonExistentIngredient() {
        try {
            getConnector().get(EntityType.INGREDIENT.name(), -1);
        } catch (CookbookException e) {
            assertThat(e.getCause(), instanceOf(NoSuchEntityException.class));
        }
    }

    @Test
    public void testGetExistentIngredientWithInvalidType() throws CookbookException {
        try {
            getConnector().get(EntityType.RECIPE.name(), entityId);
        } catch (CookbookException e) {
            assertThat(e.getMessage(), containsString("No entity of type RECIPE was found for ID 1"));
        }
    }

}
