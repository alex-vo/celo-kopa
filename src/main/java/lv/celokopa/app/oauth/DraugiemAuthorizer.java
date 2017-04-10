package lv.celokopa.app.oauth;

import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.List;
import java.util.Locale;
import lv.celokopa.app.dto.RegistrationType;
import lv.celokopa.app.dto.UserInfoDTO;
import lv.celokopa.app.security.CustomUser;
import lv.celokopa.app.services.UserService;
import org.apache.commons.lang3.LocaleUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;


/**
 * Created by avoroncovs on 10.04.2017.
 */
@Component
public class DraugiemAuthorizer {

    @Autowired
    private UserService userService;


    public void authorize(String appKey, String code) {

        try {
            JsonObject draugiemJsonObject = getAccessTokenObject(appKey, code);
            String draugiemToken = draugiemJsonObject.get("apikey").getAsString();

            UserInfoDTO dto = getBasicInfo(draugiemJsonObject);

            userService.findOrCreateDraugiemUser(dto, draugiemToken);

            CustomUser loggedInUser = CustomUser.createWithLanguage(dto.getUserName(),
                                                                    "",
                                                                    Arrays.asList(new SimpleGrantedAuthority("ROLE_USER")),
                                                                    dto.getPreferredLanguage());

            Authentication auth = new UsernamePasswordAuthenticationToken(loggedInUser,
                                                                          null,
                                                                          loggedInUser.getAuthorities());
            SecurityContextHolder.getContext().setAuthentication(auth);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    private JsonObject getAccessTokenObject(String appKey, String code)
            throws IOException {
        String urlGetAccessToken = "http://api.draugiem.lv/json/?app=" + appKey + "&code=" + code + "&action=authorize";
        URL url = new URL(urlGetAccessToken);
        HttpURLConnection request = (HttpURLConnection) url.openConnection();
        request.connect();
        JsonParser jp = new JsonParser();
        return jp.parse(new InputStreamReader((InputStream) request.getContent())).getAsJsonObject();
    }


    private UserInfoDTO getBasicInfo(JsonObject jsonObject)
            throws ParseException {
        JsonObject userObject = jsonObject.get("users").getAsJsonArray().get(0).getAsJsonObject();

        List<Locale> localeList = LocaleUtils.languagesByCountry(jsonObject.get("language").getAsString());
        String language = localeList.isEmpty() ? "lv_LV" : localeList.get(0).toString();

        return new UserInfoDTO(userObject.get("uid").getAsString(),
                                          userObject.get("name").getAsString(),
                                          userObject.get("surname").getAsString(),
                                          userObject.get("emailHash").getAsString(),
                                          new SimpleDateFormat("yyyy-MM-dd").parse(userObject.get("birthday")
                                                                                             .getAsString()),
                                          null,
                                          null,
                                          null,
                                          userObject.get("img").getAsString(),
                                          language,
                                          RegistrationType.DRAUGIEM);
    }

}
