package com.accenture.controller.advice;

import com.accenture.exception.UtilisateurException;
import com.accenture.exception.VehiculeException;
import com.accenture.model.ErreurReponse;
import jakarta.persistence.EntityNotFoundException;
import org.hibernate.StaleObjectStateException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.orm.ObjectOptimisticLockingFailureException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.time.LocalDateTime;
import java.util.stream.Collectors;

@RestControllerAdvice
public class ApplicationControllerAdvice {

    @ExceptionHandler(UtilisateurException.class)
    public ResponseEntity<ErreurReponse> gestionUtilisateurException(UtilisateurException ex){
        ErreurReponse er = new ErreurReponse(LocalDateTime.now(), "Erreur lié à l'utilisateur", ex.getMessage());
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(er);
    }

    @ExceptionHandler(EntityNotFoundException.class)
    public ResponseEntity<ErreurReponse> gestionEntityNotFoundException(EntityNotFoundException e){
        ErreurReponse er = new ErreurReponse(LocalDateTime.now(), "Mauvaise Requète, Cerveau non branché", e.getMessage());
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(er);}

    @ExceptionHandler(VehiculeException.class)
    public ResponseEntity<ErreurReponse> gestionVehiculeExeption(VehiculeException ex){
        ErreurReponse er = new ErreurReponse(LocalDateTime.now(), "Erreur lié à l'utilisateur", ex.getMessage());
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(er);
    }
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ErreurReponse> problemeValidation(MethodArgumentNotValidException ex){
        String message = ex.getBindingResult().getAllErrors()
                .stream()
                .map(objectError -> objectError.getDefaultMessage())
                .collect(Collectors.joining(","));

        ErreurReponse er = new ErreurReponse(LocalDateTime.now(), "Validation erreur ", message);
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(er);
    }
//    @ExceptionHandler(StaleObjectStateException.class)
//    public ResponseEntity<ErreurReponse> problemeEtat(StaleObjectStateException ex){
//        ErreurReponse er = new ErreurReponse(LocalDateTime.now(), "Erreur Status", ex.getMessage());
//        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(er);

//    }
//    @ExceptionHandler(ObjectOptimisticLockingFailureException.class)
//    public ResponseEntity<String> handleObjectOptimisticLockingFailureException(ObjectOptimisticLockingFailureException ex) {
//        return ResponseEntity.status(HttpStatus.CONFLICT).body("L'entité a été mise à jour ou supprimée par une autre transaction.");
//    }
}
