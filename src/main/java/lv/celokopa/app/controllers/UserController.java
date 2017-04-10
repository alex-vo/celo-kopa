package lv.celokopa.app.controllers;


import lv.celokopa.app.CannotCreateUserException;
import lv.celokopa.app.dto.NewUserDTO;
import lv.celokopa.app.dto.UserInfoDTO;
import lv.celokopa.app.model.User;
import lv.celokopa.app.services.UserService;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.security.Principal;

/**
 *
 *  REST service for users.
 *
 */

@Controller
@RequestMapping(value = "/api/user")
public class UserController {

    private static final Logger LOGGER = Logger.getLogger(UserController.class);

    @Autowired
    private UserService userService;

    @ResponseBody
    @ResponseStatus(HttpStatus.OK)
    @RequestMapping(method = RequestMethod.GET)
    public UserInfoDTO getUserInfo(Principal principal) {

        User user = userService.findUserByUsername(principal.getName());

        return user != null ? UserInfoDTO.mapFromUserEntity(user) : null;
    }


    @ResponseStatus(HttpStatus.OK)
    @RequestMapping(method = RequestMethod.POST, consumes = {"multipart/form-data"})
    public void createUser(@RequestPart(value = "data") NewUserDTO user,
                           @RequestPart(required = false, value = "profilePicture") MultipartFile profilePicture) {

        userService.createUser(user, profilePicture);
    }

    @ResponseStatus(HttpStatus.OK)
    @RequestMapping(method = RequestMethod.POST, value = "setLanguage")
    public void setPreferredLanguage(Principal principal, @RequestParam(value = "lang")  String preferredLanguage) {
        userService.updatePreferredLanguage(principal.getName(), preferredLanguage);
    }

    @ResponseStatus(HttpStatus.OK)
    @RequestMapping(method = RequestMethod.PUT)
    public void updateUser(Principal principal, @RequestBody UserInfoDTO user) {
        //todo check social networks
        userService.updateUser(principal.getName(), user);
    }

    @ResponseStatus(HttpStatus.OK)
    @RequestMapping(method = RequestMethod.POST, value = "removePicture")
    public void removePicture(Principal principal) {
        userService.removeProfilePicture(principal.getName());
    }

    @ResponseStatus(HttpStatus.OK)
    @RequestMapping(method = RequestMethod.POST, value = "changePicture")
    public void changePicture(Principal principal, @RequestPart(value = "profilePicture") MultipartFile profilePicture) {
        userService.setProfilePicture(principal.getName(), profilePicture);
    }

    @RequestMapping(method = RequestMethod.GET, value={"/checkFullProfile"})
    public ResponseEntity<String> checkFullProfile(Principal principal){
        if(!userService.isFullProfile(principal.getName())){
            return new ResponseEntity<>("", HttpStatus.BAD_REQUEST);
        }
        return new ResponseEntity<>("", HttpStatus.OK);
    }

    @ExceptionHandler(CannotCreateUserException.class)
    @ResponseStatus(HttpStatus.NOT_ACCEPTABLE)
    public @ResponseBody String handleAppException(CannotCreateUserException ex) {
        return ex.getErrorMessage();
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<String> errorHandler(Exception exc) {
        LOGGER.error(exc.getMessage(), exc);
        return new ResponseEntity<>(exc.getMessage(), HttpStatus.BAD_REQUEST);
    }

}
