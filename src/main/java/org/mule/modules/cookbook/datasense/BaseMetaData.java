/**
 * (c) 2003-2016 MuleSoft, Inc. The software in this package is
 * published under the terms of the Commercial Free Software license V.1, a copy of which
 * has been included with this distribution in the LICENSE.md file.
 */
package org.mule.modules.cookbook.datasense;

import com.cookbook.tutorial.service.Description;
import com.google.common.base.MoreObjects;
import org.mule.common.metadata.builder.DynamicObjectBuilder;
import org.mule.common.metadata.datatype.DataType;
import org.mule.modules.cookbook.CookbookConnector;
import org.mule.modules.cookbook.exception.CookbookException;
import org.mule.modules.cookbook.utils.EntityType;
import org.mule.modules.cookbook.utils.EnumUtils;

import javax.inject.Inject;

public class BaseMetaData {

    @Inject
    protected CookbookConnector connector;

    public void setConnector(final CookbookConnector connector) {
        this.connector = connector;
    }

    protected void addMetaDataField(final DynamicObjectBuilder<?> objectBuilder, final Description field) throws CookbookException {

        String dataType = field.getDataType().name();

        if(dataType.equalsIgnoreCase("LIST")) {
            if (field.getInnerType().equals("String")) {
                objectBuilder.addList(field.getName()).ofSimpleField(DataType.STRING);
            } else if (field.getInnerType().equals("Ingredient")) {
                DynamicObjectBuilder<?> innerObject = objectBuilder.addList(field.getName()).ofDynamicObject("Ingredient");
                Description ingredientDescription = connector.describeEntity(EntityType.INGREDIENT.name());
                for (Description innerField : ingredientDescription.getInnerFields()) {
                    getType(innerObject, innerField.getDataType().name(), innerField.getName());
                }
            }
        }
        else if(dataType.equalsIgnoreCase("OBJECT")){
            DynamicObjectBuilder<?> innerObject = objectBuilder.addDynamicObjectField(field.getName());
            for (Description innerField : field.getInnerFields()) {
                getType(innerObject, innerField.getDataType().name(), innerField.getName());
            }
        }
        else {
            getType(objectBuilder, field.getDataType().name(), field.getName());
        }

    }

    private void getType(final DynamicObjectBuilder<?> objectBuilder, String type, String name) {
        MoreObjects.firstNonNull(EnumUtils.getEnumFromString(Type.class, type), Type.STRING).addField(objectBuilder, name);
    }

}
