package lv.celokopa.app.controllers;

import lv.celokopa.app.model.Drive;
import lv.celokopa.app.oauth.FacebookAuthorizer;
import lv.celokopa.app.services.DriveService;
import lv.celokopa.app.services.UserService;
import lv.celokopa.app.util.FacebookNoteHelper;
import javax.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;


/**
 * Created by alex on 16.1.8.
 */
@Controller
@RequestMapping(value = "/", method = { RequestMethod.PUT, RequestMethod.HEAD })
public class UrlController {

    @Autowired
    private UserService userService;

    @Autowired
    private DriveService driveService;

    @Autowired
    private FacebookAuthorizer facebookAuthorizer;

    @Autowired
    @Qualifier("currentHost")
    String currentHost;

    @Autowired
    @Qualifier("facebookClientId")
    String facebookClientId;

    @Autowired
    @Qualifier("facebookClientSecret")
    String facebookClientSecret;

    @RequestMapping(value="/facebookCallback", method = RequestMethod.GET)
    public String facebookCallback(HttpServletRequest request, @RequestParam("code") String code){
        facebookAuthorizer.authorize(facebookClientId, "http://" + currentHost + "/facebookCallback",
                                                       facebookClientSecret, code, "");

        return "redirect:/";
    }

    @RequestMapping(value="/login", method = RequestMethod.GET)
    public String loginPage(HttpServletRequest request){
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();

        if (auth instanceof AnonymousAuthenticationToken) {
            return "/resources/public/search.html";
        }
        return "redirect:/";
    }

    @RequestMapping(value={"/", "/edit-ride/*", "/activated", "/not-activated", "/restore-password", "/add-ride",
            "/register", "/search-results", "/my-rides", "/profile", "/edit-profile", "/change-password"},
            method = RequestMethod.GET)
    public String searchPage(HttpServletRequest request){
        return "/resources/public/search.html";
    }

    @RequestMapping(value={"/ride/{rideId}"},
            method = RequestMethod.GET)
    public ModelAndView ridePage(HttpServletRequest request, @PathVariable Long rideId){
        ModelAndView modelAndView = new ModelAndView();
        Drive drive = driveService.findDriveById(rideId);
        if(drive == null){
            throw new RuntimeException("drive.not.found");
        }
        modelAndView.addObject("url", "http://" + currentHost + "/ride/" + rideId);
        modelAndView.addObject("type", "article");
        modelAndView.addObject("title", FacebookNoteHelper.createTitle(drive.getDriveFrom(), drive.getDriveTo(), drive.getOwnersLanguage()));
        modelAndView.addObject("description", drive.getTitle());
        modelAndView.addObject("image", FacebookNoteHelper.getLocalityImage(drive.getDriveTo(), currentHost));
        modelAndView.addObject("updated", System.currentTimeMillis());
        modelAndView.setViewName("/resources/public/jsp/ride.jsp");
        return modelAndView;
    }

    @RequestMapping(value="/**", method = RequestMethod.GET)
    public String searchPage1(){
        return "redirect:/";
    }

}
