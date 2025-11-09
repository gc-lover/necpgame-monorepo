package com.necpgame.backjava.model;

import java.net.URI;
import java.util.Objects;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonCreator;
import java.math.BigDecimal;
import org.springframework.lang.Nullable;
import org.openapitools.jackson.nullable.JsonNullable;
import java.time.OffsetDateTime;
import jakarta.validation.Valid;
import jakarta.validation.constraints.*;
import io.swagger.v3.oas.annotations.media.Schema;


import java.util.*;
import jakarta.annotation.Generated;

/**
 * MailStats
 */

@Generated(value = "org.openapitools.codegen.languages.SpringCodegen", comments = "Generator version: 7.17.0")
public class MailStats {

  private @Nullable String range;

  private @Nullable Integer totalSent;

  private @Nullable Integer totalReceived;

  private @Nullable BigDecimal codSuccessRate;

  private @Nullable BigDecimal attachmentClaimRate;

  private @Nullable Integer flaggedCount;

  public MailStats range(@Nullable String range) {
    this.range = range;
    return this;
  }

  /**
   * Get range
   * @return range
   */
  
  @Schema(name = "range", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("range")
  public @Nullable String getRange() {
    return range;
  }

  public void setRange(@Nullable String range) {
    this.range = range;
  }

  public MailStats totalSent(@Nullable Integer totalSent) {
    this.totalSent = totalSent;
    return this;
  }

  /**
   * Get totalSent
   * @return totalSent
   */
  
  @Schema(name = "totalSent", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("totalSent")
  public @Nullable Integer getTotalSent() {
    return totalSent;
  }

  public void setTotalSent(@Nullable Integer totalSent) {
    this.totalSent = totalSent;
  }

  public MailStats totalReceived(@Nullable Integer totalReceived) {
    this.totalReceived = totalReceived;
    return this;
  }

  /**
   * Get totalReceived
   * @return totalReceived
   */
  
  @Schema(name = "totalReceived", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("totalReceived")
  public @Nullable Integer getTotalReceived() {
    return totalReceived;
  }

  public void setTotalReceived(@Nullable Integer totalReceived) {
    this.totalReceived = totalReceived;
  }

  public MailStats codSuccessRate(@Nullable BigDecimal codSuccessRate) {
    this.codSuccessRate = codSuccessRate;
    return this;
  }

  /**
   * Get codSuccessRate
   * @return codSuccessRate
   */
  @Valid 
  @Schema(name = "codSuccessRate", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("codSuccessRate")
  public @Nullable BigDecimal getCodSuccessRate() {
    return codSuccessRate;
  }

  public void setCodSuccessRate(@Nullable BigDecimal codSuccessRate) {
    this.codSuccessRate = codSuccessRate;
  }

  public MailStats attachmentClaimRate(@Nullable BigDecimal attachmentClaimRate) {
    this.attachmentClaimRate = attachmentClaimRate;
    return this;
  }

  /**
   * Get attachmentClaimRate
   * @return attachmentClaimRate
   */
  @Valid 
  @Schema(name = "attachmentClaimRate", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("attachmentClaimRate")
  public @Nullable BigDecimal getAttachmentClaimRate() {
    return attachmentClaimRate;
  }

  public void setAttachmentClaimRate(@Nullable BigDecimal attachmentClaimRate) {
    this.attachmentClaimRate = attachmentClaimRate;
  }

  public MailStats flaggedCount(@Nullable Integer flaggedCount) {
    this.flaggedCount = flaggedCount;
    return this;
  }

  /**
   * Get flaggedCount
   * @return flaggedCount
   */
  
  @Schema(name = "flaggedCount", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("flaggedCount")
  public @Nullable Integer getFlaggedCount() {
    return flaggedCount;
  }

  public void setFlaggedCount(@Nullable Integer flaggedCount) {
    this.flaggedCount = flaggedCount;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    MailStats mailStats = (MailStats) o;
    return Objects.equals(this.range, mailStats.range) &&
        Objects.equals(this.totalSent, mailStats.totalSent) &&
        Objects.equals(this.totalReceived, mailStats.totalReceived) &&
        Objects.equals(this.codSuccessRate, mailStats.codSuccessRate) &&
        Objects.equals(this.attachmentClaimRate, mailStats.attachmentClaimRate) &&
        Objects.equals(this.flaggedCount, mailStats.flaggedCount);
  }

  @Override
  public int hashCode() {
    return Objects.hash(range, totalSent, totalReceived, codSuccessRate, attachmentClaimRate, flaggedCount);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class MailStats {\n");
    sb.append("    range: ").append(toIndentedString(range)).append("\n");
    sb.append("    totalSent: ").append(toIndentedString(totalSent)).append("\n");
    sb.append("    totalReceived: ").append(toIndentedString(totalReceived)).append("\n");
    sb.append("    codSuccessRate: ").append(toIndentedString(codSuccessRate)).append("\n");
    sb.append("    attachmentClaimRate: ").append(toIndentedString(attachmentClaimRate)).append("\n");
    sb.append("    flaggedCount: ").append(toIndentedString(flaggedCount)).append("\n");
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

