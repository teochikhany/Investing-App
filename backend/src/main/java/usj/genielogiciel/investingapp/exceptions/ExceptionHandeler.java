package usj.genielogiciel.investingapp.exceptions;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;
import usj.genielogiciel.investingapp.model.ExceptionResponce;

import javax.validation.ConstraintViolationException;
import java.util.Date;


@ControllerAdvice   // define this class as the Exception handler for the whole app
@RestController
@Slf4j
public class ExceptionHandeler extends ResponseEntityExceptionHandler
{

    @ExceptionHandler(StockNotFound.class)
    public final ResponseEntity<ExceptionResponce> handleNotFoundException(StockNotFound ex, WebRequest request)
    {
        log.error("An Exception has been raised, cannot found Stock");

        ExceptionResponce exceptionResponse
                = new ExceptionResponce(new Date(), HttpStatus.NOT_FOUND.value(), ex.getMessage());

        return new ResponseEntity<ExceptionResponce>(exceptionResponse, HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(VariableValidation.class)
    public final ResponseEntity<ExceptionResponce> handleConstraintViolationExceptions(VariableValidation ex)
    {
        log.error("An Exception has been raised, Variable Validation");

        ExceptionResponce exceptionResponse
                = new ExceptionResponce(new Date(), HttpStatus.BAD_REQUEST.value(), ex.getMessage());

        return new ResponseEntity<ExceptionResponce>(exceptionResponse, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(Exception.class)
    public final ResponseEntity<ExceptionResponce> handleAllExceptions(Exception ex)
    {
        log.error("An Unknown Exception has been raised");

        ExceptionResponce exceptionResponse
                = new ExceptionResponce(new Date(), HttpStatus.BAD_REQUEST.value(), "Unknown error");

        return new ResponseEntity<ExceptionResponce>(exceptionResponse, HttpStatus.BAD_REQUEST);
    }

}
