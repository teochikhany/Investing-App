package usj.genielogiciel.investingapp.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;
import usj.genielogiciel.investingapp.model.ExceptionResponce;

import java.util.Date;


@ControllerAdvice
@RestController
public class ExceptionHandeler extends ResponseEntityExceptionHandler
{

    @ExceptionHandler(StockNotFound.class)
    public final ResponseEntity<ExceptionResponce> handleNotFoundException(StockNotFound ex, WebRequest request)
    {
        ExceptionResponce exceptionResponse
                = new ExceptionResponce(new Date(), HttpStatus.NOT_FOUND.value(), ex.getMessage());

        return new ResponseEntity<ExceptionResponce>(exceptionResponse, HttpStatus.NOT_FOUND);
    }

}
