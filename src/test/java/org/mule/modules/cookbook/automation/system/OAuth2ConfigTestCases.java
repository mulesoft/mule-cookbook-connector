/**
 * (c) 2003-2016 MuleSoft, Inc. The software in this package is
 * published under the terms of the CPAL v1.0 license, a copy of which
 * has been included with this distribution in the LICENSE.md file.
 */
package org.mule.modules.cookbook.automation.system;

import com.cookbook.tutorial.service.InvalidTokenException;
import org.junit.Before;
import org.junit.Test;
import org.mule.api.ConnectionException;
import org.mule.modules.cookbook.config.OAuth2Config;
import org.mule.tools.devkit.ctf.configuration.util.ConfigurationUtils;

import java.util.Properties;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.instanceOf;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.notNullValue;
import static org.junit.Assert.*;

public class OAuth2ConfigTestCases {

    private OAuth2Config config;

    @Before
    public void setUp() throws Exception {
        final Properties props = ConfigurationUtils.getAutomationCredentialsProperties();
        config = new OAuth2Config();
        config.setAddress(props.getProperty("config.address"));
        config.setConsumerKey(props.getProperty("oauth.consumerKey"));
        config.setConsumerSecret(props.getProperty("oauth.consumerSecret"));
        config.setAccessToken(props.getProperty("oauth.accessToken"));
    }

    @Test
    public void testValidCredentials() throws ConnectionException {
        config.postAuthorize();
        config.testConnect();
        assertThat(config.getClient(), is(notNullValue()));
    }

    @Test
    public void testEmptyAccessToken() {
        try {
            config.setAccessToken("");
            config.postAuthorize();
            config.testConnect();
            fail();
        } catch (ConnectionException e) {
            assertThat(e.getCause(), instanceOf(InvalidTokenException.class));
        }
    }

    @Test
    public void testNullAccessToken() {
        try {
            config.setAccessToken(null);
            config.postAuthorize();
            config.testConnect();
            fail();
        } catch (ConnectionException e) {
            assertThat(e.getCause(), instanceOf(InvalidTokenException.class));
        }
    }

    @Test
    public void testInvalidAccessToken() {
        try {
            config.setAccessToken("AAA1234567#123CCC");
            config.postAuthorize();
            config.testConnect();
            fail();
        } catch (ConnectionException e) {
            // the cause of the Exception or RuntimeException
            assertThat(e.getCause().getCause(), instanceOf(InvalidTokenException.class));
        }
    }
}
