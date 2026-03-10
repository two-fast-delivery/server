package nbcamp.TwoFastDelivery.global.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.lang.NonNull;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import java.nio.file.Paths;

@Configuration
public class WebConfig implements WebMvcConfigurer {

    @Value("${file.upload-dir:uploads}")
    private String uploadDir;

    @Override
    public void addResourceHandlers(@NonNull ResourceHandlerRegistry registry) {
        // 실제 파일이 저장된 서버상의 절대 경로를 계산
        String uploadAbsolutePath = Paths.get(uploadDir).toAbsolutePath().normalize().toUri().toString();

        // uploads로 시작하는 URL 요청이 들어오면
        registry.addResourceHandler("/" + uploadDir + "/**")
                // 위에서 계산한 실제 파일 경로에서 파일을 찾아 보여줌
                .addResourceLocations(uploadAbsolutePath);
    }
}
