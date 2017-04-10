package lv.celokopa.app.controllers;

import lv.celokopa.app.dto.DriveDTO;
import lv.celokopa.app.dto.DrivesDTO;
import lv.celokopa.app.dto.SearchDriveDTO;
import lv.celokopa.app.model.Drive;
import lv.celokopa.app.model.SearchResult;
import lv.celokopa.app.services.DriveService;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;

/**
 * Created by alex on 15.30.12.
 */
@Controller
@RequestMapping("/api/drive")
public class DriveController {
    Logger LOGGER = Logger.getLogger(DriveController.class);

    @Autowired
    private DriveService driveService;

    @ResponseBody
    @ResponseStatus(HttpStatus.OK)
    @RequestMapping(method = RequestMethod.GET)
    public DrivesDTO searchDrives(Principal principal) {

        SearchResult<Drive> result = driveService.findDrives(principal.getName());

        Long resultsCount = result.getResultsCount();

        return new DrivesDTO(DriveDTO.mapFromDrivesEntities(result.getResult()));
    }

    @ResponseBody
    @ResponseStatus(HttpStatus.OK)
    @RequestMapping(method = RequestMethod.POST)
    public void saveDrive(Principal principal, @RequestBody DriveDTO driveDTO)
            throws Exception {

        //TODO validate
        driveService.saveDrive(principal.getName(), driveDTO);
    }

    @ResponseBody
    @ResponseStatus(HttpStatus.OK)
    @RequestMapping(method = RequestMethod.DELETE)
    public void deleteDrive(Principal principal, @RequestBody Long driveId)
            throws Exception {
        driveService.deleteDrive(principal.getName(), driveId);
    }

    @ResponseBody
    @ResponseStatus(HttpStatus.OK)
    @RequestMapping(value = "{id}", method = RequestMethod.GET)
    public DriveDTO getDrive(Principal principal, @PathVariable Long id) {

        //TODO check ownship
        Drive result = driveService.findDriveById(id);

        return DriveDTO.mapFromDriveEntity(result);
    }

    @ResponseBody
    @ResponseStatus(HttpStatus.OK)
    @RequestMapping(method = RequestMethod.POST, value = "find")
    public DrivesDTO findDrives(@RequestBody SearchDriveDTO dto) {

        SearchResult<Drive> result = driveService.findDrives(dto.getFrom(), dto.getTo());

        return DrivesDTO.mapFromDrivesEntities(result.getResult());
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<String> errorHandler(Exception exc) {
        LOGGER.error(exc.getMessage(), exc);
        return new ResponseEntity<>(exc.getMessage(), HttpStatus.BAD_REQUEST);
    }
}
