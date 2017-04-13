package lv.celokopa.app.validator;

import lv.celokopa.app.model.User;
import org.apache.commons.lang3.StringUtils;


/**
 * Created by avoroncovs on 12.04.2017.
 */
public class UserValidator {

    public static void validatePasswordChange(User user){
        if(StringUtils.isNotEmpty(user.getFacebookToken()) || StringUtils.isNotEmpty(user.getDraugiemToken())){
            throw new RuntimeException("Cannot change passwords for social network users.");
        }
    }


    public static void validateUserForCreatingRide(User user) {
        if (StringUtils.isEmpty(user.getName()) || StringUtils.isEmpty(user.getSurname())
                || StringUtils.isEmpty(user.getEmail()) || user.getBirthday() == null
                || StringUtils.isEmpty(user.getPhone())) {
            throw new RuntimeException("Incomplete user profile for creating a ride.");
        }
    }

}
