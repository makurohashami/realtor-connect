package com.kotyk.realtorconnect.util.validator;

import com.kotyk.realtorconnect.config.FileConfiguration;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.util.LinkedList;
import java.util.List;

@Slf4j
@Component("realEstatePhotoValidator")
@AllArgsConstructor
public class RealEstatePhotoValidator implements Validator<MultipartFile> {

    private final FileConfiguration fileConfiguration;

    @Override
    public List<String> validate(MultipartFile target) {
        List<String> errors = new LinkedList<>();
        try {
            if (target == null) {
                errors.add("Photo is null");
                return errors;
            }
            List<String> allowedTypes = fileConfiguration.getAllowedContentTypes();
            if (!allowedTypes.contains(target.getContentType())) {
                errors.add(String.format("Incorrect image type, file must be: %s", String.join(", ", allowedTypes)));
            }
            BufferedImage image = ImageIO.read(new ByteArrayInputStream(target.getBytes()));
            int maxWidthHeight = fileConfiguration.getRealEstatePhoto().getMaxWidthHeight();
            if (image.getWidth() > maxWidthHeight || image.getHeight() > maxWidthHeight) {
                errors.add(String.format("Max width/height for photo %s px", maxWidthHeight));
            }
            int minWidthHeight = fileConfiguration.getAvatar().getMinWidthHeight();
            if (image.getWidth() < minWidthHeight || image.getHeight() < minWidthHeight) {
                errors.add(String.format("Min width/height for photo %s px", minWidthHeight));
            }
        } catch (Exception ex) {
            log.error("", ex);
        }
        return errors;
    }
}
