package com.taifex;

import static org.junit.Assert.assertEquals;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.when;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;
import com.taifex.service.ForexAPIService;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@SpringBootTest(classes = TestApplication.class)
public class ForexAPIServiceTest {

  @Autowired
  private ForexAPIService forexAPIServ;

  @MockBean
  private RestTemplate restTemplate;

  @BeforeEach
  public void before() {
    log.info("Start 'before' method.");
  }

  @AfterEach
  public void after() {
    log.info("Start 'after' method.");
  }

  @Test
  public void callGetDailyForeignExchangeRatesApiTest() {
    // 模擬外部 API 的回應
    String fakeApiResponse =
        "[{\"Date\":\"20240201\",\"USD/NTD\":\"31.338\"}, {\"Date\":\"20240202\",\"USD/NTD\":\"31.243\"}]";
    ResponseEntity<String> fakeResponseEntity =
        new ResponseEntity<>(fakeApiResponse, HttpStatus.OK);

    // 當 restTemplate.getForEntity 方法被呼叫時，回傳模擬的回應
    when(this.restTemplate.getForEntity(anyString(), eq(String.class)))
        .thenReturn(fakeResponseEntity);

    String rawData = this.forexAPIServ.callGetDailyForeignExchangeRatesApi();

    assertEquals(fakeApiResponse, rawData);
  }
}
