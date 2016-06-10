/**
 * (c) 2003-2016 MuleSoft, Inc. The software in this package is
 * published under the terms of the CPAL v1.0 license, a copy of which
 * has been included with this distribution in the LICENSE.md file.
 */
package org.mule.modules.cookbook.automation.functional;

import com.cookbook.tutorial.service.Recipe;
import org.hamcrest.Matchers;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.mule.modules.cookbook.exception.CookbookException;
import org.mule.modules.cookbook.utils.EntityType;
import org.mule.tools.devkit.ctf.configuration.DeploymentProfiles;
import org.mule.tools.devkit.ctf.junit.RunOnlyOn;

import java.util.List;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.greaterThan;
import static org.hamcrest.Matchers.hasProperty;
import static org.hamcrest.Matchers.notNullValue;

public class GetRecentlyAddedTestCases extends AbstractTestCases {

    private Integer entityId;

    @Before
    public void setUp() throws CookbookException {
        entityId = getConnector().create(EntityType.RECIPE.name(), TestDataBuilder.getRecentlyAddedRecipeData()).getId();
    }

    @After
    public void tearDown() throws Exception {
        silentlyDelete(entityId);
    }

    @Test
    @RunOnlyOn(profiles = DeploymentProfiles.embedded)
    public void testGetRecentlyAdded() throws CookbookException {
        List<Recipe> recipes = getConnector().getRecentlyAdded();
        assertThat(recipes, notNullValue());
        assertThat(recipes.size(), greaterThan(0));
        assertThat(recipes, Matchers.<Recipe> hasItem(hasProperty("id", equalTo(entityId))));
    }

}
