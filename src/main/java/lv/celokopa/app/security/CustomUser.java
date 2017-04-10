package lv.celokopa.app.security;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.User;

import java.util.Collection;

/**
 * Created by alex on 16.13.11.
 */
public class CustomUser extends User {
    
    private String language;

    private CustomUser(String username, String password, Collection<? extends GrantedAuthority> authorities) {
        super(username, password, authorities);
    }

    public static CustomUser createWithLanguage(String username, String password,
                                                Collection<? extends GrantedAuthority> authorities,
                                                String language){
        CustomUser customUser = new CustomUser(username, password, authorities);
        customUser.setLanguage(language);
        return customUser;
    }

    public String getLanguage() {
        return language;
    }

    public void setLanguage(String language) {
        this.language = language;
    }
}
