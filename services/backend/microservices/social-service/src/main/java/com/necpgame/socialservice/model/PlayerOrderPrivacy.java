package com.necpgame.socialservice.model;

import java.net.URI;
import java.util.Objects;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
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
 * PlayerOrderPrivacy
 */


public class PlayerOrderPrivacy {

  /**
   * Gets or Sets mode
   */
  public enum ModeEnum {
    PUBLIC("public"),
    
    INVITE_ONLY("invite_only"),
    
    PRIVATE("private");

    private final String value;

    ModeEnum(String value) {
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
    public static ModeEnum fromValue(String value) {
      for (ModeEnum b : ModeEnum.values()) {
        if (b.value.equals(value)) {
          return b;
        }
      }
      throw new IllegalArgumentException("Unexpected value '" + value + "'");
    }
  }

  private ModeEnum mode;

  @Valid
  private List<UUID> invitedPlayerIds = new ArrayList<>();

  @Valid
  private List<UUID> invitedNpcIds = new ArrayList<>();

  private @Nullable Boolean corporateOnly;

  public PlayerOrderPrivacy() {
    super();
  }

  /**
   * Constructor with only required parameters
   */
  public PlayerOrderPrivacy(ModeEnum mode) {
    this.mode = mode;
  }

  public PlayerOrderPrivacy mode(ModeEnum mode) {
    this.mode = mode;
    return this;
  }

  /**
   * Get mode
   * @return mode
   */
  @NotNull 
  @Schema(name = "mode", requiredMode = Schema.RequiredMode.REQUIRED)
  @JsonProperty("mode")
  public ModeEnum getMode() {
    return mode;
  }

  public void setMode(ModeEnum mode) {
    this.mode = mode;
  }

  public PlayerOrderPrivacy invitedPlayerIds(List<UUID> invitedPlayerIds) {
    this.invitedPlayerIds = invitedPlayerIds;
    return this;
  }

  public PlayerOrderPrivacy addInvitedPlayerIdsItem(UUID invitedPlayerIdsItem) {
    if (this.invitedPlayerIds == null) {
      this.invitedPlayerIds = new ArrayList<>();
    }
    this.invitedPlayerIds.add(invitedPlayerIdsItem);
    return this;
  }

  /**
   * Get invitedPlayerIds
   * @return invitedPlayerIds
   */
  @Valid 
  @Schema(name = "invitedPlayerIds", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("invitedPlayerIds")
  public List<UUID> getInvitedPlayerIds() {
    return invitedPlayerIds;
  }

  public void setInvitedPlayerIds(List<UUID> invitedPlayerIds) {
    this.invitedPlayerIds = invitedPlayerIds;
  }

  public PlayerOrderPrivacy invitedNpcIds(List<UUID> invitedNpcIds) {
    this.invitedNpcIds = invitedNpcIds;
    return this;
  }

  public PlayerOrderPrivacy addInvitedNpcIdsItem(UUID invitedNpcIdsItem) {
    if (this.invitedNpcIds == null) {
      this.invitedNpcIds = new ArrayList<>();
    }
    this.invitedNpcIds.add(invitedNpcIdsItem);
    return this;
  }

  /**
   * Get invitedNpcIds
   * @return invitedNpcIds
   */
  @Valid 
  @Schema(name = "invitedNpcIds", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("invitedNpcIds")
  public List<UUID> getInvitedNpcIds() {
    return invitedNpcIds;
  }

  public void setInvitedNpcIds(List<UUID> invitedNpcIds) {
    this.invitedNpcIds = invitedNpcIds;
  }

  public PlayerOrderPrivacy corporateOnly(@Nullable Boolean corporateOnly) {
    this.corporateOnly = corporateOnly;
    return this;
  }

  /**
   * Доступ только для корпоративных исполнителей.
   * @return corporateOnly
   */
  
  @Schema(name = "corporateOnly", description = "Доступ только для корпоративных исполнителей.", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("corporateOnly")
  public @Nullable Boolean getCorporateOnly() {
    return corporateOnly;
  }

  public void setCorporateOnly(@Nullable Boolean corporateOnly) {
    this.corporateOnly = corporateOnly;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    PlayerOrderPrivacy playerOrderPrivacy = (PlayerOrderPrivacy) o;
    return Objects.equals(this.mode, playerOrderPrivacy.mode) &&
        Objects.equals(this.invitedPlayerIds, playerOrderPrivacy.invitedPlayerIds) &&
        Objects.equals(this.invitedNpcIds, playerOrderPrivacy.invitedNpcIds) &&
        Objects.equals(this.corporateOnly, playerOrderPrivacy.corporateOnly);
  }

  @Override
  public int hashCode() {
    return Objects.hash(mode, invitedPlayerIds, invitedNpcIds, corporateOnly);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class PlayerOrderPrivacy {\n");
    sb.append("    mode: ").append(toIndentedString(mode)).append("\n");
    sb.append("    invitedPlayerIds: ").append(toIndentedString(invitedPlayerIds)).append("\n");
    sb.append("    invitedNpcIds: ").append(toIndentedString(invitedNpcIds)).append("\n");
    sb.append("    corporateOnly: ").append(toIndentedString(corporateOnly)).append("\n");
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

