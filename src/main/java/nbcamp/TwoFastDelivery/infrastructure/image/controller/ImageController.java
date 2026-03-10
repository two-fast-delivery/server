package nbcamp.TwoFastDelivery.infrastructure.image.controller;

import lombok.RequiredArgsConstructor;
import nbcamp.TwoFastDelivery.global.common.CommonResponse;
import nbcamp.TwoFastDelivery.infrastructure.image.service.ImageUploadService;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.http.MediaType;

@RestController
@RequestMapping("/images")
@RequiredArgsConstructor
public class ImageController {

    private final ImageUploadService imageUploadService;

    @PostMapping(consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public CommonResponse<String> uploadImage(@RequestParam("file") MultipartFile file) {
        String imageUrl = imageUploadService.uploadImage(file);
        return CommonResponse.success("이미지 업로드 성공", imageUrl);
    }
}
