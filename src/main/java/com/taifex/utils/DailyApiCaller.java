package com.taifex.utils;

import java.time.Instant;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import com.taifex.model.DailyForeignExchangeRate;
import com.taifex.service.DailyForeignExchangeRateService;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Component
public class DailyApiCaller {

  @Autowired
  private DailyForeignExchangeRateService dailyForeignExchangeRateServ;

  @Scheduled(cron = "${spring.task.scheduling.cron}")
  public void executeDailyTask() {
    log.info("Start to execute daily task. timeStamp: {}", Instant.now());

    List<DailyForeignExchangeRate> records = this.dailyForeignExchangeRateServ.download();
    log.info("DailyForeignExchangeRates were downloaded. size: {}", records.size());

    List<DailyForeignExchangeRate> created = 
        this.dailyForeignExchangeRateServ.createAll(records);
    log.info("DailyForeignExchangeRates were created. size: {}", created.size());

    log.info("Daily task is done.");
  }

}
