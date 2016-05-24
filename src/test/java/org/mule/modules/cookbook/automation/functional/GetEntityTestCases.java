/**
 * (c) 2003-2016 MuleSoft, Inc. The software in this package is
 * published under the terms of the CPAL v1.0 license, a copy of which
 * has been included with this distribution in the LICENSE.md file.
 */
package org.mule.modules.cookbook.automation.functional;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.instanceOf;

import com.cookbook.tutorial.service.CookBookEntity;
import com.cookbook.tutorial.service.Ingredient;
import com.cookbook.tutorial.service.NoSuchEntityException;
import org.junit.Before;
import org.junit.Test;
import org.mule.modules.cookbook.exception.CookbookException;
import org.mule.modules.cookbook.utils.EntityType;

import java.util.Map;

public class GetEntityTestCases extends AbstractTestCases {

    private Map<String, Object> testData;

    @Before
    public void setUp() throws Exception {
        testData = TestDataBuilder.getTestData();
    }

    @Test
    public void testGetIngredient() {
        final CookBookEntity entity = getConnector().get(EntityType.find((String) testData.get("type")), (Integer) testData.get("id"));
        assertThat(entity, instanceOf(Ingredient.class));
        assertThat(entity.getName(), equalTo(testData.get("name")));
    }

    @Test
    public void testGetNonExistentIngredient() {
        try {
            getConnector().get(EntityType.INGREDIENT, -1);
        } catch(CookbookException e){
            assertThat(e.getCause(), instanceOf(NoSuchEntityException.class));
        }
    }

}
