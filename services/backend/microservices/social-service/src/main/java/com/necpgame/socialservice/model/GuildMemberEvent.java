package com.necpgame.socialservice.model;

import java.net.URI;
import java.util.Objects;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;
import com.necpgame.socialservice.model.GuildMember;
import org.springframework.lang.Nullable;
import org.openapitools.jackson.nullable.JsonNullable;
import java.time.OffsetDateTime;
import jakarta.validation.Valid;
import jakarta.validation.constraints.*;
import io.swagger.v3.oas.annotations.media.Schema;


import java.util.*;
import jakarta.annotation.Generated;

/**
 * GuildMemberEvent
 */


public class GuildMemberEvent {

  private @Nullable String guildId;

  private @Nullable GuildMember member;

  /**
   * Gets or Sets action
   */
  public enum ActionEnum {
    JOINED("joined"),
    
    LEFT("left"),
    
    RANK_CHANGED("rankChanged");

    private final String value;

    ActionEnum(String value) {
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
    public static ActionEnum fromValue(String value) {
      for (ActionEnum b : ActionEnum.values()) {
        if (b.value.equals(value)) {
          return b;
        }
      }
      throw new IllegalArgumentException("Unexpected value '" + value + "'");
    }
  }

  private @Nullable ActionEnum action;

  public GuildMemberEvent guildId(@Nullable String guildId) {
    this.guildId = guildId;
    return this;
  }

  /**
   * Get guildId
   * @return guildId
   */
  
  @Schema(name = "guildId", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("guildId")
  public @Nullable String getGuildId() {
    return guildId;
  }

  public void setGuildId(@Nullable String guildId) {
    this.guildId = guildId;
  }

  public GuildMemberEvent member(@Nullable GuildMember member) {
    this.member = member;
    return this;
  }

  /**
   * Get member
   * @return member
   */
  @Valid 
  @Schema(name = "member", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("member")
  public @Nullable GuildMember getMember() {
    return member;
  }

  public void setMember(@Nullable GuildMember member) {
    this.member = member;
  }

  public GuildMemberEvent action(@Nullable ActionEnum action) {
    this.action = action;
    return this;
  }

  /**
   * Get action
   * @return action
   */
  
  @Schema(name = "action", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("action")
  public @Nullable ActionEnum getAction() {
    return action;
  }

  public void setAction(@Nullable ActionEnum action) {
    this.action = action;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    GuildMemberEvent guildMemberEvent = (GuildMemberEvent) o;
    return Objects.equals(this.guildId, guildMemberEvent.guildId) &&
        Objects.equals(this.member, guildMemberEvent.member) &&
        Objects.equals(this.action, guildMemberEvent.action);
  }

  @Override
  public int hashCode() {
    return Objects.hash(guildId, member, action);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class GuildMemberEvent {\n");
    sb.append("    guildId: ").append(toIndentedString(guildId)).append("\n");
    sb.append("    member: ").append(toIndentedString(member)).append("\n");
    sb.append("    action: ").append(toIndentedString(action)).append("\n");
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

