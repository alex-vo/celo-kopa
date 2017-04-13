package lv.celokopa.app.model;


import javax.persistence.Entity;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;
import java.util.Date;

/**
 *
 * The User JPA entity.
 *
 */
@Entity
@Table(name = "USERS")
@NamedQueries({
        @NamedQuery(
                name = User.FIND_BY_USERNAME,
                query = "select u from User u where username = :username"
        ),
        @NamedQuery(
                name = User.FIND_BY_EMAIL,
                query = "select u from User u where email = :email"
        )
})
public class User extends AbstractEntity {

    public static final String FIND_BY_USERNAME = "user.findByUserName";
    public static final String FIND_BY_EMAIL = "user.findByEmail";

    private String username;
    private String name;
    private String surname;
    private Date birthday;
    private String phone;
    private String aboutMe;
    private String car;
    private String carRegNumber;
    private String passwordDigest;
    private String email;
    private Boolean isActive;
    private String activationCode;
    private String profileImage;
    private String preferredLanguage;
    private String facebookToken;
    private Date facebookTokenExpires;
    private String draugiemToken;

    public User() {

    }

    public User(String username, String name, String surname, Date birthday, String aboutMe, String car, String carRegNumber,
                String passwordDigest, String email, Boolean isActive, String activationCode, String image,
                String language, String facebookToken, Date facebookTokenExpires, String draugiemToken) {
        this.username = username;
        this.name = name;
        this.surname = surname;
        this.birthday = birthday;
        this.aboutMe = aboutMe;
        this.car = car;
        this.carRegNumber = carRegNumber;
        this.passwordDigest = passwordDigest;
        this.email = email;
        this.isActive = isActive;
        this.activationCode = activationCode;
        this.profileImage = image;
        this.preferredLanguage = language;
        this.facebookToken = facebookToken;
        this.facebookTokenExpires = facebookTokenExpires;
        this.draugiemToken = draugiemToken;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
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


    public String getAboutMe() {
        return aboutMe;
    }


    public void setAboutMe(String aboutMe) {
        this.aboutMe = aboutMe;
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

    public String getPasswordDigest() {
        return passwordDigest;
    }

    public void setPasswordDigest(String passwordDigest) {
        this.passwordDigest = passwordDigest;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public Boolean getIsActive() {
        return isActive;
    }

    public void setIsActive(Boolean isActive) {
        this.isActive = isActive;
    }

    public String getActivationCode() {
        return activationCode;
    }

    public void setActivationCode(String activationCode) {
        this.activationCode = activationCode;
    }

    public String getProfileImage() {
        return profileImage;
    }

    public void setProfileImage(String profileImage) {
        this.profileImage = profileImage;
    }


    public String getPreferredLanguage() {
        return preferredLanguage;
    }


    public void setPreferredLanguage(String preferredLanguage) {
        this.preferredLanguage = preferredLanguage;
    }


    public String getFacebookToken() {
        return facebookToken;
    }


    public void setFacebookToken(String facebookToken) {
        this.facebookToken = facebookToken;
    }


    public Date getFacebookTokenExpires() {
        return facebookTokenExpires;
    }


    public void setFacebookTokenExpires(Date facebookTokenExpires) {
        this.facebookTokenExpires = facebookTokenExpires;
    }


    public String getDraugiemToken() {
        return draugiemToken;
    }


    public void setDraugiemToken(String draugiemToken) {
        this.draugiemToken = draugiemToken;
    }


    @Override
    public String toString() {
        return "User{" +
                "username='" + username + '\'' +
                ", email='" + email + '\'' +
                '}';
    }
}
