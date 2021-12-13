package usj.genielogiciel.investingapp.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;


@ResponseStatus(value = HttpStatus.NOT_FOUND, reason = "No such Stock")
public class StockNotFound extends RuntimeException
{
    public StockNotFound()
    {
    }
}


