package com.necpgame.socialservice.model;

import java.net.URI;
import java.util.Objects;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;
import com.necpgame.socialservice.model.CreateOrderRequestRequirements;
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
 * CreateOrderRequest
 */


public class CreateOrderRequest {

  private UUID creatorId;

  /**
   * Gets or Sets type
   */
  public enum TypeEnum {
    CRAFTING("CRAFTING"),
    
    GATHERING("GATHERING"),
    
    COMBAT_ASSISTANCE("COMBAT_ASSISTANCE"),
    
    TRANSPORTATION("TRANSPORTATION"),
    
    SERVICE("SERVICE");

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

  private String description;

  private @Nullable CreateOrderRequestRequirements requirements;

  private Integer payment;

  @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME)
  private JsonNullable<OffsetDateTime> deadline = JsonNullable.<OffsetDateTime>undefined();

  private Boolean recurring = false;

  private @Nullable Boolean premium;

  public CreateOrderRequest() {
    super();
  }

  /**
   * Constructor with only required parameters
   */
  public CreateOrderRequest(UUID creatorId, TypeEnum type, String description, Integer payment) {
    this.creatorId = creatorId;
    this.type = type;
    this.description = description;
    this.payment = payment;
  }

  public CreateOrderRequest creatorId(UUID creatorId) {
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

  public CreateOrderRequest type(TypeEnum type) {
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

  public CreateOrderRequest title(@Nullable String title) {
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

  public CreateOrderRequest description(String description) {
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

  public CreateOrderRequest requirements(@Nullable CreateOrderRequestRequirements requirements) {
    this.requirements = requirements;
    return this;
  }

  /**
   * Get requirements
   * @return requirements
   */
  @Valid 
  @Schema(name = "requirements", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("requirements")
  public @Nullable CreateOrderRequestRequirements getRequirements() {
    return requirements;
  }

  public void setRequirements(@Nullable CreateOrderRequestRequirements requirements) {
    this.requirements = requirements;
  }

  public CreateOrderRequest payment(Integer payment) {
    this.payment = payment;
    return this;
  }

  /**
   * Get payment
   * @return payment
   */
  @NotNull 
  @Schema(name = "payment", requiredMode = Schema.RequiredMode.REQUIRED)
  @JsonProperty("payment")
  public Integer getPayment() {
    return payment;
  }

  public void setPayment(Integer payment) {
    this.payment = payment;
  }

  public CreateOrderRequest deadline(OffsetDateTime deadline) {
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

  public CreateOrderRequest recurring(Boolean recurring) {
    this.recurring = recurring;
    return this;
  }

  /**
   * Get recurring
   * @return recurring
   */
  
  @Schema(name = "recurring", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("recurring")
  public Boolean getRecurring() {
    return recurring;
  }

  public void setRecurring(Boolean recurring) {
    this.recurring = recurring;
  }

  public CreateOrderRequest premium(@Nullable Boolean premium) {
    this.premium = premium;
    return this;
  }

  /**
   * Премиум заказ (больше видимость)
   * @return premium
   */
  
  @Schema(name = "premium", description = "Премиум заказ (больше видимость)", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("premium")
  public @Nullable Boolean getPremium() {
    return premium;
  }

  public void setPremium(@Nullable Boolean premium) {
    this.premium = premium;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    CreateOrderRequest createOrderRequest = (CreateOrderRequest) o;
    return Objects.equals(this.creatorId, createOrderRequest.creatorId) &&
        Objects.equals(this.type, createOrderRequest.type) &&
        Objects.equals(this.title, createOrderRequest.title) &&
        Objects.equals(this.description, createOrderRequest.description) &&
        Objects.equals(this.requirements, createOrderRequest.requirements) &&
        Objects.equals(this.payment, createOrderRequest.payment) &&
        equalsNullable(this.deadline, createOrderRequest.deadline) &&
        Objects.equals(this.recurring, createOrderRequest.recurring) &&
        Objects.equals(this.premium, createOrderRequest.premium);
  }

  private static <T> boolean equalsNullable(JsonNullable<T> a, JsonNullable<T> b) {
    return a == b || (a != null && b != null && a.isPresent() && b.isPresent() && Objects.deepEquals(a.get(), b.get()));
  }

  @Override
  public int hashCode() {
    return Objects.hash(creatorId, type, title, description, requirements, payment, hashCodeNullable(deadline), recurring, premium);
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
    sb.append("class CreateOrderRequest {\n");
    sb.append("    creatorId: ").append(toIndentedString(creatorId)).append("\n");
    sb.append("    type: ").append(toIndentedString(type)).append("\n");
    sb.append("    title: ").append(toIndentedString(title)).append("\n");
    sb.append("    description: ").append(toIndentedString(description)).append("\n");
    sb.append("    requirements: ").append(toIndentedString(requirements)).append("\n");
    sb.append("    payment: ").append(toIndentedString(payment)).append("\n");
    sb.append("    deadline: ").append(toIndentedString(deadline)).append("\n");
    sb.append("    recurring: ").append(toIndentedString(recurring)).append("\n");
    sb.append("    premium: ").append(toIndentedString(premium)).append("\n");
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

