package com.alibou.whatsappclone;

import io.swagger.v3.oas.annotations.enums.SecuritySchemeIn;
import io.swagger.v3.oas.annotations.enums.SecuritySchemeType;
import io.swagger.v3.oas.annotations.security.OAuthFlow;
import io.swagger.v3.oas.annotations.security.OAuthFlows;
import io.swagger.v3.oas.annotations.security.OAuthScope;
import io.swagger.v3.oas.annotations.security.SecurityScheme;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

@SpringBootApplication
@EnableJpaAuditing
@SecurityScheme(
        name = "keycloak",
        type = SecuritySchemeType.OAUTH2,
        bearerFormat = "JWT",
        scheme = "bearer",
        in = SecuritySchemeIn.HEADER,
        flows = @OAuthFlows(
                password = @OAuthFlow(
                        authorizationUrl = "http://localhost:9091/realms/whatsapp-testing/protocol/openid-connect/auth",
                        tokenUrl = "http://localhost:9091/realms/whatsapp-testing/protocol/openid-connect/token"
                )
        )
)
public class WhatsAppCloneApiApplication {

    public static void main(String[] args) {
        SpringApplication.run(WhatsAppCloneApiApplication.class, args);
    }

}
/* localhost:9091 - user/pass: admin/admin
1. Run Keycloak Server: docker-compose up -d
2. RUN DOCKER
3. Run this Application: http://localhost:8080/swagger-ui/index.html#/
4. Run Angular Application:
    - cd whatsapp-clone-ui
    - npm start
5. Open in browser: http://localhost:4200
 */