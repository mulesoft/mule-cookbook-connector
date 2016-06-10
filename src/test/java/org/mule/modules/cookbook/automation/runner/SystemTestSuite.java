/**
 * (c) 2003-2016 MuleSoft, Inc. The software in this package is
 * published under the terms of the CPAL v1.0 license, a copy of which
 * has been included with this distribution in the LICENSE.md file.
 */
package org.mule.modules.cookbook.automation.runner;

import org.junit.runner.RunWith;
import org.junit.runners.Suite;
import org.junit.runners.Suite.SuiteClasses;
import org.mule.modules.cookbook.automation.system.BasicConfigTestCases;
import org.mule.modules.cookbook.automation.system.OAuth2ConfigTestCases;

@RunWith(Suite.class)
@SuiteClasses({
        BasicConfigTestCases.class,
        OAuth2ConfigTestCases.class
})
public class SystemTestSuite {

}