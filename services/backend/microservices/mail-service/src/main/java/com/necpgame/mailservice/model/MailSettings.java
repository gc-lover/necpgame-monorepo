package com.necpgame.mailservice.model;

import java.net.URI;
import java.util.Objects;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.necpgame.mailservice.model.MailSettingsFiltersInner;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import org.springframework.lang.Nullable;
import org.openapitools.jackson.nullable.JsonNullable;
import java.time.OffsetDateTime;
import jakarta.validation.Valid;
import jakarta.validation.constraints.*;
import io.swagger.v3.oas.annotations.media.Schema;


import java.util.*;
import jakarta.annotation.Generated;

/**
 * MailSettings
 */


public class MailSettings {

  private @Nullable Boolean autoDeleteExpired;

  private @Nullable Boolean autoAcceptCOD;

  @Valid
  private List<String> blockedSenders = new ArrayList<>();

  @Valid
  private List<@Valid MailSettingsFiltersInner> filters = new ArrayList<>();

  public MailSettings autoDeleteExpired(@Nullable Boolean autoDeleteExpired) {
    this.autoDeleteExpired = autoDeleteExpired;
    return this;
  }

  /**
   * Get autoDeleteExpired
   * @return autoDeleteExpired
   */
  
  @Schema(name = "autoDeleteExpired", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("autoDeleteExpired")
  public @Nullable Boolean getAutoDeleteExpired() {
    return autoDeleteExpired;
  }

  public void setAutoDeleteExpired(@Nullable Boolean autoDeleteExpired) {
    this.autoDeleteExpired = autoDeleteExpired;
  }

  public MailSettings autoAcceptCOD(@Nullable Boolean autoAcceptCOD) {
    this.autoAcceptCOD = autoAcceptCOD;
    return this;
  }

  /**
   * Get autoAcceptCOD
   * @return autoAcceptCOD
   */
  
  @Schema(name = "autoAcceptCOD", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("autoAcceptCOD")
  public @Nullable Boolean getAutoAcceptCOD() {
    return autoAcceptCOD;
  }

  public void setAutoAcceptCOD(@Nullable Boolean autoAcceptCOD) {
    this.autoAcceptCOD = autoAcceptCOD;
  }

  public MailSettings blockedSenders(List<String> blockedSenders) {
    this.blockedSenders = blockedSenders;
    return this;
  }

  public MailSettings addBlockedSendersItem(String blockedSendersItem) {
    if (this.blockedSenders == null) {
      this.blockedSenders = new ArrayList<>();
    }
    this.blockedSenders.add(blockedSendersItem);
    return this;
  }

  /**
   * Get blockedSenders
   * @return blockedSenders
   */
  
  @Schema(name = "blockedSenders", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("blockedSenders")
  public List<String> getBlockedSenders() {
    return blockedSenders;
  }

  public void setBlockedSenders(List<String> blockedSenders) {
    this.blockedSenders = blockedSenders;
  }

  public MailSettings filters(List<@Valid MailSettingsFiltersInner> filters) {
    this.filters = filters;
    return this;
  }

  public MailSettings addFiltersItem(MailSettingsFiltersInner filtersItem) {
    if (this.filters == null) {
      this.filters = new ArrayList<>();
    }
    this.filters.add(filtersItem);
    return this;
  }

  /**
   * Get filters
   * @return filters
   */
  @Valid 
  @Schema(name = "filters", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("filters")
  public List<@Valid MailSettingsFiltersInner> getFilters() {
    return filters;
  }

  public void setFilters(List<@Valid MailSettingsFiltersInner> filters) {
    this.filters = filters;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    MailSettings mailSettings = (MailSettings) o;
    return Objects.equals(this.autoDeleteExpired, mailSettings.autoDeleteExpired) &&
        Objects.equals(this.autoAcceptCOD, mailSettings.autoAcceptCOD) &&
        Objects.equals(this.blockedSenders, mailSettings.blockedSenders) &&
        Objects.equals(this.filters, mailSettings.filters);
  }

  @Override
  public int hashCode() {
    return Objects.hash(autoDeleteExpired, autoAcceptCOD, blockedSenders, filters);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class MailSettings {\n");
    sb.append("    autoDeleteExpired: ").append(toIndentedString(autoDeleteExpired)).append("\n");
    sb.append("    autoAcceptCOD: ").append(toIndentedString(autoAcceptCOD)).append("\n");
    sb.append("    blockedSenders: ").append(toIndentedString(blockedSenders)).append("\n");
    sb.append("    filters: ").append(toIndentedString(filters)).append("\n");
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

