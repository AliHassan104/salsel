package com.salsel.exception;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;


@ControllerAdvice
public class GlobalExceptionHandler
{
    @ExceptionHandler(value = RecordNotFoundException.class)
    public ResponseEntity<ErrorMessage<Object>> resourceNotFoundException(RecordNotFoundException ex)
    {
        ErrorMessage<Object> errorMessage = ErrorMessage.builder()
                .error(ex.getMessage())
                .time(LocalDateTime.now())
                .build();
        return new ResponseEntity<>(errorMessage,HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(value = BillingException.class)
    public ResponseEntity<ErrorMessage<Object>> billingException(BillingException ex)
    {
        ErrorMessage<Object> errorMessage = ErrorMessage.builder()
                .error(ex.getMessage())
                .time(LocalDateTime.now())
                .build();
        return new ResponseEntity<>(errorMessage,HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @ExceptionHandler(value = AddressBookDetailsAlreadyExist.class)
    public ResponseEntity<ErrorMessage<Object>> resourceAlreadyExistException(AddressBookDetailsAlreadyExist ex)
    {
        ErrorMessage<Object> errorMessage = ErrorMessage.builder()
                .error(ex.getMessage())
                .time(LocalDateTime.now())
                .build();
        return new ResponseEntity<>(errorMessage,HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(value = UserAlreadyExistAuthenticationException.class)
    public ResponseEntity<ErrorMessage<Object>> UserAlreadyExistException(UserAlreadyExistAuthenticationException ex)
    {
        ErrorMessage<Object> errorMessage = ErrorMessage.builder()
                .error(ex.getMessage())
                .time(LocalDateTime.now())
                .build();
        return new ResponseEntity<>(errorMessage,HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<Map<String, String>> handleMethodArgumentNotValid(MethodArgumentNotValidException ex) {
        Map<String, String> errors = new HashMap<>();

        ex.getBindingResult().getFieldErrors().forEach(error ->
                errors.put(error.getField(), error.getDefaultMessage()));

        return new ResponseEntity<Map<String, String>>(errors,HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(BadCredentialsException.class)
    public ResponseEntity<ErrorMessage<Object>> handleBadCredentialsException(BadCredentialsException ex) {

        ErrorMessage<Object> errorMessage = ErrorMessage.builder()
                .error(ex.getMessage())
                .time(LocalDateTime.now())
                .build();

        return new ResponseEntity<>(errorMessage,HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(value = InvalidResetCodeException.class)
    public ResponseEntity<ErrorMessage<Object>> handleInvalidResetCodeException(InvalidResetCodeException ex)
    {
        ErrorMessage<Object> errorMessage = ErrorMessage.builder()
                .error(ex.getMessage())
                .time(LocalDateTime.now())
                .build();
        return new ResponseEntity<>(errorMessage,HttpStatus.BAD_REQUEST);
    }
}
