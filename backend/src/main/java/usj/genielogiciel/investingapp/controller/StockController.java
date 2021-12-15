package usj.genielogiciel.investingapp.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import usj.genielogiciel.investingapp.model.Stock;
import usj.genielogiciel.investingapp.service.StockService;

import javax.validation.Valid;
import java.util.List;

@RestController()
@CrossOrigin("*")
// CrossOrigin("http://frontend:4200")
@RequestMapping("/api/v1/stocks/")
public class StockController
{
    private final StockService stockService;

    @Autowired
    public StockController(StockService stockService)
    {
        this.stockService = stockService;
    }

    @GetMapping("")
    @ResponseStatus(HttpStatus.OK)
    private List<Stock> getAllStocks()
    {
        return stockService.getStocks();
    }

    @GetMapping("{id}")
    @ResponseStatus(HttpStatus.OK)
    private Stock getStock(@PathVariable int id)
    {
        return stockService.getStock(id);
    }

    @PostMapping("")
    @ResponseStatus(HttpStatus.CREATED)
    private int addStock(@RequestBody @Valid Stock stock)
    {
        return stockService.addStock(stock);
    }

    @DeleteMapping("{id}")
    @ResponseStatus(HttpStatus.OK)
    private void deleteStock(@PathVariable int id)
    {
        stockService.deleteStock(id);
    }

    @PutMapping("")
    @ResponseStatus(HttpStatus.OK)
    private void updateStock(@RequestBody @Valid Stock stock)
    {
        stockService.updateStock(stock);
    }
}
