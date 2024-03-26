package ndt.project.authservice.controller;


import ndt.project.common.constants.AuthoritiesConstants;
import ndt.project.common.constants.CommonConstant;
import ndt.project.common.utils.NumberUtil;
import org.apache.commons.lang3.StringUtils;
import org.apache.logging.log4j.util.Strings;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import java.util.Objects;

@RestController
@RequestMapping
public class BaseController {

    @Autowired
    public HttpServletRequest request;

    public Long getUserIdFromToken() {
        return NumberUtil.parseLong(Objects.toString(request
                .getHeader(CommonConstant.USER_ID), StringUtils.EMPTY), 0L);
    }

    public String getDeviceId() {
        return Objects.toString(request.getHeader(CommonConstant.DEVICE_ID), StringUtils.EMPTY);
    }

    public boolean isNotVerifyOtp() {
        String isVerifyOtp = request.getHeader(AuthoritiesConstants.IS_VERIFY_OTP);
        return Strings.isNotBlank(isVerifyOtp) && String.valueOf(false).equalsIgnoreCase(isVerifyOtp);
    }
}