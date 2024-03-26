package ndt.project.authservice.controller;

import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import ndt.project.authservice.dto.LoginResponseDTO;
import ndt.project.authservice.dto.RefreshTokenReq;
import ndt.project.authservice.dto.ResMDLogin;
import ndt.project.authservice.dto.UserRegisterDTO;
import ndt.project.authservice.service.AESService;
import ndt.project.authservice.service.AuthService;
import ndt.project.authservice.service.UserService;
import ndt.project.authservice.service.ValidationService;
import ndt.project.common.constants.AuthoritiesConstants;
import ndt.project.common.constants.CommonConstant;
import ndt.project.common.dto.MetaDTO;
import ndt.project.common.dto.ResponseMetaData;
import ndt.project.common.enums.MetaData;
import org.apache.commons.lang.StringUtils;
import org.apache.logging.log4j.util.Strings;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.util.CollectionUtils;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.servlet.http.HttpServletRequest;
import java.util.List;
import java.util.Objects;

@Controller
@Slf4j
@RequiredArgsConstructor
@RequestMapping("/v1/auth")
public class AuthController {

    private final UserService userService;

    private final AuthService authService;

    private final AESService aesService;

    private final ValidationService validationService;

    @Autowired
    private Environment env;

    @Autowired
    public HttpServletRequest request;

    @RequestMapping("/")
    public String home() {
        // This is useful for debugging
        // When having multiple instance of gallery service running at different ports.
        // We load balance among them, and display which instance received the request.
        return "Hello from Gallery Service running at port: " + env.getProperty("local.registry.port");
    }

    public boolean isNotVerifyOtp() {
        String isVerifyOtp = request.getHeader(AuthoritiesConstants.IS_VERIFY_OTP);
        return Strings.isNotBlank(isVerifyOtp) && String.valueOf(false).equalsIgnoreCase(isVerifyOtp);
    }

    @SneakyThrows
    @PostMapping("/register")
    public ResponseEntity<ResponseMetaData> register(@RequestBody UserRegisterDTO registerDTO) {
        registerDTO.setEmail(StringUtils.lowerCase(registerDTO.getEmail()));
        log.info("Start register with email: {}", registerDTO.getEmail());
        ResMDLogin response = new ResMDLogin();
        registerDTO.setPassword(aesService.decryptDataWithoutUrl(registerDTO.getPassword()));


        List<MetaDTO> metaListCheckInput = validationService.validationInputRegister(registerDTO);
        if (!CollectionUtils.isEmpty(metaListCheckInput)) {
            response.setMeta(metaListCheckInput);
            return ResponseEntity.badRequest().body(response);
        }

        boolean isNotVerifyOtp = isNotVerifyOtp();
        MetaDTO meta = userService.checkEmailAndPhoneBeforeRegister(registerDTO);
        if (Objects.nonNull(meta)) {
            response.setMeta(meta);
            return ResponseEntity.badRequest().body(response);
        }

        LoginResponseDTO loginResponseDTO = authService.saveUserRegisSuccess(registerDTO);
        if (Objects.isNull(loginResponseDTO)) {
            response.setMeta(new MetaDTO(MetaData.SERVICE_UNAVAILABLE));
            return ResponseEntity.status(HttpStatus.SERVICE_UNAVAILABLE).body(response);
        }

        response.setMeta(new MetaDTO(MetaData.SUCCESS));
        response.setData(loginResponseDTO);
        return ResponseEntity.ok(response);
    }

    @PostMapping("/refresh-token")
    public ResponseEntity<ResMDLogin> getLoginInfo(@RequestBody RefreshTokenReq refreshTokenRequest) {
        return authService.loginByRefreshToken(refreshTokenRequest);
    }
}