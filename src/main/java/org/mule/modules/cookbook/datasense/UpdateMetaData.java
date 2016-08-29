/**
 * (c) 2003-2016 MuleSoft, Inc. The software in this package is
 * published under the terms of the CPAL v1.0 license, a copy of which
 * has been included with this distribution in the LICENSE.md file.
 */
package org.mule.modules.cookbook.datasense;

import org.jetbrains.annotations.NotNull;
import org.mule.api.annotations.MetaDataKeyRetriever;
import org.mule.api.annotations.MetaDataOutputRetriever;
import org.mule.api.annotations.MetaDataRetriever;
import org.mule.api.annotations.components.MetaDataCategory;
import org.mule.common.metadata.MetaData;
import org.mule.common.metadata.MetaDataKey;

import java.util.List;

@MetaDataCategory
public class UpdateMetaData extends BaseMetaData {

    /**
     * Retrieves the list of keys.
     */
    @MetaDataKeyRetriever
    public List<MetaDataKey> getMetaDataKeys() throws Exception {
        return createKeys();
    }

    /**
     * Retrieves the {@link MetaData} model for the key the user selects.
     *
     * @param key
     *            The key selected from the list of valid keys.
     * @return The {@link MetaData} model associated to the key.
     * @throws Exception
     *             If there are errors in the retrieval.
     */
    @MetaDataRetriever
    public MetaData getMetaData(@NotNull final MetaDataKey key) throws Exception {
        return createInputMetadata(key, BaseMetaData.EXCLUDED_UPDATE_FIELDS);
    }

    @MetaDataOutputRetriever
    public MetaData getOutputMetaData(final MetaDataKey key) throws Exception {
        return createMetadata(key);
    }

}
