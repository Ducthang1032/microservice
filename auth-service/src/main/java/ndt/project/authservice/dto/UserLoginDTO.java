package ndt.project.authservice.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class UserLoginDTO implements Serializable {
    @JsonProperty("user_name")
    private String username;

    @JsonProperty("password")
    private String password;

    @JsonProperty("type")
    private String type;

    @JsonProperty("device_id")
    private String deviceId;

    public UserLoginDTO(UserRegisterDTO userRegisterDTO) {
        this.username = userRegisterDTO.getEmail();
        this.password = userRegisterDTO.getPassword();
        this.deviceId = userRegisterDTO.getDeviceId();
    }
}
