package lv.celokopa.app.controllers;

import lv.celokopa.app.services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;

/**
 * Created by alex on 16.4.12.
 */
@Controller
@RequestMapping("/api/password")
public class PasswordController {

    @Autowired
    UserService userService;

    @ResponseBody
    @ResponseStatus(HttpStatus.OK)
    @RequestMapping(method = RequestMethod.POST, value = "restore")
    public void passwordLost(@RequestParam("id") String loginOrEmail){
        //TODO: validate
        userService.restorePassword(loginOrEmail);
    }

    @ResponseBody
    @ResponseStatus(HttpStatus.OK)
    @RequestMapping(method = RequestMethod.POST, value = "change")
    public void changePassword(Principal principal,
                               @RequestParam("oldPassword") String oldPassword,
                               @RequestParam("newPassword") String newPassword){
        //TODO: validate
        userService.changePassword(principal.getName(), oldPassword, newPassword);
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<String> errorHandler(Exception exc) {
        return new ResponseEntity<>(exc.getMessage(), HttpStatus.BAD_REQUEST);
    }
}
