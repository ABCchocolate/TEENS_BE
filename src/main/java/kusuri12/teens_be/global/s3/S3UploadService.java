package kusuri12.teens_be.global.s3;

import kusuri12.teens_be.global.s3.exception.BadFileExtensionException;
import kusuri12.teens_be.global.s3.exception.EmptyFileException;
import kusuri12.teens_be.global.s3.exception.FailUploadImageException;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import software.amazon.awssdk.core.sync.RequestBody;
import software.amazon.awssdk.services.s3.S3Client;
import software.amazon.awssdk.services.s3.model.*;
import software.amazon.awssdk.services.s3.presigner.S3Presigner;
import software.amazon.awssdk.services.s3.presigner.model.GetObjectPresignRequest;
import software.amazon.awssdk.services.s3.presigner.model.PresignedGetObjectRequest;

import java.time.Duration;
import java.util.Locale;
import java.util.Set;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class S3UploadService {

    private final S3Client s3Client;
    private final S3Presigner s3Presigner;

    @Value("${cloud.aws.s3.bucket}")
    private String bucket;

    @Value("${cloud.aws.s3.exp-time}")
    private String s3Exp;

    public String verifyFile(MultipartFile file) {
        if (file.isEmpty() || file.getOriginalFilename() == null) throw EmptyFileException.EXCEPTION;

        final Set<String> allowedExtensions = Set.of("jpg", "jpeg", "png", "gif");

        String originalName = file.getOriginalFilename();
        String ext = originalName.substring(originalName.lastIndexOf(".") + 1).toLowerCase(Locale.getDefault());

        if (!allowedExtensions.contains(ext)) throw BadFileExtensionException.EXCEPTION;

        return ext;
    }

    public String upload(MultipartFile file, String path) {
        String ext = verifyFile(file);

        String randomName = UUID.randomUUID().toString();
        String fileKey = path + randomName + "." + ext;

        try {
            PutObjectRequest putObjectRequest = PutObjectRequest.builder()
                    .bucket(bucket)
                    .key(fileKey)
                    .contentType(file.getContentType())
                    .build();

            s3Client.putObject(putObjectRequest, RequestBody.fromInputStream(file.getInputStream(), file.getSize()));
            return generatePresignedUrl(fileKey);

        } catch (Exception e) {
            throw new FailUploadImageException(e);
        }
    }

    public String generatePresignedUrl(String fileKey) {
        // 1. URL 만료 시간을 1시간으로 설정
        GetObjectPresignRequest presignRequest = GetObjectPresignRequest.builder()
                .signatureDuration(Duration.ofHours(1))
                .getObjectRequest(GetObjectRequest.builder()
                        .bucket(bucket)
                        .key(fileKey)
                        .build())
                .build();

        // 2. Presigner를 통해 사전 서명된 URL 생성
        PresignedGetObjectRequest presignedObject = s3Presigner.presignGetObject(presignRequest);

        // 3. 서명된 URL 반환
        return presignedObject.url().toString();
    }

    public void delete(String fileName, String path) {
        String fileKey = path + fileName;

        try {
            DeleteObjectRequest deleteObjectRequest = DeleteObjectRequest.builder()
                    .bucket(bucket)
                    .key(fileKey)
                    .build();

            s3Client.deleteObject(deleteObjectRequest);
        } catch (S3Exception e) {
            if ("NoSuchKey".equals(e.awsErrorDetails().errorCode())) {
                return;
            }
            throw new FailUploadImageException(e);

        } catch (Exception e) {
            throw new FailUploadImageException(e);
        }
    }
}
