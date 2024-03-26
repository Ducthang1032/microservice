package ndt.project.authservice.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import ndt.project.authservice.domain.UserEntity;
import ndt.project.authservice.service.AESService;
import ndt.project.common.constants.CommonConstant;

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class LoginResponseDTO {
    @JsonProperty("user_id")
    private String userId;

    @JsonProperty("display_name")
    private String displayName;

    @JsonProperty("avatar")
    private String avatar;

    @JsonProperty("cq_access_token")
    private String cqAccessToken;

    @JsonProperty("refresh_token")
    private String refreshToken;

    @JsonProperty("pwd_change_flg")
    private Boolean pwdChangeFlg;

    @JsonProperty("expire_time")
    private String expireTime;

    @JsonProperty("lang")
    private String lang;

    @JsonProperty("chat_id")
    private String chatId;

    @JsonProperty("role")
    private String role;

    @JsonProperty("time_zone")
    private String timeZone;

    public LoginResponseDTO(String cqAccessToken, String refreshToken, LocalDateTime expirationTime,
                            UserEntity userEntity, AESService aesService, String role) {
        this.userId = aesService.encryptData(userEntity.getId().toString());
        this.cqAccessToken = cqAccessToken;
        this.refreshToken = refreshToken;
        //TODO: set from data collumn
        this.pwdChangeFlg = userEntity.getPwdChangeFlg();
        this.expireTime = String.valueOf(expirationTime);
        this.lang = userEntity.getLang();
        this.displayName = userEntity.getDisplayName();
        this.role = role;
        this.timeZone = String.format(CommonConstant.GMT, ZoneId.systemDefault().getRules().getOffset(Instant.now()).getId());
    }

    public LoginResponseDTO(String cqAccessToken, LocalDateTime expirationTime, UserEntity userEntity,
                            AESService aesService) {
        this.userId = aesService.encryptData(userEntity.getId().toString());
        this.cqAccessToken = cqAccessToken;
        this.pwdChangeFlg = userEntity.getPwdChangeFlg();
        this.expireTime = String.valueOf(expirationTime);
        this.lang = userEntity.getLang();
        this.displayName = userEntity.getDisplayName();
        this.role = role;
        this.timeZone = String.format(CommonConstant.GMT, ZoneId.systemDefault().getRules().getOffset(Instant.now()).getId());
    }

    public static LoginResponseDTO build() {
        return new LoginResponseDTO();
    }

    public LoginResponseDTO setLoginResponseForHK(UserEntity userEntity, LocalDateTime expirationTime,
                                                  AESService aesService, String role, String cqAccessToken) {
        this.userId = aesService.encryptData(userEntity.getId().toString());
        this.cqAccessToken = cqAccessToken;
        this.expireTime = String.valueOf(expirationTime);
        this.lang = userEntity.getLang();
        this.displayName = userEntity.getDisplayName();
        this.role = role;
        this.timeZone = String.format(CommonConstant.GMT, ZoneId.systemDefault().getRules().getOffset(Instant.now()).getId());
        return this;
    }

}
