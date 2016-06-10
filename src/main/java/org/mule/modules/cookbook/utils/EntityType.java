/**
 * (c) 2003-2016 MuleSoft, Inc. The software in this package is
 * published under the terms of the CPAL v1.0 license, a copy of which
 * has been included with this distribution in the LICENSE.md file.
 */
package org.mule.modules.cookbook.utils;

import com.cookbook.tutorial.service.CookBookEntity;
import org.jetbrains.annotations.NotNull;
import org.mule.modules.cookbook.exception.CookbookException;

public enum EntityType {
    INGREDIENT("Ingredient"),
    RECIPE("Recipe");

    private String displayName;

    EntityType(final String displayName) {
        this.displayName = displayName;
    }

    public static EntityType find(final String string) {
        return EnumUtils.getEnumFromString(EntityType.class, string);
    }

    public static CookBookEntity getClassFromType(@NotNull EntityType type) throws CookbookException {
        try {
            return (CookBookEntity) Class.forName("com.cookbook.tutorial.service." + type.displayName).newInstance();
        } catch (ClassNotFoundException | IllegalAccessException | InstantiationException e) {
            throw new CookbookException(e);
        }
    }

    @Override
    public String toString() {
        return displayName;
    }

}
