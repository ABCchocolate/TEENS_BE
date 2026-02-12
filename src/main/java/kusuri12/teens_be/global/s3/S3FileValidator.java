package kusuri12.teens_be.global.s3;

import kusuri12.teens_be.global.s3.exception.BadFileExtensionException;
import kusuri12.teens_be.global.s3.exception.EmptyFileException;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import java.util.Locale;

@Component
public class S3FileValidator {

    private static final String CONTENT_TYPE_IMAGE = "image/";

    public String verifyImageFile(MultipartFile file) {
        // 1. 파일 존재 여부 확인
        if (file == null || file.isEmpty() || file.getOriginalFilename() == null) {
            throw EmptyFileException.EXCEPTION;
        }

        // 2. 모든 이미지 타입 허용 (MIME 타입 체크)
        String contentType = file.getContentType();
        if (contentType == null || !contentType.startsWith(CONTENT_TYPE_IMAGE)) {
            throw BadFileExtensionException.EXCEPTION;
        }

        // 3. 확장자 추출
        return extractExtension(file.getOriginalFilename());
    }

    private String extractExtension(String fileName) {
        int lastDotIndex = fileName.lastIndexOf(".");
        if (lastDotIndex == -1) {
            throw BadFileExtensionException.EXCEPTION;
        }
        return fileName.substring(lastDotIndex + 1).toLowerCase(Locale.getDefault());
    }
}
