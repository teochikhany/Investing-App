package usj.genielogiciel.investingapp.repository;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;
import usj.genielogiciel.investingapp.model.Stock;

@Repository
public interface StockRepository extends CrudRepository<Stock, Integer>
{
    Stock findByTicker(String ticker);
}
