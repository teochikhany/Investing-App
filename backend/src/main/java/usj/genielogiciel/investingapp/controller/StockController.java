package usj.genielogiciel.investingapp.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import usj.genielogiciel.investingapp.model.Stock;
import usj.genielogiciel.investingapp.service.StockService;

import java.util.List;

//@RestController("/api/v1")
@CrossOrigin("http://localhost:4200")
@RestController
public class StockController
{

    private StockService stockService;

    @Autowired
    public StockController(StockService stockService)
    {
        this.stockService = stockService;
    }

    // @CrossOrigin("http://localhost:4200")
    @GetMapping("/api/v1/stocks")
    private List<Stock> getAllStocks()
    {
        return stockService.getStocks();
    }

    @GetMapping("/api/v1/stocks/{id}")
    private Stock getStock(@PathVariable int id)
    {
        return stockService.getStock(id);
    }

    @PostMapping("/api/v1/stocks")
    private void addStock(@RequestBody Stock stock)
    {
        System.out.println(stock.getName());
        stockService.addStock(stock);
    }

    @DeleteMapping("/api/v1/stocks/{id}")
    private void deleteStock(@PathVariable int id)
    {
        stockService.deleteStock(id);
    }

    @PutMapping("/api/v1/stocks")
    private void updateStock(@RequestBody Stock stock)
    {
        stockService.updateStock(stock);
    }

}
