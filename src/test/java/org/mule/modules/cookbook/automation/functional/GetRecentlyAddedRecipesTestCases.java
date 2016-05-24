/**
 * (c) 2003-2016 MuleSoft, Inc. The software in this package is
 * published under the terms of the CPAL v1.0 license, a copy of which
 * has been included with this distribution in the LICENSE.md file.
 */
package org.mule.modules.cookbook.automation.functional;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;

import com.cookbook.tutorial.service.Recipe;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.mule.modules.cookbook.utils.EntityType;
import org.mule.tools.devkit.ctf.configuration.DeploymentProfiles;
import org.mule.tools.devkit.ctf.junit.RunOnlyOn;

import java.util.List;
import java.util.Map;

public class GetRecentlyAddedRecipesTestCases extends AbstractTestCases {

    private Map<String, Object> testData;
    private Integer recipeId;

    @Before
    public void setUp() throws Exception {
        testData = TestDataBuilder.getRecentlyAddedTestData();
    }

    @Test
    @RunOnlyOn(profiles = DeploymentProfiles.embedded)
    public void testGetRecentlyAdded() {
        List<Recipe> recipes = getConnector().getRecentlyAdded();
        assertThat(recipes, notNullValue());
        if(recipes.isEmpty()){
            getConnector().create(EntityType.RECIPE, (Map<String, Object>) testData.get("recipe-ref"));
            recipes = getConnector().getRecentlyAdded();
            assertThat(recipes, notNullValue());
            assertThat(recipes.size(), is(1));
            recipeId = recipes.get(0).getId();
        }
        assertThat(recipes.size(), greaterThan(0));
    }

    @After
    public void tearDown() throws Exception {
        if (recipeId != null) {
            getConnector().delete(recipeId);
        }
    }
}
