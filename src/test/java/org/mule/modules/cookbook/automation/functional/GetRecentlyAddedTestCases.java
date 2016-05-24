/**
 * (c) 2003-2016 MuleSoft, Inc. The software in this package is
 * published under the terms of the CPAL v1.0 license, a copy of which
 * has been included with this distribution in the LICENSE.md file.
 */
package org.mule.modules.cookbook.automation.functional;

import org.junit.After;
import org.junit.Before;

import java.util.Map;

public class GetRecentlyAddedTestCases extends AbstractTestCases {

    Map<String, Object> testData;

    @Before
    public void setup() throws Exception {
        testData = TestDataBuilder.getRecentlyAddedTestData();
    }
/**
    @Test
    @RunOnlyOn(profiles = DeploymentProfiles.embedded)
    public void testGetRecentlyAdded() {
        try {
            List<Recipe> recipes = getConnector().getRecentlyAdded();
            if (recipes.isEmpty()) {
                final Map<String, Object> objectMap = getConnector().create((String) testData.get("type"), (Map<String, Object>) testData.get("recipe-ref"));
                testData.put("id", objectMap.get("id"));
                recipes = getConnector().getRecentlyAdded();
            }
            assertTrue(recipes.size() > 0);
        } catch (Exception e) {
            fail(e.getMessage());
        }
    }
**/
    @After
    public void tearDown() throws Exception {
        if (testData.containsKey("id")) {
            getConnector().delete((Integer) testData.get("id"));
        }
    }
}
