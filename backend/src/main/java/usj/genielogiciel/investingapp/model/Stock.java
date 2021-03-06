package usj.genielogiciel.investingapp.model;


import javax.persistence.*;
import javax.validation.constraints.NotEmpty;
import java.util.Objects;

@Entity
@Table
public class Stock
{
    @Id
    @GeneratedValue
    private int id;

    @NotEmpty(message = "Name cannot be empty")
    private String name;

    @Column(unique = true)
    @NotEmpty(message = "Ticker cannot be empty")
    private String ticker;

    private int price;

    public Stock()
    {
    }

    public Stock(int id, String name, String ticker, int price)
    {
        this.id = id;
        this.name = name;
        this.ticker = ticker;
        this.price = price;
    }

    public int getId()
    {
        return id;
    }

    public String getName()
    {
        return name;
    }

    public String getTicker()
    {
        return ticker;
    }

    public int getPrice()
    {
        return price;
    }

    public void setId(int id)
    {
        this.id = id;
    }

    public void setName(String name)
    {
        this.name = name;
    }

    public void setTicker(String ticker)
    {
        this.ticker = ticker;
    }

    public void setPrice(int price)
    {
        this.price = price;
    }


    @Override
    public boolean equals(Object o)
    {
        if (this == o) return true;
        if (!(o instanceof Stock)) return false;
        Stock stock = (Stock) o;
        return getId() == stock.getId() && getPrice() == stock.getPrice() && getName().equals(stock.getName()) && getTicker().equals(stock.getTicker());
    }

    @Override
    public int hashCode()
    {
        return Objects.hash(getId(), getName(), getTicker(), getPrice());
    }

    @Override
    public String toString()
    {
        return "Stock{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", ticker='" + ticker + '\'' +
                ", price=" + price +
                '}';
    }
}
