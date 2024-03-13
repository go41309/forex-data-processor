package com.taifex.controller;

import java.time.LocalDate;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import com.taifex.model.DailyForeignExchangeRate;
import com.taifex.pojo.DailyForeignExchangeRateRequest;
import com.taifex.pojo.DailyForeignExchangeRateResponse;
import com.taifex.pojo.ErrorResponse;
import com.taifex.service.DailyForeignExchangeRateService;
import lombok.extern.slf4j.Slf4j;

@RestController
@RequestMapping(path = "foreign_exchange_rate")
@Slf4j
public class DailyForeignExchangeRateController {
  
  @Autowired
  private DailyForeignExchangeRateService dailyForeignExchangeRateServ;
  
  @PostMapping(
      consumes = MediaType.APPLICATION_JSON_VALUE,
      produces = MediaType.APPLICATION_JSON_VALUE)
  public ResponseEntity<?> query(
      @RequestBody DailyForeignExchangeRateRequest request) {
    String currency = request.getCurrency();
    log.info(
        "Call query daily foreign exchange rate API. "
            + "startDate: {}, endDate: {}, currency: {}",
            request.getStartDate(), request.getEndDate(), currency);

    try {
      LocalDate startDate = LocalDate.parse(request.getStartDate().replace("/", "-"));
      LocalDate endDate = LocalDate.parse(request.getEndDate().replace("/", "-"));

      List<DailyForeignExchangeRate> queryResult =
          this.dailyForeignExchangeRateServ.query(startDate, endDate, currency);

      String currencyData = this.dailyForeignExchangeRateServ.convertToJsonArray(currency, queryResult);
      
      DailyForeignExchangeRateResponse response = 
          DailyForeignExchangeRateResponse.builder()
          .error(new ErrorResponse("0000"))
          .currency(currencyData)
          .build();
      
      return new ResponseEntity<>(response, HttpStatus.OK);
    } catch (IllegalArgumentException e) {
      String msg = "Failed to call query daily foreign exchange rate API "
          + "due to illegal query period.";
      log.error(msg, e);
      return new ResponseEntity<>(ErrorResponse.builder().code("E001").build(),
          HttpStatus.BAD_REQUEST);
    } catch (Throwable t) {
      String msg = "Failed to call query daily foreign exchange rate API "
          + "due to internal server error.";
      log.error(msg, t);
      return new ResponseEntity<>(ErrorResponse.builder().code("500").build(),
          HttpStatus.INTERNAL_SERVER_ERROR);
    }
  }
  
}
