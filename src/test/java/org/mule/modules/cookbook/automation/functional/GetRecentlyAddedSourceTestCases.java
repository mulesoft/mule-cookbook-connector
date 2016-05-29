/**
 * (c) 2003-2016 MuleSoft, Inc. The software in this package is
 * published under the terms of the CPAL v1.0 license, a copy of which
 * has been included with this distribution in the LICENSE.md file.
 */
package org.mule.modules.cookbook.automation.functional;

import com.cookbook.tutorial.service.Recipe;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.mule.modules.cookbook.utils.EntityType;

import java.util.ArrayList;
import java.util.List;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.greaterThan;
import static org.hamcrest.Matchers.notNullValue;

public class GetRecentlyAddedSourceTestCases extends AbstractTestCases {

    private List<Integer> entityIds = new ArrayList<>(0);

    @Before
    public void setUp() throws Throwable {
        getDispatcher().initializeSource("getRecentlyAddedSource", new Object[] { null });
        getConnector().create(EntityType.RECIPE.name(), TestDataBuilder.getRecentlyAddedRecipeData());
        Thread.sleep(2000);
    }

    @After
    public void tearDown() throws Throwable {
        silentlyDelete(entityIds);
        getDispatcher().shutDownSource("getRecentlyAddedSource");
    }

    @Test
    public void testGetRecentlyAddedSource() {
        List<Object> sources = getDispatcher().getSourceMessages("getRecentlyAddedSource");
        assertThat(sources, notNullValue());
        assertThat(sources.size(), greaterThan(0));
        List<Recipe> recipes = (List<Recipe>) sources.get(0);
        for (Object recipe : recipes) {
            entityIds.add(((Recipe) recipe).getId());
        }
    }

}
