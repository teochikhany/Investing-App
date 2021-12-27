package usj.genielogiciel.investingapp.controller;

import lombok.val;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.Errors;
import org.springframework.web.bind.annotation.*;
import usj.genielogiciel.investingapp.exceptions.VariableValidation;
import usj.genielogiciel.investingapp.model.webResponce;
import usj.genielogiciel.investingapp.model.Stock;
import usj.genielogiciel.investingapp.service.StockService;

import javax.validation.Valid;
import java.util.Date;
import java.util.List;

@RestController()
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
    private ResponseEntity<webResponce> getStock(@PathVariable int id)
    {
        val stock = stockService.getStock(id);

        if (!stock.isPresent())
        {
            val result = webResponce.builder()
                                                    .timestamp(new Date())
                                                    .statusCode(HttpStatus.NOT_FOUND.value())
                                                    .errorMessage("No Stock with this id: " + id)
                                                    .build();
            return new ResponseEntity<webResponce>(result, HttpStatus.NOT_FOUND);
        }

        val result = webResponce.builder()
                .timestamp(new Date())
                .statusCode(HttpStatus.OK.value())
                .data(stock.get().toString())
                .build();
        return new ResponseEntity<webResponce>(result, HttpStatus.OK);
    }

    @PostMapping("")
    @ResponseStatus(HttpStatus.CREATED)
    private int addStock(@RequestBody @Valid Stock stock, Errors errors)
    {
        if (errors.hasErrors()) {
            throw new VariableValidation(errors);
        }

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
    private void updateStock(@RequestBody @Valid Stock stock, Errors errors)
    {
        if (errors.hasErrors()) {
            throw new VariableValidation(errors);
        }

        stockService.updateStock(stock);
    }
}
