package usj.genielogiciel.investingapp.exceptions;

import org.springframework.validation.Errors;
import org.springframework.validation.ObjectError;

import javax.validation.ConstraintViolation;
import java.util.ArrayList;
import java.util.List;

//public class VariableValidation extends RuntimeException
//{
//    private final Errors errors;
//
//    public VariableValidation(Errors errors)
//    {
//        this.errors = errors;
//    }
//
//    @Override
//    public String getMessage()
//    {
//        String exceptionMessage = "";
//        List<ConstraintViolation<?>> violationsList = new ArrayList<>();
//
//        for (ObjectError e : errors.getAllErrors())
//        {
//            violationsList.add(e.unwrap(ConstraintViolation.class));
//        }
//
//        // Construct a helpful message for each input violation
//        for (ConstraintViolation<?> violation : violationsList)
//        {
//            exceptionMessage += violation.getMessage() + "\n";
//        }
//
//        return exceptionMessage;
//    }
//}
