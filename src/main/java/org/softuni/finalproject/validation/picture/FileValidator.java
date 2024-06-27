package org.softuni.finalproject.validation.picture;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

public class FileValidator implements ConstraintValidator<ValidFile, MultipartFile> {

    private final static List<String> ALLOWED_EXTENSIONS = List.of(
            ".jpg", ".jpeg", ".png"
    );

    private final static List<String> ALLOWED_CONTENT_TYPES = List.of(
            "image/jpeg", "image/png"
    );
    @Override
    public boolean isValid(MultipartFile file, ConstraintValidatorContext context) {

        boolean fileExist = fileExist(file);

        if (!fileExist) {
            context.disableDefaultConstraintViolation();
            context.buildConstraintViolationWithTemplate("Upload a file!")
                    .addConstraintViolation();

            return false;
        }

        boolean fileIsImage = fileIsImage(file);

        if (!fileIsImage) {
            context.disableDefaultConstraintViolation();
            context
                    .buildConstraintViolationWithTemplate
                            ("Image must be one of the following formats:" +
                                    " .jpg, .jpeg or .png!")
                    .addConstraintViolation();

            return false;
        }

        return true;
    }

    //Checks if file is an image
    private boolean fileIsImage(MultipartFile file) {
        String fileName = file.getOriginalFilename();

        if (fileName == null || fileName.isEmpty()) {
            return false;
        }

        String contentType = file.getContentType();

        if (contentType == null || contentType.isEmpty()) {
            return false;
        }

        boolean hasAllowedExtension = ALLOWED_EXTENSIONS.stream().anyMatch(fileName::endsWith);

        boolean hasAllowedContentType = ALLOWED_CONTENT_TYPES.contains(contentType);

        return hasAllowedExtension && hasAllowedContentType;
    }

    //Checks if file exist
    private boolean fileExist(MultipartFile file) {
        return  file != null && !file.isEmpty();
    }
}
