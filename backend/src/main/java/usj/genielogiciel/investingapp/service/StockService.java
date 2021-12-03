package usj.genielogiciel.investingapp.service;

import usj.genielogiciel.investingapp.model.Stock;

import java.util.List;

public interface StockService
{
    List<Stock> getStocks();

    Stock getStock(int id);

    void addStock(Stock stock);

    void deleteStock(int id);

    void updateStock(Stock stock);
}
