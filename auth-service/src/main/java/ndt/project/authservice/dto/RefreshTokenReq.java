package ndt.project.authservice.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

@Data
public class RefreshTokenReq {
    @JsonProperty("refresh_token")
    private String refreshToken;
}
