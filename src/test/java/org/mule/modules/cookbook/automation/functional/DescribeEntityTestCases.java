package org.mule.modules.cookbook.automation.functional;

import com.cookbook.tutorial.service.Description;
import org.junit.Test;
import org.mule.modules.cookbook.exception.CookbookException;
import org.mule.modules.cookbook.utils.EntityType;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.notNullValue;

public class DescribeEntityTestCases extends AbstractTestCases  {

    @Test
    public void testDescribeIngredient() throws CookbookException {
        final Description description = getConnector().describeEntity(EntityType.INGREDIENT);
        assertThat(description, notNullValue());
    }

}
