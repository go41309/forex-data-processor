package com.taifex.service;

import java.math.RoundingMode;
import java.time.LocalDate;
import java.util.LinkedList;
import java.util.List;
import org.apache.commons.lang3.StringUtils;
import org.json.JSONArray;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Service;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.taifex.model.DailyForeignExchangeRate;
import com.taifex.repository.DailyForeignExchangeRateRepository;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
public class DailyForeignExchangeRateService {

  @Autowired
  private ObjectMapper objectMapper;

  @Autowired
  private ForexAPIService forexAPIService;

  @Autowired
  private DailyForeignExchangeRateRepository dailyForeignExchangeRateRepo;

  @Autowired
  private MongoTemplate mongoTemplate;

  public List<DailyForeignExchangeRate> createAll(List<DailyForeignExchangeRate> records) {
    log.info("Start to create DailyForeignExchangeRates.");

    return this.dailyForeignExchangeRateRepo.saveAll(records);
  }

  public List<DailyForeignExchangeRate> download() {
    log.info("Start to get daily foreign exchange rates.");

    String rawData = this.forexAPIService.callGetDailyForeignExchangeRatesApi();
    return this.convertToObject(rawData);
  }

  public List<DailyForeignExchangeRate> convertToObject(String data) {

    List<DailyForeignExchangeRate> result = new LinkedList<DailyForeignExchangeRate>();

    if (data == null || StringUtils.isEmpty(data)) {
      return result;
    }

    try {
      return objectMapper.readValue(data, new TypeReference<List<DailyForeignExchangeRate>>() {});
    } catch (Exception e) {
      log.warn("Failed to convert data to DailyForeignExchangeRate.");
      e.printStackTrace();
    }

    return result;
  }

  public List<DailyForeignExchangeRate> query(
      LocalDate startDate, 
      LocalDate endDate,
      String currency) {
    
    log.info("Start to query. "
        + "startDate: {}, endDate: {}, currency: {}", startDate, endDate,
        currency);

    // Check date is illegal or not
    this.checkQueryDate(startDate, endDate);

    // Query mongoDb
    List<DailyForeignExchangeRate> queryResult = this.queryDb(startDate, endDate, currency);
    log.info("MongoDb query result size: {}", queryResult.size());

    return queryResult;
  }

  private void checkQueryDate(LocalDate startDate, LocalDate endDate) {
    log.info("Start to check query date." + "startDate: {}, endDate: {}", startDate, endDate);

    LocalDate today = LocalDate.now();
    LocalDate permitEndDate = today.minusDays(1L);
    LocalDate permitStartDate =
        LocalDate.of(today.getYear() - 1, today.getMonthValue(), today.getDayOfMonth());

    if (startDate.isBefore(permitStartDate) || endDate.isAfter(permitEndDate)) {
      String msg = "日期區間不符";
      throw new IllegalArgumentException(msg);
    }

  }

  private List<DailyForeignExchangeRate> queryDb(
      LocalDate startDate, 
      LocalDate endDate,
      String currency) {
    log.info("Start to query mongoDb. "
        + "startDate: {}, endDate: {}, currency: {}", startDate, endDate,
        currency);

    Query query = new Query();
    query.addCriteria(Criteria.where("Date").gte(startDate).lte(endDate));

    return mongoTemplate.find(query, DailyForeignExchangeRate.class);
  }
  
  public String convertToJsonArray(
      String currency,
      List<DailyForeignExchangeRate> data) {
    log.info("Start to convert data to json array.");
    
    JSONArray jsonArray = new JSONArray();

    for (DailyForeignExchangeRate rate : data) {
      JSONObject currencyObject = new JSONObject();
      currencyObject.put("date", rate.getDate());
      currencyObject.put(currency, this.getValueForCurrency(currency, rate));
      jsonArray.put(currencyObject);
    }
    
    return jsonArray.toString();
  }

  private String getValueForCurrency(String currency, DailyForeignExchangeRate rate) {
    switch (currency) {
      case "usd":
        return rate.getUsdNtd().setScale(2, RoundingMode.DOWN).toString();

      default:
        break;
    }
    
    throw new RuntimeException("Failed to get value for currency.");
  }
  
}
