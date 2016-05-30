/**
 * (c) 2003-2016 MuleSoft, Inc. The software in this package is
 * published under the terms of the CPAL v1.0 license, a copy of which
 * has been included with this distribution in the LICENSE.md file.
 */
package org.mule.modules.cookbook.automation.functional;

import com.cookbook.tutorial.service.CookBookEntity;
import com.cookbook.tutorial.service.Ingredient;
import com.cookbook.tutorial.service.Recipe;
import org.junit.Test;
import org.mule.modules.cookbook.exception.CookbookException;

import java.util.List;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.isA;
import static org.hamcrest.Matchers.notNullValue;

public class GetTestCases extends AbstractTestCases {

    @Test
    public void testGetAvailableTypes() throws CookbookException {
        List<CookBookEntity> entities = getConnector().getEntities();
        assertThat(entities, notNullValue());
        assertThat(entities.size(), is(2));
        assertThat(entities, hasItem(isA(Ingredient.class)));
        assertThat(entities, hasItem(isA(Recipe.class)));
    }

}
