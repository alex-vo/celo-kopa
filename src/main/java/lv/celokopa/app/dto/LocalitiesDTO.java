package lv.celokopa.app.dto;

import lv.celokopa.app.model.Locality;

import java.util.LinkedList;
import java.util.List;


/**
 * Created by alex on 16.10.5.
 */
public class LocalitiesDTO {
    private List<String> localities = new LinkedList<String>();

    public static LocalitiesDTO mapFromLocalityEntities(List<Locality> localities){
        if(localities == null) {
            return null;
        }
        LocalitiesDTO dto = new LocalitiesDTO();
        for(Locality locality: localities){
            dto.localities.add(locality.getTitle());
        }
        return dto;
    }

    public List<String> getLocalities() {
        return localities;
    }

    public void setLocalities(List<String> localities) {
        this.localities = localities;
    }
}
