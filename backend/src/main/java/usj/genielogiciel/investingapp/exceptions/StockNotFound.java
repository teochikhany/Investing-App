package usj.genielogiciel.investingapp.exceptions;


public class StockNotFound extends RuntimeException
{
    private final int id;

    public StockNotFound(int id)
    {
        this.id = id;
    }

    @Override
    public String getMessage()
    {
        return "No Stock with this id: " + id;
    }
}


