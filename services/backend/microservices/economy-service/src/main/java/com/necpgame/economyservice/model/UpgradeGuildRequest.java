package com.necpgame.economyservice.model;

import java.net.URI;
import java.util.Objects;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonTypeName;
import com.fasterxml.jackson.annotation.JsonValue;
import org.springframework.lang.Nullable;
import org.openapitools.jackson.nullable.JsonNullable;
import java.time.OffsetDateTime;
import jakarta.validation.Valid;
import jakarta.validation.constraints.*;
import io.swagger.v3.oas.annotations.media.Schema;


import java.util.*;
import jakarta.annotation.Generated;

/**
 * UpgradeGuildRequest
 */

@JsonTypeName("upgradeGuild_request")

public class UpgradeGuildRequest {

  /**
   * Gets or Sets upgradeType
   */
  public enum UpgradeTypeEnum {
    LEVEL_UP("LEVEL_UP"),
    
    GUILD_HALL("GUILD_HALL"),
    
    WAREHOUSE("WAREHOUSE"),
    
    TRADE_OFFICE("TRADE_OFFICE");

    private final String value;

    UpgradeTypeEnum(String value) {
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
    public static UpgradeTypeEnum fromValue(String value) {
      for (UpgradeTypeEnum b : UpgradeTypeEnum.values()) {
        if (b.value.equals(value)) {
          return b;
        }
      }
      throw new IllegalArgumentException("Unexpected value '" + value + "'");
    }
  }

  private @Nullable UpgradeTypeEnum upgradeType;

  public UpgradeGuildRequest upgradeType(@Nullable UpgradeTypeEnum upgradeType) {
    this.upgradeType = upgradeType;
    return this;
  }

  /**
   * Get upgradeType
   * @return upgradeType
   */
  
  @Schema(name = "upgrade_type", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("upgrade_type")
  public @Nullable UpgradeTypeEnum getUpgradeType() {
    return upgradeType;
  }

  public void setUpgradeType(@Nullable UpgradeTypeEnum upgradeType) {
    this.upgradeType = upgradeType;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    UpgradeGuildRequest upgradeGuildRequest = (UpgradeGuildRequest) o;
    return Objects.equals(this.upgradeType, upgradeGuildRequest.upgradeType);
  }

  @Override
  public int hashCode() {
    return Objects.hash(upgradeType);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class UpgradeGuildRequest {\n");
    sb.append("    upgradeType: ").append(toIndentedString(upgradeType)).append("\n");
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

