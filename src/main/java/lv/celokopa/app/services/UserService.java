package lv.celokopa.app.services;

import lv.celokopa.app.CannotCreateUserException;
import lv.celokopa.app.dao.UserRepository;
import lv.celokopa.app.dto.NewUserDTO;
import lv.celokopa.app.dto.UserInfoDTO;
import lv.celokopa.app.model.User;
import lv.celokopa.app.util.EmailSender;
import lv.celokopa.app.util.FileHelper;
import lv.celokopa.app.util.RandomStringGenerator;
import java.util.Date;
import java.util.regex.Pattern;
import org.apache.commons.lang3.StringUtils;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import static lv.celokopa.app.services.ValidationUtils.assertMatches;
import static lv.celokopa.app.services.ValidationUtils.assertMinimumLength;
import static lv.celokopa.app.services.ValidationUtils.assertNotBlank;

/**
 *
 * Business service for User entity related operations
 *
 */
@Service
public class UserService {

    private static final Logger LOGGER = Logger.getLogger(UserService.class);

    private static final Pattern PASSWORD_REGEX = Pattern.compile("(?=.*\\d)(?=.*[a-z])(?=.*[A-Z]).{6,}");

    private static final Pattern EMAIL_REGEX = Pattern.compile("^[_A-Za-z0-9-\\+]+(\\.[_A-Za-z0-9-]+)*@"
            + "[A-Za-z0-9-]+(\\.[A-Za-z0-9]+)*(\\.[A-Za-z]{2,})$");

    @Autowired
    private UserRepository userRepository;

    @Value("${current.host}")
    String currentHost;

    @Value("${picture.file.path}")
    private String path;

    @Value("${email.username}")
    private String username;

    @Value("${email.password}")
    private String password;


    @Transactional
    public void createUser(NewUserDTO newUserDTO, MultipartFile profilePicture) {

        //todo: validate separately
        assertNotBlank(newUserDTO.getUsername(), "Username cannot be empty.");
        assertMinimumLength(newUserDTO.getUsername(), 5, "Username must have at least 5 characters.");
        assertNotBlank(newUserDTO.getEmail(), "Email cannot be empty.");
        assertMatches(newUserDTO.getEmail(), EMAIL_REGEX, "Invalid email.");
        assertNotBlank(newUserDTO.getPlainTextPassword(), "Password cannot be empty.");
        assertMinimumLength(newUserDTO.getPlainTextPassword(), 6, "Password must have at least 6 characters");

        if (!userRepository.isUsernameAvailable(newUserDTO.getUsername())) {
            throw new CannotCreateUserException("error.username.unavailable");
        }

        String profilePictureName = null;

        if (profilePicture != null) {
            String extension = profilePicture.getOriginalFilename().split("\\.")[1];
            profilePictureName = newUserDTO.getUsername() + "." + extension;
            FileHelper.saveFile(path + "/" + profilePictureName, profilePicture);
        }

        String activationCode = RandomStringGenerator.getRandomString(128);
        User user = new User(
                newUserDTO.getUsername(),
                null,
                null,
                null,
                null,
                null,
                null,
                new BCryptPasswordEncoder().encode(newUserDTO.getPlainTextPassword()),
                newUserDTO.getEmail(),
                false,
                activationCode,
                "/resources/static" + profilePictureName,
                newUserDTO.getPreferedLanguage(),
                null,
                null,
                null);

        userRepository.save(user);

        EmailSender.getInstance().sendEmail(
                "Aktivizējiet savu CeļoKopā.lv profilu",
                "Lai aktivizētu jūsu profilu, " + "lūdzu uzspiediet uz hipersaiti: http://" + currentHost + "/api/activate?username=" + user
                        .getUsername() + "&code=" + activationCode,
                user.getEmail(),
                username,
                password);
    }


//    @Transactional
//    public void createFacebookUser(UserInfoDTO dto){
//
//    }

    @Transactional
    public void updateUser(String username, UserInfoDTO userInfoDTO){
        User user = this.findUserByUsername(username);
        user.setName(userInfoDTO.getName());
        user.setSurname(userInfoDTO.getSurname());
        user.setName(userInfoDTO.getName());
        user.setBirthday(userInfoDTO.getBirthday());
        user.setAboutMe(userInfoDTO.getAboutMe());
        user.setCar(userInfoDTO.getCar());
        user.setCarRegNumber(userInfoDTO.getCarRegNumber());
        userRepository.save(user);
    }

    @Transactional
    public void setProfilePicture(String username, MultipartFile profilePicture){
        User user = this.findUserByUsername(username);
        String extension = profilePicture.getOriginalFilename().split("\\.")[1];
        FileHelper.saveFile(path + "/" + username + "." + extension, profilePicture);
        user.setProfileImage("/resources/static/" + username + "." + extension);
        userRepository.save(user);
    }

    @Transactional
    public void removeProfilePicture(String username){
        User user = this.findUserByUsername(username);
        FileHelper.removeFile(path + "/" + user.getProfileImage());
        user.setProfileImage(null);
        userRepository.save(user);
    }


    public User findUserByUsername(String username) {
        return userRepository.findUserByUsername(username);
    }


    @Transactional
    public void findOrCreateFacebookUser(UserInfoDTO dto, String facebookToken, Date tokenExpires)
            throws Exception {
        User user = userRepository.findUserByUsername(dto.getUserName());
        if(user == null){
            user = new User(dto.getUserName(), dto.getName(), dto.getSurname(), dto.getBirthday(), dto.getAboutMe(),
                            dto.getCar(), dto.getCarRegNumber(), null, dto.getEmail(), true, null,
                            dto.getProfileImage(), dto.getPreferredLanguage(), facebookToken, tokenExpires, null);
        }else if(StringUtils.isEmpty(user.getFacebookToken())){
            throw new Exception("This is not a Facebook user.");
        }else {
            //TODO: update rest of the info
            user.setFacebookToken(facebookToken);
        }
        userRepository.save(user);
    }


    @Transactional
    public void findOrCreateDraugiemUser(UserInfoDTO dto, String draugiemToken) throws Exception {
        User user = userRepository.findUserByUsername(dto.getUserName());
        if(user == null){
            user = new User(dto.getUserName(), dto.getName(), dto.getSurname(), dto.getBirthday(), dto.getAboutMe(),
                            dto.getCar(), dto.getCarRegNumber(), null, dto.getEmail(), true, null,
                            dto.getProfileImage(), dto.getPreferredLanguage(), null, null, draugiemToken);
        }else if(StringUtils.isEmpty(user.getDraugiemToken())){
            throw new Exception("This is not a Draugiem.lv user.");
        }else {
            //TODO: update rest of the info
            user.setDraugiemToken(draugiemToken);
        }
        userRepository.save(user);
    }


    @Transactional
    public void updateFacebookToken(){

    }

    @Transactional
    public Boolean activateUser(String username, String activationCode){
        User user = this.findUserByUsername(username);
        if(user != null && !user.getIsActive() && activationCode != null && activationCode.equals(user.getActivationCode())){
            user.setActivationCode(null);
            user.setIsActive(true);
            userRepository.save(user);
            return true;
        }
        return false;
    }

    @Transactional
    public void updatePreferredLanguage(String username, String preferredLanguage){
        User user = this.findUserByUsername(username);
        user.setPreferredLanguage(preferredLanguage);
        userRepository.save(user);
    }

    @Transactional
    public void restorePassword(String loginOrEmail) {
        User user = userRepository.findUserByUsername(loginOrEmail);
        if(user == null){
            user = userRepository.findUserByEmail(loginOrEmail);
        }
        if(user != null) {
            String newPassword = RandomStringGenerator.getRandomString(40);
            String passwordDigest = new BCryptPasswordEncoder().encode(newPassword);
            user.setPasswordDigest(passwordDigest);
            userRepository.save(user);
            EmailSender.getInstance().sendEmail("CeļoKopā.lv - jauna parole", "Jūsu jauna parole ir " +
                    newPassword + ". Ielogojoties http://" + currentHost + "/, jūs varat nomainīt " +
                    "šo paroli savā profilā.", user.getEmail(), username, password);
        }

    }

    @Transactional
    public void changePassword(String name, String oldPassword, String newPassword) {
        User user = userRepository.findUserByUsername(name);
        BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();
        if(user != null && encoder.matches(oldPassword, user.getPasswordDigest())) {
            user.setPasswordDigest(new BCryptPasswordEncoder().encode(newPassword));
            userRepository.save(user);
        }
    }

    public boolean isFullProfile(String userName){
        User user = userRepository.findUserByUsername(userName);
        return user != null && StringUtils.isNotEmpty(user.getName()) && StringUtils.isNotEmpty(user.getSurname())
                && StringUtils.isNotEmpty(user.getCar()) && StringUtils.isNotEmpty(user.getCarRegNumber());
    }
}
