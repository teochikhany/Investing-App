package usj.genielogiciel.investingapp.service;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.TransactionSystemException;
import usj.genielogiciel.investingapp.exceptions.StockNotFound;
import usj.genielogiciel.investingapp.model.Stock;


import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

@SpringBootTest
public class stockServiceTest
{
    private final StockService stockService;

    @Autowired
    public stockServiceTest(StockService stockService)
    {
        this.stockService = stockService;
    }

    @BeforeEach
    public void CleanUp()
    {
        List<Stock> stocks = stockService.getStocks();

        for(Stock stock : stocks)
        {
            stockService.deleteStock(stock.getId());
        }
    }

    @Test
    public void getInvalidStock()
    {
        StockNotFound thrown = Assertions.assertThrows(StockNotFound.class, () -> {
            stockService.getStock(999);
        });

        assertEquals("No Stock with this id: 999", thrown.getMessage());
    }

    @Test
    public void getStock()
    {
        Stock stock = new Stock(0, "name", "ticker", 45);
        stockService.addStock(stock);

        List<Stock> stocks = stockService.getStocks();
        long size = stocks.size();
        Stock new_stock = stocks.get(0);

        assertEquals(1, size);
        assertEquals("ticker", new_stock.getTicker());
    }

    @Test
    public void addStock()
    {
        long oldSize = stockService.getStocks().size();
        Stock stock = new Stock(0, "name", "ticker", 45);
        stockService.addStock(stock);

        long newSize = stockService.getStocks().size();

        assertEquals(oldSize + 1, newSize);
    }

    @Test
    public void addInvalidStockEmptyTicker()
    {
        Stock stock = new Stock(0, "name", "", 45);

        Assertions.assertThrows(TransactionSystemException.class, () -> {
            stockService.addStock(stock);
        });
    }

    @Test
    public void addInvalidStockEmptyName()
    {
        Stock stock = new Stock(0, "", "ticker", 45);

        Assertions.assertThrows(TransactionSystemException.class, () -> {
            stockService.addStock(stock);
        });
    }

    @Test
    public void deleteStock()
    {
        Stock stock = new Stock(0, "name", "ticker", 45);
        stockService.addStock(stock);
        long oldSize = stockService.getStocks().size();

        Stock newStock = stockService.getStocks().get(0);

        stockService.deleteStock(newStock.getId());
        long newSize = stockService.getStocks().size();

        assertEquals(oldSize - 1, newSize);
    }


    @Test
    public void deleteInvalidStock()
    {
        Stock stock = new Stock(0, "name", "ticker", 45);
        stockService.addStock(stock);

        StockNotFound thrown = Assertions.assertThrows(StockNotFound.class, () -> {
            stockService.deleteStock(99);
        });

        assertEquals("No Stock with this id: 99", thrown.getMessage());
    }
}
