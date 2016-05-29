/**
 * (c) 2003-2016 MuleSoft, Inc. The software in this package is
 * published under the terms of the CPAL v1.0 license, a copy of which
 * has been included with this distribution in the LICENSE.md file.
 */
package org.mule.modules.cookbook.automation.functional;

import com.cookbook.tutorial.service.NoSuchEntityException;
import org.junit.Before;
import org.junit.Test;
import org.mule.modules.cookbook.exception.CookbookException;
import org.mule.modules.cookbook.utils.EntityType;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.instanceOf;
import static org.junit.Assert.fail;

public class DeleteEntityTestCases extends AbstractTestCases {

    private Integer entityId;

    @Before
    public void setUp() throws CookbookException {
        entityId = getConnector().create(EntityType.INGREDIENT.name(), TestDataBuilder.createIngredientData()).getId();
    }

    @Test
    public void testDelete() throws CookbookException {
        getConnector().delete(entityId);
        try{
            getConnector().get(EntityType.INGREDIENT.name(), entityId);
            fail();
        } catch(CookbookException e){
            assertThat(e.getCause(), instanceOf(NoSuchEntityException.class));
        }
    }

    @Test
    public void testDeleteEntityNotFound() {
        try{
            getConnector().delete(-1);
            fail();
        } catch(CookbookException e){
            assertThat(e.getCause(), instanceOf(NoSuchEntityException.class));
        }
    }

}
