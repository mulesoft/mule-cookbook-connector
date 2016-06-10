/**
 * (c) 2003-2016 MuleSoft, Inc. The software in this package is
 * published under the terms of the CPAL v1.0 license, a copy of which
 * has been included with this distribution in the LICENSE.md file.
 */
package org.mule.modules.cookbook.automation.runner;

import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.runner.RunWith;
import org.junit.runners.Suite;
import org.junit.runners.Suite.SuiteClasses;
import org.mule.modules.cookbook.CookbookConnector;
import org.mule.modules.cookbook.automation.functional.*;
import org.mule.tools.devkit.ctf.mockup.ConnectorTestContext;

@RunWith(Suite.class)
@SuiteClasses({
        CreateMetaDataTestCases.class,
        CreateMultipleTestCases.class,
        CreateTestCases.class,
        DeleteMultipleTestCases.class,
        DeleteTestCases.class,
        DescribeEntityTestCases.class,
        EntityMetaDataTestCases.class,
        GetMultipleTestCases.class,
        GetRecentlyAddedSourceTestCases.class,
        GetRecentlyAddedTestCases.class,
        GetTestCases.class,
        QueryTestCases.class,
        TransformToMapTestCases.class,
        UpdateMetaDataTestCases.class,
        UpdateMultipleTestCases.class,
        UpdateTestCases.class })
public class FunctionalTestSuite {

    @BeforeClass
    public static void initialiseSuite() {
        ConnectorTestContext.initialize(CookbookConnector.class);
    }

    @AfterClass
    public static void shutdownSuite() {
        ConnectorTestContext.shutDown();
    }
}
