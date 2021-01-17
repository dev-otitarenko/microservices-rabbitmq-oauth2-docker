package com.maestro.app.sample.ms.auth.configuration;

import com.maestro.app.utils.domains.APIErrorResponse;
import com.maestro.app.utils.types.ErrorStatus;
import org.hibernate.exception.GenericJDBCException;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.orm.jpa.JpaSystemException;
import org.springframework.security.authentication.DisabledException;
import org.springframework.security.authentication.LockedException;
import org.springframework.transaction.TransactionSystemException;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import javax.persistence.RollbackException;
import javax.validation.ConstraintViolation;
import javax.validation.ConstraintViolationException;
import java.io.UnsupportedEncodingException;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

@ControllerAdvice
public class GlobalExceptionHandler {
    @ExceptionHandler(JpaSystemException.class)
    public Object handleError(final JpaSystemException gse) {
        if (gse.getCause() != null && gse.getCause() instanceof GenericJDBCException) {
            final GenericJDBCException re = (GenericJDBCException) gse.getCause();
            if (re.getCause() != null && re.getCause() instanceof SQLException) {
                return handleSQLException((SQLException) re.getCause());
            }
        }
        throw gse;
    }

    @ExceptionHandler(SQLException.class)
    public Object handleSQLException(SQLException ex) {
        APIErrorResponse apiError = new APIErrorResponse(ErrorStatus.ERROR, ex.getLocalizedMessage(), ex.toString());

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);

        return new ResponseEntity<>(apiError, headers, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(TransactionSystemException.class)
    public ResponseEntity<Object> handleError(final TransactionSystemException tse) {
        if (tse.getCause() != null && tse.getCause() instanceof RollbackException) {
            final RollbackException re = (RollbackException) tse.getCause();
            if (re.getCause() != null && re.getCause() instanceof ConstraintViolationException) {
                return handleConstraintViolation((ConstraintViolationException) re.getCause());
            }
        }
        throw tse;
    }

    @ExceptionHandler(ConstraintViolationException.class)
    public ResponseEntity<Object> handleConstraintViolation(
            ConstraintViolationException ex) {
        List<String> errors = new ArrayList<>();
        for (ConstraintViolation<?> violation : ex.getConstraintViolations()) {
//            errors.add(violation.getPropertyPath().toString() + ": " + violation.getMessage());
            errors.add(violation.getMessage());
        }
        APIErrorResponse apiError = new APIErrorResponse(ErrorStatus.ERROR, ex.getLocalizedMessage(), errors);

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);

        return new ResponseEntity<>(apiError, headers, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<Object> handleMethodArgumentNotValidException(
            MethodArgumentNotValidException ex) {
        List<String> errors = new ArrayList<>();
        for (FieldError fieldError : ex.getBindingResult().getFieldErrors()) {
            errors.add(fieldError.getDefaultMessage());
        }
        APIErrorResponse apiError = new APIErrorResponse(ErrorStatus.ERROR, ex.getLocalizedMessage(), errors);

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);

        return new ResponseEntity<>(apiError, headers, HttpStatus.FORBIDDEN);
    }

    @ExceptionHandler(UnsupportedEncodingException.class)
    public ResponseEntity<Object> handleUnsupportedEncodingException(UnsupportedEncodingException ex) {
        APIErrorResponse apiError = new APIErrorResponse(ErrorStatus.ERROR, ex.getLocalizedMessage(), ex.getLocalizedMessage());

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);

        return new ResponseEntity<>(apiError, headers, HttpStatus.FORBIDDEN);
    }

    @ExceptionHandler({DisabledException.class, LockedException.class})
    public ResponseEntity<Object> handleDisabledOrLockedException(Exception ex) {
        APIErrorResponse apiError = new APIErrorResponse(ErrorStatus.ERROR,
                "Account has been disabled. Please contact your administrator for more details.", "");
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        return new ResponseEntity<>(apiError, headers, HttpStatus.UNAUTHORIZED);
    }
}
