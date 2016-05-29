/**
 * (c) 2003-2016 MuleSoft, Inc. The software in this package is
 * published under the terms of the CPAL v1.0 license, a copy of which
 * has been included with this distribution in the LICENSE.md file.
 */
package org.mule.modules.cookbook.automation.functional;

import com.cookbook.tutorial.service.CookBookEntity;
import com.cookbook.tutorial.service.InvalidRequestException;
import com.google.common.collect.Iterables;
import org.junit.Test;
import org.mule.streaming.PagingConfiguration;
import org.mule.util.CollectionUtils;

import java.util.Collection;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.instanceOf;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.core.IsNull.notNullValue;
import static org.junit.Assert.fail;

public class QueryEntitiesTestCases extends AbstractTestCases {

    @Test
    public void testQueryIngredients() throws Throwable {
        final Collection<CookBookEntity> entities = (Collection<CookBookEntity>) getDispatcher().runPaginatedMethod("queryEntities", new Object[] {
                "GET ALL FROM INGREDIENT",
                new PagingConfiguration(10) });
        assertThat(CollectionUtils.isEmpty(entities), is(false));
        assertThat(Iterables.getFirst(entities, null), notNullValue());
    }

    @Test
    public void testInvalidQuery() throws Throwable {
        try {
            getDispatcher().runPaginatedMethod("queryEntities", new Object[] {
                    "SOME INVALID QUERY",
                    new PagingConfiguration(10) });
            fail();
        } catch (Throwable e) {
            assertThat(e.getCause(), instanceOf(InvalidRequestException.class));
        }
    }
}
