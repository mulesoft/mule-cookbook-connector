/**
 * (c) 2003-2016 MuleSoft, Inc. The software in this package is
 * published under the terms of the CPAL v1.0 license, a copy of which
 * has been included with this distribution in the LICENSE.md file.
 */
package org.mule.modules.cookbook.automation.functional;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.instanceOf;

import com.cookbook.tutorial.service.CookBookEntity;
import com.cookbook.tutorial.service.Ingredient;
import com.cookbook.tutorial.service.InvalidEntityException;
import com.cookbook.tutorial.service.SessionExpiredException;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.mule.modules.cookbook.exception.CookbookException;
import org.mule.modules.cookbook.utils.EntityType;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Map;

public class CreateTestCases extends AbstractTestCases {

    private static final Logger logger = LoggerFactory.getLogger(CreateTestCases.class);

    private Map<String, Object> testData;
    private Integer entityId;

    @Before
    public void setUp() throws Exception {
        testData = TestDataBuilder.createTestData();
    }

    @Test
    public void testCreate() throws InvalidEntityException, SessionExpiredException {
        Map<String, Object> testEntity = (Map<String, Object>)testData.get("entity-ref");
        final CookBookEntity createdEntity = getConnector().create(EntityType.find((String)testData.get("type")), testEntity);
        entityId = createdEntity.getId();
        assertThat(createdEntity, instanceOf(Ingredient.class));
        assertThat((createdEntity).getName(), equalTo(testEntity.get("name")));
        assertThat(((Ingredient)createdEntity).getQuantity(), equalTo(Double.valueOf((String)testEntity.get("quantity"))));
    }

    @Test(expected = IllegalArgumentException.class)
    public void testCreateInvalidEntity() throws InvalidEntityException, SessionExpiredException {
        getConnector().create(EntityType.RECIPE, (Map<String, Object>)testData.get("entity-ref"));
    }

    @After
    public void tearDown() throws Exception {
        if (entityId != null) {
            try {
                getConnector().delete(entityId);
            } catch (CookbookException e) {
                logger.warn("Couldn't delete program for id: {}", entityId, e);
            }
        }
    }
}
