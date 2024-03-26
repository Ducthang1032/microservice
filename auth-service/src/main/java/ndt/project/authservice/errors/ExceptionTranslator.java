package ndt.project.authservice.errors;


import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.MalformedJwtException;
import io.jsonwebtoken.SignatureException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import ndt.project.common.dto.MetaDTO;
import ndt.project.common.dto.ResponseMetaData;
import ndt.project.common.enums.MetaData;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.InternalAuthenticationServiceException;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.web.HttpMediaTypeNotSupportedException;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;


/**
 * Controller advice to translate the server side exceptions to client-friendly json structures.
 * The error response follows RFC7807 - Problem Details for HTTP APIs (https://tools.ietf.org/html/rfc7807).
 */
@ControllerAdvice
@Slf4j
@RequiredArgsConstructor
public class ExceptionTranslator {

    @ExceptionHandler
    public ResponseEntity<ResponseMetaData> handleException(Exception ex) {
        log.warn("Error", ex);
        return ResponseEntity.status(MetaData.INTERNAL_SERVER_ERROR.getMetaCode()).body(new ResponseMetaData(new MetaDTO(MetaData.INTERNAL_SERVER_ERROR), ""));
    }

    @ExceptionHandler
    public ResponseEntity<ResponseMetaData> handleUsernameNotFoundException(UsernameNotFoundException ex) {
        return ResponseEntity.status(MetaData.BAD_REQUEST.getMetaCode()).body(new ResponseMetaData(new MetaDTO(MetaData.BAD_REQUEST), null));
    }

    @ExceptionHandler
    public ResponseEntity<ResponseMetaData> handleBadCredentialsException(BadCredentialsException ex) {
        return ResponseEntity.badRequest().body(new ResponseMetaData(new MetaDTO(MetaData.USERNAME_OR_PASSWORD_INCORRECT), null));
    }

    @ExceptionHandler
    public ResponseEntity<ResponseMetaData> handleInternalAuthenticationServiceException(InternalAuthenticationServiceException ex) {
        return ResponseEntity.status(MetaData.BAD_REQUEST.getMetaCode()).body(new ResponseMetaData(new MetaDTO(MetaData.BAD_REQUEST), null));
    }

    @ExceptionHandler
    public ResponseEntity<ResponseMetaData> handleAuthenticationException(AuthenticationException ex) {
        return ResponseEntity.status(MetaData.BAD_REQUEST.getMetaCode()).body(new ResponseMetaData(new MetaDTO(MetaData.BAD_REQUEST), null));
    }

    @ExceptionHandler
    public ResponseEntity<ResponseMetaData> handleHttpRequestMethodNotSupportedException(HttpRequestMethodNotSupportedException ex) {
        return ResponseEntity.status(MetaData.METHOD_NOT_ALLOWED.getMetaCode()).body(new ResponseMetaData(new MetaDTO(MetaData.METHOD_NOT_ALLOWED), null));
    }

    @ExceptionHandler
    public ResponseEntity<ResponseMetaData> handleHttpMessageNotReadableException(HttpMessageNotReadableException ex) {
        return ResponseEntity.badRequest().body(new ResponseMetaData(new MetaDTO(MetaData.DOB_INVALID), null));
    }

    @ExceptionHandler
    public ResponseEntity<ResponseMetaData> handleHttpMediaTypeNotSupportedException(HttpMediaTypeNotSupportedException ex) {
        log.warn("Http Media Type Not Supported Exception ", ex);
        return ResponseEntity.badRequest().body(new ResponseMetaData(new MetaDTO(MetaData.HTTP_MEDIA_TYPE_NOT_SUPPORTED), null));
    }

    @ExceptionHandler
    public ResponseEntity<ResponseMetaData> handleEncodingDataInvalidException(EncodingDataInvalidException ex) {
        log.warn("Input Encoding Data Invalid Exception ", ex);
        return ResponseEntity.badRequest().body(new ResponseMetaData(new MetaDTO(MetaData.INPUT_ENCODING_DATA_INVALID), null));
    }

    @ExceptionHandler
    public ResponseEntity<ResponseMetaData> handleSignatureException(SignatureException ex) {
        log.warn("Error {}", ex.getMessage());
        return ResponseEntity.status(MetaData.UNAUTHORIZED.getMetaCode()).body(new ResponseMetaData(new MetaDTO(MetaData.UNAUTHORIZED), null));
    }

    @ExceptionHandler
    public ResponseEntity<ResponseMetaData> handleMalformedJwtException(MalformedJwtException ex) {
        log.warn("Error {}", ex.getMessage());
        return ResponseEntity.badRequest().body(new ResponseMetaData(new MetaDTO(MetaData.TOKEN_INVALID), null));
    }

    @ExceptionHandler
    public ResponseEntity<ResponseMetaData> handleExpiredJwtException(ExpiredJwtException ex) {
        log.warn("Error {}", ex.getMessage());
        return ResponseEntity.badRequest().body(new ResponseMetaData(new MetaDTO(MetaData.TOKEN_EXPIRED)));
    }

}
