package usj.genielogiciel.investingapp.model;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.experimental.SuperBuilder;

import java.util.Date;

@Data
@SuperBuilder
@JsonInclude(JsonInclude.Include.NON_NULL)
public class webResponce
{
    private Date timestamp;
    private int statusCode;
    private String errorMessage;
    private String data;
}
