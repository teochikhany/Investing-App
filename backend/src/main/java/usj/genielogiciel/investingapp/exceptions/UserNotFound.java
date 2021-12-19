package usj.genielogiciel.investingapp.exceptions;

public class UserNotFound extends RuntimeException
{
    private final long id;

    public UserNotFound(long id)
    {
        this.id = id;
    }

    @Override
    public String getMessage()
    {
        return "No User with this id: " + id;
    }
}


