/**
 * (c) 2003-2016 MuleSoft, Inc. The software in this package is
 * published under the terms of the CPAL v1.0 license, a copy of which
 * has been included with this distribution in the LICENSE.md file.
 */
package org.mule.modules.cookbook.automation.system;

import org.junit.Before;
import org.junit.Test;
import org.mule.api.ConnectionException;
import org.mule.modules.cookbook.config.Config;
import org.mule.tools.devkit.ctf.configuration.util.ConfigurationUtils;

import java.util.Properties;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.notNullValue;

public class BasicConfigTestCases {

    private Config config;
    private String username;
    private String password;

    @Before
    public void setUp() throws Exception {
        final Properties props = ConfigurationUtils.getAutomationCredentialsProperties();
        config = new Config();
        config.setAddress(props.getProperty("config.address"));
        username = props.getProperty("config.username");
        password = props.getProperty("config.password");
    }

    @Test
    public void testValidCredentials() throws ConnectionException {
        config.connect(username, password);
        assertThat(config.getClient(), is(notNullValue()));
    }

    @Test(expected = ConnectionException.class)
    public void testInvalidCredentials() throws ConnectionException {
        config.connect("noUsername", "noPassword");
    }

    @Test(expected = ConnectionException.class)
    public void testEmptyCredentials() throws ConnectionException {
        config.connect("", "");
    }

    @Test(expected = ConnectionException.class)
    public void testNullCredentials() throws ConnectionException {
        config.connect(null, null);
    }

    @Test(expected = ConnectionException.class)
    public void testInvalidUsername() throws ConnectionException {
        config.connect("noUsername", password);
    }

    @Test(expected = ConnectionException.class)
    public void testInvalidPassword() throws ConnectionException {
        config.connect(username, "noPassword");
    }

}
