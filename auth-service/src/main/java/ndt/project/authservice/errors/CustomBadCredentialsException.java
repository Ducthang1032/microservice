package ndt.project.authservice.errors;

import lombok.Getter;
import org.springframework.security.authentication.BadCredentialsException;

@Getter
public class CustomBadCredentialsException extends BadCredentialsException {
    private final String userId;

    public CustomBadCredentialsException(String msg, String userId) {
        super(msg);
        this.userId = userId;
    }
}
