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

public class CreateMultipleEntitiesTestCases  extends AbstractTestCases {

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
        assertThat(entities,
                Matchers.<CookBookEntity> hasItems(
                        Matchers.hasProperty("name", equalTo("Charqui")),
                        Matchers.hasProperty("name", equalTo("Hondashi")),
                        Matchers.hasProperty("name", equalTo("Miso")),
                        Matchers.hasProperty("name", equalTo("Nori"))
                )
        );
    }

}
