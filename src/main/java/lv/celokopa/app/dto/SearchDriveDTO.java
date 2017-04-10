package lv.celokopa.app.dto;

/**
 * Created by alex on 16.25.5.
 */
public class SearchDriveDTO {
    private String from;
    private String to;

    public SearchDriveDTO(){}

    public String getFrom() {
        return from;
    }

    public void setFrom(String from) {
        this.from = from;
    }

    public String getTo() {
        return to;
    }

    public void setTo(String to) {
        this.to = to;
    }
}
