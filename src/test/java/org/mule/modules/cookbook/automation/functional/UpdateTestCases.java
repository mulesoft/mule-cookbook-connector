/**
 * (c) 2003-2016 MuleSoft, Inc. The software in this package is
 * published under the terms of the CPAL v1.0 license, a copy of which
 * has been included with this distribution in the LICENSE.md file.
 */
package org.mule.modules.cookbook.automation.functional;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;

import com.cookbook.tutorial.service.Ingredient;
import com.cookbook.tutorial.service.UnitType;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.mule.modules.cookbook.utils.EntityType;

import java.util.Map;

public class UpdateTestCases extends AbstractTestCases {

    private Map<String, Object> testData;

    @Before
    public void setUp() throws Exception {
        testData = TestDataBuilder.updateTestData();
    }

    @Test
    public void testUpdate() {
        Map<String, Object> entityRef = (Map<String, Object>) testData.get("entity-ref");
        Double quantity = Double.valueOf((String)entityRef.get("quantity"));
        UnitType unit = UnitType.valueOf((String)entityRef.get("unit"));

        Ingredient updated = (Ingredient)getConnector().update(EntityType.INGREDIENT, entityRef);
        assertThat(updated.getQuantity(), equalTo(quantity));
        assertThat(updated.getUnit(), equalTo(unit));

        Ingredient newCurrent = (Ingredient)getConnector().get(EntityType.INGREDIENT, 1);
        assertThat(newCurrent.getQuantity(), equalTo(quantity));
        assertThat(newCurrent.getUnit(), equalTo(unit));

    }

    @After
    public void tearDown() throws Exception {
        getConnector().update(EntityType.INGREDIENT, TestDataBuilder.updateRollbackTestData());
    }

}
