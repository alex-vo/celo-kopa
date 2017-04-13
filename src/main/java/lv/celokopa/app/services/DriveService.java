package lv.celokopa.app.services;

import java.util.List;
import lv.celokopa.app.dao.DriveRepository;
import lv.celokopa.app.dao.UserRepository;
import lv.celokopa.app.dto.DriveDTO;
import lv.celokopa.app.model.Drive;
import lv.celokopa.app.model.SearchResult;
import lv.celokopa.app.model.User;
import lv.celokopa.app.validator.DriveValidator;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Created by alex on 15.30.12.
 */
@Service
public class DriveService {
    private static final Logger LOGGER = Logger.getLogger(DriveService.class);

    @Autowired
    DriveRepository driveRepository;

    @Autowired
    UserRepository userRepository;

    @Transactional(readOnly = true)
    public SearchResult<Drive> findDrives(String username) {

        List<Drive> drives = driveRepository.findDrives(username);

        return new SearchResult<>(drives.size(), drives);
    }

    @Transactional(readOnly = true)
    public SearchResult<Drive> findDrives(String from, String to) {

        List<Drive> drives = driveRepository.findDrives(from, to);

        return new SearchResult<>(drives.size(), drives);
    }

    @Transactional(readOnly = true)
    public Drive findDriveById(Long id) {

        Drive drive = driveRepository.findDriveById(id);

        return drive;
    }


    @Transactional
    public Drive saveDrive(String username, DriveDTO driveDTO)
            throws Exception {

        Drive drive;
        if (driveDTO.getId() != null) {
            drive = driveRepository.findDriveById(driveDTO.getId());
            if (drive != null && !username.equals(drive.getUser().getUsername())) {
                throw new Exception("Drive does not belong to user");
            }
        } else {
            User user = userRepository.findUserByUsername(username);
            drive = new Drive(user,
                              driveDTO.getTitle(),
                              driveDTO.getDriveFrom(),
                              driveDTO.getDriveTo(),
                              driveDTO.getFromAddress(),
                              driveDTO.getToAddress(),
                              driveDTO.getPhone(),
                              driveDTO.getDepartureTime(),
                              driveDTO.getArrivalTime(),
                              driveDTO.getText(),
                              driveDTO.getPrice(),
                              driveDTO.getCarRegNumber(),
                              driveDTO.getPlacesLeft(),
                              driveDTO.getPlacesOverall(),
                              driveDTO.getOwnersLanguage());
        }
        drive.setTitle(driveDTO.getTitle());
        drive.setDriveFrom(driveDTO.getDriveFrom());
        drive.setDriveTo(driveDTO.getDriveTo());
        drive.setFromAddress(driveDTO.getFromAddress());
        drive.setToAddress(driveDTO.getToAddress());
        drive.setDepartureTime(driveDTO.getDepartureTime());
        drive.setArrivalTime(driveDTO.getArrivalTime());
        drive.setText(driveDTO.getText());
        drive.setPrice(driveDTO.getPrice());
        drive.setCarRegNumber(driveDTO.getCarRegNumber());
        drive.setPlacesLeft(driveDTO.getPlacesLeft());
        drive.setPlacesOverall(driveDTO.getPlacesOverall());

        DriveValidator.validateNewRide(drive);

        return driveRepository.save(drive);
    }

    public void deleteDrive(String username, Long id) throws Exception {
        Drive drive = driveRepository.findDriveById(id);
        if(!drive.getUser().getUsername().equals(username)){
            throw new Exception("User and drive do not match");
        }
        driveRepository.delete(drive.getId());
    }
}
