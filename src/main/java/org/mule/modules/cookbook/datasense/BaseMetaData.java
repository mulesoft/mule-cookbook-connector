/**
 * (c) 2003-2016 MuleSoft, Inc. The software in this package is
 * published under the terms of the CPAL v1.0 license, a copy of which
 * has been included with this distribution in the LICENSE.md file.
 */
/**
 * (c) 2003-2016 MuleSoft, Inc. The software in this package is
 * published under the terms of the Commercial Free Software license V.1, a copy of which
 * has been included with this distribution in the LICENSE.md file.
 */
package org.mule.modules.cookbook.datasense;

import com.cookbook.tutorial.service.CookBookEntity;
import com.cookbook.tutorial.service.Description;
import com.google.common.base.MoreObjects;
import com.google.common.base.Predicate;
import com.google.common.collect.Iterables;
import org.jetbrains.annotations.NotNull;
import org.mule.common.metadata.DefaultMetaData;
import org.mule.common.metadata.DefaultMetaDataKey;
import org.mule.common.metadata.MetaData;
import org.mule.common.metadata.MetaDataKey;
import org.mule.common.metadata.builder.DefaultMetaDataBuilder;
import org.mule.common.metadata.builder.DynamicObjectBuilder;
import org.mule.common.metadata.datatype.DataType;
import org.mule.modules.cookbook.CookbookConnector;
import org.mule.modules.cookbook.exception.CookbookException;
import org.mule.modules.cookbook.utils.EntityType;
import org.mule.modules.cookbook.utils.EnumUtils;

import javax.inject.Inject;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class BaseMetaData {

    protected static final String[] EXCLUDED_CREATION_FIELDS = {
            "id",
            "created",
            "lastModified" };

    protected static final String[] EXCLUDED_UPDATE_FIELDS = {
            "created",
            "lastModified" };

    @Inject
    protected CookbookConnector connector;

    public void setConnector(final CookbookConnector connector) {
        this.connector = connector;
    }

    protected void addMetaDataField(final DynamicObjectBuilder<?> objectBuilder, final Description field) throws CookbookException {

        String dataType = field.getDataType().name();

        if (dataType.equalsIgnoreCase("LIST")) {
            if (field.getInnerType().equals("String")) {
                objectBuilder.addList(field.getName()).ofSimpleField(DataType.STRING);
            } else if (field.getInnerType().equals("Ingredient")) {
                DynamicObjectBuilder<?> innerObject = objectBuilder.addList(field.getName()).ofDynamicObject("Ingredient");
                Description ingredientDescription = connector.describeEntity(EntityType.INGREDIENT.name());
                for (Description innerField : ingredientDescription.getInnerFields()) {
                    getType(innerObject, innerField.getDataType().name(), innerField.getName());
                }
            }
        } else if (dataType.equalsIgnoreCase("OBJECT")) {
            DynamicObjectBuilder<?> innerObject = objectBuilder.addDynamicObjectField(field.getName());
            for (Description innerField : field.getInnerFields()) {
                getType(innerObject, innerField.getDataType().name(), innerField.getName());
            }
        } else {
            getType(objectBuilder, field.getDataType().name(), field.getName());
        }

    }

    protected void addInputMetaDataField(final DynamicObjectBuilder<?> objectBuilder, final Description field) throws CookbookException {
        getType(objectBuilder, field.getDataType().name(), field.getName());
    }

    private void getType(final DynamicObjectBuilder<?> objectBuilder, String type, String name) {
        MoreObjects.firstNonNull(EnumUtils.getEnumFromString(Type.class, type), Type.STRING).addField(objectBuilder, name);
    }

    protected List<MetaDataKey> createKeys() throws Exception {
        List<MetaDataKey> keys = new ArrayList<>();
        List<CookBookEntity> entities = connector.getConfig().getClient().getEntities();
        for (CookBookEntity entity : entities) {
            keys.add(new DefaultMetaDataKey(entity.getName(), entity.getName()));
        }
        return keys;
    }

    protected DefaultMetaData createMetadata(final MetaDataKey key) throws Exception {
        final Description description = connector.describeEntity(key.getId());
        DefaultMetaDataBuilder builder = new DefaultMetaDataBuilder();
        DynamicObjectBuilder<?> objectBuilder = builder.createDynamicObject(key.getId());
        List<Description> fields = description.getInnerFields();
        for (Description field : fields) {
            addMetaDataField(objectBuilder, field);
        }
        objectBuilder.endDynamicObject();
        return new DefaultMetaData(builder.build());
    }

    public MetaData createInputMetadata(@NotNull final MetaDataKey key, final String[] excludedFields) throws Exception {
        final Description description = connector.describeEntity(key.getId());
        DefaultMetaDataBuilder builder = new DefaultMetaDataBuilder();
        DynamicObjectBuilder<?> objectBuilder = builder.createDynamicObject(key.getId());
        Iterable<Description> fields = Iterables.filter(description.getInnerFields(), new Predicate<Description>() {

            @Override
            public boolean apply(final Description input) {
                return !Arrays.asList(excludedFields).contains(input.getName());
            }
        });
        for (Description field : fields) {
            addMetaDataField(objectBuilder, field);
        }
        objectBuilder.endDynamicObject();
        return new DefaultMetaData(builder.build());
    }

}
