package com.necpgame.economyservice.model;

import java.net.URI;
import java.util.Objects;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;
import com.necpgame.economyservice.model.ContractTerms;
import java.time.OffsetDateTime;
import java.util.Arrays;
import java.util.UUID;
import org.openapitools.jackson.nullable.JsonNullable;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.lang.Nullable;
import java.util.NoSuchElementException;
import org.openapitools.jackson.nullable.JsonNullable;
import java.time.OffsetDateTime;
import jakarta.validation.Valid;
import jakarta.validation.constraints.*;
import io.swagger.v3.oas.annotations.media.Schema;


import java.util.*;
import jakarta.annotation.Generated;

/**
 * CreateContractRequest
 */


public class CreateContractRequest {

  private UUID creatorId;

  /**
   * Gets or Sets type
   */
  public enum TypeEnum {
    EXCHANGE("EXCHANGE"),
    
    SERVICE("SERVICE"),
    
    COURIER("COURIER"),
    
    AUCTION("AUCTION");

    private final String value;

    TypeEnum(String value) {
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
    public static TypeEnum fromValue(String value) {
      for (TypeEnum b : TypeEnum.values()) {
        if (b.value.equals(value)) {
          return b;
        }
      }
      throw new IllegalArgumentException("Unexpected value '" + value + "'");
    }
  }

  private TypeEnum type;

  private @Nullable String title;

  private @Nullable String description;

  private ContractTerms terms;

  private @Nullable Integer payment;

  private JsonNullable<Integer> collateralRequired = JsonNullable.<Integer>undefined();

  @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME)
  private JsonNullable<OffsetDateTime> deadline = JsonNullable.<OffsetDateTime>undefined();

  private Boolean autoExecute = true;

  public CreateContractRequest() {
    super();
  }

  /**
   * Constructor with only required parameters
   */
  public CreateContractRequest(UUID creatorId, TypeEnum type, ContractTerms terms) {
    this.creatorId = creatorId;
    this.type = type;
    this.terms = terms;
  }

  public CreateContractRequest creatorId(UUID creatorId) {
    this.creatorId = creatorId;
    return this;
  }

  /**
   * Get creatorId
   * @return creatorId
   */
  @NotNull @Valid 
  @Schema(name = "creator_id", requiredMode = Schema.RequiredMode.REQUIRED)
  @JsonProperty("creator_id")
  public UUID getCreatorId() {
    return creatorId;
  }

  public void setCreatorId(UUID creatorId) {
    this.creatorId = creatorId;
  }

  public CreateContractRequest type(TypeEnum type) {
    this.type = type;
    return this;
  }

  /**
   * Get type
   * @return type
   */
  @NotNull 
  @Schema(name = "type", requiredMode = Schema.RequiredMode.REQUIRED)
  @JsonProperty("type")
  public TypeEnum getType() {
    return type;
  }

  public void setType(TypeEnum type) {
    this.type = type;
  }

  public CreateContractRequest title(@Nullable String title) {
    this.title = title;
    return this;
  }

  /**
   * Get title
   * @return title
   */
  
  @Schema(name = "title", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("title")
  public @Nullable String getTitle() {
    return title;
  }

  public void setTitle(@Nullable String title) {
    this.title = title;
  }

  public CreateContractRequest description(@Nullable String description) {
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

  public CreateContractRequest terms(ContractTerms terms) {
    this.terms = terms;
    return this;
  }

  /**
   * Get terms
   * @return terms
   */
  @NotNull @Valid 
  @Schema(name = "terms", requiredMode = Schema.RequiredMode.REQUIRED)
  @JsonProperty("terms")
  public ContractTerms getTerms() {
    return terms;
  }

  public void setTerms(ContractTerms terms) {
    this.terms = terms;
  }

  public CreateContractRequest payment(@Nullable Integer payment) {
    this.payment = payment;
    return this;
  }

  /**
   * Get payment
   * @return payment
   */
  
  @Schema(name = "payment", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("payment")
  public @Nullable Integer getPayment() {
    return payment;
  }

  public void setPayment(@Nullable Integer payment) {
    this.payment = payment;
  }

  public CreateContractRequest collateralRequired(Integer collateralRequired) {
    this.collateralRequired = JsonNullable.of(collateralRequired);
    return this;
  }

  /**
   * Get collateralRequired
   * @return collateralRequired
   */
  
  @Schema(name = "collateral_required", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("collateral_required")
  public JsonNullable<Integer> getCollateralRequired() {
    return collateralRequired;
  }

  public void setCollateralRequired(JsonNullable<Integer> collateralRequired) {
    this.collateralRequired = collateralRequired;
  }

  public CreateContractRequest deadline(OffsetDateTime deadline) {
    this.deadline = JsonNullable.of(deadline);
    return this;
  }

  /**
   * Get deadline
   * @return deadline
   */
  @Valid 
  @Schema(name = "deadline", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("deadline")
  public JsonNullable<OffsetDateTime> getDeadline() {
    return deadline;
  }

  public void setDeadline(JsonNullable<OffsetDateTime> deadline) {
    this.deadline = deadline;
  }

  public CreateContractRequest autoExecute(Boolean autoExecute) {
    this.autoExecute = autoExecute;
    return this;
  }

  /**
   * Get autoExecute
   * @return autoExecute
   */
  
  @Schema(name = "auto_execute", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("auto_execute")
  public Boolean getAutoExecute() {
    return autoExecute;
  }

  public void setAutoExecute(Boolean autoExecute) {
    this.autoExecute = autoExecute;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    CreateContractRequest createContractRequest = (CreateContractRequest) o;
    return Objects.equals(this.creatorId, createContractRequest.creatorId) &&
        Objects.equals(this.type, createContractRequest.type) &&
        Objects.equals(this.title, createContractRequest.title) &&
        Objects.equals(this.description, createContractRequest.description) &&
        Objects.equals(this.terms, createContractRequest.terms) &&
        Objects.equals(this.payment, createContractRequest.payment) &&
        equalsNullable(this.collateralRequired, createContractRequest.collateralRequired) &&
        equalsNullable(this.deadline, createContractRequest.deadline) &&
        Objects.equals(this.autoExecute, createContractRequest.autoExecute);
  }

  private static <T> boolean equalsNullable(JsonNullable<T> a, JsonNullable<T> b) {
    return a == b || (a != null && b != null && a.isPresent() && b.isPresent() && Objects.deepEquals(a.get(), b.get()));
  }

  @Override
  public int hashCode() {
    return Objects.hash(creatorId, type, title, description, terms, payment, hashCodeNullable(collateralRequired), hashCodeNullable(deadline), autoExecute);
  }

  private static <T> int hashCodeNullable(JsonNullable<T> a) {
    if (a == null) {
      return 1;
    }
    return a.isPresent() ? Arrays.deepHashCode(new Object[]{a.get()}) : 31;
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class CreateContractRequest {\n");
    sb.append("    creatorId: ").append(toIndentedString(creatorId)).append("\n");
    sb.append("    type: ").append(toIndentedString(type)).append("\n");
    sb.append("    title: ").append(toIndentedString(title)).append("\n");
    sb.append("    description: ").append(toIndentedString(description)).append("\n");
    sb.append("    terms: ").append(toIndentedString(terms)).append("\n");
    sb.append("    payment: ").append(toIndentedString(payment)).append("\n");
    sb.append("    collateralRequired: ").append(toIndentedString(collateralRequired)).append("\n");
    sb.append("    deadline: ").append(toIndentedString(deadline)).append("\n");
    sb.append("    autoExecute: ").append(toIndentedString(autoExecute)).append("\n");
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

