package com.taifex;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.LinkedList;
import java.util.List;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Query;
import com.taifex.model.DailyForeignExchangeRate;
import com.taifex.repository.DailyForeignExchangeRateRepository;
import com.taifex.service.DailyForeignExchangeRateService;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@SpringBootTest(classes = TestApplication.class)
public class DownloadDailyForeignExchangeRateTest {

  @Mock
  private MongoTemplate mongoTemplate;

  @Autowired
  private DailyForeignExchangeRateService dailyServ;

  @Autowired
  private DailyForeignExchangeRateRepository dailyRepo;

  @BeforeEach
  public void before() {
    log.info("Start 'before' method.");
  }

  @AfterEach
  public void after() {
    log.info("Start 'after' method.");
  }

  @Test
  public void testCreateAll() {
    // 準備測試數據
    List<DailyForeignExchangeRate> records = new LinkedList<DailyForeignExchangeRate>();
    DailyForeignExchangeRate rate1 = DailyForeignExchangeRate.builder()
        .date(LocalDate.of(2024, 2, 1))
        .usdNtd(BigDecimal.valueOf(31.338))
        .build();
    records.add(rate1);

    DailyForeignExchangeRate rate2 = DailyForeignExchangeRate.builder()
        .date(LocalDate.of(2024, 2, 2))
        .usdNtd(BigDecimal.valueOf(31.243))
        .build();
    records.add(rate2);

    // 模擬
    when(this.dailyRepo.saveAll(records)).thenReturn(records);

    // 執行測試
    List<DailyForeignExchangeRate> result = this.dailyServ.createAll(records);

    // 驗證方法
    verify(this.dailyRepo).saveAll(records);

    // 驗證結果
    assertEquals(records, result);
  }

  @Test
  public void testQuery() {
    // 準備測試參數
    LocalDate startDate = LocalDate.of(2023, 2, 1);
    LocalDate endDate = LocalDate.of(2023, 2, 10);
    String currency = "usd";

    List<DailyForeignExchangeRate> testData = new LinkedList<DailyForeignExchangeRate>();

    // 模擬
    when(mongoTemplate.find(any(Query.class), eq(DailyForeignExchangeRate.class)))
        .thenReturn(testData);

    // 執行測試
    List<DailyForeignExchangeRate> result = this.dailyServ.query(startDate, endDate, currency);

    // 驗證結果
    assertNotNull(result);
  }

}
