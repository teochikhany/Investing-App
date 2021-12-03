package usj.genielogiciel.investingapp.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import usj.genielogiciel.investingapp.model.Stock;
import usj.genielogiciel.investingapp.repository.StockRepository;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

@Service
public class StockServiceImpl implements StockService
{


    private StockRepository stockRepository;

    @Autowired
    public StockServiceImpl(StockRepository stockRepository)
    {
        this.stockRepository = stockRepository;
    }

    @Override
    public List<Stock> getStocks()
    {
        List<Stock> stocks = new ArrayList<>();
        stockRepository.findAll().forEach(stocks::add);
        return Collections.unmodifiableList(stocks);
    }

    @Override
    public Stock getStock(int id)
    {
        return stockRepository.findById(id).orElse(new Stock());
    }

    @Override
    public void addStock(Stock stock)
    {
        stockRepository.save(stock);
    }

    @Override
    public void deleteStock(int id)
    {
        stockRepository.deleteById(id);
    }

    @Override
    public void updateStock(Stock stock)
    {
        stockRepository.save(stock);
        //stockRepository.findByTicker("test");
    }
}
