/**
 * (c) 2003-2016 MuleSoft, Inc. The software in this package is
 * published under the terms of the CPAL v1.0 license, a copy of which
 * has been included with this distribution in the LICENSE.md file.
 */
package org.mule.modules.cookbook.automation.functional;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;

import com.cookbook.tutorial.service.Recipe;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.mule.modules.cookbook.utils.EntityType;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class GetRecentlyAddedRecipesSourceTestCases extends AbstractTestCases {

    Map<String, Object> testData;
    List<Integer> recipeIds = new ArrayList<>(0);

    @Before
    public void setUp() throws Throwable {
        testData = TestDataBuilder.getRecentlyAddedTestData();
        getDispatcher().initializeSource("getRecentlyAddedSource", new Object[] { null });
        getConnector().create(EntityType.RECIPE, (Map<String, Object>) testData.get("recipe-ref"));
        Thread.sleep(2000);
    }

    @Test
    public void testGetRecentlyAddedSource() {
            List<Object> sources = getDispatcher().getSourceMessages("getRecentlyAddedSource");
            assertThat(sources.isEmpty(), is(false));
            List<Recipe> recipes = (List<Recipe>) sources.get(0);
            for (Object recipe : recipes) {
                recipeIds.add(((Recipe) recipe).getId());
            }
    }

    @After
    public void tearDown() throws Throwable {
        for (Integer id : recipeIds) {
            getConnector().delete(id);
        }
        getDispatcher().shutDownSource("getRecentlyAddedSource");
    }
}
