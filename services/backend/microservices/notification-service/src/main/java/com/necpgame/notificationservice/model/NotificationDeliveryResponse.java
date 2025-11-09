package com.necpgame.notificationservice.model;

import java.net.URI;
import java.util.Objects;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.necpgame.notificationservice.model.DeliveryStatus;
import com.necpgame.notificationservice.model.PaginationMeta;
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
 * NotificationDeliveryResponse
 */


public class NotificationDeliveryResponse {

  @Valid
  private List<@Valid DeliveryStatus> deliveries = new ArrayList<>();

  private @Nullable PaginationMeta pagination;

  public NotificationDeliveryResponse deliveries(List<@Valid DeliveryStatus> deliveries) {
    this.deliveries = deliveries;
    return this;
  }

  public NotificationDeliveryResponse addDeliveriesItem(DeliveryStatus deliveriesItem) {
    if (this.deliveries == null) {
      this.deliveries = new ArrayList<>();
    }
    this.deliveries.add(deliveriesItem);
    return this;
  }

  /**
   * Get deliveries
   * @return deliveries
   */
  @Valid 
  @Schema(name = "deliveries", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("deliveries")
  public List<@Valid DeliveryStatus> getDeliveries() {
    return deliveries;
  }

  public void setDeliveries(List<@Valid DeliveryStatus> deliveries) {
    this.deliveries = deliveries;
  }

  public NotificationDeliveryResponse pagination(@Nullable PaginationMeta pagination) {
    this.pagination = pagination;
    return this;
  }

  /**
   * Get pagination
   * @return pagination
   */
  @Valid 
  @Schema(name = "pagination", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("pagination")
  public @Nullable PaginationMeta getPagination() {
    return pagination;
  }

  public void setPagination(@Nullable PaginationMeta pagination) {
    this.pagination = pagination;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    NotificationDeliveryResponse notificationDeliveryResponse = (NotificationDeliveryResponse) o;
    return Objects.equals(this.deliveries, notificationDeliveryResponse.deliveries) &&
        Objects.equals(this.pagination, notificationDeliveryResponse.pagination);
  }

  @Override
  public int hashCode() {
    return Objects.hash(deliveries, pagination);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class NotificationDeliveryResponse {\n");
    sb.append("    deliveries: ").append(toIndentedString(deliveries)).append("\n");
    sb.append("    pagination: ").append(toIndentedString(pagination)).append("\n");
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

