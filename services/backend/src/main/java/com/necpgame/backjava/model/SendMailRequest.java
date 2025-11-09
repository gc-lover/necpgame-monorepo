package com.necpgame.backjava.model;

import java.net.URI;
import java.util.Objects;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonTypeName;
import com.necpgame.backjava.model.SendMailRequestAttachmentsInner;
import java.math.BigDecimal;
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
 * SendMailRequest
 */

@JsonTypeName("sendMail_request")
@Generated(value = "org.openapitools.codegen.languages.SpringCodegen", comments = "Generator version: 7.17.0")
public class SendMailRequest {

  private String senderCharacterId;

  private String receiverCharacterName;

  private String subject;

  private @Nullable String body;

  @Valid
  private List<@Valid SendMailRequestAttachmentsInner> attachments = new ArrayList<>();

  private @Nullable BigDecimal gold;

  private @Nullable BigDecimal codAmount;

  public SendMailRequest() {
    super();
  }

  /**
   * Constructor with only required parameters
   */
  public SendMailRequest(String senderCharacterId, String receiverCharacterName, String subject) {
    this.senderCharacterId = senderCharacterId;
    this.receiverCharacterName = receiverCharacterName;
    this.subject = subject;
  }

  public SendMailRequest senderCharacterId(String senderCharacterId) {
    this.senderCharacterId = senderCharacterId;
    return this;
  }

  /**
   * Get senderCharacterId
   * @return senderCharacterId
   */
  @NotNull 
  @Schema(name = "sender_character_id", requiredMode = Schema.RequiredMode.REQUIRED)
  @JsonProperty("sender_character_id")
  public String getSenderCharacterId() {
    return senderCharacterId;
  }

  public void setSenderCharacterId(String senderCharacterId) {
    this.senderCharacterId = senderCharacterId;
  }

  public SendMailRequest receiverCharacterName(String receiverCharacterName) {
    this.receiverCharacterName = receiverCharacterName;
    return this;
  }

  /**
   * Get receiverCharacterName
   * @return receiverCharacterName
   */
  @NotNull 
  @Schema(name = "receiver_character_name", requiredMode = Schema.RequiredMode.REQUIRED)
  @JsonProperty("receiver_character_name")
  public String getReceiverCharacterName() {
    return receiverCharacterName;
  }

  public void setReceiverCharacterName(String receiverCharacterName) {
    this.receiverCharacterName = receiverCharacterName;
  }

  public SendMailRequest subject(String subject) {
    this.subject = subject;
    return this;
  }

  /**
   * Get subject
   * @return subject
   */
  @NotNull @Size(max = 100) 
  @Schema(name = "subject", requiredMode = Schema.RequiredMode.REQUIRED)
  @JsonProperty("subject")
  public String getSubject() {
    return subject;
  }

  public void setSubject(String subject) {
    this.subject = subject;
  }

  public SendMailRequest body(@Nullable String body) {
    this.body = body;
    return this;
  }

  /**
   * Get body
   * @return body
   */
  @Size(max = 1000) 
  @Schema(name = "body", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("body")
  public @Nullable String getBody() {
    return body;
  }

  public void setBody(@Nullable String body) {
    this.body = body;
  }

  public SendMailRequest attachments(List<@Valid SendMailRequestAttachmentsInner> attachments) {
    this.attachments = attachments;
    return this;
  }

  public SendMailRequest addAttachmentsItem(SendMailRequestAttachmentsInner attachmentsItem) {
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
  @Valid @Size(max = 10) 
  @Schema(name = "attachments", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("attachments")
  public List<@Valid SendMailRequestAttachmentsInner> getAttachments() {
    return attachments;
  }

  public void setAttachments(List<@Valid SendMailRequestAttachmentsInner> attachments) {
    this.attachments = attachments;
  }

  public SendMailRequest gold(@Nullable BigDecimal gold) {
    this.gold = gold;
    return this;
  }

  /**
   * Get gold
   * minimum: 0
   * @return gold
   */
  @Valid @DecimalMin(value = "0") 
  @Schema(name = "gold", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("gold")
  public @Nullable BigDecimal getGold() {
    return gold;
  }

  public void setGold(@Nullable BigDecimal gold) {
    this.gold = gold;
  }

  public SendMailRequest codAmount(@Nullable BigDecimal codAmount) {
    this.codAmount = codAmount;
    return this;
  }

  /**
   * Cash On Delivery amount
   * minimum: 0
   * @return codAmount
   */
  @Valid @DecimalMin(value = "0") 
  @Schema(name = "cod_amount", description = "Cash On Delivery amount", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("cod_amount")
  public @Nullable BigDecimal getCodAmount() {
    return codAmount;
  }

  public void setCodAmount(@Nullable BigDecimal codAmount) {
    this.codAmount = codAmount;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    SendMailRequest sendMailRequest = (SendMailRequest) o;
    return Objects.equals(this.senderCharacterId, sendMailRequest.senderCharacterId) &&
        Objects.equals(this.receiverCharacterName, sendMailRequest.receiverCharacterName) &&
        Objects.equals(this.subject, sendMailRequest.subject) &&
        Objects.equals(this.body, sendMailRequest.body) &&
        Objects.equals(this.attachments, sendMailRequest.attachments) &&
        Objects.equals(this.gold, sendMailRequest.gold) &&
        Objects.equals(this.codAmount, sendMailRequest.codAmount);
  }

  @Override
  public int hashCode() {
    return Objects.hash(senderCharacterId, receiverCharacterName, subject, body, attachments, gold, codAmount);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class SendMailRequest {\n");
    sb.append("    senderCharacterId: ").append(toIndentedString(senderCharacterId)).append("\n");
    sb.append("    receiverCharacterName: ").append(toIndentedString(receiverCharacterName)).append("\n");
    sb.append("    subject: ").append(toIndentedString(subject)).append("\n");
    sb.append("    body: ").append(toIndentedString(body)).append("\n");
    sb.append("    attachments: ").append(toIndentedString(attachments)).append("\n");
    sb.append("    gold: ").append(toIndentedString(gold)).append("\n");
    sb.append("    codAmount: ").append(toIndentedString(codAmount)).append("\n");
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

