package lv.celokopa.app.dto;

import lv.celokopa.app.model.User;
import com.fasterxml.jackson.annotation.JsonFormat;

import java.util.Date;
import org.apache.commons.lang3.StringUtils;


/**
 *
 * JSON-serializable DTO containing user data
 *
 */
public class UserInfoDTO {

    private String userName;
    private String email;
    private String name;
    private String surname;

    @JsonFormat(pattern = "dd.MM.yyyy")
    private Date birthday;

    private String profileImage;
    private String phone;
    private String aboutMe;
    private String car;
    private String carRegNumber;
    private String preferredLanguage;
    private RegistrationType registrationType;

    public UserInfoDTO(String userName, String name, String surname, String email, Date birthday, String phone, String aboutMe,
                       String car, String carRegNumber, String profileImage, String preferredLanguage, RegistrationType registrationType) {
        this.userName = userName;
        this.name = name;
        this.surname = surname;
        this.email = email;
        this.birthday = birthday;
        this.phone = phone;
        this.aboutMe = aboutMe;
        this.car = car;
        this.carRegNumber = carRegNumber;
        this.profileImage = profileImage;
        this.preferredLanguage = preferredLanguage;
        this.registrationType = registrationType;
    }

    public UserInfoDTO(){}


    public static UserInfoDTO mapFromUserEntity(User user) {
        RegistrationType registrationType;
        if(StringUtils.isNotEmpty(user.getFacebookToken())){
            registrationType = RegistrationType.FACEBOOK;
        }else if(StringUtils.isNotEmpty(user.getDraugiemToken())){
            registrationType = RegistrationType.DRAUGIEM;
        }else {
            registrationType = RegistrationType.PLAIN;
        }
        return new UserInfoDTO(user.getUsername(),
                               user.getName(),
                               user.getSurname(),
                               user.getEmail(),
                               user.getBirthday(),
                               user.getPhone(),
                               user.getAboutMe(),
                               user.getCar(),
                               user.getCarRegNumber(),
                               user.getProfileImage(),
                               user.getPreferredLanguage(),
                               registrationType);
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getSurname() {
        return surname;
    }

    public void setSurname(String surname) {
        this.surname = surname;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public Date getBirthday() {
        return birthday;
    }

    public void setBirthday(Date birthday) {
        this.birthday = birthday;
    }


    public String getPhone() {
        return phone;
    }


    public void setPhone(String phone) {
        this.phone = phone;
    }


    public String getCar() {
        return car;
    }

    public void setCar(String car) {
        this.car = car;
    }

    public String getCarRegNumber() {
        return carRegNumber;
    }

    public void setCarRegNumber(String carRegNumber) {
        this.carRegNumber = carRegNumber;
    }

    public String getProfileImage() {
        return profileImage;
    }

    public void setProfileImage(String profileImage) {
        this.profileImage = profileImage;
    }


    public String getAboutMe() {
        return aboutMe;
    }


    public void setAboutMe(String aboutMe) {
        this.aboutMe = aboutMe;
    }


    public String getPreferredLanguage() {
        return preferredLanguage;
    }


    public void setPreferredLanguage(String preferredLanguage) {
        this.preferredLanguage = preferredLanguage;
    }


    public RegistrationType getRegistrationType() {
        return registrationType;
    }


    public void setRegistrationType(RegistrationType registrationType) {
        this.registrationType = registrationType;
    }
}
