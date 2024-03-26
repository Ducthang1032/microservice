package ndt.project.authservice.service;


import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import ndt.project.authservice.domain.UserEntity;
import ndt.project.authservice.dto.LoginOtpDTO;
import ndt.project.authservice.dto.UserRegisterDTO;
import ndt.project.common.dto.MetaDTO;
import ndt.project.common.enums.SendEmailOtpType;
import ndt.project.common.enums.UserStatus;
import ndt.project.common.utils.EmailUtils;
import ndt.project.common.utils.PasswordUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;
import ndt.project.common.enums.MetaData;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;

@Slf4j
@Service
@RequiredArgsConstructor
public class ValidationService {

    public List<MetaDTO> validationInputRegister(UserRegisterDTO registerDTO) {
        List<MetaDTO> metaList = validationEmail(registerDTO.getEmail());

        if (StringUtils.isBlank(registerDTO.getPassword()) || !PasswordUtils.isPassword(registerDTO.getPassword())) {
            log.warn("Param password is invalid");
            metaList.add(new MetaDTO(MetaData.PASSWORD_INVALID));
        }
        return metaList;
    }


    public List<MetaDTO> validationEmail(String email) {
        List<MetaDTO> metaList = new ArrayList<>();

        if (StringUtils.isBlank(email) || !EmailUtils.isEmail(email)) {
            log.warn("Param email is invalid");
            metaList.add(new MetaDTO(MetaData.EMAIL_INVALID));
        }
        return metaList;
    }


    public MetaDTO validationUserWithType(String type, UserEntity userEntity) {
        if ((Objects.isNull(userEntity) || !UserStatus.ACTIVE.getValue().equalsIgnoreCase(userEntity.getStatus()))
                && Arrays.asList(SendEmailOtpType.FORGOT_PASSWORD.getValue(), SendEmailOtpType.PIN_LOGIN_OTP.getValue()).contains(type))
            return new MetaDTO(MetaData.EMAIL_NOT_EXIST);

        if (Objects.nonNull(userEntity) && type.equals(SendEmailOtpType.REGISTER.getValue()))
            return new MetaDTO(MetaData.EMAIL_NOT_EXIST);

        return null;
    }

    public List<MetaDTO> validationLoginOtp(LoginOtpDTO loginOtp) {
        List<MetaDTO> metaList = this.validationEmail(loginOtp.getEmail());

        if (StringUtils.isBlank(loginOtp.getPin())) {
            log.warn("Param pin is null or empty");
            metaList.add(new MetaDTO(MetaData.PIN_INVALID));
        }

        if (StringUtils.isBlank(loginOtp.getDeviceId())) {
            log.warn("Param device_id is null or empty");
            metaList.add(new MetaDTO(MetaData.DEVICE_ID_MISSING));
        }

        return metaList;
    }

    public List<MetaDTO> validateUpdateNewPassword(UpdateNewPassword updateNewPassword) {
        List<MetaDTO> metaList = new ArrayList<>();
        if (StringUtils.isBlank(updateNewPassword.getNewPassword())
                || !PasswordUtils.isPassword(updateNewPassword.getNewPassword())) {
            log.warn("New password is invalid");
            metaList.add(new MetaDTO(MetaData.PASSWORD_INVALID));
        }

        if (StringUtils.isBlank(updateNewPassword.getCurrentPassword())) {
            log.warn("Param current password is missing");
            metaList.add(new MetaDTO(MetaData.PASSWORD_MISSING));
        }

        if (StringUtils.isBlank(updateNewPassword.getDeviceId())) {
            log.warn("Param device id is null or empty");
            metaList.add(new MetaDTO(MetaData.DEVICE_ID_INVALID));
        }
        metaList.addAll(validateNotificationTokenAndPlatform(updateNewPassword.getPlatform(),
                updateNewPassword.getFcmToken(), updateNewPassword.getApnsToken()));
        return metaList;
    }

    public List<MetaDTO> validateLoginWithQRCode(QRCodeLoginDTO qrCodeLoginDTO) {
        List<MetaDTO> metaList = new ArrayList<>();

        if (StringUtils.isBlank(qrCodeLoginDTO.getDeviceId())) {
            log.warn("Param device id is null or empty");
            metaList.add(new MetaDTO(MetaData.DEVICE_ID_INVALID));
        }

        metaList.addAll(validateNotificationTokenAndPlatform(qrCodeLoginDTO.getPlatform(),/
                qrCodeLoginDTO.getFcmToken(), qrCodeLoginDTO.getApnsToken()));
        return metaList;
    }
}