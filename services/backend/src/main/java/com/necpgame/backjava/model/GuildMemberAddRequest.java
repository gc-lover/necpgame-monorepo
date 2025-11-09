package com.necpgame.backjava.model;

import java.net.URI;
import java.util.Objects;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonCreator;
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
 * GuildMemberAddRequest
 */

@Generated(value = "org.openapitools.codegen.languages.SpringCodegen", comments = "Generator version: 7.17.0")
public class GuildMemberAddRequest {

  private @Nullable String playerId;

  private @Nullable String inviteNote;

  /**
   * Gets or Sets source
   */
  public enum SourceEnum {
    INVITE("invite"),
    
    APPLICATION("application");

    private final String value;

    SourceEnum(String value) {
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
    public static SourceEnum fromValue(String value) {
      for (SourceEnum b : SourceEnum.values()) {
        if (b.value.equals(value)) {
          return b;
        }
      }
      throw new IllegalArgumentException("Unexpected value '" + value + "'");
    }
  }

  private @Nullable SourceEnum source;

  public GuildMemberAddRequest playerId(@Nullable String playerId) {
    this.playerId = playerId;
    return this;
  }

  /**
   * Get playerId
   * @return playerId
   */
  
  @Schema(name = "playerId", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("playerId")
  public @Nullable String getPlayerId() {
    return playerId;
  }

  public void setPlayerId(@Nullable String playerId) {
    this.playerId = playerId;
  }

  public GuildMemberAddRequest inviteNote(@Nullable String inviteNote) {
    this.inviteNote = inviteNote;
    return this;
  }

  /**
   * Get inviteNote
   * @return inviteNote
   */
  
  @Schema(name = "inviteNote", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("inviteNote")
  public @Nullable String getInviteNote() {
    return inviteNote;
  }

  public void setInviteNote(@Nullable String inviteNote) {
    this.inviteNote = inviteNote;
  }

  public GuildMemberAddRequest source(@Nullable SourceEnum source) {
    this.source = source;
    return this;
  }

  /**
   * Get source
   * @return source
   */
  
  @Schema(name = "source", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("source")
  public @Nullable SourceEnum getSource() {
    return source;
  }

  public void setSource(@Nullable SourceEnum source) {
    this.source = source;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    GuildMemberAddRequest guildMemberAddRequest = (GuildMemberAddRequest) o;
    return Objects.equals(this.playerId, guildMemberAddRequest.playerId) &&
        Objects.equals(this.inviteNote, guildMemberAddRequest.inviteNote) &&
        Objects.equals(this.source, guildMemberAddRequest.source);
  }

  @Override
  public int hashCode() {
    return Objects.hash(playerId, inviteNote, source);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class GuildMemberAddRequest {\n");
    sb.append("    playerId: ").append(toIndentedString(playerId)).append("\n");
    sb.append("    inviteNote: ").append(toIndentedString(inviteNote)).append("\n");
    sb.append("    source: ").append(toIndentedString(source)).append("\n");
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

