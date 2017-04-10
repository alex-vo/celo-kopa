package lv.celokopa.app.util;

import org.apache.log4j.Logger;
import org.springframework.util.FileCopyUtils;
import org.springframework.web.multipart.MultipartFile;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;

/**
 * Created by alex on 16.26.5.
 */
public class FileHelper {

    private static final Logger LOGGER = Logger.getLogger(FileHelper.class);

    public static void saveFile(String path, MultipartFile file){
        if (file != null && !file.isEmpty()) {
            try {
                BufferedOutputStream stream = new BufferedOutputStream(
                        new FileOutputStream(new File(path)));
                FileCopyUtils.copy(file.getInputStream(), stream);
                stream.close();

            } catch (Exception e) {
                LOGGER.error(e.getMessage(), e);
            }
        }
    }

    public static void removeFile(String path){
        try {
            File file = new File(path);
            file.delete();
        } catch (Exception e) {
            LOGGER.error(e.getMessage(), e);
        }
    }
}
