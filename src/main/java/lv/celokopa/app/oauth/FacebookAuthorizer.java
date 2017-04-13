package lv.celokopa.app.oauth;

import lv.celokopa.app.dto.RegistrationType;
import lv.celokopa.app.dto.UserInfoDTO;
import lv.celokopa.app.security.CustomUser;
import lv.celokopa.app.services.UserService;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.util.Arrays;
import java.util.Date;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;


/**
 * Created by avoroncovs on 03.04.2017.
 */
@Component
public class FacebookAuthorizer {

    @Autowired
    private UserService userService;

    public void authorize(
            String clientId, String callbackUrl, String clientSecret, String facebookCode, String preferredLanguage) {

        try {
            JsonObject accessTokenObject = getAccessTokenObject(clientId, callbackUrl, clientSecret, facebookCode);
            String accessToken = accessTokenObject.get("access_token").getAsString();
            Integer expiresInSeconds = accessTokenObject.get("expires_in").getAsInt();

            UserInfoDTO dto = getBasicInfo(preferredLanguage, accessToken);

            userService.findOrCreateFacebookUser(dto, accessToken, new Date(System.currentTimeMillis() + expiresInSeconds * 1000));

            CustomUser loggedInUser = CustomUser.createWithLanguage(dto.getUserName(),
                                                                    "",
                                                                    Arrays.asList(new SimpleGrantedAuthority("ROLE_USER")),
                                                                    preferredLanguage);

            Authentication auth = new UsernamePasswordAuthenticationToken(loggedInUser,
                                                                          null,
                                                                          loggedInUser.getAuthorities());
            SecurityContextHolder.getContext().setAuthentication(auth);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    private JsonObject getAccessTokenObject(
            String clientId, String callbackUrl, String clientSecret, String facebookCode)
            throws IOException {
        String urlGetAccessToken = "https://graph.facebook.com/oauth/access_token?client_id=" + clientId + "&redirect_uri=" + URLEncoder
                .encode(callbackUrl, "UTF-8") + "&client_secret=" + clientSecret + "&code=" + facebookCode;
        URL url = new URL(urlGetAccessToken);
        HttpURLConnection request = (HttpURLConnection) url.openConnection();
        request.connect();
        JsonParser jp = new JsonParser();
        return jp.parse(new InputStreamReader((InputStream) request.getContent())).getAsJsonObject();
    }


    private UserInfoDTO getBasicInfo(String preferredLanguage, String accessToken)
            throws IOException {

        String urlGetUserInfo = "https://graph.facebook.com/v2.8/me?fields=first_name,last_name," + "email,picture.type(large)&access_token=" + accessToken;
        URL url = new URL(urlGetUserInfo);
        HttpURLConnection request = (HttpURLConnection) url.openConnection();
        request.connect();
        JsonParser jp = new JsonParser();
        JsonObject basicInfoObject = jp.parse(new InputStreamReader((InputStream) request.getContent()))
                                       .getAsJsonObject();
        String email = basicInfoObject.get("email").getAsString();
        return new UserInfoDTO(
                email,
                basicInfoObject.get("first_name").getAsString(),
                basicInfoObject.get("last_name").getAsString(),
                basicInfoObject.get("email").getAsString(),
                null,
                null,
                null,
                null,
                null,
                basicInfoObject.get("picture").getAsJsonObject().get("data").getAsJsonObject().get("url").getAsString(),
                preferredLanguage,
                RegistrationType.FACEBOOK);
    }
}
