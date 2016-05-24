/**
 * (c) 2003-2016 MuleSoft, Inc. The software in this package is
 * published under the terms of the CPAL v1.0 license, a copy of which
 * has been included with this distribution in the LICENSE.md file.
 */
package org.mule.modules.cookbook.automation.functional;

import org.junit.After;

import java.util.Map;

public class UpdateTestCases extends AbstractTestCases {

    Map<String, Object> testData;
    Map<String, Object> objectMap;

/**
    @Before
    public void setup() throws Exception {
        testData = TestDataBuilder.updateTestData();
        Map<String, Object> ingredient = (Map<String, Object>) testData.get("ingredient-ref");
        objectMap = getConnector().create((String) testData.get("type"), ingredient);
    }

    @Test
    public void testUpdate() {
        try {
            Map<String, Object> ingredient = (Map<String, Object>) testData.get("ingredient-update-ref");
            // update the objectMap.
            objectMap.put("unit", ingredient.get("unit"));
            objectMap.put("quantity", ingredient.get("quantity"));
            final Map<String, Object> updatedObjectMap = getConnector().update((String) testData.get("type"), objectMap);
            assertEquals(((UnitType) ingredient.get("unit")).name(), updatedObjectMap.get("unit"));
            assertEquals(Double.valueOf((String) ingredient.get("quantity")), updatedObjectMap.get("quantity"));
        } catch (NoSuchEntityException e) {
            fail(e.getMessage());
        } catch (SessionExpiredException e) {
            fail(e.getMessage());
        } catch (InvalidEntityException e) {
            fail(e.getMessage());
        }
    }
**/
    @After
    public void tearDown() throws Exception {
        getConnector().delete((Integer) objectMap.get("id"));
    }

}
