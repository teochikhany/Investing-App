package usj.genielogiciel.investingapp.exceptions;

import lombok.extern.slf4j.Slf4j;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;
import usj.genielogiciel.investingapp.model.webResponce;

import java.util.Date;
import java.util.Objects;


@ControllerAdvice   // define this class as the Exception handler for the whole app
@RestController
@Slf4j
public class ExceptionHandeler extends ResponseEntityExceptionHandler
{

//    @ExceptionHandler(StockNotFound.class)
//    public final ResponseEntity<webResponce> handleNotFoundException(StockNotFound ex, WebRequest request)
//    {
//        log.error("An Exception has been raised, cannot found Stock");
//
//        webResponce exceptionResponse = webResponce.builder()
//                                                            .timestamp(new Date())
//                                                            .statusCode(HttpStatus.NOT_FOUND.value())
//                                                            .errorMessage(ex.getMessage())
//                                                            .build();
//
//        return new ResponseEntity<webResponce>(exceptionResponse, HttpStatus.NOT_FOUND);
//    }

//    @ExceptionHandler(VariableValidation.class)
//    public final ResponseEntity<webResponce> handleConstraintViolationExceptions(VariableValidation ex)
//    {
//        log.error("An Exception has been raised, Variable Validation");
//
//        webResponce exceptionResponse = webResponce.builder()
//                                                            .timestamp(new Date())
//                                                            .statusCode(HttpStatus.BAD_REQUEST.value())
//                                                            .errorMessage(ex.getMessage())
//                                                            .build();
//
//        return new ResponseEntity<webResponce>(exceptionResponse, HttpStatus.BAD_REQUEST);
//    }

    @ExceptionHandler(DataIntegrityViolationException.class)
    public final ResponseEntity<webResponce> handleSqlExceptions(DataIntegrityViolationException ex, WebRequest request)
    {
        String message = "";

        if (Objects.requireNonNull(ex.getMessage()).contains("APP_USER(USERNAME)"))
        {
            message = "Username needs to be unique";
        }
        else if (ex.getMessage().contains("STOCK(TICKER)"))
        {
            message = "Ticker needs to be unique";
        }

        log.error("Sql Error: {}", message);

        webResponce exceptionResponse = webResponce.builder()
                                                            .timestamp(new Date())
                                                            .statusCode(HttpStatus.BAD_REQUEST.value())
                                                            .errorMessage(message)
                                                            .build();

        return new ResponseEntity<webResponce>(exceptionResponse, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(Exception.class)
    public final ResponseEntity<webResponce> handleAllExceptions(Exception ex)
    {
        log.error("An Unknown Exception has been raised, {}", ex.getClass().toString());

        webResponce exceptionResponse = webResponce.builder()
                                                            .timestamp(new Date())
                                                            .statusCode(HttpStatus.BAD_REQUEST.value())
                                                            .errorMessage("Unknown error Occurred")
                                                            .build();

        return new ResponseEntity<webResponce>(exceptionResponse, HttpStatus.BAD_REQUEST);
    }

}
