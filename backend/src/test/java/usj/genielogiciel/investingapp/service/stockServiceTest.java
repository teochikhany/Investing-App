package usj.genielogiciel.investingapp.service;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import usj.genielogiciel.investingapp.exceptions.StockNotFound;


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

    @Test
    public void getInvalidStock()
    {
        StockNotFound thrown = Assertions.assertThrows(StockNotFound.class, () -> {
            stockService.getStock(999);
        });

        assertEquals("No Stock with this id: 999", thrown.getMessage());
    }




}
