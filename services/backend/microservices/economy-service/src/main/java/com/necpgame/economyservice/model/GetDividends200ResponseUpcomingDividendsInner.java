package com.necpgame.economyservice.model;

import java.net.URI;
import java.util.Objects;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonTypeName;
import java.math.BigDecimal;
import java.time.OffsetDateTime;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.lang.Nullable;
import org.openapitools.jackson.nullable.JsonNullable;
import java.time.OffsetDateTime;
import jakarta.validation.Valid;
import jakarta.validation.constraints.*;
import io.swagger.v3.oas.annotations.media.Schema;


import java.util.*;
import jakarta.annotation.Generated;

/**
 * GetDividends200ResponseUpcomingDividendsInner
 */

@JsonTypeName("getDividends_200_response_upcoming_dividends_inner")

public class GetDividends200ResponseUpcomingDividendsInner {

  private @Nullable String ticker;

  private @Nullable Integer shares;

  private @Nullable BigDecimal dividendPerShare;

  private @Nullable BigDecimal totalPayout;

  @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME)
  private @Nullable OffsetDateTime payoutDate;

  public GetDividends200ResponseUpcomingDividendsInner ticker(@Nullable String ticker) {
    this.ticker = ticker;
    return this;
  }

  /**
   * Get ticker
   * @return ticker
   */
  
  @Schema(name = "ticker", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("ticker")
  public @Nullable String getTicker() {
    return ticker;
  }

  public void setTicker(@Nullable String ticker) {
    this.ticker = ticker;
  }

  public GetDividends200ResponseUpcomingDividendsInner shares(@Nullable Integer shares) {
    this.shares = shares;
    return this;
  }

  /**
   * Get shares
   * @return shares
   */
  
  @Schema(name = "shares", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("shares")
  public @Nullable Integer getShares() {
    return shares;
  }

  public void setShares(@Nullable Integer shares) {
    this.shares = shares;
  }

  public GetDividends200ResponseUpcomingDividendsInner dividendPerShare(@Nullable BigDecimal dividendPerShare) {
    this.dividendPerShare = dividendPerShare;
    return this;
  }

  /**
   * Get dividendPerShare
   * @return dividendPerShare
   */
  @Valid 
  @Schema(name = "dividend_per_share", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("dividend_per_share")
  public @Nullable BigDecimal getDividendPerShare() {
    return dividendPerShare;
  }

  public void setDividendPerShare(@Nullable BigDecimal dividendPerShare) {
    this.dividendPerShare = dividendPerShare;
  }

  public GetDividends200ResponseUpcomingDividendsInner totalPayout(@Nullable BigDecimal totalPayout) {
    this.totalPayout = totalPayout;
    return this;
  }

  /**
   * Get totalPayout
   * @return totalPayout
   */
  @Valid 
  @Schema(name = "total_payout", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("total_payout")
  public @Nullable BigDecimal getTotalPayout() {
    return totalPayout;
  }

  public void setTotalPayout(@Nullable BigDecimal totalPayout) {
    this.totalPayout = totalPayout;
  }

  public GetDividends200ResponseUpcomingDividendsInner payoutDate(@Nullable OffsetDateTime payoutDate) {
    this.payoutDate = payoutDate;
    return this;
  }

  /**
   * Get payoutDate
   * @return payoutDate
   */
  @Valid 
  @Schema(name = "payout_date", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("payout_date")
  public @Nullable OffsetDateTime getPayoutDate() {
    return payoutDate;
  }

  public void setPayoutDate(@Nullable OffsetDateTime payoutDate) {
    this.payoutDate = payoutDate;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    GetDividends200ResponseUpcomingDividendsInner getDividends200ResponseUpcomingDividendsInner = (GetDividends200ResponseUpcomingDividendsInner) o;
    return Objects.equals(this.ticker, getDividends200ResponseUpcomingDividendsInner.ticker) &&
        Objects.equals(this.shares, getDividends200ResponseUpcomingDividendsInner.shares) &&
        Objects.equals(this.dividendPerShare, getDividends200ResponseUpcomingDividendsInner.dividendPerShare) &&
        Objects.equals(this.totalPayout, getDividends200ResponseUpcomingDividendsInner.totalPayout) &&
        Objects.equals(this.payoutDate, getDividends200ResponseUpcomingDividendsInner.payoutDate);
  }

  @Override
  public int hashCode() {
    return Objects.hash(ticker, shares, dividendPerShare, totalPayout, payoutDate);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class GetDividends200ResponseUpcomingDividendsInner {\n");
    sb.append("    ticker: ").append(toIndentedString(ticker)).append("\n");
    sb.append("    shares: ").append(toIndentedString(shares)).append("\n");
    sb.append("    dividendPerShare: ").append(toIndentedString(dividendPerShare)).append("\n");
    sb.append("    totalPayout: ").append(toIndentedString(totalPayout)).append("\n");
    sb.append("    payoutDate: ").append(toIndentedString(payoutDate)).append("\n");
    sb.append("}");
    return sb.toString();
  }

  /**
   * Convert the given object to string with each line indented by 4 spaces
   * (except the first line).
   */
  private String toIndentedString(Object o) {
    if (o == null) {
      return "null";
    }
    return o.toString().replace("\n", "\n    ");
  }
}

