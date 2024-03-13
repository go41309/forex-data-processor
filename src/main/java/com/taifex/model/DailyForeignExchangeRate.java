package com.taifex.model;

import java.math.BigDecimal;
import java.time.LocalDate;
import javax.persistence.Column;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.taifex.json.deserializer.LocalDateDeserializer;
import com.taifex.json.serializer.LocalDateSerializer;
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
@Document(collection = "daily_foreign_exchange_rate")
public class DailyForeignExchangeRate {

    @Id
    @Column(name = "id")
    @JsonProperty("id")
    private String id;
    
    @Column(name = "date")
    @JsonProperty("Date")
    @JsonSerialize(using = LocalDateSerializer.class)
    @JsonDeserialize(using = LocalDateDeserializer.class)
    private LocalDate date;
    
    @Column(name = "usd_ntd")
    @JsonProperty("USD/NTD")
    private BigDecimal usdNtd;
}