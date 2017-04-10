package lv.celokopa.app.util;

import java.util.HashMap;
import java.util.Map;


/**
 * Created by avoroncovs on 30.12.2016.
 */
public class FacebookNoteHelper {

    private static Map<String, String> localizedTextMap = new HashMap<>();
    public static Map<String, String> localityToImageMap = new HashMap<>();
    static {
        //TODO: move this to properties
        localizedTextMap.put("lv_LV", "Brauciens");
        localizedTextMap.put("ru_RU", "Поездка");

        localityToImageMap.put("Rīga", "1e60bbe5318b631a747b0f5b36c7a942.jpg");
        localityToImageMap.put("Jūrmala", "Jurmala-1.png");
        localityToImageMap.put("Daugavpils", "3-baznicu-kalns-45369318.jpg");
        localityToImageMap.put("Jelgava", "1280px-Jelgava_aerial_view.jpg");
        localityToImageMap.put("Liepāja", "Liepaja%20city%20center%20Kurzeme.jpg");
    }
    public static String createTitle(String from, String to, String language){
        String ride = "Brauciens";
        if(localizedTextMap.containsKey(language)){
            ride = localizedTextMap.get(language);
        }
        return ride + " " + from + " - " + to + ".";
    }

    public static String getLocalityImage(String locality, String host){
        if(localityToImageMap.containsKey(locality)){
            return "http://" + host + "/resources/static/" + localityToImageMap.get(locality);
        }
        return "http://" + host + "/resources/img/logo1.png";
    }
}
