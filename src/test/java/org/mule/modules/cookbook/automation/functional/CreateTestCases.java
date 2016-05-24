/**
 * (c) 2003-2016 MuleSoft, Inc. The software in this package is
 * published under the terms of the CPAL v1.0 license, a copy of which
 * has been included with this distribution in the LICENSE.md file.
 */
package org.mule.modules.cookbook.automation.functional;

import java.util.Map;

public class CreateTestCases extends AbstractTestCases {

    Map<String, Object> testData;

    /**
    @Before
    public void setup() throws Exception {
        testData = TestDataBuilder.createTestData();
    }

    @Test
    public void testCreate() {
       try {
            final Map<String, Object> ingredient = (Map<String, Object>) testData.get("ingredient-ref");
            final Map<String, Object> objectMap = getConnector().create((String) testData.get("type"), ingredient);
            testData.put("id", objectMap.get("id"));
            assertEquals(ingredient.get("name"), objectMap.get("name"));
            assertEquals(Double.valueOf((String) ingredient.get("quantity")), objectMap.get("quantity"));
        } catch (InvalidEntityException e) {
            fail(e.getMessage());
        } catch (SessionExpiredException e) {
            fail(e.getMessage());
        }
    }

    @After
    public void tearDown() throws Exception {
        getConnector().delete((Integer) testData.get("id"));
    }**/
}
