/**
 * (c) 2003-2016 MuleSoft, Inc. The software in this package is
 * published under the terms of the CPAL v1.0 license, a copy of which
 * has been included with this distribution in the LICENSE.md file.
 */
package org.mule.modules.cookbook.automation.functional;

import java.util.Map;

public class DeleteTestCases extends AbstractTestCases {

    Map<String, Object> testData;

/**
    @Before
    public void setup() throws Exception {
        testData = TestDataBuilder.deleteTestData();
        final Map<String, Object> ingredient = (Map<String, Object>) testData.get("ingredient-ref");
        final Map<String, Object> objectMap = getConnector().create((String) testData.get("type"), ingredient);
        testData.put("id", objectMap.get("id"));
    }

    @Test
    public void testDelete() {
        try {
            getConnector().delete((Integer) testData.get("id"));
        } catch (NoSuchEntityException e) {
            fail(e.getMessage());
        } catch (SessionExpiredException e) {
            fail(e.getMessage());
        }
        // Check to confirm the object does not exist.
        try {
            getConnector().get((String) testData.get("type"), (Integer) testData.get("id"));
        } catch (InvalidEntityException e) {
            fail(e.getMessage());
        } catch (NoSuchEntityException e) {
            assertTrue(e instanceof NoSuchEntityException);
        } catch (SessionExpiredException e) {
            fail(e.getMessage());
        }
    }**/
}
