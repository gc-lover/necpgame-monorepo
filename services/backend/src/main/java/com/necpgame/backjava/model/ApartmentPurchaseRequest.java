package com.necpgame.backjava.model;

import java.net.URI;
import java.util.Objects;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;
import com.necpgame.backjava.model.LayoutPresetPayload;
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
 * ApartmentPurchaseRequest
 */

@Generated(value = "org.openapitools.codegen.languages.SpringCodegen", comments = "Generator version: 7.17.0")
public class ApartmentPurchaseRequest {

  private String locationId;

  private Integer tier;

  /**
   * Gets or Sets paymentMethod
   */
  public enum PaymentMethodEnum {
    CREDITS("credits"),
    
    SHARDS("shards"),
    
    PRESTIGE_POINTS("prestige_points");

    private final String value;

    PaymentMethodEnum(String value) {
      this.value = value;
    }

    @JsonValue
    public String getValue() {
      return value;
    }

    @Override
    public String toString() {
      return String.valueOf(value);
    }

    @JsonCreator
    public static PaymentMethodEnum fromValue(String value) {
      for (PaymentMethodEnum b : PaymentMethodEnum.values()) {
        if (b.value.equals(value)) {
          return b;
        }
      }
      throw new IllegalArgumentException("Unexpected value '" + value + "'");
    }
  }

  private PaymentMethodEnum paymentMethod;

  private @Nullable LayoutPresetPayload initialLayout;

  @Valid
  private List<String> inviteFriends = new ArrayList<>();

  public ApartmentPurchaseRequest() {
    super();
  }

  /**
   * Constructor with only required parameters
   */
  public ApartmentPurchaseRequest(String locationId, Integer tier, PaymentMethodEnum paymentMethod) {
    this.locationId = locationId;
    this.tier = tier;
    this.paymentMethod = paymentMethod;
  }

  public ApartmentPurchaseRequest locationId(String locationId) {
    this.locationId = locationId;
    return this;
  }

  /**
   * Get locationId
   * @return locationId
   */
  @NotNull 
  @Schema(name = "locationId", requiredMode = Schema.RequiredMode.REQUIRED)
  @JsonProperty("locationId")
  public String getLocationId() {
    return locationId;
  }

  public void setLocationId(String locationId) {
    this.locationId = locationId;
  }

  public ApartmentPurchaseRequest tier(Integer tier) {
    this.tier = tier;
    return this;
  }

  /**
   * Get tier
   * minimum: 1
   * maximum: 5
   * @return tier
   */
  @NotNull @Min(value = 1) @Max(value = 5) 
  @Schema(name = "tier", requiredMode = Schema.RequiredMode.REQUIRED)
  @JsonProperty("tier")
  public Integer getTier() {
    return tier;
  }

  public void setTier(Integer tier) {
    this.tier = tier;
  }

  public ApartmentPurchaseRequest paymentMethod(PaymentMethodEnum paymentMethod) {
    this.paymentMethod = paymentMethod;
    return this;
  }

  /**
   * Get paymentMethod
   * @return paymentMethod
   */
  @NotNull 
  @Schema(name = "paymentMethod", requiredMode = Schema.RequiredMode.REQUIRED)
  @JsonProperty("paymentMethod")
  public PaymentMethodEnum getPaymentMethod() {
    return paymentMethod;
  }

  public void setPaymentMethod(PaymentMethodEnum paymentMethod) {
    this.paymentMethod = paymentMethod;
  }

  public ApartmentPurchaseRequest initialLayout(@Nullable LayoutPresetPayload initialLayout) {
    this.initialLayout = initialLayout;
    return this;
  }

  /**
   * Get initialLayout
   * @return initialLayout
   */
  @Valid 
  @Schema(name = "initialLayout", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("initialLayout")
  public @Nullable LayoutPresetPayload getInitialLayout() {
    return initialLayout;
  }

  public void setInitialLayout(@Nullable LayoutPresetPayload initialLayout) {
    this.initialLayout = initialLayout;
  }

  public ApartmentPurchaseRequest inviteFriends(List<String> inviteFriends) {
    this.inviteFriends = inviteFriends;
    return this;
  }

  public ApartmentPurchaseRequest addInviteFriendsItem(String inviteFriendsItem) {
    if (this.inviteFriends == null) {
      this.inviteFriends = new ArrayList<>();
    }
    this.inviteFriends.add(inviteFriendsItem);
    return this;
  }

  /**
   * Get inviteFriends
   * @return inviteFriends
   */
  
  @Schema(name = "inviteFriends", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("inviteFriends")
  public List<String> getInviteFriends() {
    return inviteFriends;
  }

  public void setInviteFriends(List<String> inviteFriends) {
    this.inviteFriends = inviteFriends;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    ApartmentPurchaseRequest apartmentPurchaseRequest = (ApartmentPurchaseRequest) o;
    return Objects.equals(this.locationId, apartmentPurchaseRequest.locationId) &&
        Objects.equals(this.tier, apartmentPurchaseRequest.tier) &&
        Objects.equals(this.paymentMethod, apartmentPurchaseRequest.paymentMethod) &&
        Objects.equals(this.initialLayout, apartmentPurchaseRequest.initialLayout) &&
        Objects.equals(this.inviteFriends, apartmentPurchaseRequest.inviteFriends);
  }

  @Override
  public int hashCode() {
    return Objects.hash(locationId, tier, paymentMethod, initialLayout, inviteFriends);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class ApartmentPurchaseRequest {\n");
    sb.append("    locationId: ").append(toIndentedString(locationId)).append("\n");
    sb.append("    tier: ").append(toIndentedString(tier)).append("\n");
    sb.append("    paymentMethod: ").append(toIndentedString(paymentMethod)).append("\n");
    sb.append("    initialLayout: ").append(toIndentedString(initialLayout)).append("\n");
    sb.append("    inviteFriends: ").append(toIndentedString(inviteFriends)).append("\n");
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

