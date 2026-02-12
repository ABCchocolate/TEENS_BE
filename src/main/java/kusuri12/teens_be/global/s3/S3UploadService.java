package kusuri12.teens_be.global.s3;

import kusuri12.teens_be.global.s3.exception.FailUploadImageException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import software.amazon.awssdk.core.sync.RequestBody;
import software.amazon.awssdk.services.s3.S3Client;
import software.amazon.awssdk.services.s3.model.*;

import java.net.URI;
import java.util.UUID;

@Service
@RequiredArgsConstructor
@Slf4j
public class S3UploadService {

    private final S3Client s3Client;
    private final S3Properties s3Properties;
    private final S3FileValidator s3FileValidator;
    private final String bucket = s3Properties.getS3().bucket();

    public String upload(MultipartFile file, String path) {
        String ext = s3FileValidator.verifyImageFile(file);

        String randomName = UUID.randomUUID().toString();
        String fileKey = path + randomName + "." + ext;

        try {
            PutObjectRequest putObjectRequest = PutObjectRequest.builder()
                    .bucket(bucket)
                    .key(fileKey)
                    .contentType(file.getContentType())
                    .build();

            s3Client.putObject(putObjectRequest, RequestBody.fromInputStream(file.getInputStream(), file.getSize()));
            return fileKey;
        } catch (Exception e) {
            throw new FailUploadImageException(e);
        }
    }

    public String getFileUrl(String fileKey) {
        String region = s3Properties.getRegion().staticRegion();
        return String.format("https://%s.s3.%s.amazonaws.com/%s", bucket, region, fileKey);
    }

    public void delete(String fileUrl) {
        try {
            String fileKey = URI.create(fileUrl).getPath().substring(1);

            DeleteObjectRequest deleteObjectRequest = DeleteObjectRequest.builder()
                    .bucket(bucket)
                    .key(fileKey)
                    .build();

            s3Client.deleteObject(deleteObjectRequest);
        } catch (Exception e) {
            log.error("S3 파일 삭제 실패: {}", e.getMessage());
        }
    }
}
