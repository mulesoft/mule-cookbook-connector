package org.mule.modules.cookbook.automation.functional;

import org.mule.modules.cookbook.CookbookConnector;
import org.mule.tools.devkit.ctf.junit.AbstractTestCase;

public abstract class AbstractTestCases extends AbstractTestCase<CookbookConnector> {

    protected AbstractTestCases() {
        super(CookbookConnector.class);
    }

}
