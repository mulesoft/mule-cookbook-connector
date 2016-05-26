/**
 * (c) 2003-2016 MuleSoft, Inc. The software in this package is
 * published under the terms of the Commercial Free Software license V.1, a copy of which
 * has been included with this distribution in the LICENSE.md file.
 */
package org.mule.modules.cookbook.utils;

public class EnumUtils {

    private EnumUtils() { }

    public static <T extends Enum<T>> T getEnumFromString(final Class<T> enumType, final String string) {
        if (enumType != null && string != null) {
            for (T v : enumType.getEnumConstants()) {
                if (v.toString().equalsIgnoreCase(string)) {
                    return v;
                }
            }
        }
        return null;
    }
}
