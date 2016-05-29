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
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.instanceOf;
import static org.junit.Assert.*;

public class UpdateEntityTestCases extends AbstractTestCases {

    private Integer entityId;

    @Before
    public void setUp() throws CookbookException {
        entityId = getConnector().create(EntityType.INGREDIENT.name(), TestDataBuilder.createIngredientData()).getId();
    }

    @After
    public void tearDown() throws CookbookException {
        silentlyDelete(entityId);
    }

    @Test
    public void testUpdate() throws CookbookException {
        Map<String, Object> testData = TestDataBuilder.updateIngredientData();

        Ingredient updated = (Ingredient)getConnector().update(EntityType.INGREDIENT.name(), testData);
        assertThat(updated.getQuantity(), equalTo(Double.valueOf((String)testData.get("quantity"))));
        assertThat(updated.getUnit().name(), equalTo(testData.get("unit")));

        // Double check
        Ingredient current = (Ingredient)getConnector().get(EntityType.INGREDIENT.name(), 1);
        assertThat(current.getQuantity(),equalTo(Double.valueOf((String)testData.get("quantity"))));
        assertThat(current.getUnit().name(), equalTo(testData.get("unit")));
    }

    @Test
    public void testUpdateWithoutId() throws CookbookException {
        try{
            getConnector().update(EntityType.INGREDIENT.name(), TestDataBuilder.updateIngredientWithoutIdData());
            fail();
        } catch(CookbookException e){
            assertThat(e.getCause(), instanceOf(NoSuchEntityException.class));
        }
    }

}
