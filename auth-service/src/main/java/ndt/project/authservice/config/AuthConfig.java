package ndt.project.authservice.config;

import lombok.Getter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Getter
@Component
public class AuthConfig {

    @Value("${app.rest-template.connect-timeout}")
    private Integer restTemplateConnectTimeout;
}
