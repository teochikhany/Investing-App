package usj.genielogiciel.investingapp.controller;

import lombok.extern.slf4j.Slf4j;
import lombok.val;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.Errors;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.annotation.*;
import usj.genielogiciel.investingapp.model.webResponce;
import usj.genielogiciel.investingapp.model.Stock;
import usj.genielogiciel.investingapp.service.StockService;

import javax.validation.ConstraintViolation;
import javax.validation.Valid;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Slf4j
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
    private ResponseEntity<webResponce> getAllStocks()
    {
        List<Stock> stockList = stockService.getStocks();

        val result = webResponce.builder()
                .timestamp(new Date())
                .statusCode(HttpStatus.OK.value())
                .data(stockList.toString())
                .build();

        return new ResponseEntity<webResponce>(result, HttpStatus.OK);
    }

    @GetMapping("{id}")
    private ResponseEntity<webResponce> getStock(@PathVariable int id)
    {
        val stock = stockService.getStock(id);

        if (!stock.isPresent())
        {
            log.error("Cannot find Stock with id: {} in getStock", id);

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
    private ResponseEntity<webResponce> addStock(@RequestBody @Valid Stock stock, Errors errors)
    {
        if (errors.hasErrors()) {
            log.error("Variable Validation error in addStock");

            val result = webResponce.builder()
                                                .timestamp(new Date())
                                                .statusCode(HttpStatus.BAD_REQUEST.value())
                                                .errorMessage(getErrors(errors))
                                                .build();
            return new ResponseEntity<webResponce>(result, HttpStatus.BAD_REQUEST);
        }

        val stockId = stockService.addStock(stock);

        val result = webResponce.builder()
                                            .timestamp(new Date())
                                            .statusCode(HttpStatus.OK.value())
                                            .data(stockId + "")
                                            .build();

        return new ResponseEntity<webResponce>(result, HttpStatus.CREATED);
    }

    @DeleteMapping("{id}")
    private ResponseEntity<webResponce> deleteStock(@PathVariable int id)
    {
        stockService.deleteStock(id);

        val result = webResponce.builder()
                                            .timestamp(new Date())
                                            .statusCode(HttpStatus.OK.value())
                                            .build();

        return new ResponseEntity<webResponce>(result, HttpStatus.OK);
    }

//    @PutMapping("")
//    @ResponseStatus(HttpStatus.OK)
//    private void updateStock(@RequestBody @Valid Stock stock, Errors errors)
//    {
//        if (errors.hasErrors()) {
//            throw new VariableValidation(errors);
//        }
//
//        stockService.updateStock(stock);
//    }

    private String getErrors(Errors errors)
    {
        String exceptionMessage = "";
        List<ConstraintViolation<?>> violationsList = new ArrayList<>();

        for (ObjectError e : errors.getAllErrors())
        {
            violationsList.add(e.unwrap(ConstraintViolation.class));
        }

        // Construct a helpful message for each input violation
        for (ConstraintViolation<?> violation : violationsList)
        {
            exceptionMessage += violation.getMessage() + "\n";
        }

        return exceptionMessage;
    }
}
