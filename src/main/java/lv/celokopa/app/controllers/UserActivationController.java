package lv.celokopa.app.controllers;

import lv.celokopa.app.services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

/**
 * Created by alex on 16.19.7.
 */
@Controller
@RequestMapping("/api/activate")
public class UserActivationController {

    @Autowired
    UserService userService;

    //TODO no html

    @RequestMapping(method = RequestMethod.GET)
    public String activate(@RequestParam(value = "username") String username,
                           @RequestParam(value = "code") String activationCode){

        if(userService.activateUser(username, activationCode)) {
            return "redirect:/activated";
        }
        return "redirect:/not-activated";
    }
}
