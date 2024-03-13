package com.taifex.service;

import javax.net.ssl.HostnameVerifier;
import javax.net.ssl.HttpsURLConnection;
import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLSession;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
public class ForexAPIService {

  @Value("${forex.daily-foreign-exchange-rate.url}")
  private String dailyForeignExchangeRateUrl;

  @Autowired
  private RestTemplate restTemplate;

  public String callGetDailyForeignExchangeRatesApi() {
    log.info("Start to call get daily foreign exchange reates API.");

    this.igoreVerify();

    ResponseEntity<String> response =
        restTemplate.getForEntity(this.dailyForeignExchangeRateUrl, String.class);
    if (response.getStatusCode().is2xxSuccessful()) {
      log.info("Succeed to read data from daily foreign exchane rate url.");

      return response.getBody();
    } else {
      String msg = "Failed to fetch data from API.";
      log.error("%s. Status code:%d " + msg, response.getStatusCodeValue());

      throw new RuntimeException(msg);
    }

  }

  /**
   * 忽略驗證https
   */
  private void igoreVerify() {

    try {
      ignoreVerifyHttpsTrustManager();
      ignoreVerifyHttpsHostName();
    } catch (Exception e) {
      e.printStackTrace();
    }
  }

  private void ignoreVerifyHttpsHostName() {
    HostnameVerifier hv = new HostnameVerifier()

    {

      public boolean verify(String urlHostName, SSLSession session)

      {

        System.out.println("Warning: URL Host: " + urlHostName + " vs. " + session.getPeerHost());

        return true;

      }

    };

    HttpsURLConnection.setDefaultHostnameVerifier(hv);
  }

  private void ignoreVerifyHttpsTrustManager() throws Exception {
    TrustManager[] trustAllCerts = new TrustManager[] {new X509TrustManager() {
      public java.security.cert.X509Certificate[] getAcceptedIssuers() {
        return null;
      }

      public void checkClientTrusted(java.security.cert.X509Certificate[] certs, String authType) {}

      public void checkServerTrusted(java.security.cert.X509Certificate[] certs, String authType) {}
    }};

    SSLContext sc = SSLContext.getInstance("SSL");
    sc.init(null, trustAllCerts, new java.security.SecureRandom());
    HttpsURLConnection.setDefaultSSLSocketFactory(sc.getSocketFactory());
  }

}
