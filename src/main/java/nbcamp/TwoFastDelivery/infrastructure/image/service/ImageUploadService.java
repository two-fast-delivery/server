package nbcamp.TwoFastDelivery.infrastructure.image.service;

import lombok.extern.slf4j.Slf4j;
import nbcamp.TwoFastDelivery.global.exception.CustomException;
import nbcamp.TwoFastDelivery.global.exception.ErrorCode;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.UUID;

@Slf4j
@Service
public class ImageUploadService {

    @Value("${file.upload-dir:uploads}")
    private String uploadDir;

    public String uploadImage(MultipartFile file) {
        if (file.isEmpty()) {
            throw new CustomException(ErrorCode.INVALID_INPUT);
        }

        try {
            Path uploadPath = Paths.get(uploadDir).toAbsolutePath().normalize();
            if (!Files.exists(uploadPath)) {
                Files.createDirectories(uploadPath);
            }

            // 원본 파일명에서 확장자 추출
            String originalFilename = file.getOriginalFilename();
            String extension = "";
            if (originalFilename != null && originalFilename.contains(".")) {
                extension = originalFilename.substring(originalFilename.lastIndexOf("."));
            }

            // 고유한 파일명 생성
            String storedFileName = UUID.randomUUID().toString() + extension;
            Path targetLocation = uploadPath.resolve(storedFileName);

            Files.copy(file.getInputStream(), targetLocation, StandardCopyOption.REPLACE_EXISTING);

            // 클라이언트가 접근할 수 있는 다운로드 웹 URL 생성
            String fileDownloadUri = ServletUriComponentsBuilder.fromCurrentContextPath()
                    .path("/" + uploadDir + "/")
                    .path(storedFileName)
                    .toUriString();

            log.info("이미지 업로드 완료: {}", fileDownloadUri);
            return fileDownloadUri;

        } catch (IOException ex) {
            log.error("파일 업로드 실패", ex);
            throw new CustomException(ErrorCode.INTERNAL_ERROR);
        }
    }
}
