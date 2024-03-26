package ndt.project.authservice.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class LoginOtpDTO {

    @JsonProperty("email")
    private String email;

    @JsonProperty("phone")
    private String phone;

    @JsonProperty("device_id")
    private String deviceId;

    @JsonProperty("pin")
    private String pin;

    @JsonProperty("platform")
    private String platform;

    @JsonProperty("fcm_token")
    private String fcmToken;

    @JsonProperty("apns_token")
    private String apnsToken;

}
