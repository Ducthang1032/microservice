package ndt.project.authservice.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import ndt.project.common.constants.EmailConstants;
import ndt.project.common.constants.RegexConstants;

import javax.validation.constraints.Email;

@Data
@AllArgsConstructor
public class UserRegisterDTO {

    @Email(message = EmailConstants.EMAIL_VALIDATION_MESSAGE, regexp = RegexConstants.EMAIL_REGEX)
    @JsonProperty("email")
    private String email;

    @JsonProperty("password")
    private String password;

    @JsonProperty("device_id")
    private String deviceId;

    @JsonProperty("about")
    private String about;
}
