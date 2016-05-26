/**
 * (c) 2003-2016 MuleSoft, Inc. The software in this package is
 * published under the terms of the CPAL v1.0 license, a copy of which
 * has been included with this distribution in the LICENSE.md file.
 */
package org.mule.modules.cookbook.automation.functional;

import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableMap;
import java.util.Map;

public class TestDataBuilder {

    private TestDataBuilder() {
        // No instances of this class allowed
    }

    public static Map<String, Object> createTestData() {
        return ImmutableMap.<String, Object> of("type", "ingredient", "entity-ref", ImmutableMap.<String, Object> of("name", "Uncooked Pasta", "unit", "GRAMS", "quantity", "8.0"));
    }

    public static Map<String, Object> createWithIdTestData() {
        return ImmutableMap.<String, Object> of("type", "ingredient", "entity-ref", ImmutableMap.<String, Object> of("id", 1, "name", "Uncooked Pasta", "unit", "GRAMS", "quantity", "8.0"));
    }

    public static Map<String, Object> deleteTestData() {
        return ImmutableMap.<String, Object> of("type", "ingredient", "entity-ref", ImmutableMap.<String, Object> of("name", "Uncooked Pasta", "unit", "GRAMS", "quantity", "8.0"));
    }

    public static Map<String, Object> getTestData() {
        return ImmutableMap.<String, Object> of("type", "ingredient", "id", 1, "name", "Extra Lean Ground Beef");
    }

    public static Integer[] getMultipleEntitiesIDs() {
        return new Integer[]{1, 2, 3, 4, 959};
    }

    public static Integer[] getMultipleInvalidEntitiesIDs() {
        return new Integer[]{1, 2, -3, -4, -959};
    }


    public static Map<String, Object> getRecentlyAddedTestData() {
        ImmutableList directions = ImmutableList. of(
                "Whip the curd till its smooth.",
                "Mix well.",
                "Add the chopped pineapple and half of the pomegranate seeds.",
                "Reserve the other half for garnishing.",
                "Chill the pineapple raita before serving.",
                "Garnish the pineapple raita with the remaining pomegranate and chopped coriander leaves."
        );

        ImmutableList ingredients = ImmutableList.<Map<String, Object>> of(
                ImmutableMap.<String, Object> of("name", "Yogurt", "unit", "CUPS", "quantity", "2.5"),
                ImmutableMap.<String, Object> of("name", "Chopped fresh sweet pineapple", "unit", "CUPS", "quantity", "2.0"),
                ImmutableMap.<String, Object> of("name", "Cayenne Pepper", "unit", "UNIT", "quantity", "1.0"),
                ImmutableMap.<String, Object> of("name", "Roasted cumin powder", "unit", "SPOONS", "quantity", "2.0"),
                ImmutableMap.<String, Object> of("name", "Sugar", "unit", "SPOONS", "quantity", "3.0"),
                ImmutableMap.<String, Object> of("name", "Pomegranate", "unit", "CUPS", "quantity", "0.5"),
                ImmutableMap.<String, Object> of("name", "Coriander Leaves", "unit", "UNIT", "quantity", "3"),
                ImmutableMap.<String, Object> of("name", "Black Salt/Rock Salt", "unit", "SPOONS", "quantity", "1"));

        ImmutableMap recipe = ImmutableMap.<String, Object> of(
                "cookTime", "55.0",
                "directions", directions,
                "ingredients", ingredients,
                "prepTime", "15.0"
        );

        return ImmutableMap.<String, Object> of(
                "type", "recipe",
                "recipe-ref", recipe);
    }

    public static Map<String, Object> queryPaginatedTestData() {
        return ImmutableMap.<String, Object> of("query", "GET ALL FROM INGREDIENT", "fetchSize", "10");
    }

    public static Map<String, Object> updateTestData() {
        return ImmutableMap.<String, Object> of("type", "ingredient", "entity-ref", ImmutableMap.<String, Object> of("id", 1, "name", "Extra Lean Ground Beef", "unit", "POUNDS", "quantity", "888.8"));
    }

    public static Map<String, Object> updateWithoutIdTestData() {
        return ImmutableMap.<String, Object> of("type", "ingredient", "entity-ref", ImmutableMap.<String, Object> of("name", "Extra Lean Ground Beef", "unit", "POUNDS", "quantity", "888.8"));
    }

    public static Map<String, Object> updateRollbackTestData() {
        return ImmutableMap.<String, Object> of("id", 1, "name", "Extra Lean Ground Beef", "unit", "GRAMS", "quantity", "1.0");
    }

}
