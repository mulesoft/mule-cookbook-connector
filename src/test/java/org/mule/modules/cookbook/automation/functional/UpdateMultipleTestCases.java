/**
 * (c) 2003-2016 MuleSoft, Inc. The software in this package is
 * published under the terms of the CPAL v1.0 license, a copy of which
 * has been included with this distribution in the LICENSE.md file.
 */
package org.mule.modules.cookbook.automation.functional;

import com.cookbook.tutorial.service.CookBookEntity;
import com.cookbook.tutorial.service.Ingredient;
import com.cookbook.tutorial.service.UnitType;
import com.google.common.base.Function;
import com.google.common.base.Predicate;
import com.google.common.collect.Iterables;
import com.google.common.collect.Lists;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.mule.modules.cookbook.exception.CookbookException;

import java.util.List;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.is;

public class UpdateMultipleTestCases extends AbstractTestCases {

    private List<CookBookEntity> createdEntities;
    private List<Integer> entityIds;

    @Before
    public void setUp() throws CookbookException {
        createdEntities = getConnector().createMultiple(TestDataBuilder.createMultipleEntitiesData());
        entityIds = Lists.transform(createdEntities, ENTITY_IDS_FUNCTION);
    }

    @After
    public void tearDown() throws CookbookException {
        silentlyDelete(entityIds);
    }

    @Test
    public void testUpdateMultipleIngredients() throws CookbookException {
        // modify ingredients fields
        List<CookBookEntity> transformedEntities = Lists.newArrayList(Iterables.transform(createdEntities, new Function<CookBookEntity, CookBookEntity>() {

            @Override
            public CookBookEntity apply(final CookBookEntity input) {
                Ingredient ingredient = (Ingredient) input;
                if (input.getName().equals("Nori")) {
                    ingredient.setName("Nori sheets");
                    ingredient.setQuantity(9.0);
                } else if (input.getName().equals("Charqui")) {
                    ingredient.setQuantity(500.0);
                    ingredient.setUnit(UnitType.GRAMS);
                }
                return ingredient;
            }
        }));

        // update
        List<CookBookEntity> updatedEntities = getConnector().updateMultiple(Lists.newArrayList(transformedEntities));

        // fetch single ingredient
        Ingredient charqui = (Ingredient) Iterables.find(updatedEntities, new Predicate<CookBookEntity>() {

            @Override
            public boolean apply(final CookBookEntity input) {
                return input.getName().equals("Charqui");
            }
        });
        assertThat(charqui.getQuantity(), is(new Double("500.0")));
        assertThat(charqui.getUnit(), is(UnitType.GRAMS));

        // fetch single ingredient
        Ingredient miso = (Ingredient) Iterables.find(updatedEntities, new Predicate<CookBookEntity>() {

            @Override
            public boolean apply(final CookBookEntity input) {
                return input.getName().contains("Nori");
            }
        });
        assertThat(miso.getName(), equalTo("Nori sheets"));
        assertThat(miso.getQuantity(), is(new Double("9.0")));
    }

}
