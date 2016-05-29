/**
 * (c) 2003-2016 MuleSoft, Inc. The software in this package is
 * published under the terms of the CPAL v1.0 license, a copy of which
 * has been included with this distribution in the LICENSE.md file.
 */
package org.mule.modules.cookbook.automation.functional;

import com.cookbook.tutorial.service.CookBookEntity;
import com.cookbook.tutorial.service.NoSuchEntityException;
import com.google.common.base.Function;
import com.google.common.collect.Lists;
import org.junit.Before;
import org.junit.Test;
import org.mule.modules.cookbook.exception.CookbookException;

import java.util.List;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.instanceOf;
import static org.junit.Assert.fail;

public class DeleteMultipleTestCases extends AbstractTestCases {

    private List<Integer> entityIds;

    @Before
    public void setUp() throws CookbookException {
        List<CookBookEntity> createdEntities = getConnector().createMultiple(TestDataBuilder.createMultipleEntitiesData());
        entityIds = Lists.transform(createdEntities, new Function<CookBookEntity, Integer>() {

            @Override
            public Integer apply(final CookBookEntity input) {
                return input.getId();
            }
        });
    }

    @Test
    public void testDeleteMultipleIngredients() throws CookbookException {
        getConnector().deleteMultiple(entityIds);
        try {
            getConnector().getMultiple(entityIds);
            fail();
        } catch (CookbookException e) {
            assertThat(e.getCause(), instanceOf(NoSuchEntityException.class));
        }
    }

    @Test
    public void testDeleteMultipleIngredientsNotFound() throws CookbookException {
        try {
            List<Integer> copyOfIds = Lists.newArrayList(entityIds);
            copyOfIds.add(-1);
            getConnector().deleteMultiple(copyOfIds);
            fail();
        } catch (CookbookException e) {
            assertThat(e.getCause(), instanceOf(NoSuchEntityException.class));
        }

    }

}
