package usj.genielogiciel.investingapp.service;

import usj.genielogiciel.investingapp.model.Stock;

import java.util.List;
import java.util.Optional;

public interface StockService
{
    List<Stock> getStocks();

    Optional<Stock> getStock(int id);

    int addStock(Stock stock);

    void deleteStock(int id);

    void updateStock(Stock stock);
}
