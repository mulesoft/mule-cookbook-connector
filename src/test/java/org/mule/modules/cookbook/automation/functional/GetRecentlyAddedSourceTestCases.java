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
import org.mule.modules.cookbook.CookbookConnector;
import org.mule.tools.devkit.ctf.junit.AbstractTestCase;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.fail;

public class GetRecentlyAddedSourceTestCases extends AbstractTestCase<CookbookConnector> {

    Map<String, Object> testData;
    List<Integer> recipeIds = new ArrayList<Integer>(0);

    public GetRecentlyAddedSourceTestCases() {
        super(CookbookConnector.class);
    }

    @Before
    public void setUp() throws Throwable {
        testData = TestDataBuilder.getRecentlyAddedSourceTestData();
        getDispatcher().initializeSource("getRecentlyAddedSource", new Object[] {
                null
        });
        getConnector().create((String) testData.get("type"), (Map<String, Object>) testData.get("recipe-ref"));
        Thread.sleep(2000);
    }

    @Test
    public void testGetRecentlyAddedSource() {
        try {
            List<Object> sources = getDispatcher().getSourceMessages("getRecentlyAddedSource");
            assertFalse(sources.isEmpty());
            List<Recipe> recipes = (List<Recipe>) sources.get(0);
            for (Object recipe : recipes) {
                recipeIds.add(((Recipe) recipe).getId());
            }
        } catch (Exception e) {
            fail(e.getMessage());
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
