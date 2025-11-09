package com.necpgame.backjava.model;

import java.net.URI;
import java.util.Objects;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;
import java.util.UUID;
import org.springframework.lang.Nullable;
import org.openapitools.jackson.nullable.JsonNullable;
import java.time.OffsetDateTime;
import jakarta.validation.Valid;
import jakarta.validation.constraints.*;
import io.swagger.v3.oas.annotations.media.Schema;


import java.util.*;
import jakarta.annotation.Generated;

/**
 * GuaranteedReward
 */

@Generated(value = "org.openapitools.codegen.languages.SpringCodegen", comments = "Generator version: 7.17.0")
public class GuaranteedReward {

  private UUID rewardId;

  private String templateId;

  private @Nullable String description;

  /**
   * Gets or Sets deliveryMode
   */
  public enum DeliveryModeEnum {
    DIRECT("DIRECT"),
    
    MAIL("MAIL"),
    
    VENDOR("VENDOR"),
    
    TOKEN("TOKEN");

    private final String value;

    DeliveryModeEnum(String value) {
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
    public static DeliveryModeEnum fromValue(String value) {
      for (DeliveryModeEnum b : DeliveryModeEnum.values()) {
        if (b.value.equals(value)) {
          return b;
        }
      }
      throw new IllegalArgumentException("Unexpected value '" + value + "'");
    }
  }

  private @Nullable DeliveryModeEnum deliveryMode;

  public GuaranteedReward() {
    super();
  }

  /**
   * Constructor with only required parameters
   */
  public GuaranteedReward(UUID rewardId, String templateId) {
    this.rewardId = rewardId;
    this.templateId = templateId;
  }

  public GuaranteedReward rewardId(UUID rewardId) {
    this.rewardId = rewardId;
    return this;
  }

  /**
   * Get rewardId
   * @return rewardId
   */
  @NotNull @Valid 
  @Schema(name = "rewardId", requiredMode = Schema.RequiredMode.REQUIRED)
  @JsonProperty("rewardId")
  public UUID getRewardId() {
    return rewardId;
  }

  public void setRewardId(UUID rewardId) {
    this.rewardId = rewardId;
  }

  public GuaranteedReward templateId(String templateId) {
    this.templateId = templateId;
    return this;
  }

  /**
   * Get templateId
   * @return templateId
   */
  @NotNull 
  @Schema(name = "templateId", requiredMode = Schema.RequiredMode.REQUIRED)
  @JsonProperty("templateId")
  public String getTemplateId() {
    return templateId;
  }

  public void setTemplateId(String templateId) {
    this.templateId = templateId;
  }

  public GuaranteedReward description(@Nullable String description) {
    this.description = description;
    return this;
  }

  /**
   * Get description
   * @return description
   */
  
  @Schema(name = "description", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("description")
  public @Nullable String getDescription() {
    return description;
  }

  public void setDescription(@Nullable String description) {
    this.description = description;
  }

  public GuaranteedReward deliveryMode(@Nullable DeliveryModeEnum deliveryMode) {
    this.deliveryMode = deliveryMode;
    return this;
  }

  /**
   * Get deliveryMode
   * @return deliveryMode
   */
  
  @Schema(name = "deliveryMode", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("deliveryMode")
  public @Nullable DeliveryModeEnum getDeliveryMode() {
    return deliveryMode;
  }

  public void setDeliveryMode(@Nullable DeliveryModeEnum deliveryMode) {
    this.deliveryMode = deliveryMode;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    GuaranteedReward guaranteedReward = (GuaranteedReward) o;
    return Objects.equals(this.rewardId, guaranteedReward.rewardId) &&
        Objects.equals(this.templateId, guaranteedReward.templateId) &&
        Objects.equals(this.description, guaranteedReward.description) &&
        Objects.equals(this.deliveryMode, guaranteedReward.deliveryMode);
  }

  @Override
  public int hashCode() {
    return Objects.hash(rewardId, templateId, description, deliveryMode);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class GuaranteedReward {\n");
    sb.append("    rewardId: ").append(toIndentedString(rewardId)).append("\n");
    sb.append("    templateId: ").append(toIndentedString(templateId)).append("\n");
    sb.append("    description: ").append(toIndentedString(description)).append("\n");
    sb.append("    deliveryMode: ").append(toIndentedString(deliveryMode)).append("\n");
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

