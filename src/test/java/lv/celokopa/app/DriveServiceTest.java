package lv.celokopa.app;

import lv.celokopa.app.model.Drive;
import lv.celokopa.app.model.SearchResult;
import lv.celokopa.app.services.DriveService;
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
 * Created by alex on 15.31.12.
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ActiveProfiles("test")
@ContextConfiguration(classes={TestConfiguration.class, RootContextConfig.class})
public class DriveServiceTest {

    @Autowired
    private DriveService driveService;

    @Test
    public void testGetDrives(){
        SearchResult<Drive> res = driveService.findDrives("test123");
        assertTrue("Result not expected, total " + res.getResultsCount(), res.getResultsCount() == 3);
    }
}
