/**
 * (c) 2003-2016 MuleSoft, Inc. The software in this package is
 * published under the terms of the CPAL v1.0 license, a copy of which
 * has been included with this distribution in the LICENSE.md file.
 */
package org.mule.modules.cookbook.config;

import com.cookbook.tutorial.client.MuleCookBookClient;
import com.cookbook.tutorial.service.InvalidTokenException;
import com.cookbook.tutorial.service.SessionExpiredException;
import org.apache.cxf.common.util.StringUtils;
import org.mule.api.ConnectionException;
import org.mule.api.ConnectionExceptionCode;
import org.mule.api.annotations.Configurable;
import org.mule.api.annotations.TestConnectivity;
import org.mule.api.annotations.oauth.OAuth2;
import org.mule.api.annotations.oauth.OAuthAccessToken;
import org.mule.api.annotations.oauth.OAuthConsumerKey;
import org.mule.api.annotations.oauth.OAuthConsumerSecret;
import org.mule.api.annotations.oauth.OAuthPostAuthorization;

/**
 * Authenticates against the service using OAuth 2.0 protocol
 *
 * @author MuleSoft, Inc.
 */
@OAuth2(configElementName = "oauth2", friendlyName = "OAuth 2.0", authorizationUrl = "http://devkit-cookbook.cloudhub.io/rest/oauth/authorize", accessTokenUrl = "http://devkit-cookbook.cloudhub.io/rest/oauth/accessToken")
public class OAuthConfig extends AbstractConfig {

    /**
     * An access token associated with this instance.
     */
    @OAuthAccessToken
    private String accessToken;

    /**
     * Your application's client identifier - OAuth consumer key
     */
    @Configurable
    @OAuthConsumerKey
    private String consumerKey;

    /**
     * Your application's client secret - OAuth consumer secret
     */
    @Configurable
    @OAuthConsumerSecret
    private String consumerSecret;

    @OAuthPostAuthorization
    public void postAuthorize() throws ConnectionException {
        setClient(new MuleCookBookClient(getAddress()));
        if(StringUtils.isEmpty(getAccessToken())){
            throw new ConnectionException(ConnectionExceptionCode.INCORRECT_CREDENTIALS, "" , "Access Token is null or empty", new InvalidTokenException());
        }
        getClient().setToken(getAccessToken());
    }

    @TestConnectivity
    public void testConnect() throws ConnectionException {
        try {
            getClient().getEntities();
        }
        catch(SessionExpiredException e){
            throw new ConnectionException(ConnectionExceptionCode.CREDENTIALS_EXPIRED, e.getMessage(), "Access token is expired.", e);
        }
        catch(Exception e){
            throw new ConnectionException(ConnectionExceptionCode.UNKNOWN, e.getMessage(), "Failed to connect to the Cookbook service.", e);
        }
    }

    public void setAccessToken(String accessToken) {
        this.accessToken = accessToken;
    }

    public String getAccessToken() {
        return this.accessToken;
    }

    public void setConsumerKey(String consumerKey) {
        this.consumerKey = consumerKey;
    }

    public String getConsumerKey() {
        return this.consumerKey;
    }

    public void setConsumerSecret(String consumerSecret) {
        this.consumerSecret = consumerSecret;
    }

    public String getConsumerSecret() {
        return this.consumerSecret;
    }

}
