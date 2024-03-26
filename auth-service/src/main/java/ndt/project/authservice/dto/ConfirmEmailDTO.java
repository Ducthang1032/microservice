package ndt.project.authservice.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;
import ndt.project.common.constants.EmailConstants;
import ndt.project.common.constants.RegexConstants;

import javax.validation.constraints.Email;

@Data
public class ConfirmEmailDTO {
    @Email(message = EmailConstants.EMAIL_VALIDATION_MESSAGE, regexp = RegexConstants.EMAIL_REGEX)
    @JsonProperty("email")
    private String email;

    @JsonProperty("type")
    private String type;
}
