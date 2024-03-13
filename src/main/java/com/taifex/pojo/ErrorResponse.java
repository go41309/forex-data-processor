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
public class ErrorResponse implements Serializable {

  private static final long serialVersionUID = -603209461372130185L;

  @JsonProperty("code")
  private String code;

  @JsonProperty("message")
  private String message;
  
  public ErrorResponse(String code) {
    this.code = code;
    
    switch (code) {
      case "0000":
        this.message = "成功";
        break;

      case "E001":
        this.message = "日期區間不符";
        break;
        
      default:
        this.message = "INTERNAL_SERVER_ERROR";
        break;
    }
  }
}
