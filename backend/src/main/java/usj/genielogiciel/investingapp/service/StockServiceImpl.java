package usj.genielogiciel.investingapp.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import usj.genielogiciel.investingapp.model.Stock;
import usj.genielogiciel.investingapp.repository.StockRepository;

import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

@Service
public class StockServiceImpl implements StockService
{
    private final StockRepository stockRepository;
    private static final Logger logger = LoggerFactory.getLogger(StockServiceImpl.class);

    @Autowired
    public StockServiceImpl(StockRepository stockRepository)
    {
        this.stockRepository = stockRepository;
    }

    @Override
    public List<Stock> getStocks()
    {
        logger.info("Getting all stocks");
        List<Stock> stocks = new ArrayList<>();
        stockRepository.findAll().forEach(stocks::add);
        return Collections.unmodifiableList(stocks);
    }

    @Override
    public Stock getStock(int id)
    {
        logger.info(MessageFormat.format("Getting stock with id: {0}", id));
        return stockRepository.findById(id).orElse(new Stock());
    }

    @Override
    public int addStock(Stock stock)
    {
        final int new_id = stockRepository.save(stock).getId();
        logger.info(MessageFormat.format("Adding stock to id: {0}", new_id));
        return new_id;
    }

    @Override
    public void deleteStock(int id)
    {
        logger.info(MessageFormat.format("Deleting stock with id: {0}", id));
        stockRepository.deleteById(id);
    }

    @Override
    public void updateStock(Stock stock)
    {
        stockRepository.save(stock);
    }
}
