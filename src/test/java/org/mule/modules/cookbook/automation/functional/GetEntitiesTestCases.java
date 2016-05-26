package org.mule.modules.cookbook.automation.functional;

import com.cookbook.tutorial.service.CookBookEntity;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;

import com.cookbook.tutorial.service.Ingredient;
import com.cookbook.tutorial.service.Recipe;
import org.junit.Test;
import org.mule.modules.cookbook.exception.CookbookException;

import java.util.List;

public class GetEntitiesTestCases extends AbstractTestCases {

    @Test
    public void testGetAvailableEntities() throws CookbookException {
        List<CookBookEntity> entities = getConnector().getEntities();
        assertThat(entities, notNullValue());
        assertThat(entities.size(), is(2));
        assertThat(entities, hasItem(isA(Ingredient.class)));
        assertThat(entities, hasItem(isA(Recipe.class)));
    }

}
