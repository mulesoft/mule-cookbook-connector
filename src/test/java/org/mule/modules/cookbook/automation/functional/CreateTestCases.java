/**
 * (c) 2003-2016 MuleSoft, Inc. The software in this package is
 * published under the terms of the CPAL v1.0 license, a copy of which
 * has been included with this distribution in the LICENSE.md file.
 */
package org.mule.modules.cookbook.automation.functional;

import com.cookbook.tutorial.service.CookBookEntity;
import com.cookbook.tutorial.service.Ingredient;
import com.cookbook.tutorial.service.InvalidEntityException;
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

public class CreateTestCases extends AbstractTestCases {

    private Map<String, Object> testData;
    private Integer entityId;

    @Before
    public void setUp() {
        testData = TestDataBuilder.createIngredientData();
    }

    @After
    public void tearDown() {
        silentlyDelete(entityId);
    }

    @Test
    public void testCreateIngredient() throws CookbookException {
        final CookBookEntity createdEntity = getConnector().create(EntityType.INGREDIENT.name(), testData);
        entityId = createdEntity.getId();
        assertThat(createdEntity, instanceOf(Ingredient.class));
        assertThat((createdEntity).getName(), equalTo(testData.get("name")));
        assertThat(((Ingredient) createdEntity).getQuantity(), equalTo(Double.valueOf((String) testData.get("quantity"))));
    }

    @Test
    public void testCreateIngredientWithInvalidIdParam() throws CookbookException {
        testData = TestDataBuilder.createWithIdData();
        try {
            getConnector().create(EntityType.INGREDIENT.name(), testData);
        } catch (CookbookException e) {
            assertThat(e.getCause(), instanceOf(InvalidEntityException.class));
            assertThat(e.getCause().getMessage(), containsString("Cannot specify Id at creation"));
        }
    }

    @Test(expected = IllegalArgumentException.class)
    public void testCreateInvalidEntityType() throws CookbookException {
        getConnector().create(EntityType.RECIPE.name(), testData);
    }

}
