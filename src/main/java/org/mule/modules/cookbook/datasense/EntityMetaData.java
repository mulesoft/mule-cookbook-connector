/**
 * (c) 2003-2016 MuleSoft, Inc. The software in this package is
 * published under the terms of the CPAL v1.0 license, a copy of which
 * has been included with this distribution in the LICENSE.md file.
 */
package org.mule.modules.cookbook.datasense;

import com.cookbook.tutorial.service.*;
import org.jetbrains.annotations.NotNull;
import org.mule.api.annotations.MetaDataKeyRetriever;
import org.mule.api.annotations.MetaDataOutputRetriever;
import org.mule.api.annotations.MetaDataRetriever;
import org.mule.api.annotations.components.MetaDataCategory;
import org.mule.common.metadata.DefaultMetaData;
import org.mule.common.metadata.DefaultMetaDataKey;
import org.mule.common.metadata.MetaData;
import org.mule.common.metadata.MetaDataKey;
import org.mule.common.metadata.builder.DefaultMetaDataBuilder;
import org.mule.common.metadata.builder.DynamicObjectBuilder;
import org.mule.common.metadata.datatype.DataType;
import org.mule.modules.cookbook.CookbookConnector;
import org.mule.modules.cookbook.utils.EntityType;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.inject.Inject;
import java.util.ArrayList;
import java.util.List;

@MetaDataCategory
public class EntityMetaData {

    private static final Logger logger = LoggerFactory.getLogger(EntityMetaData.class);

    @Inject
    private CookbookConnector connector;

    /**
     * Retrieves the list of keys
     */
    @MetaDataKeyRetriever
    public List<MetaDataKey> getMetaDataKeys() throws Exception {
        List<MetaDataKey> keys = new ArrayList<>();
        List<CookBookEntity> entities = getConnector().getConfig().getClient().getEntities();
        for (CookBookEntity entity : entities) {
            keys.add(new DefaultMetaDataKey(entity.getName(), entity.getName()));
        }
        return keys;
    }

    /**
     * Get MetaData given the Key the user selects
     *
     * @param key The key selected from the list of valid keys
     * @return The MetaData model of that corresponds to the key
     * @throws Exception If anything fails
     */
    @MetaDataRetriever
    public MetaData getMetaData(@NotNull final MetaDataKey key) throws Exception {
        return createMetaData(key);
    }

    @MetaDataOutputRetriever
    public MetaData getOutputMetaData(final MetaDataKey key) throws Exception {
        return createMetaData(key);
    }

    private DefaultMetaData createMetaData(final MetaDataKey key) throws Exception {
        DefaultMetaDataBuilder builder = new DefaultMetaDataBuilder();

        CookBookEntity entity = EntityType.getClassFromType(EntityType.find(key.getId()));
        Description description = getConnector().getConfig().getClient().describeEntity(entity);

        DynamicObjectBuilder<?> dynamicObject = builder.createDynamicObject(key.getId());
        for (Description fields : description.getInnerFields()) {
            addFields(fields, dynamicObject);
        }
        return new DefaultMetaData(builder.build());
    }

    private void addFields(@NotNull final Description description, @NotNull final DynamicObjectBuilder<?> dynamicObject) {
        switch (description.getDataType()) {
            case DATE:
                dynamicObject.addSimpleField(description.getName(), DataType.DATE_TIME);
                break;
            case DOUBLE:
                dynamicObject.addSimpleField(description.getName(), DataType.DOUBLE);
                break;
            case INTEGER:
                dynamicObject.addSimpleField(description.getName(), DataType.INTEGER);
                break;
            case LIST:
                if (description.getInnerType().equals("String")) {
                    dynamicObject.addList(description.getName()).ofSimpleField(DataType.STRING);
                } else if (description.getInnerType().equals("Ingredient")) {
                    DynamicObjectBuilder<?> innerObject = dynamicObject.addList(description.getName()).ofDynamicObject("ingredients");
                    try {
                        Description ingredientDescription = getConnector().getConfig().getClient().describeEntity(new Ingredient());
                        for (Description desc : ingredientDescription.getInnerFields()) {
                            addFields(desc, innerObject);
                        }
                    } catch (InvalidTokenException | InvalidEntityException | NoSuchEntityException | SessionExpiredException e) {
                        logger.error(e.getMessage(), e);
                    }
                }
                break;
            case OBJECT:
                DynamicObjectBuilder<?> innerObject = dynamicObject.addDynamicObjectField(description.getName());
                for (Description field : description.getInnerFields()) {
                    addFields(field, innerObject);
                }
                break;
            case STRING:
                dynamicObject.addSimpleField(description.getName(), DataType.STRING);
                break;
            case UNIT_TYPE:
                dynamicObject.addEnumField(description.getName(), UnitType.class.getName());
                break;
            default:
                break;
        }

    }

    public CookbookConnector getConnector() {
        return connector;
    }

    public void setConnector(CookbookConnector connector) {
        this.connector = connector;
    }

}