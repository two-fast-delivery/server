package nbcamp.TwoFastDelivery.global.config.swagger;

import io.swagger.v3.oas.models.Components;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.security.SecurityRequirement;
import io.swagger.v3.oas.models.security.SecurityScheme;
import org.springdoc.core.models.GroupedOpenApi;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class SwaggerConfig {

    @Bean
    public GroupedOpenApi authApi() {
        return GroupedOpenApi.builder()
                .group("인증")
                .displayName("인증 API")
                .pathsToMatch("/api/auth/**")
                .build();
    }

    @Bean
    public GroupedOpenApi userApi() {
        return GroupedOpenApi.builder()
                .group("회원 관리")
                .displayName("회원 API")
                .pathsToMatch("/users/**")
                .build();
    }

    @Bean
    public GroupedOpenApi addressApi() {
        return GroupedOpenApi.builder()
                .group("주소 관리")
                .displayName("주소 API")
                .pathsToMatch("/v1/addresses/**")
                .build();
    }

    @Bean
    public GroupedOpenApi storeApi() {
        return GroupedOpenApi.builder()
                .group("매장 관리")
                .displayName("매장 API")
                .pathsToMatch("/stores/**", "/owner/stores/**")
                .build();
    }

    @Bean
    public GroupedOpenApi orderApi() {
        return GroupedOpenApi.builder()
                .group("주문 관리")
                .displayName("주문 API")
                .pathsToMatch("/api/orders/**")
                .build();
    }

    @Bean
    public GroupedOpenApi paymentApi() {
        return GroupedOpenApi.builder()
                .group("결제 관리")
                .displayName("결제 API")
                .pathsToMatch("/api/payments/**")
                .build();
    }

    @Bean
    public GroupedOpenApi reviewApi() {
        return GroupedOpenApi.builder()
                .group("리뷰 관리")
                .displayName("리뷰 API")
                .pathsToMatch("/reviews/**", "/reports/**")
                .build();
    }

    @Bean
    public GroupedOpenApi storeRequestApi() {
        return GroupedOpenApi.builder()
                .group("입점 요청 관리")
                .displayName("입점 요청 API")
                .pathsToMatch("/owner/store-requests/**", "/admin/store-requests/**")
                .build();
    }

    @Bean
    public GroupedOpenApi imageApi() {
        return GroupedOpenApi.builder()
                .group("이미지 관리")
                .displayName("이미지 API")
                .pathsToMatch("/images/**")
                .build();
    }

    @Bean
    public OpenAPI openAPI() {
        String jwtSchemeName = "BearerAuth";

        SecurityRequirement securityRequirement = new SecurityRequirement().addList(jwtSchemeName);

        Components components = new Components()
                .addSecuritySchemes(jwtSchemeName, new SecurityScheme()
                        .name(jwtSchemeName)
                        .type(SecurityScheme.Type.HTTP)
                        .scheme("bearer")
                        .bearerFormat("JWT")
                        .description("발급받은 Access Token을 입력하세요. (Bearer 키워드 제외)"));

        return new OpenAPI()
                .info(new Info()
                        .title("배달 서비스 REST API")
                        .description("스파르타 배달 서비스 프로젝트의 API 명세서입니다.")
                        .version("1.0")
                        .contact(new Contact().email("yonggyo00@kakao.com")))
                .addSecurityItem(securityRequirement)
                .components(components);
    }
}