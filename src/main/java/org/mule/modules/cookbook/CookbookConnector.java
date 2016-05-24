/**
 * (c) 2003-2016 MuleSoft, Inc. The software in this package is
 * published under the terms of the CPAL v1.0 license, a copy of which
 * has been included with this distribution in the LICENSE.md file.
 */
package org.mule.modules.cookbook;

import com.cookbook.tutorial.client.ICookbookCallback;
import com.cookbook.tutorial.service.CookBookEntity;
import com.cookbook.tutorial.service.Ingredient;
import com.cookbook.tutorial.service.InvalidEntityException;
import com.cookbook.tutorial.service.NoSuchEntityException;
import com.cookbook.tutorial.service.Recipe;
import com.cookbook.tutorial.service.SessionExpiredException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.common.base.Preconditions;
import org.jetbrains.annotations.NotNull;
import org.mule.api.annotations.Config;
import org.mule.api.annotations.Connector;
import org.mule.api.annotations.MetaDataScope;
import org.mule.api.annotations.Paged;
import org.mule.api.annotations.Processor;
import org.mule.api.annotations.ReconnectOn;
import org.mule.api.annotations.Source;
import org.mule.api.annotations.SourceStrategy;
import org.mule.api.annotations.oauth.OAuthProtected;
import org.mule.api.annotations.param.Default;
import org.mule.api.annotations.param.MetaDataKeyParam;
import org.mule.api.annotations.param.MetaDataKeyParamAffectsType;
import org.mule.api.annotations.param.RefOnly;
import org.mule.api.callback.SourceCallback;
import org.mule.modules.cookbook.config.AbstractConfig;
import org.mule.modules.cookbook.datasense.DataSenseResolver;
import org.mule.modules.cookbook.pagination.CookbookPagingDelegate;
import org.mule.modules.cookbook.util.EntityType;
import org.mule.streaming.PagingConfiguration;
import org.mule.streaming.ProviderAwarePagingDelegate;

import java.util.List;
import java.util.Map;

/**
 * Anypoint Cookbook Connector
 *
 * @author MuleSoft, Inc.
 */
@ReconnectOn(exceptions = {
        SessionExpiredException.class
})
@Connector(name = "cookbook", friendlyName = "Cookbook", minMuleVersion = "3.6")
@MetaDataScope(DataSenseResolver.class)
public class CookbookConnector {

    @Config
    private AbstractConfig config;

    @NotNull
    public AbstractConfig getConfig() {
        return config;
    }

    public void setConfig(@NotNull AbstractConfig config) {
        this.config = config;
    }

    /**
     * Returns the list of recently added recipes
     * <p/>
     *
     * @return A list of the recently added recipes
     */
    @OAuthProtected
    @Processor
    public List<Recipe> getRecentlyAdded() {
        return config.getClient().getRecentlyAdded();
    }

    /**
     * Constantly polls the system to return the recently added recipes.
     * <p/>
     *
     * @param callback The callback that will hook the result into mule event.
     * @throws Exception When the source fails.
     */
    @OAuthProtected
    @Source(sourceStrategy = SourceStrategy.POLLING, pollingPeriod = 10000)
    public void getRecentlyAddedSource(final SourceCallback callback) throws Exception {
        Preconditions.checkNotNull(callback);
        if (config.getClient() != null) {
            // Every 5 seconds our callback will be executed
            this.getConfig().getClient().getRecentlyAdded(new ICookbookCallback() {

                public void execute(List<Recipe> recipes) throws Exception {
                    callback.process(recipes);
                }
            });

            if (Thread.interrupted()) {
                throw new InterruptedException();
            }
        }
    }

    /**
     * Create Recipes and Ingredients
     * <p/>
     *
     * @param type   Type of the entity
     * @param entity Ingredient/Recipe to be created
     * @return return Id of the entity from the system.
     * @throws SessionExpiredException Exception thrown when an action is taken by a client who's session has expired.
     * @throws InvalidEntityException  Exception thrown when an wrong entity or type is mapped to the entity parameter.
     */
    @OAuthProtected
    @Processor
    public CookBookEntity create(@MetaDataKeyParam(affects = MetaDataKeyParamAffectsType.BOTH) final EntityType type,
            @Default("#[payload]") @RefOnly final Map<String, Object> entity) throws InvalidEntityException, SessionExpiredException {
        Preconditions.checkNotNull(entity);
        return config.getClient().create(getCookBookEntity(type, entity));
    }

    /**
     * Update the existing entities (Recipes/Ingredients)
     * <p/>
     *
     * @param type   Type of the entity
     * @param entity Entity to be updated
     * @return return Entity with Id from the system.
     * @throws SessionExpiredException Exception thrown when an action is taken by a client who's session has expired.
     * @throws InvalidEntityException  Exception thrown when an wrong entity or type is mapped to the entity parameter.
     * @throws NoSuchEntityException   Exception thrown when the specified entity does not exist in the system.
     */
    @OAuthProtected
    @Processor
    public CookBookEntity update(@MetaDataKeyParam(affects = MetaDataKeyParamAffectsType.BOTH) final EntityType type,
            @Default("#[payload]") @RefOnly final Map<String, Object> entity) throws InvalidEntityException, SessionExpiredException, NoSuchEntityException {
        Preconditions.checkNotNull(type);
        Preconditions.checkNotNull(entity);
        return config.getClient().update(getCookBookEntity(type, entity));
    }

    /**
     * Retrieve the entity from the system based on the identifier.
     * <p/>
     *
     * @param type Type of the entity
     * @param id   Id of the entity to retrieve
     * @return return Ingredient with Id from the system.
     * @throws SessionExpiredException Exception thrown when an action is taken by a client who's session has expired.
     * @throws InvalidEntityException  Exception thrown when an wrong entity or type is mapped to the entity parameter.
     * @throws NoSuchEntityException   Exception thrown when the specified entity does not exist in the system.
     */
    @OAuthProtected
    @Processor
    public CookBookEntity get(@MetaDataKeyParam(affects = MetaDataKeyParamAffectsType.OUTPUT) final String type, @Default("1") final Integer id)
            throws InvalidEntityException, SessionExpiredException, NoSuchEntityException {
        Preconditions.checkNotNull(type);
        Preconditions.checkNotNull(id);
        return config.getClient().get(id);
    }

    /**
     * Performs delete operation, based on the id corresponding entity is delete from system.
     * <p/>
     *
     * @param id Id of the entity to delete
     * @throws SessionExpiredException Exception thrown when an action is taken by a client who's session has expired.
     * @throws NoSuchEntityException   Exception thrown when the specified entity does not exist in the system.
     */
    @OAuthProtected
    @Processor
    public void delete(@Default("1") final Integer id) throws NoSuchEntityException, SessionExpiredException {
        Preconditions.checkNotNull(id);
        config.getClient().delete(id);
    }

    /**
     * ResultSet retrieved based on the query.
     * <p/>
     *
     * @param query               The query
     * @param pagingConfiguration the paging configuration
     * @return return comment
     * @throws SessionExpiredException Exception thrown when an action is taken by a client who's session has expired.
     */
    @OAuthProtected
    @Processor
    @Paged
    public ProviderAwarePagingDelegate<Map<String, Object>, CookbookConnector> queryPaginated(final String query, final PagingConfiguration pagingConfiguration)
            throws SessionExpiredException {
        Preconditions.checkNotNull(query);
        return new CookbookPagingDelegate(query, pagingConfiguration.getFetchSize());
    }

    private CookBookEntity getCookBookEntity(@NotNull @MetaDataKeyParam(affects = MetaDataKeyParamAffectsType.BOTH) final EntityType type,
            @NotNull @Default("#[payload]") @RefOnly final Map<String, Object> entity) throws InvalidEntityException {
        CookBookEntity input;
        ObjectMapper mapper = new ObjectMapper();
        if (type == EntityType.RECIPE) { input = mapper.convertValue(entity, Recipe.class);
        } else if (type == EntityType.INGREDIENT) { input = mapper.convertValue(entity, Ingredient.class);
        } else {
            throw new InvalidEntityException("Don't know how to handle type:" + type);
        }
        return input;
    }

}