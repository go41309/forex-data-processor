package com.taifex.pojo;

import java.io.Serializable;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@Builder(toBuilder = true)
@NoArgsConstructor
@AllArgsConstructor
public class DailyForeignExchangeRateResponse implements Serializable {

  private static final long serialVersionUID = -603209461372130185L;

  @JsonProperty("error")
  private ErrorResponse error;

  @JsonProperty("currency")
  private String currency;
  
}
