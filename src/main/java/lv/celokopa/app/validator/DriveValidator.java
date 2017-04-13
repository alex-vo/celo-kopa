package lv.celokopa.app.validator;

import java.util.Date;
import lv.celokopa.app.model.Drive;
import org.apache.commons.lang3.StringUtils;


/**
 * Created by avoroncovs on 13.04.2017.
 */
public class DriveValidator {

    public static void validateNewRide(Drive drive) {
        UserValidator.validateUserForCreatingRide(drive.getUser());

        if (StringUtils.isEmpty(drive.getTitle()) || StringUtils.isEmpty(drive.getDriveFrom()) || StringUtils.isEmpty(
                drive.getDriveTo()) || drive.getDepartureTime() == null || drive.getArrivalTime() == null || drive.getUser() == null || drive
                .getPrice() == null || StringUtils.isEmpty(drive.getPhone()) || StringUtils.isEmpty(drive.getCarRegNumber())) {
            throw new RuntimeException("Incomplete ride info.");
        }

        Date now = new Date(System.currentTimeMillis());
        if (drive.getDepartureTime().before(now) || drive.getArrivalTime().before(now) || drive.getArrivalTime()
                                                                                               .before(drive.getDepartureTime())) {
            throw new RuntimeException("Ride dates not correct.");
        }
    }

}
