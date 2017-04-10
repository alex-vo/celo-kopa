package lv.celokopa.app;

import lv.celokopa.app.model.Locality;
import lv.celokopa.app.model.SearchResult;
import lv.celokopa.app.services.LocalityService;
import lv.celokopa.config.root.RootContextConfig;
import lv.celokopa.config.root.TestConfiguration;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import static org.junit.Assert.assertTrue;

/**
 * Created by alex on 16.10.5.
 */

@RunWith(SpringJUnit4ClassRunner.class)
@ActiveProfiles("test")
@ContextConfiguration(classes={TestConfiguration.class, RootContextConfig.class})
public class LocalityServiceTest {
    @Autowired
    LocalityService localityService;

    @Test
    public void testFindLocalities(){
        SearchResult<Locality> result = localityService.findLocalities("Rīga");
        assertTrue("Result not expected, total " + result.getResultsCount(),
                result.getResultsCount() == 1);
    }
}
