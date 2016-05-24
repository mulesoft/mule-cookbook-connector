/**
 * (c) 2003-2016 MuleSoft, Inc. The software in this package is
 * published under the terms of the CPAL v1.0 license, a copy of which
 * has been included with this distribution in the LICENSE.md file.
 */
package org.mule.modules.cookbook.automation.functional;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class GetRecentlyAddedSourceTestCases extends AbstractTestCases {

    Map<String, Object> testData;
    List<Integer> recipeIds = new ArrayList<Integer>(0);

/**
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
    }**/
}
