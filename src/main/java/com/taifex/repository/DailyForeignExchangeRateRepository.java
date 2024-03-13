package com.taifex.repository;
import java.time.LocalDate;
import java.util.List;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;
import com.taifex.model.DailyForeignExchangeRate;

@Repository
public interface DailyForeignExchangeRateRepository extends MongoRepository<DailyForeignExchangeRate, String> {

  List<DailyForeignExchangeRate> findByDateBetween(LocalDate startDate, LocalDate endDate);
  
}

