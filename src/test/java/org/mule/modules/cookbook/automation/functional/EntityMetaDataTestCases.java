package org.mule.modules.cookbook.automation.functional;

import com.google.common.collect.ImmutableList;
import org.mule.modules.cookbook.CookbookConnector;
import org.mule.modules.cookbook.datasense.EntityMetaData;
import org.mule.tools.devkit.ctf.junit.AbstractMetaDataTestCase;

import java.util.List;

public class EntityMetaDataTestCases extends AbstractMetaDataTestCase<CookbookConnector> {

    private static final List<String> KEYS = ImmutableList.<String>builder().add("Ingredient").add("Recipe").build();

    public EntityMetaDataTestCases() {
        super(KEYS, EntityMetaData.class, CookbookConnector.class);
    }

}
