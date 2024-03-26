package ndt.project.common.utils;

import javax.servlet.http.HttpServletRequest;
import java.net.InetAddress;
import java.util.Objects;

import static io.undertow.util.Headers.X_FORWARDED_FOR;

public class SecurityUtil {

    public static String getClientIP(HttpServletRequest request) {
        try {
            if (Objects.isNull(request))
                return InetAddress.getLocalHost().getHostAddress();

            if (Objects.nonNull(request.getHeader(X_FORWARDED_FOR.toString())))
                return request.getHeader(X_FORWARDED_FOR.toString());

            return request.getRemoteAddr();
        } catch (Exception ex) {
            return "";
        }
    }
}
