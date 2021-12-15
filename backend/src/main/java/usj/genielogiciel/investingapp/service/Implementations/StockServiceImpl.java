package usj.genielogiciel.investingapp.service.Implementations;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import usj.genielogiciel.investingapp.exceptions.StockNotFound;
import usj.genielogiciel.investingapp.model.Stock;
import usj.genielogiciel.investingapp.repository.StockRepository;
import usj.genielogiciel.investingapp.service.StockService;

import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

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
        List<Stock> stocks = new ArrayList<>();
        stockRepository.findAll().forEach(stocks::add);
        logger.info("Getting all stocks: " + stocks.size() + " stocks");
        return Collections.unmodifiableList(stocks);
    }

    @Override
    public Stock getStock(int id)
    {
        final Optional<Stock> stock = stockRepository.findById(id);
        if (!stock.isPresent())
        {
            logger.error(MessageFormat.format("Add: No stock with id: {0}", id));
            throw new StockNotFound();
        }

        logger.info(MessageFormat.format("Getting stock with id: {0}", id));
        return stock.get();
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
        final Optional<Stock> stock = stockRepository.findById(id);
        if (!stock.isPresent())
        {
            logger.error(MessageFormat.format("Delete: No stock with id: {0}", id));
            throw new StockNotFound();
        }

        logger.info(MessageFormat.format("Deleting stock with id: {0}", id));
        stockRepository.deleteById(id);
    }

    @Override
    public void updateStock(Stock stock)
    {
        logger.info(MessageFormat.format("Deleting stock with id: {0}", stock.getId()));
        stockRepository.save(stock);
    }
}
