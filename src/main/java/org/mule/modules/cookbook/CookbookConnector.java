/**
 * (c) 2003-2016 MuleSoft, Inc. The software in this package is
 * published under the terms of the CPAL v1.0 license, a copy of which
 * has been included with this distribution in the LICENSE.md file.
 */
package org.mule.modules.cookbook;

import com.cookbook.tutorial.client.ICookbookCallback;
import com.cookbook.tutorial.service.CookBookEntity;
import com.cookbook.tutorial.service.Description;
import com.cookbook.tutorial.service.Ingredient;
import com.cookbook.tutorial.service.InvalidEntityException;
import com.cookbook.tutorial.service.InvalidTokenException;
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
import org.mule.api.annotations.Query;
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
import org.mule.modules.cookbook.datasense.EntityMetaData;
import org.mule.modules.cookbook.exception.CookbookException;
import org.mule.modules.cookbook.pagination.QueryPagingDelegate;
import org.mule.modules.cookbook.utils.EntityType;
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
@MetaDataScope(EntityMetaData.class)
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
     * Allows the caller to creates a new Ingredient or Recipe.
     * <p/>
     *
     * @param type   Type of entity: {@link Ingredient} or {@link Recipe}.
     * @param entity  Map containing the data of the entity to be created.
     * @return {@link CookBookEntity} The created entity.
     * @throws CookbookException If any of the provided entity parameters is incorrect/missing or if the user session has expired.
     */
    @OAuthProtected
    @Processor(friendlyName = "Create Entity")
    public CookBookEntity create(@MetaDataKeyParam(affects = MetaDataKeyParamAffectsType.BOTH) final String type,
            @Default("#[payload]") @RefOnly final Map<String, Object> entity) throws CookbookException {
        Preconditions.checkNotNull(entity);
        try {
            return config.getClient().create(getCookBookEntity(EntityType.find(type), entity));
        } catch(InvalidEntityException | SessionExpiredException e){
            throw new CookbookException(e);
        }
    }

    /**
     * Allows de caller to create multiple new Ingredients and/or Recipes in a single execution.
     * <p/>
     *
     * @param entities   List of {@link CookBookEntity} entities to be created. It can contain a combination of Ingredients and Recipes.
     * @return List <{@link CookBookEntity}> The list of created entities.
     * @throws CookbookException If any of the provided entity parameters is incorrect/missing or if the user session has expired.
     */
    @OAuthProtected
    @Processor(friendlyName = "Create Multiple Entities")
    public List<CookBookEntity> createMultipleEntities(@Default("#[payload]") @RefOnly final List<CookBookEntity> entities) throws CookbookException {
        Preconditions.checkNotNull(entities);
        try{
            return config.getClient().addList(entities);
        } catch(InvalidEntityException | SessionExpiredException e){
            throw new CookbookException(e);
        }
    }

    /**
     * Allows the caller to delete an entity (Ingredient or Recipe) by its ID.
     * <p/>
     *
     * @param id ID of the entity to delete.
     * @throws CookbookException If the specified entity does not exist or if the user session has expired.
     */
    @OAuthProtected
    @Processor(friendlyName = "Delete Entity")
    public void delete(@Default("1") final Integer id) throws CookbookException {
        Preconditions.checkNotNull(id);
        try{
            config.getClient().delete(id);
        } catch(NoSuchEntityException | SessionExpiredException e){
            throw new CookbookException(e);
        }
    }

    /**
     * Allows the caller to delete multiple entities (Ingredient or Recipe) by their ID.
     * <p/>
     *
     * @param entityIds List of IDs of the entities to delete.
     * @throws CookbookException If the specified entity does not exist or if the user session has expired.
     */
    @OAuthProtected
    @Processor(friendlyName = "Delete Multiple Entities by ID")
    public void deleteMultipleEntities(final List<Integer> entityIds) throws CookbookException {
        Preconditions.checkNotNull(entityIds);
        try{
            config.getClient().deleteList(entityIds);
        } catch(NoSuchEntityException | SessionExpiredException e){
            throw new CookbookException(e);
        }
    }

    /**
     * Allows the caller to retrieve all fields and data types of a Ingredient or Recipe object.
     * <p/>
     *
     * @param type Type of entity: Ingredient or Recipe.
     * @throws CookbookException If the provided token is invalid, if the provided parameter is invalid, if the entity type does not exist, or if the user session has expired.
     */
    @OAuthProtected
    @Processor(friendlyName = "Describe Entity")
    public Description describeEntity(@MetaDataKeyParam(affects = MetaDataKeyParamAffectsType.OUTPUT) final String type)
            throws CookbookException {
        Preconditions.checkNotNull(type);
        try{
            return config.getClient().describeEntity(EntityType.getClassFromType(EntityType.find(type)));
        } catch(InvalidTokenException | InvalidEntityException | NoSuchEntityException | SessionExpiredException e){
            throw new CookbookException(e);
        }
    }

    /**
     * Allows the caller to retrieve a {@link CookBookEntity} based on its ID.
     * <p/>
     *
     * @param type Type of the entity.
     * @param id   ID of the entity.
     * @return {@link CookBookEntity} The requested {@link CookBookEntity}.
     * @throws CookbookException If the specified entity does not exist or if the user session has expired.
     */
    @OAuthProtected
    @Processor(friendlyName = "Get Entity")
    public CookBookEntity get(@MetaDataKeyParam(affects = MetaDataKeyParamAffectsType.OUTPUT) final String type, @Default("1") final Integer id)
            throws CookbookException {
        Preconditions.checkNotNull(type);
        Preconditions.checkNotNull(id);
        try{
            CookBookEntity entity = config.getClient().get(id);
            CookBookEntity entityClazz = EntityType.getClassFromType(EntityType.find(type));

            if(entity.getClass() == entityClazz.getClass()){
                return entity;
            }
            else{
                throw new CookbookException("No entity of type " + type + " was found for ID " + id);
            }
        } catch(NoSuchEntityException | SessionExpiredException e){
            throw new CookbookException(e);
        }
    }

    /**
     * Allows the caller to retrieve the list of available entities on which to perform operations. Currently, {@link Ingredient} and {@link Recipe} are the only available options.
     * <p/>
     * @return List of available {@link CookBookEntity}.
     * @throws CookbookException If the user session has expired.
     */
    @OAuthProtected
    @Processor(friendlyName = "Get Available Entities")
    public List<CookBookEntity> getEntities() throws CookbookException {
        try{
            return config.getClient().getEntities();
        } catch(SessionExpiredException e){
            throw new CookbookException(e);
        }
    }

    /**
     * Allows the caller to retrieve multiple entities by their IDs.
     * <p/>
     * @return The requested list of {@link CookBookEntity}.
     * @throws CookbookException If the specified entity does not exist or if the user session has expired.
     */
    @OAuthProtected
    @Processor(friendlyName = "Get Multiple Entities by ID")
    public List<CookBookEntity> getMultipleEntities(@Default("#[payload]") @RefOnly final List<Integer> entityIds) throws CookbookException {
        Preconditions.checkNotNull(entityIds);
        try{
            return config.getClient().getList(entityIds);
        } catch(NoSuchEntityException | SessionExpiredException e){
            throw new CookbookException(e);
        }
    }

    /**
     * Allows the caller to return the list of recently added Recipes.
     * <p/>
     *
     * @return List of recently added {@link Recipe}.
     */
    @OAuthProtected
    @Processor(friendlyName = "Get Recently Added Recipes")
    public List<Recipe> getRecentlyAdded() {
        return config.getClient().getRecentlyAdded();
    }

    /**
     * Polls the system every 10 seconds (default value) to fetch the recently added Recipes.
     * <p/>
     *
     * @param callback A {@link SourceCallback} object that will hook the result into the Mule Event.
     * @throws CookbookException If the poll connection is interrupted or an error occurred in the Cookbook service.
     */
    @OAuthProtected
    @Source(friendlyName = "Poll Recently Added Recipes", sourceStrategy = SourceStrategy.POLLING, pollingPeriod = 10000)
    public void getRecentlyAddedSource(final SourceCallback callback) throws CookbookException {
        Preconditions.checkNotNull(callback);
        if (config.getClient() != null) {
            try{
                // Every 5 seconds our callback will be executed
                this.getConfig().getClient().getRecentlyAdded(new ICookbookCallback() {

                    public void execute(List<Recipe> recipes) throws Exception {
                        callback.process(recipes);
                    }
                });
                if (Thread.interrupted()) {
                    throw new CookbookException("Connection poll was interrupted.");
                }
            } catch(Exception e){
                throw new CookbookException(e);
            }
        }
    }

    /**
     * Allows the caller to update an existing {@link CookBookEntity}.
     * <p/>
     *
     * @param type   Type of the entity.
     * @param entity Map containing the data of the entity to be updated.
     * @return The updated {@link CookBookEntity}.
     * @throws CookbookException If any of the provided entity parameters is incorrect/missing, if the entity does not exist, or if the user session has expired.
     */
    @OAuthProtected
    @Processor(friendlyName = "Update Entity")
    public CookBookEntity update(@MetaDataKeyParam(affects = MetaDataKeyParamAffectsType.BOTH) final String type,
            @Default("#[payload]") @RefOnly final Map<String, Object> entity) throws CookbookException {
        Preconditions.checkNotNull(type);
        Preconditions.checkNotNull(entity);
        try {
            return config.getClient().update(getCookBookEntity(EntityType.find(type), entity));
        } catch(InvalidEntityException | NoSuchEntityException | SessionExpiredException e){
            throw new CookbookException(e);
        }
    }
    /**
     * Allows the caller to update one or more existing {@link CookBookEntity}.
     * <p/>
     *
     * @param entities List of {@link CookBookEntity} to be updated.
     * @return The updated list of {@link CookBookEntity}.
     * @throws CookbookException If any of the provided entity parameters is incorrect/missing, if the entity does not exist, or if the user session has expired.
     */
    @OAuthProtected
    @Processor(friendlyName = "Update Multiple Entities")
    public List<CookBookEntity> updateMultipleEntities(@MetaDataKeyParam(affects = MetaDataKeyParamAffectsType.BOTH) @Default("#[payload]") @RefOnly final List<CookBookEntity> entities) throws CookbookException {
        Preconditions.checkNotNull(entities);
        try {
            return config.getClient().updateList(entities);
        } catch(InvalidEntityException | NoSuchEntityException | SessionExpiredException e){
            throw new CookbookException(e);
        }
    }

    /**
     * Allows the caller to execute simple DSQL queries to the Cookbook service.
     * <p/>
     *
     * @param query The query is DSQL format.
     * @param pagingConfiguration The {@link PagingConfiguration} object with the needed parameters for paged queries.
     * @return The {@link QueryPagingDelegate} that handles the calls to Cookbook to run the query.
     * @throws CookbookException If the provided query string is invalid and cannot be executed or if the user session has expired.
     */
    @OAuthProtected
    @Processor(friendlyName = "Query Entities")
    @Paged
    public ProviderAwarePagingDelegate<CookBookEntity, CookbookConnector> queryEntities(@Query final String query, @RefOnly final PagingConfiguration pagingConfiguration)
            throws CookbookException {
        Preconditions.checkNotNull(query);
        try{
            return new QueryPagingDelegate(query, pagingConfiguration);
        } catch(Exception e){
            throw new CookbookException(e);
        }
    }

    /**
     * Auxiliary method to convert a Map entity parameters to a {@link CookBookEntity} object.
     * <p/>
     *
     * @param type {@link EntityType}
     * @param entity Map containing the parameters of the entity to be mapped.
     * @return A {@link CookBookEntity} object.
     * @throws InvalidEntityException If the provided entity type is invalid.
     */
    private CookBookEntity getCookBookEntity(@NotNull final EntityType type,
            @NotNull @Default("#[payload]") @RefOnly final Map<String, Object> entity) throws InvalidEntityException {
        ObjectMapper mapper = new ObjectMapper();
        if (type == EntityType.RECIPE) {
            return mapper.convertValue(entity, Recipe.class);
        } else if (type == EntityType.INGREDIENT) {
            return mapper.convertValue(entity, Ingredient.class);
        } else {
            throw new InvalidEntityException("Could not handle type:" + type);
        }
    }

}