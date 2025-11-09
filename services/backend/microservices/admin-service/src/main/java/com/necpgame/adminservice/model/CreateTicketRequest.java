package com.necpgame.adminservice.model;

import java.net.URI;
import java.util.Objects;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;
import com.necpgame.adminservice.model.AttachmentMetadata;
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
 * CreateTicketRequest
 */


public class CreateTicketRequest {

  /**
   * Gets or Sets source
   */
  public enum SourceEnum {
    PLAYER("PLAYER"),
    
    AGENT("AGENT"),
    
    SYSTEM("SYSTEM");

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

  private SourceEnum source;

  private @Nullable String playerId;

  private @Nullable String accountId;

  private String category;

  private String subject;

  private String description;

  /**
   * Gets or Sets priority
   */
  public enum PriorityEnum {
    LOW("low"),
    
    MEDIUM("medium"),
    
    HIGH("high"),
    
    CRITICAL("critical");

    private final String value;

    PriorityEnum(String value) {
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
    public static PriorityEnum fromValue(String value) {
      for (PriorityEnum b : PriorityEnum.values()) {
        if (b.value.equals(value)) {
          return b;
        }
      }
      throw new IllegalArgumentException("Unexpected value '" + value + "'");
    }
  }

  private PriorityEnum priority = PriorityEnum.MEDIUM;

  private @Nullable String platform;

  private @Nullable String gameVersion;

  @Valid
  private List<@Valid AttachmentMetadata> attachments = new ArrayList<>();

  public CreateTicketRequest() {
    super();
  }

  /**
   * Constructor with only required parameters
   */
  public CreateTicketRequest(SourceEnum source, String category, String subject, String description) {
    this.source = source;
    this.category = category;
    this.subject = subject;
    this.description = description;
  }

  public CreateTicketRequest source(SourceEnum source) {
    this.source = source;
    return this;
  }

  /**
   * Get source
   * @return source
   */
  @NotNull 
  @Schema(name = "source", requiredMode = Schema.RequiredMode.REQUIRED)
  @JsonProperty("source")
  public SourceEnum getSource() {
    return source;
  }

  public void setSource(SourceEnum source) {
    this.source = source;
  }

  public CreateTicketRequest playerId(@Nullable String playerId) {
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

  public CreateTicketRequest accountId(@Nullable String accountId) {
    this.accountId = accountId;
    return this;
  }

  /**
   * Get accountId
   * @return accountId
   */
  
  @Schema(name = "accountId", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("accountId")
  public @Nullable String getAccountId() {
    return accountId;
  }

  public void setAccountId(@Nullable String accountId) {
    this.accountId = accountId;
  }

  public CreateTicketRequest category(String category) {
    this.category = category;
    return this;
  }

  /**
   * Get category
   * @return category
   */
  @NotNull 
  @Schema(name = "category", requiredMode = Schema.RequiredMode.REQUIRED)
  @JsonProperty("category")
  public String getCategory() {
    return category;
  }

  public void setCategory(String category) {
    this.category = category;
  }

  public CreateTicketRequest subject(String subject) {
    this.subject = subject;
    return this;
  }

  /**
   * Get subject
   * @return subject
   */
  @NotNull 
  @Schema(name = "subject", requiredMode = Schema.RequiredMode.REQUIRED)
  @JsonProperty("subject")
  public String getSubject() {
    return subject;
  }

  public void setSubject(String subject) {
    this.subject = subject;
  }

  public CreateTicketRequest description(String description) {
    this.description = description;
    return this;
  }

  /**
   * Get description
   * @return description
   */
  @NotNull 
  @Schema(name = "description", requiredMode = Schema.RequiredMode.REQUIRED)
  @JsonProperty("description")
  public String getDescription() {
    return description;
  }

  public void setDescription(String description) {
    this.description = description;
  }

  public CreateTicketRequest priority(PriorityEnum priority) {
    this.priority = priority;
    return this;
  }

  /**
   * Get priority
   * @return priority
   */
  
  @Schema(name = "priority", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("priority")
  public PriorityEnum getPriority() {
    return priority;
  }

  public void setPriority(PriorityEnum priority) {
    this.priority = priority;
  }

  public CreateTicketRequest platform(@Nullable String platform) {
    this.platform = platform;
    return this;
  }

  /**
   * Get platform
   * @return platform
   */
  
  @Schema(name = "platform", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("platform")
  public @Nullable String getPlatform() {
    return platform;
  }

  public void setPlatform(@Nullable String platform) {
    this.platform = platform;
  }

  public CreateTicketRequest gameVersion(@Nullable String gameVersion) {
    this.gameVersion = gameVersion;
    return this;
  }

  /**
   * Get gameVersion
   * @return gameVersion
   */
  
  @Schema(name = "gameVersion", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("gameVersion")
  public @Nullable String getGameVersion() {
    return gameVersion;
  }

  public void setGameVersion(@Nullable String gameVersion) {
    this.gameVersion = gameVersion;
  }

  public CreateTicketRequest attachments(List<@Valid AttachmentMetadata> attachments) {
    this.attachments = attachments;
    return this;
  }

  public CreateTicketRequest addAttachmentsItem(AttachmentMetadata attachmentsItem) {
    if (this.attachments == null) {
      this.attachments = new ArrayList<>();
    }
    this.attachments.add(attachmentsItem);
    return this;
  }

  /**
   * Get attachments
   * @return attachments
   */
  @Valid 
  @Schema(name = "attachments", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("attachments")
  public List<@Valid AttachmentMetadata> getAttachments() {
    return attachments;
  }

  public void setAttachments(List<@Valid AttachmentMetadata> attachments) {
    this.attachments = attachments;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    CreateTicketRequest createTicketRequest = (CreateTicketRequest) o;
    return Objects.equals(this.source, createTicketRequest.source) &&
        Objects.equals(this.playerId, createTicketRequest.playerId) &&
        Objects.equals(this.accountId, createTicketRequest.accountId) &&
        Objects.equals(this.category, createTicketRequest.category) &&
        Objects.equals(this.subject, createTicketRequest.subject) &&
        Objects.equals(this.description, createTicketRequest.description) &&
        Objects.equals(this.priority, createTicketRequest.priority) &&
        Objects.equals(this.platform, createTicketRequest.platform) &&
        Objects.equals(this.gameVersion, createTicketRequest.gameVersion) &&
        Objects.equals(this.attachments, createTicketRequest.attachments);
  }

  @Override
  public int hashCode() {
    return Objects.hash(source, playerId, accountId, category, subject, description, priority, platform, gameVersion, attachments);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class CreateTicketRequest {\n");
    sb.append("    source: ").append(toIndentedString(source)).append("\n");
    sb.append("    playerId: ").append(toIndentedString(playerId)).append("\n");
    sb.append("    accountId: ").append(toIndentedString(accountId)).append("\n");
    sb.append("    category: ").append(toIndentedString(category)).append("\n");
    sb.append("    subject: ").append(toIndentedString(subject)).append("\n");
    sb.append("    description: ").append(toIndentedString(description)).append("\n");
    sb.append("    priority: ").append(toIndentedString(priority)).append("\n");
    sb.append("    platform: ").append(toIndentedString(platform)).append("\n");
    sb.append("    gameVersion: ").append(toIndentedString(gameVersion)).append("\n");
    sb.append("    attachments: ").append(toIndentedString(attachments)).append("\n");
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

