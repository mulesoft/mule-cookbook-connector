/**
 * (c) 2003-2016 MuleSoft, Inc. The software in this package is
 * published under the terms of the CPAL v1.0 license, a copy of which
 * has been included with this distribution in the LICENSE.md file.
 */
package org.mule.modules.cookbook;

import com.cookbook.tutorial.service.CookBookEntity;
import com.cookbook.tutorial.service.Description;
import com.cookbook.tutorial.service.Ingredient;
import com.cookbook.tutorial.service.InvalidEntityException;
import com.cookbook.tutorial.service.InvalidTokenException;
import com.cookbook.tutorial.service.NoSuchEntityException;
import com.cookbook.tutorial.service.Recipe;
import com.cookbook.tutorial.service.SessionExpiredException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.common.base.Preconditions;
import org.jetbrains.annotations.NotNull;
import org.mule.api.annotations.Config;
import org.mule.api.annotations.Connector;
import org.mule.api.annotations.MetaDataScope;
import org.mule.api.annotations.Paged;
import org.mule.api.annotations.Processor;
import org.mule.api.annotations.Query;
import org.mule.api.annotations.ReconnectOn;
import org.mule.api.annotations.Source;
import org.mule.api.annotations.SourceStrategy;
import org.mule.api.annotations.Transformer;
import org.mule.api.annotations.oauth.OAuthProtected;
import org.mule.api.annotations.param.Default;
import org.mule.api.annotations.param.MetaDataKeyParam;
import org.mule.api.annotations.param.MetaDataKeyParamAffectsType;
import org.mule.api.annotations.param.RefOnly;
import org.mule.api.callback.SourceCallback;
import org.mule.modules.cookbook.config.AbstractConfig;
import org.mule.modules.cookbook.datasense.CreateMetaData;
import org.mule.modules.cookbook.datasense.DescribeMetaData;
import org.mule.modules.cookbook.datasense.EntityMetaData;
import org.mule.modules.cookbook.datasense.UpdateMetaData;
import org.mule.modules.cookbook.exception.CookbookException;
import org.mule.modules.cookbook.pagination.QueryPagingDelegate;
import org.mule.modules.cookbook.utils.EntityType;
import org.mule.streaming.PagingConfiguration;
import org.mule.streaming.ProviderAwarePagingDelegate;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;
import java.util.Map;

/**
 * Anypoint Cookbook Connector
 *
 * @author MuleSoft, Inc.
 */
@ReconnectOn(exceptions = { SessionExpiredException.class })
@Connector(name = "cookbook", friendlyName = "Cookbook", minMuleVersion = "3.6")
public class CookbookConnector {

    private static final Logger logger = LoggerFactory.getLogger(CookbookConnector.class);

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
     * Allows the caller to create a new Ingredient or Recipe.
     * <p>
     *
     * @param type
     *            Type of entity: {@link Ingredient} or {@link Recipe}.
     * @param params
     *            Map containing the data of the entity to be created.
     * @return {@link CookBookEntity} The created entity.
     * @throws CookbookException
     *             If any of the provided entity parameters is incorrect/missing or if the user session has expired.
     */
    @OAuthProtected
    @Processor(friendlyName = "Create")
    @MetaDataScope(CreateMetaData.class)
    public CookBookEntity create(@MetaDataKeyParam(affects = MetaDataKeyParamAffectsType.BOTH) final String type, @Default("#[payload]") @RefOnly final Map<String, Object> params)
            throws CookbookException {
        Preconditions.checkNotNull(params);
        try {
            return config.getClient().create(convertToCookBookEntity(EntityType.find(type), params));
        } catch (InvalidEntityException | SessionExpiredException e) {
            logger.error("Unable to create entity of type {}", type, e);
            throw new CookbookException(e);
        }
    }

    /**
     * Allows de caller to create multiple new Ingredients and/or Recipes in a single execution.
     * <p>
     *
     * @param entities
     *            List of {@link CookBookEntity} entities to be created. It can contain a combination of Ingredients and Recipes.
     * @return List <{@link CookBookEntity}> The list of created entities.
     * @throws CookbookException
     *             If any of the provided entity parameters is incorrect/missing or if the user session has expired.
     */
    @OAuthProtected
    @Processor(friendlyName = "Create multiple")
    @MetaDataScope(CreateMetaData.class)
    public List<CookBookEntity> createMultiple(@RefOnly @Default("#[payload]") final List<CookBookEntity> entities) throws CookbookException {
        Preconditions.checkNotNull(entities);
        try {
            return config.getClient().addList(entities);
        } catch (InvalidEntityException | SessionExpiredException e) {
            logger.error("Unable to create multiple entities", e);
            throw new CookbookException(e);
        }
    }

    /**
     * Allows the caller to delete an entity (Ingredient or Recipe) by its ID.
     * <p>
     *
     * @param id
     *            ID of the entity to delete.
     * @throws CookbookException
     *             If the specified entity does not exist or if the user session has expired.
     */
    @OAuthProtected
    @Processor(friendlyName = "Delete")
    public void delete(@Default("1") final Integer id) throws CookbookException {
        Preconditions.checkNotNull(id);
        try {
            config.getClient().delete(id);
        } catch (NoSuchEntityException | SessionExpiredException e) {
            logger.error("Unable to delete entity with ID {}", e, id);
            throw new CookbookException(e);
        }
    }

    /**
     * Allows the caller to delete multiple entities (Ingredient or Recipe) by their ID.
     * <p>
     *
     * @param entityIds
     *            List of IDs of the entities to delete.
     * @throws CookbookException
     *             If the specified entity does not exist or if the user session has expired.
     */
    @OAuthProtected
    @Processor(friendlyName = "Delete multiple")
    public void deleteMultiple(@RefOnly @Default("#[payload]") final List<Integer> entityIds) throws CookbookException {
        Preconditions.checkNotNull(entityIds);
        try {
            config.getClient().deleteList(entityIds);
        } catch (NoSuchEntityException | SessionExpiredException e) {
            logger.error("Unable to delete multiple entities with IDs {}", entityIds.toString(), e);
            throw new CookbookException(e);
        }
    }

    /**
     * Allows the caller to retrieve all fields and data types of a Ingredient or Recipe object.
     * <p>
     *
     * @param type
     *            Type of entity: Ingredient or Recipe.
     * @throws CookbookException
     *             If the provided token is invalid, if the provided parameter is invalid, if the entity type does not exist, or if the user session has expired.
     */
    @OAuthProtected
    @Processor(friendlyName = "Describe")
    @MetaDataScope(DescribeMetaData.class)
    public Description describeEntity(@MetaDataKeyParam(affects = MetaDataKeyParamAffectsType.OUTPUT) final String type) throws CookbookException {
        Preconditions.checkNotNull(type);
        try {
            return config.getClient().describeEntity(EntityType.getClassFromType(EntityType.find(type)));
        } catch (InvalidTokenException | InvalidEntityException | NoSuchEntityException | SessionExpiredException e) {
            logger.error("Unable to describe entity of type {}", type, e);
            throw new CookbookException(e);
        }
    }

    /**
     * Allows the caller to retrieve a {@link CookBookEntity} based on its ID.
     * <p>
     *
     * @param type
     *            Type of the entity.
     * @param id
     *            ID of the entity.
     * @return {@link CookBookEntity} The requested {@link CookBookEntity}.
     * @throws CookbookException
     *             If the specified entity does not exist or if the user session has expired.
     */
    @OAuthProtected
    @Processor(friendlyName = "Get")
    @MetaDataScope(EntityMetaData.class)
    public CookBookEntity get(@MetaDataKeyParam(affects = MetaDataKeyParamAffectsType.OUTPUT) final String type, @Default("1") final Integer id) throws CookbookException {
        Preconditions.checkNotNull(type);
        Preconditions.checkNotNull(id);
        try {
            return config.getClient().get(id);
        } catch (NoSuchEntityException | SessionExpiredException e) {
            logger.error("Unable to retrieve entity with ID {}", id, e);
            throw new CookbookException(e);
        }
    }

    /**
     * Allows the caller to retrieve multiple entities by their IDs.
     * <p>
     *
     * @return The requested list of {@link CookBookEntity}.
     * @throws CookbookException
     *             If the specified entity does not exist or if the user session has expired.
     */
    @OAuthProtected
    @Processor(friendlyName = "Get multiple")
    @MetaDataScope(EntityMetaData.class)
    public List<CookBookEntity> getMultiple(@RefOnly @Default("#[payload]") final List<Integer> entityIds) throws CookbookException {
        Preconditions.checkNotNull(entityIds);
        try {
            return config.getClient().getList(entityIds);
        } catch (NoSuchEntityException | SessionExpiredException e) {
            logger.error("Unable to retrieve multiple entities with IDs {}", entityIds.toString(), e);
            throw new CookbookException(e);
        }
    }

    /**
     * Allows the caller to return the list of recently added Recipes.
     * <p>
     *
     * @return List of recently added {@link Recipe}.
     */
    @OAuthProtected
    @Processor(friendlyName = "Get recently added recipes")
    @MetaDataScope(EntityMetaData.class)
    public List<Recipe> getRecentlyAdded() {
        return config.getClient().getRecentlyAdded();
    }

    /**
     * Polls the system every X seconds (default value is 10 sec) to fetch the recently added Recipes.
     * <p>
     *
     * @param callback
     *            A {@link SourceCallback} object that will hook the result into the Mule Event.
     * @throws CookbookException
     *             If the poll connection is interrupted or an error occurred in the Cookbook service.
     */
    @OAuthProtected
    @Source(friendlyName = "Get recently added recipes", sourceStrategy = SourceStrategy.POLLING, pollingPeriod = 10000)
    public void getRecentlyAddedSource(final SourceCallback callback) throws CookbookException {
        try {
            logger.info("Creating connection to wait for incoming Recipes...");
            callback.process(getRecentlyAdded());
        } catch (Exception e) {
            logger.error("Unable to create connection to the Cookbook service", e);
            throw new CookbookException(e);
        }
    }

    /**
     * Allows the caller to update an existing {@link CookBookEntity}.
     * <p>
     *
     * @param type
     *            Type of the entity.
     * @param entity
     *            Map containing the data of the entity to be updated.
     * @return The updated {@link CookBookEntity}.
     * @throws CookbookException
     *             If any of the provided entity parameters is incorrect/missing, if the entity does not exist, or if the user session has expired.
     */
    @OAuthProtected
    @Processor(friendlyName = "Update")
    @MetaDataScope(UpdateMetaData.class)
    public CookBookEntity update(@MetaDataKeyParam(affects = MetaDataKeyParamAffectsType.BOTH) final String type, @RefOnly @Default("#[payload]") final Map<String, Object> entity)
            throws CookbookException {
        Preconditions.checkNotNull(type);
        Preconditions.checkNotNull(entity);
        try {
            return config.getClient().update(convertToCookBookEntity(EntityType.find(type), entity));
        } catch (InvalidEntityException | NoSuchEntityException | SessionExpiredException e) {
            logger.error("Unable to update entity with params {}", entity, e);
            throw new CookbookException(e);
        }
    }

    /**
     * Allows the caller to update one or more existing {@link CookBookEntity}.
     * <p>
     *
     * @param entities
     *            List of {@link CookBookEntity} to be updated.
     * @return The updated list of {@link CookBookEntity}.
     * @throws CookbookException
     *             If any of the provided entity parameters is incorrect/missing, if the entity does not exist, or if the user session has expired.
     */
    @OAuthProtected
    @Processor(friendlyName = "Update multiple")
    @MetaDataScope(UpdateMetaData.class)
    public List<CookBookEntity> updateMultiple(@MetaDataKeyParam(affects = MetaDataKeyParamAffectsType.BOTH) @RefOnly @Default("#[payload]") final List<CookBookEntity> entities)
            throws CookbookException {
        Preconditions.checkNotNull(entities);
        try {
            return config.getClient().updateList(entities);
        } catch (InvalidEntityException | NoSuchEntityException | SessionExpiredException e) {
            logger.error("Unable to update multiple entities", e);
            throw new CookbookException(e);
        }
    }

    /**
     * Allows the caller to execute calls to the Cookbook service using a query string in CQL (Cookbook Query Language). For a detailed grammar description, please check the
     * related section in this reference guide.
     * <p>
     *
     * @param query
     *            The query is CQL format.
     * @param pagingConfiguration
     *            The {@link PagingConfiguration} object with the needed parameters for paged queries.
     * @return The {@link QueryPagingDelegate} that handles the calls to Cookbook to run the query.
     * @throws CookbookException
     *             If the provided query string is invalid and cannot be executed or if the user session has expired.
     */
    @OAuthProtected
    @Processor(friendlyName = "Query")
    @Paged
    public ProviderAwarePagingDelegate<CookBookEntity, CookbookConnector> query(@Query @RefOnly @Default("#[payload]") final String query,
            @RefOnly final PagingConfiguration pagingConfiguration) throws CookbookException {
        Preconditions.checkNotNull(query);
        logger.info("About to execute query '{}'", query);
        try {
            return new QueryPagingDelegate(query, pagingConfiguration);
        } catch (Exception e) {
            logger.error("Unable to execute query '{}'", query, e);
            throw new CookbookException(e);
        }
    }

    /**
     * Basic object mapper that converts any Cookbook entity to a Map.
     *
     * @param entity
     *            provide an Ingredient or Recipe to be converted.
     * @return Map containing key/value pairs of the Ingredient or Recipe Object.
     */
    @Transformer(sourceTypes = {
            Ingredient.class,
            Recipe.class })
    public static Map<String, Object> transformToMap(final CookBookEntity entity) {
        Preconditions.checkNotNull(entity);
        return new ObjectMapper().convertValue(entity, new TypeReference<Map<String, Object>>() {
        });
    }

    /**
     * Auxiliary method to convert a Map entity parameters to a {@link CookBookEntity} object.
     * <p>
     *
     * @param type
     *            {@link EntityType}
     * @param params
     *            Map containing the parameters of the entity to be mapped.
     * @return A {@link CookBookEntity} object.
     * @throws CookbookException
     *             If the {@link CookBookEntity} cannot be generated from the given parameters.
     */
    private CookBookEntity convertToCookBookEntity(@NotNull final EntityType type, @NotNull final Map<String, Object> params) throws CookbookException {
        logger.info("Converting params {} to type {}...", params, type);
        return new ObjectMapper().convertValue(params, EntityType.getClassFromType(type).getClass());
    }

}