package com.example.project_sem4_springboot_api.exception;

import com.example.project_sem4_springboot_api.dto.ResponseErr;
import io.jsonwebtoken.JwtException;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import java.io.IOException;
import java.time.OffsetDateTime;
import java.util.stream.Collectors;

@RestControllerAdvice
public class ApiExceptionHandler {
    /**
     * Tất cả các Exception không được khai báo sẽ được xử lý tại đây
     */
    @ExceptionHandler(Exception.class)
    @ResponseStatus(value = HttpStatus.INTERNAL_SERVER_ERROR)
    public ResponseErr handleAllException(Exception ex,  HttpServletRequest request) {
        // quá trình kiểm soat lỗi diễn ra ở đây
        return new ResponseErr(
                OffsetDateTime.now(),
                HttpStatus.INTERNAL_SERVER_ERROR.value(),
                ex.getLocalizedMessage(),
                request.getRequestURI(),
                "Internal Server Error"
        );
    }

    /**
     * IndexOutOfBoundsException
     *
     * @description: nằm ngoài phạm vi của mảng
     */
    @ExceptionHandler({IndexOutOfBoundsException.class,})
    @ResponseStatus(value = HttpStatus.BAD_REQUEST)
    public ResponseErr TodoException(IndexOutOfBoundsException ex,  HttpServletRequest request,RuntimeException exRun) {
        return new ResponseErr(
                OffsetDateTime.now(),
                HttpStatus.BAD_REQUEST.value(),
                ex.getMessage(),
                request.getRequestURI(),
                "Bad Request"
        );
    }
    /**
     * MethodArgumentNotValidException
     *
     * @description: Tham số request không hợp lệ
     */
    @ExceptionHandler({MethodArgumentNotValidException.class,})
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ResponseErr handleException(MethodArgumentNotValidException exception, HttpServletRequest request) {
        var errorMessages = exception.getBindingResult().getFieldErrors();
        var mess = errorMessages.stream().map(err -> err.getDefaultMessage() +" with : "+ err.getField() + ": " +err.getRejectedValue()).collect(Collectors.joining("\n "));
        return new ResponseErr(
                OffsetDateTime.now(),
                HttpStatus.BAD_REQUEST.value(),
                "Validation failed at: "+mess,
                request.getRequestURI(),
                "Bad Request"
        );
    }

    /**
     * DataExistedException
     *
     * @description: Data đã tồn tại
     */
    @ExceptionHandler(DataExistedException.class)
    @ResponseStatus(value = HttpStatus.BAD_REQUEST)
    public ResponseErr userAlreadyRegisteredException(DataExistedException ex, HttpServletRequest request) {
        return new ResponseErr(
                OffsetDateTime.now(),
                HttpStatus.BAD_REQUEST.value(),
                ex.getMessage(),
                request.getRequestURI(),
                "Bad Request"
        );
    }

    /**
     * NullPointerException
     *
     * @description: Không tìm thấy data
     */
    @ExceptionHandler(NullPointerException.class)
    @ResponseStatus(value = HttpStatus.NOT_FOUND)
    public ResponseErr handleNullPointerException(NullPointerException ex, HttpServletRequest request) {
        return new ResponseErr(
                OffsetDateTime.now(),
                HttpStatus.NOT_FOUND.value(),
                ex.getMessage(),
                request.getRequestURI(),
                "Not Found"
        );
    }

    /**
     * AuthException
     *
     * @description: Lỗi xác thực
     */
    @ExceptionHandler(AuthException.class)
    @ResponseStatus(value = HttpStatus.UNAUTHORIZED)
    public ResponseErr handleAuthException(AuthException ex, HttpServletRequest request) {
        return new ResponseErr(
                OffsetDateTime.now(),
                HttpStatus.UNAUTHORIZED.value(),
                ex.getMessage(),
                request.getRequestURI(),
                "Authorization Error"
        );
    }

    /**
     * JwtException
     *
     * @description: Lỗi xác thực token
     */
    @ExceptionHandler(JwtException.class)
    @ResponseStatus(value = HttpStatus.UNAUTHORIZED)
    public ResponseErr handleJwtException(JwtException ex, HttpServletRequest request) {
        return new ResponseErr(
                OffsetDateTime.now(),
                HttpStatus.UNAUTHORIZED.value(),
                ex.getMessage(),
                request.getRequestURI(),
                "Authorization Error"
        );
    }

    /**
     * SecurityException
     *
     * @description: Lỗi bảo mật
     */
    @ExceptionHandler(SecurityException.class)
    @ResponseStatus(value = HttpStatus.FORBIDDEN)
    public ResponseErr handleSecurityException(SecurityException ex,HttpServletRequest request) {
        return new ResponseErr(
                OffsetDateTime.now(),
                HttpStatus.FORBIDDEN.value(),
                ex.getMessage(),
                request.getRequestURI(),
                "Forbidden"
        );
    }

    @ExceptionHandler(IOException.class)
    @ResponseStatus(value = HttpStatus.FORBIDDEN)
    public ResponseErr handleIOException(IOException ex ,HttpServletRequest request) {
        return new ResponseErr(
                OffsetDateTime.now(),
                HttpStatus.FORBIDDEN.value(),
                ex.getMessage(),
                request.getRequestURI(),
                "Forbidden"
        );
    }

}