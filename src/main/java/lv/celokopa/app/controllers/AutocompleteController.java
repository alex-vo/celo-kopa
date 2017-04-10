package lv.celokopa.app.controllers;

import lv.celokopa.app.dto.LocalitiesDTO;
import lv.celokopa.app.model.Locality;
import lv.celokopa.app.model.SearchResult;
import lv.celokopa.app.services.LocalityService;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

/**
 * Created by alex on 16.3.1.
 */
@Controller
@RequestMapping("/api/autocomplete")
public class AutocompleteController {
    Logger LOGGER = Logger.getLogger(AutocompleteController.class);

    @Autowired
    private LocalityService localityService;

    @ResponseBody
    @RequestMapping(method = RequestMethod.GET)
    public LocalitiesDTO autoComplete(@RequestParam("predicate") String predicate) {

        SearchResult<Locality> result = localityService.findLocalities(predicate);
        return LocalitiesDTO.mapFromLocalityEntities(result.getResult());
    }
}
