package lv.celokopa.app.dto;

import lv.celokopa.app.model.Drive;

import java.util.List;


/**
 * Created by alex on 15.30.12.
 */
public class DrivesDTO {
    List<DriveDTO> drives;

    public DrivesDTO(List<DriveDTO> drives) {
        this.drives = drives;
    }

    public static DrivesDTO mapFromDrivesEntities(List<Drive> drives) {
        List<DriveDTO> list = DriveDTO.mapFromDrivesEntities(drives);
        return new DrivesDTO(list);
    }

    public List<DriveDTO> getDrives() {
        return drives;
    }

    public void setDrives(List<DriveDTO> drives) {
        this.drives = drives;
    }
}
