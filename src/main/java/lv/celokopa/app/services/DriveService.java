package lv.celokopa.app.services;

import lv.celokopa.app.dao.DriveRepository;
import lv.celokopa.app.dao.UserRepository;
import lv.celokopa.app.dto.DriveDTO;
import lv.celokopa.app.model.Drive;
import lv.celokopa.app.model.SearchResult;
import lv.celokopa.app.model.User;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.List;

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
    public Drive saveDrive(String username, Long id, String title, String driveFrom, String driveTo, String fromAddress,
                           String toAddress, Date departureTime, Date arrivalTime, String text, Double price,
                           String carRegNumber, Integer placesLeft, Integer placesOverall, String ownersLanguage) throws Exception {

        Drive drive;
        if(id != null){
            drive = driveRepository.findDriveById(id);
            if(drive != null && !username.equals(drive.getUser().getUsername())){
                throw new Exception("Drive and user do not match");
            }
        }else{
            User user = userRepository.findUserByUsername(username);
            drive = driveRepository.save(new Drive(user, title, driveFrom, driveTo, fromAddress, toAddress, departureTime, arrivalTime, text,
                    price, carRegNumber, placesLeft, placesOverall, ownersLanguage));
        }
        drive.setTitle(title);
        drive.setDriveFrom(driveFrom);
        drive.setDriveTo(driveTo);
        drive.setFromAddress(fromAddress);
        drive.setToAddress(toAddress);
        drive.setDepartureTime(departureTime);
        drive.setArrivalTime(arrivalTime);
        drive.setText(text);
        drive.setPrice(price);
        drive.setCarRegNumber(carRegNumber);
        drive.setPlacesLeft(placesLeft);
        drive.setPlacesOverall(placesOverall);
        return driveRepository.save(drive);
    }

    public Drive saveDrive(String username, DriveDTO driveDTO) throws Exception {
        return saveDrive(username, driveDTO.getId(), driveDTO.getTitle(), driveDTO.getDriveFrom(), driveDTO.getDriveTo(), driveDTO.getFromAddress(),
                driveDTO.getToAddress(), driveDTO.getDepartureTime(), driveDTO.getArrivalTime(), driveDTO.getText(),
                driveDTO.getPrice(), driveDTO.getCarRegNumber(), driveDTO.getPlacesLeft(), driveDTO.getPlacesOverall(),
                         driveDTO.getOwnersLanguage());
    }

    public void deleteDrive(String username, Long id) throws Exception {
        Drive drive = driveRepository.findDriveById(id);
        if(!drive.getUser().getUsername().equals(username)){
            throw new Exception("User and drive do not match");
        }
        driveRepository.delete(drive.getId());
    }
}
