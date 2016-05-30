/**
 * (c) 2003-2016 MuleSoft, Inc. The software in this package is
 * published under the terms of the CPAL v1.0 license, a copy of which
 * has been included with this distribution in the LICENSE.md file.
 */
package org.mule.modules.cookbook.datasense;

import com.cookbook.tutorial.service.CookBookEntity;
import com.cookbook.tutorial.service.Description;
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
import org.mule.modules.cookbook.CookbookConnector;

import java.util.ArrayList;
import java.util.List;

@MetaDataCategory
public class EntityMetaData extends BaseMetaData {

    /**
     * Retrieves the list of keys
     */
    @MetaDataKeyRetriever
    public List<MetaDataKey> getMetaDataKeys() throws Exception {
        List<MetaDataKey> keys = new ArrayList<>();
        List<CookBookEntity> entities = getConnector().getEntities();
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
        final Description description = getConnector().describeEntity(key.getId());

        DefaultMetaDataBuilder builder = new DefaultMetaDataBuilder();
        DynamicObjectBuilder<?> objectBuilder = builder.createDynamicObject(key.getId());
        List<Description> fields = description.getInnerFields();
        for (Description field : fields) {
            addMetaDataField(objectBuilder, field);
        }
        objectBuilder.endDynamicObject();
        return new DefaultMetaData(builder.build());
    }

    public CookbookConnector getConnector() {
        return connector;
    }

    public void setConnector(CookbookConnector connector) {
        this.connector = connector;
    }

}
