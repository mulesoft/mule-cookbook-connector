/**
 * (c) 2003-2016 MuleSoft, Inc. The software in this package is
 * published under the terms of the CPAL v1.0 license, a copy of which
 * has been included with this distribution in the LICENSE.md file.
 */
package org.mule.modules.cookbook.automation.system;

import com.cookbook.tutorial.service.InvalidTokenException;

import org.junit.Before;
import org.junit.Test;
import org.mule.modules.cookbook.config.OAuthConfig;
import org.mule.tools.devkit.ctf.configuration.util.ConfigurationUtils;

import java.util.Properties;

import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

public class OAuthConfigTestCases {

    private Properties validCredentials;
    private OAuthConfig config;

    @Before
    public void setUp() throws Exception {
        validCredentials = ConfigurationUtils.getAutomationCredentialsProperties();
        config = new OAuthConfig();
        config.setAddress(validCredentials.getProperty("config.address"));
        config.setConsumerKey(validCredentials.getProperty("oauth.consumerKey"));
        config.setConsumerSecret(validCredentials.getProperty("oauth.consumerSecret"));
        config.setAccessToken(validCredentials.getProperty("oauth.accessToken"));
        config.postAuthorize();
    }

    @Test
    public void validCredentialsTest() {
        try {
            config.testConnect();
        } catch (Exception e) {
            fail(e.getMessage());
        }
    }

    @Test
    public void invalidCredentialsTest() {
        try {
            config.setAccessToken("");
            config.postAuthorize();

            config.testConnect();
        } catch (Exception e) {
            if (e.getCause() instanceof InvalidTokenException) {
                assertTrue(true);
            } else {
                fail(e.getMessage());
            }
        }
    }
}
