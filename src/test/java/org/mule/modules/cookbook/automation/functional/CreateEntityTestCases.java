/**
 * (c) 2003-2016 MuleSoft, Inc. The software in this package is
 * published under the terms of the CPAL v1.0 license, a copy of which
 * has been included with this distribution in the LICENSE.md file.
 */
package org.mule.modules.cookbook.automation.functional;

import com.cookbook.tutorial.service.CookBookEntity;
import com.cookbook.tutorial.service.Ingredient;
import com.cookbook.tutorial.service.InvalidEntityException;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.mule.modules.cookbook.exception.CookbookException;
import org.mule.modules.cookbook.utils.EntityType;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Map;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.containsString;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.instanceOf;

public class CreateEntityTestCases extends AbstractTestCases {

    private static final Logger logger = LoggerFactory.getLogger(CreateEntityTestCases.class);

    private Map<String, Object> testData;
    private Integer entityId;

    @Before
    public void setUp() {
        testData = TestDataBuilder.createTestData();
    }

    @Test
    public void testCreate() throws CookbookException {
        Map<String, Object> testEntity = (Map<String, Object>)testData.get("entity-ref");
        final CookBookEntity createdEntity = getConnector().create((String)testData.get("type"), testEntity);
        entityId = createdEntity.getId();
        assertThat(createdEntity, instanceOf(Ingredient.class));
        assertThat((createdEntity).getName(), equalTo(testEntity.get("name")));
        assertThat(((Ingredient)createdEntity).getQuantity(), equalTo(Double.valueOf((String)testEntity.get("quantity"))));
    }

    @Test
    public void testCreateInvalidEntityWithId() throws CookbookException {
        testData = TestDataBuilder.createWithIdTestData();
        Map<String, Object> testEntity = (Map<String, Object>)testData.get("entity-ref");
        try{
            getConnector().create((String)testData.get("type"), testEntity);
        } catch(CookbookException e){
            assertThat(e.getCause(), instanceOf(InvalidEntityException.class));
            assertThat(e.getCause().getMessage(), containsString("Cannot specify Id at creation"));
        }
    }

    @Test(expected = IllegalArgumentException.class)
    public void testCreateInvalidEntityType() throws CookbookException {
        getConnector().create(EntityType.RECIPE.name(), (Map<String, Object>)testData.get("entity-ref"));
    }

    @After
    public void tearDown() {
        if (entityId != null) {
            try {
                getConnector().delete(entityId);
            } catch (CookbookException e) {
                logger.warn("Couldn't delete program for id: {}", entityId, e);
            }
        }
    }
}
