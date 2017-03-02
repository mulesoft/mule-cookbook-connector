/**
 * (c) 2003-2016 MuleSoft, Inc. The software in this package is
 * published under the terms of the CPAL v1.0 license, a copy of which
 * has been included with this distribution in the LICENSE.md file.
 */
package org.mule.modules.cookbook.automation.functional;

import com.cookbook.tutorial.service.CookBookEntity;
import com.cookbook.tutorial.service.NoSuchEntityException;
import com.google.common.collect.Lists;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.mule.modules.cookbook.exception.CookbookException;

import java.util.Arrays;
import java.util.List;

import static com.google.common.collect.Lists.newArrayList;
import static com.google.common.collect.Lists.transform;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.instanceOf;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.notNullValue;

public class GetMultipleTestCases extends AbstractTestCases {

    private List<CookBookEntity> createdEntities;
    private List<Integer> entityIds;

    @Before
    public void setUp() throws CookbookException {
        createdEntities = getConnector().createMultiple(TestDataBuilder.createMultipleEntitiesData());
        entityIds = newArrayList(transform(createdEntities, ENTITY_IDS_FUNCTION));
    }

    @After
    public void tearDown() throws CookbookException {
        silentlyDelete(entityIds);
    }

    @Test
    public void testGetMultipleEntities() throws CookbookException {
        List<CookBookEntity> entities = getConnector().getMultiple(entityIds);
        assertThat(entities, notNullValue());
        assertThat(entities.size(), is(createdEntities.size()));
    }

    @Test
    public void testGetMultipleNonExistentEntities() throws CookbookException {
        try {
            getConnector().getMultiple(Arrays.asList(TestDataBuilder.getMultipleInvalidEntitiesIDs()));
        } catch (CookbookException e) {
            assertThat(e.getCause(), instanceOf(NoSuchEntityException.class));
        }
    }

}
