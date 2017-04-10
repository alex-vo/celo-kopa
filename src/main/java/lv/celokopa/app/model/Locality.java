package lv.celokopa.app.model;

import javax.persistence.Entity;
import javax.persistence.Table;

/**
 * Created by alex on 16.10.5.
 */
@Entity
@Table(name = "LOCALITY")
public class Locality extends AbstractEntity {
    private String title;
    private String area;
    private String region;

    public Locality(){}

    public Locality(String title, String area, String region){
        this.title = title;
        this.area = area;
        this.region = region;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getArea() {
        return area;
    }

    public void setArea(String area) {
        this.area = area;
    }

    public String getRegion() {
        return region;
    }

    public void setRegion(String region) {
        this.region = region;
    }
}
