package usj.genielogiciel.investingapp.repository;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.dao.DataIntegrityViolationException;
import usj.genielogiciel.investingapp.exceptions.StockNotFound;
import usj.genielogiciel.investingapp.model.Stock;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;


@SpringBootTest
public class StockRepositoryTest
{
    private final StockRepository stockRepository;

    @Autowired
    StockRepositoryTest(StockRepository stockRepository)
    {
        this.stockRepository = stockRepository;
    }

    @BeforeEach
    public void CleanUp()
    {
        stockRepository.deleteAll();
    }

    @Test
    public void AddStockSize()
    {
        long before = stockRepository.count();
        stockRepository.save(new Stock(0, "teo", "asdf", 20));
        long after = stockRepository.count();

        assertEquals(before + 1, after);
    }

    @Test
    public void AddStock()
    {
        stockRepository.save(new Stock(0, "teo", "addStock", 20));
        Stock stock = stockRepository.findByTicker("addStock");

        assertNotNull(stock);
    }

    @Test
    public void AddStockDuplicate()
    {
        DataIntegrityViolationException thrown = Assertions.assertThrows(DataIntegrityViolationException.class, () -> {
            stockRepository.save(new Stock(0, "teo", "addStock", 20));
            stockRepository.save(new Stock(0, "teo", "addStock", 20));
        });
    }

    @Test
    public void DeleteStockSize()
    {
        stockRepository.save(new Stock(0, "deleteStock", "deleteStock", 20));
        long before = stockRepository.count();

        Stock stock = stockRepository.findByTicker("deleteStock");

        stockRepository.deleteById(stock.getId());
        long after = stockRepository.count();

        assertEquals(before - 1, after);
    }

    @Test
    public void DeleteStock()
    {
        stockRepository.save(new Stock(0, "deleteStock", "deleteStock", 20));

        Stock stock = stockRepository.findByTicker("deleteStock");
        stockRepository.deleteById(stock.getId());

        Stock stock1 = stockRepository.findByTicker("deleteStock");

        assertNull(stock1);
    }

    @Test
    public void getStockNotFound()
    {
        Optional<Stock> stock = stockRepository.findById(99);

        assertFalse(stock.isPresent());
    }

    @Test
    public void getStockFound()
    {
        stockRepository.save(new Stock(0, "deleteStock", "deleteStock", 20));
        Stock stock = stockRepository.findByTicker("deleteStock");

        assertNotNull(stock);
    }
}
