package ndt.project.authservice.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class RefreshTokenInfo {
    @JsonProperty("user_id")
    private String userId;

    @JsonProperty("device_id")
    private String deviceId;
}
