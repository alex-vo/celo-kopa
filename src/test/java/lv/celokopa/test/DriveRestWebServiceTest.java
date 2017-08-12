package lv.celokopa.test;

import lv.celokopa.app.dao.DriveRepository;
import lv.celokopa.app.model.Drive;
import lv.celokopa.config.root.RootContextConfig;
import lv.celokopa.config.root.TestConfiguration;
import lv.celokopa.config.servlet.ServletContextConfig;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;
import sun.security.acl.PrincipalImpl;

import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

/**
 * Created by alex on 16.2.1.
 */
@RunWith(SpringJUnit4ClassRunner.class)
@WebAppConfiguration
@ActiveProfiles("test")
@ContextConfiguration(classes={TestConfiguration.class, RootContextConfig.class, ServletContextConfig.class})
public class DriveRestWebServiceTest {
    private MockMvc mockMvc;

    @Autowired
    private DriveRepository driveRepository;

    @Autowired
    private WebApplicationContext wac;

    @Before
    public void init()  {
        mockMvc = MockMvcBuilders.webAppContextSetup(this.wac).build();
    }

//    @Test
//    public void testGetDrives() throws Exception {
//        ResultActions r = mockMvc.perform(get("/drive")
//                .accept(MediaType.APPLICATION_JSON)
//                .principal(new PrincipalImpl(UserServiceTest.USERNAME)));
//
//        r.andDo(print()).andExpect(status().isOk())
//                .andExpect(content().contentType("application/json;charset=UTF-8"))
//                .andExpect(jsonPath("$.['drives'].[0].['fromAddress']").value("Brivibas 20"));
//    }

    @Test
    public void testUpdateDrive() throws Exception {
        Drive drive = driveRepository.findDriveById(1L);
        assertTrue(drive.getPrice() == 20.);
        mockMvc.perform(post("/drive")
                .contentType(MediaType.APPLICATION_JSON)
                .content("{\"id\":1,\"title\":\"A drive to Dpilsss\",\"driveFrom\":\"Riga\"," +
                        "\"driveTo\":\"Daugavpils\",\"fromAddress\":\"Brivibas 20\",\"toAddress\":\"Skolas iela 25\"," +
                        "\"departureTime\":\"2016-01-03 12:25\",\"arrivalTime\":\"2016-01-03 15:25\"," +
                        "\"text\":\"I go Dpils\",\"price\":30.0,\"carRegNumber\":\"BA-8283\",\"placesLeft\":3," +
                        "\"placesOverall\":4}")
                .accept(MediaType.APPLICATION_JSON)
                .principal(new PrincipalImpl(UserServiceTest.USERNAME)))
                .andDo(print())
                .andExpect(status().isOk());
        drive = driveRepository.findDriveById(1L);
        assertTrue(drive.getPrice() == 30.);
    }

    @Test
    public void testAddDrive() throws Exception {
        mockMvc.perform(post("/drive")
                .contentType(MediaType.APPLICATION_JSON)
                .content("{\"title\":\"A drive to Cesis\",\"driveFrom\":\"Sigulda\"," +
                        "\"driveTo\":\"Cesis\",\"fromAddress\":\"Cesu 12\",\"toAddress\":\"Skolas iela 25\"," +
                        "\"departureTime\":\"2016-01-03 12:25\",\"arrivalTime\":\"2016-01-03 15:25\"," +
                        "\"text\":\"I go Dpils\",\"price\":30.0,\"carRegNumber\":\"BA-8283\",\"placesLeft\":3," +
                        "\"placesOverall\":4}")
                .accept(MediaType.APPLICATION_JSON)
                .principal(new PrincipalImpl(UserServiceTest.USERNAME)))
                .andDo(print())
                .andExpect(status().isOk());
        Drive drive = driveRepository.findDriveById(4L);
        assertTrue("Sigulda".equals(drive.getDriveFrom()) && "Cesu 12".equals(drive.getFromAddress()));
    }

    @Test
    public void testDeleteDrive() throws Exception {
        mockMvc.perform(delete("/drive")
                .contentType(MediaType.APPLICATION_JSON)
                .content("3")
                .accept(MediaType.APPLICATION_JSON)
                .principal(new PrincipalImpl(UserServiceTest.USERNAME)))
                .andDo(print())
                .andExpect(status().isOk());
        Drive drive = driveRepository.findDriveById(3L);
        assertNull(drive);
    }
}
