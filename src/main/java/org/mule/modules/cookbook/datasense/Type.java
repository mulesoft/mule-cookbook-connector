/**
 * (c) 2003-2016 MuleSoft, Inc. The software in this package is
 * published under the terms of the Commercial Free Software license V.1, a copy of which
 * has been included with this distribution in the LICENSE.md file.
 */
package org.mule.modules.cookbook.datasense;

import com.cookbook.tutorial.service.UnitType;
import org.mule.common.metadata.builder.DynamicObjectBuilder;
import org.mule.common.metadata.datatype.DataType;

public enum Type {
    DATE {

        public void addField(final DynamicObjectBuilder<?> objectBuilder, String name) {
            objectBuilder.addSimpleField(name, DataType.DATE_TIME);
        }
    },
    DOUBLE {

        public void addField(final DynamicObjectBuilder<?> objectBuilder, String name) {
            objectBuilder.addSimpleField(name, DataType.DOUBLE);
        }
    },
    INTEGER {

        public void addField(final DynamicObjectBuilder<?> objectBuilder, String name) {
            objectBuilder.addSimpleField(name, DataType.INTEGER);
        }
    },
    STRING {

        public void addField(final DynamicObjectBuilder<?> objectBuilder, String name) {
            objectBuilder.addSimpleField(name, DataType.STRING);
        }
    },
    UNIT_TYPE {

        public void addField(final DynamicObjectBuilder<?> objectBuilder, String name) {
            objectBuilder.addEnumField(name, UnitType.class.getName());
        }
    };

    public abstract void addField(final DynamicObjectBuilder<?> objectBuilder, String name);


}
