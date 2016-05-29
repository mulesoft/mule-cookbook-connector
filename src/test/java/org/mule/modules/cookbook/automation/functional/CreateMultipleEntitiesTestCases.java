/**
 * (c) 2003-2016 MuleSoft, Inc. The software in this package is
 * published under the terms of the CPAL v1.0 license, a copy of which
 * has been included with this distribution in the LICENSE.md file.
 */
package org.mule.modules.cookbook.automation.functional;

import com.cookbook.tutorial.service.CookBookEntity;
import com.google.common.base.Function;
import com.google.common.collect.Lists;
import org.hamcrest.Matchers;
import org.junit.After;
import org.junit.Test;
import org.mule.modules.cookbook.exception.CookbookException;

import java.util.List;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.notNullValue;

public class CreateMultipleEntitiesTestCases extends AbstractTestCases {

    private List<Integer> entityIds;

    @After
    public void tearDown() {
        silentlyDelete(entityIds);
    }

    @Test
    public void testCreateMultipleIngredients() throws CookbookException {
        final List<CookBookEntity> entities = getConnector().createMultipleEntities(TestDataBuilder.createMultipleEntitiesData());
        assertThat(entities, notNullValue());
        assertThat(entities.size(), is(4));
        entityIds = Lists.transform(entities, new Function<CookBookEntity, Integer>() {

            @Override
            public Integer apply(final CookBookEntity input) {
                return input.getId();
            }
        });
        assertThat(
                entities,
                Matchers.<CookBookEntity> hasItems(Matchers.hasProperty("name", equalTo("Charqui")), Matchers.hasProperty("name", equalTo("Hondashi")),
                        Matchers.hasProperty("name", equalTo("Miso")), Matchers.hasProperty("name", equalTo("Nori"))));
    }

}
