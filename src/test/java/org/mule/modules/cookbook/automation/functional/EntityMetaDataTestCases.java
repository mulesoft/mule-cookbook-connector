package org.mule.modules.cookbook.automation.functional;

import org.mule.modules.cookbook.CookbookConnector;
import org.mule.modules.cookbook.datasense.DataSenseResolver;
import org.mule.tools.devkit.ctf.junit.AbstractMetaDataTestCase;
import org.mule.util.CollectionUtils;

import java.util.ArrayList;
import java.util.List;

public class EntityMetaDataTestCases extends AbstractMetaDataTestCase<CookbookConnector> {

    private static final List<String> keys = new ArrayList<>();

    static {
        CollectionUtils.addAll(keys, new Object[] {

                "Ingredient", "Recipe"
        });

    }

    public EntityMetaDataTestCases() {
        super(keys, DataSenseResolver.class, CookbookConnector.class);
    }


}
