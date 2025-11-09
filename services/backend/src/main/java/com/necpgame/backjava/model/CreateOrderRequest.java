package com.necpgame.backjava.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonTypeName;
import com.necpgame.backjava.entity.enums.PlayerOrderType;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.annotation.Generated;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import java.time.OffsetDateTime;
import java.util.Objects;
import java.util.UUID;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.lang.Nullable;

@JsonTypeName("CreateOrderRequest")
@Generated(value = "org.openapitools.codegen.languages.SpringCodegen", comments = "Generator version: 7.17.0")
public class CreateOrderRequest {

    @NotNull
    @Schema(name = "creator_id", requiredMode = Schema.RequiredMode.REQUIRED)
    @JsonProperty("creator_id")
    private UUID creatorId;

    @NotNull
    @Schema(name = "type", requiredMode = Schema.RequiredMode.REQUIRED)
    @JsonProperty("type")
    private PlayerOrderType type;

    @Schema(name = "title")
    @JsonProperty("title")
    private String title;

    @NotNull
    @Schema(name = "description", requiredMode = Schema.RequiredMode.REQUIRED)
    @JsonProperty("description")
    private String description;

    @Valid
    @Schema(name = "requirements")
    @JsonProperty("requirements")
    private PlayerOrderRequirements requirements;

    @NotNull
    @Schema(name = "payment", requiredMode = Schema.RequiredMode.REQUIRED)
    @JsonProperty("payment")
    private Integer payment;

    @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME)
    @Schema(name = "deadline")
    @JsonProperty("deadline")
    private OffsetDateTime deadline;

    @Schema(name = "recurring", defaultValue = "false")
    @JsonProperty("recurring")
    private Boolean recurring = Boolean.FALSE;

    @Schema(name = "premium", description = "Премиум заказ (больше видимость)")
    @JsonProperty("premium")
    private Boolean premium = Boolean.FALSE;

    public UUID getCreatorId() {
        return creatorId;
    }

    public void setCreatorId(UUID creatorId) {
        this.creatorId = creatorId;
    }

    public PlayerOrderType getType() {
        return type;
    }

    public void setType(PlayerOrderType type) {
        this.type = type;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public PlayerOrderRequirements getRequirements() {
        return requirements;
    }

    public void setRequirements(PlayerOrderRequirements requirements) {
        this.requirements = requirements;
    }

    public Integer getPayment() {
        return payment;
    }

    public void setPayment(Integer payment) {
        this.payment = payment;
    }

    public OffsetDateTime getDeadline() {
        return deadline;
    }

    public void setDeadline(OffsetDateTime deadline) {
        this.deadline = deadline;
    }

    public Boolean getRecurring() {
        return recurring;
    }

    public void setRecurring(Boolean recurring) {
        this.recurring = recurring;
    }

    public Boolean getPremium() {
        return premium;
    }

    public void setPremium(Boolean premium) {
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
        CreateOrderRequest that = (CreateOrderRequest) o;
        return Objects.equals(creatorId, that.creatorId)
            && type == that.type
            && Objects.equals(title, that.title)
            && Objects.equals(description, that.description)
            && Objects.equals(requirements, that.requirements)
            && Objects.equals(payment, that.payment)
            && Objects.equals(deadline, that.deadline)
            && Objects.equals(recurring, that.recurring)
            && Objects.equals(premium, that.premium);
    }

    @Override
    public int hashCode() {
        return Objects.hash(creatorId, type, title, description, requirements, payment, deadline, recurring, premium);
    }

    @Override
    public String toString() {
        return "CreateOrderRequest{" +
            "creatorId=" + creatorId +
            ", type=" + type +
            ", title='" + title + '\'' +
            ", description='" + description + '\'' +
            ", requirements=" + requirements +
            ", payment=" + payment +
            ", deadline=" + deadline +
            ", recurring=" + recurring +
            ", premium=" + premium +
            '}';
    }
}


