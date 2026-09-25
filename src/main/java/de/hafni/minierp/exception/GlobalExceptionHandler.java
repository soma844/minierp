package de.hafni.minierp.exception;

import java.util.Map;

import org.springframework.http.HttpStatus;
import org.springframework.http.ProblemDetail;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(DomainValidationException.class)
    public ProblemDetail handleDomainValidation(
            DomainValidationException exception) {

        ProblemDetail problem =
                ProblemDetail.forStatusAndDetail(
                        HttpStatus.BAD_REQUEST,
                        exception.getMessage());

        problem.setTitle(
                "Ungültige Fachdaten");

        problem.setProperty(
                "fehlerCode",
                exception.getFehlerCode());

        return problem;
    }

    @ExceptionHandler(GeschaeftsregelException.class)
    public ProblemDetail handleGeschaeftsregel(
            GeschaeftsregelException exception) {

        ProblemDetail problem =
                ProblemDetail.forStatusAndDetail(
                        HttpStatus.CONFLICT,
                        exception.getMessage());

        problem.setTitle(
                "Geschäftsregel verletzt");

        problem.setProperty(
                "fehlerCode",
                exception.getFehlerCode());

        return problem;
    }

    @ExceptionHandler(IllegalArgumentException.class)
    public ProblemDetail handleIllegalArgument(
            IllegalArgumentException exception) {

        ProblemDetail problem =
                ProblemDetail.forStatusAndDetail(
                        HttpStatus.BAD_REQUEST,
                        exception.getMessage());

        problem.setTitle(
                "Ungültige Anfrage");

        return problem;
    }

    @ExceptionHandler(IllegalStateException.class)
    public ProblemDetail handleIllegalState(
            IllegalStateException exception) {

        ProblemDetail problem =
                ProblemDetail.forStatusAndDetail(
                        HttpStatus.CONFLICT,
                        exception.getMessage());

        problem.setTitle(
                "Ungültiger Zustand");

        return problem;
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ProblemDetail handleValidation(
            MethodArgumentNotValidException exception) {

        Map<String, String> feldFehler =
                exception
                        .getBindingResult()
                        .getFieldErrors()
                        .stream()
                        .collect(
                                java.util.stream.Collectors.toMap(
                                        fehler ->
                                                fehler.getField(),

                                        fehler ->
                                                fehler.getDefaultMessage()
                                                        != null
                                                                ? fehler
                                                                        .getDefaultMessage()
                                                                : "Ungültiger Wert",

                                        (a, b) -> a));

        ProblemDetail problem =
                ProblemDetail.forStatusAndDetail(
                        HttpStatus.BAD_REQUEST,
                        "Die Anfrage enthält ungültige Felder.");

        problem.setTitle(
                "Validierungsfehler");

        problem.setProperty(
                "felder",
                feldFehler);

        return problem;
    }
    @ExceptionHandler(RessourceNichtGefundenException.class)
    public ProblemDetail handleRessourceNichtGefunden(
            RessourceNichtGefundenException ex) {

        ProblemDetail problem =
                ProblemDetail.forStatusAndDetail(
                        HttpStatus.NOT_FOUND,
                        ex.getMessage()
                );

        problem.setTitle("Ressource nicht gefunden");

        return problem;
    }
   
}