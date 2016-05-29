/**
 * (c) 2003-2016 MuleSoft, Inc. The software in this package is
 * published under the terms of the CPAL v1.0 license, a copy of which
 * has been included with this distribution in the LICENSE.md file.
 */
package org.mule.modules.cookbook.automation.functional;

import org.mule.modules.cookbook.CookbookConnector;
import org.mule.modules.cookbook.exception.CookbookException;
import org.mule.tools.devkit.ctf.junit.AbstractTestCase;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;

public abstract class AbstractTestCases extends AbstractTestCase<CookbookConnector> {

    private static final Logger logger = LoggerFactory.getLogger(AbstractTestCases.class);

    protected AbstractTestCases() {
        super(CookbookConnector.class);
    }

    protected void silentlyDelete(Integer id) {
        if (id != null) {
            try {
                getConnector().delete(id);
            } catch (CookbookException e) {
                logger.warn("Couldn't delete program for id: {}", id, e);
            }
        }
    }

    protected void silentlyDelete(List<Integer> ids) {
        if (ids != null) {
            try {
                getConnector().deleteMultipleEntities(ids);
            } catch (CookbookException e) {
                logger.warn("Couldn't delete program for id: {}", ids, e);
            }
        }
    }

}
