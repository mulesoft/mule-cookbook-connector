/**
 * (c) 2003-2016 MuleSoft, Inc. The software in this package is
 * published under the terms of the CPAL v1.0 license, a copy of which
 * has been included with this distribution in the LICENSE.md file.
 */
package org.mule.modules.cookbook.automation.functional;

import com.cookbook.tutorial.service.CookBookEntity;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;

import com.cookbook.tutorial.service.NoSuchEntityException;
import org.junit.Test;
import org.mule.modules.cookbook.exception.CookbookException;

import java.util.Arrays;
import java.util.List;

public class GetMultipleTestCases extends AbstractTestCases {

    @Test
    public void testGetMultipleEntities() throws CookbookException {
        List<CookBookEntity> entities = getConnector().getMultiple(Arrays.asList(TestDataBuilder.getMultipleEntitiesIDs()));
        assertThat(entities, notNullValue());
        assertThat(entities.size(), is(5));
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
