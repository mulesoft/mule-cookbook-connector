/**
 * (c) 2003-2016 MuleSoft, Inc. The software in this package is
 * published under the terms of the CPAL v1.0 license, a copy of which
 * has been included with this distribution in the LICENSE.md file.
 */
package org.mule.modules.cookbook.pagination;

import com.cookbook.tutorial.service.CookBookEntity;
import com.cookbook.tutorial.service.InvalidEntityException;
import com.cookbook.tutorial.service.InvalidRequestException;
import com.cookbook.tutorial.service.SessionExpiredException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.mule.api.MuleException;
import org.mule.modules.cookbook.CookbookConnector;
import org.mule.modules.cookbook.exception.CookbookException;
import org.mule.streaming.PagingConfiguration;
import org.mule.streaming.ProviderAwarePagingDelegate;

import java.util.List;
import java.util.Map;

public class CookbookPagingDelegate extends ProviderAwarePagingDelegate<CookBookEntity, CookbookConnector> {

    private final String query;
    private final Integer pageSize;
    private Integer currentPage = 0;

    public CookbookPagingDelegate(String query, final PagingConfiguration pagingConfiguration) {
        super();
        this.query = query;
        this.pageSize = pagingConfiguration.getFetchSize();
    }

    public void close() throws MuleException {
        this.currentPage = 0;
    }

    /**
     * Returns the next page of items. If the return value is <code>null</code> or an empty list, then it means no more items are available
     *
     * @param provider The provider to be used to do the query. You can assume this provider is already properly initialised
     * @return a populated list of elements. Returning <code>null</code> or an empty list, means no more items are available.
     * @throws Exception
     */
    @Override
    public List<CookBookEntity> getPage(final CookbookConnector provider) throws Exception {
        try {
            return provider.getConfig().getClient().searchWithQuery(query, currentPage++, pageSize);
        } catch (InvalidRequestException | SessionExpiredException e) {
            // Revert the increment since we want to retry to get the same page if the reconnection is configured.
            currentPage--;
            throw e;
        }
    }

    /**
     * Returns the total amount of items in the non-paged result set.
     * <p/>
     * In some scenarios, it might not be possible/convenient to actually retrieve this value. -1 is returned in such a case.
     *
     * @param provider The provider to be used to do the query. You can assume this provider is already properly initialised
     */
    @Override
    public int getTotalResults(final CookbookConnector provider) throws Exception {
        return -1;
    }

}
