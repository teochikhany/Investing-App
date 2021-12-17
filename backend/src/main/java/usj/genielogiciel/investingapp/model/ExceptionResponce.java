package usj.genielogiciel.investingapp.model;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.Date;

@Data
@AllArgsConstructor
public class ExceptionResponce
{
    private Date timestamp;
    private int statusCode;
    private String message;
}
