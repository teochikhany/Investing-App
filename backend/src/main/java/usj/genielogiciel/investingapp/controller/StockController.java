package usj.genielogiciel.investingapp.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import usj.genielogiciel.investingapp.model.Stock;
import usj.genielogiciel.investingapp.service.StockService;

import java.util.List;

//@RestController("/api/v1")
@CrossOrigin("*")
@RestController
public class StockController
{

    private final StockService stockService;

    @Autowired
    public StockController(StockService stockService)
    {
        this.stockService = stockService;
    }

    @GetMapping("/api/v1/stocks")
    @ResponseStatus(HttpStatus.OK)
    private List<Stock> getAllStocks()
    {
        return stockService.getStocks();
    }

    @GetMapping("/api/v1/stocks/{id}")
    @ResponseStatus(HttpStatus.OK)
    private Stock getStock(@PathVariable int id)
    {
        return stockService.getStock(id);
    }

    @PostMapping("/api/v1/stocks")
    @ResponseStatus(HttpStatus.CREATED)
    private int addStock(@RequestBody Stock stock)
    {
        return stockService.addStock(stock);
    }

    @DeleteMapping("/api/v1/stocks/{id}")
    @ResponseStatus(HttpStatus.OK)
    private void deleteStock(@PathVariable int id)
    {
        stockService.deleteStock(id);
    }

    @PutMapping("/api/v1/stocks")
    @ResponseStatus(HttpStatus.OK)
    private void updateStock(@RequestBody Stock stock)
    {
        stockService.updateStock(stock);
    }

}
