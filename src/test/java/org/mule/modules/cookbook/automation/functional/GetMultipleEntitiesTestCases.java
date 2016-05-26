package org.mule.modules.cookbook.automation.functional;

import com.cookbook.tutorial.service.CookBookEntity;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;

import com.cookbook.tutorial.service.NoSuchEntityException;
import org.junit.Test;
import org.mule.modules.cookbook.exception.CookbookException;

import java.util.Arrays;
import java.util.List;

public class GetMultipleEntitiesTestCases extends AbstractTestCases {

    @Test
    public void testGetMultipleEntities() throws CookbookException {
        List<CookBookEntity> entities = getConnector().getMultipleEntities(Arrays.asList(TestDataBuilder.getMultipleEntitiesIDs()));
        assertThat(entities, notNullValue());
        assertThat(entities.size(), is(5));
    }

    @Test
    public void testGetMultipleNonExistentEntities() throws CookbookException {
        try{
            getConnector().getMultipleEntities(Arrays.asList(TestDataBuilder.getMultipleInvalidEntitiesIDs()));
        } catch(CookbookException e){
            assertThat(e.getCause(), instanceOf(NoSuchEntityException.class));
        }
    }

}
