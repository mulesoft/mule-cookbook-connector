package org.mule.modules.cookbook.utils;

public enum EntityType {
    INGREDIENT("ingredient"),
    RECIPE("recipe");

    private String displayName;

    EntityType(final String displayName){
        this.displayName = displayName;
    }

    public static EntityType find(final String string) {
        return EnumUtils.getEnumFromString(EntityType.class, string);
    }

    @Override
    public String toString() {
        return displayName;
    }

}
