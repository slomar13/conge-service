package com.emsi.conge_service.exception;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import java.time.LocalDateTime;
import java.util.stream.Collectors;

@ControllerAdvice
public class GlobalExceptionHandler {

    // Validation des @Valid
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ErrorResponse> handleValidationException(MethodArgumentNotValidException ex) {
        String message = "Erreur de validation des champs";
        String details = ex.getBindingResult()
                .getAllErrors()
                .stream()
                .map(error -> ((FieldError) error).getField() + ": " + error.getDefaultMessage())
                .collect(Collectors.joining(" | "));


        ErrorResponse error = new ErrorResponse(LocalDateTime.now(), message, details);

        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(error);
    }

    @ExceptionHandler(CongeNotFoundException.class)
    public ResponseEntity<ErrorResponse> handleEmployeeNotFoundException(CongeNotFoundException ex) {
        String message = "Le congé n'existe pas dans la base";
        String details = ex.getMessage();

        ErrorResponse error = new ErrorResponse(LocalDateTime.now(), message, details);

        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(error);
    }

    @ExceptionHandler(EmployeNotFoundException.class)
    public ResponseEntity<ErrorResponse> handleEmployeNotFoundException(EmployeNotFoundException ex) {
        String message = "L'employé n'existe pas dans la base";
        String details = ex.getMessage();
        ErrorResponse error = new ErrorResponse(LocalDateTime.now(), message, details);
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(error);
    }

    @ExceptionHandler(SoldeInsuffisantException.class)
    public ResponseEntity<ErrorResponse> handleSoldeInsuffisantException(SoldeInsuffisantException ex) {
        String message = "Solde de congé insuffisant";
        String details = ex.getMessage();

        ErrorResponse error = new ErrorResponse(LocalDateTime.now(), message, details);

        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(error);
    }

    @ExceptionHandler(CongeConflitException.class)
    public ResponseEntity<ErrorResponse> handleCongeConflitException(CongeConflitException ex) {
        String message = "Conflit de congé (Vous avez déjà un congé à ces dates)";
        String details = ex.getMessage();
        ErrorResponse error = new ErrorResponse(LocalDateTime.now(), message, details);

        return ResponseEntity.status(HttpStatus.CONFLICT).body(error);
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ErrorResponse> handleGeneralException(Exception ex) {
        String message = "Erreur interne du serveur";
        String details = ex.getMessage();

        ErrorResponse error = new ErrorResponse(LocalDateTime.now(), message, details);

        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(error);
    }
}
