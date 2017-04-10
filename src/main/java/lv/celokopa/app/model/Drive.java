package lv.celokopa.app.model;

import javax.persistence.Entity;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import java.util.Date;

/**
 * Created by alex on 15.30.12.
 */
@Entity
@Table(name = "DRIVE")
public class Drive extends AbstractEntity{

    @ManyToOne
    private User user;

    private String title;
    private String driveFrom;
    private String driveTo;
    private String fromAddress;
    private String toAddress;
    private Date departureTime;
    private Date arrivalTime;
    private String text;
    private Double price;
    private String carRegNumber;
    private Integer placesLeft;
    private Integer placesOverall;
    private String ownersLanguage;

    public Drive(){}

    public Drive(User user, String title, String driveFrom, String driveTo, String fromAddress, String toAddress,
                 Date departureTime, Date arrivalTime, String text, Double price, String carRegNumber,
                 Integer placesLeft, Integer placesOverall, String ownersLanguage) {
        this.user = user;
        this.title = title;
        this.driveFrom = driveFrom;
        this.driveTo = driveTo;
        this.fromAddress = fromAddress;
        this.toAddress = toAddress;
        this.departureTime = departureTime;
        this.arrivalTime = arrivalTime;
        this.text = text;
        this.price = price;
        this.carRegNumber = carRegNumber;
        this.placesLeft = placesLeft;
        this.placesOverall = placesOverall;
        this.ownersLanguage = ownersLanguage;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDriveFrom() {
        return driveFrom;
    }

    public void setDriveFrom(String driveFrom) {
        this.driveFrom = driveFrom;
    }

    public String getDriveTo() {
        return driveTo;
    }

    public void setDriveTo(String driveTo) {
        this.driveTo = driveTo;
    }

    public String getFromAddress() {
        return fromAddress;
    }

    public void setFromAddress(String fromAddress) {
        this.fromAddress = fromAddress;
    }

    public String getToAddress() {
        return toAddress;
    }

    public void setToAddress(String toAddress) {
        this.toAddress = toAddress;
    }

    public Date getDepartureTime() {
        return departureTime;
    }

    public void setDepartureTime(Date departureTime) {
        this.departureTime = departureTime;
    }

    public Date getArrivalTime() {
        return arrivalTime;
    }

    public void setArrivalTime(Date arrivalTime) {
        this.arrivalTime = arrivalTime;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public Double getPrice() {
        return price;
    }

    public void setPrice(Double price) {
        this.price = price;
    }

    public String getCarRegNumber() {
        return carRegNumber;
    }

    public void setCarRegNumber(String carRegNumber) {
        this.carRegNumber = carRegNumber;
    }

    public Integer getPlacesLeft() {
        return placesLeft;
    }

    public void setPlacesLeft(Integer placesLeft) {
        this.placesLeft = placesLeft;
    }

    public Integer getPlacesOverall() {
        return placesOverall;
    }

    public void setPlacesOverall(Integer placesOverall) {
        this.placesOverall = placesOverall;
    }

    public String getOwnersLanguage() {
        return ownersLanguage;
    }

    public void setOwnersLanguage(String ownersLanguage) {
        this.ownersLanguage = ownersLanguage;
    }
}
