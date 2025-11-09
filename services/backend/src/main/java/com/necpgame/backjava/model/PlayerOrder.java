package com.necpgame.backjava.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonTypeName;
import com.necpgame.backjava.entity.enums.PlayerOrderStatus;
import com.necpgame.backjava.entity.enums.PlayerOrderType;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.annotation.Generated;
import jakarta.validation.constraints.NotNull;
import java.time.OffsetDateTime;
import java.util.Objects;
import java.util.UUID;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.lang.Nullable;

@JsonTypeName("PlayerOrder")
@Generated(value = "org.openapitools.codegen.languages.SpringCodegen", comments = "Generator version: 7.17.0")
public class PlayerOrder {

    @Schema(name = "order_id")
    @JsonProperty("order_id")
    private UUID orderId;

    @NotNull
    @Schema(name = "creator_id", requiredMode = Schema.RequiredMode.REQUIRED)
    @JsonProperty("creator_id")
    private UUID creatorId;

    @Schema(name = "creator_name")
    @JsonProperty("creator_name")
    private String creatorName;

    @Schema(name = "type")
    @JsonProperty("type")
    private PlayerOrderType type;

    @Schema(name = "title")
    @JsonProperty("title")
    private String title;

    @Schema(name = "description")
    @JsonProperty("description")
    private String description;

    @NotNull
    @Schema(name = "payment", requiredMode = Schema.RequiredMode.REQUIRED)
    @JsonProperty("payment")
    private Integer payment;

    @Schema(name = "status")
    @JsonProperty("status")
    private PlayerOrderStatus status;

    @Schema(name = "difficulty")
    @JsonProperty("difficulty")
    private String difficulty;

    @Schema(name = "executor_id")
    @JsonProperty("executor_id")
    private UUID executorId;

    @Schema(name = "executor_name")
    @JsonProperty("executor_name")
    private String executorName;

    @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME)
    @Schema(name = "created_at")
    @JsonProperty("created_at")
    private OffsetDateTime createdAt;

    @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME)
    @Schema(name = "deadline")
    @JsonProperty("deadline")
    private OffsetDateTime deadline;

    @Schema(name = "views")
    @JsonProperty("views")
    private Integer views;

    public UUID getOrderId() {
        return orderId;
    }

    public void setOrderId(UUID orderId) {
        this.orderId = orderId;
    }

    public UUID getCreatorId() {
        return creatorId;
    }

    public void setCreatorId(UUID creatorId) {
        this.creatorId = creatorId;
    }

    public String getCreatorName() {
        return creatorName;
    }

    public void setCreatorName(String creatorName) {
        this.creatorName = creatorName;
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

    public Integer getPayment() {
        return payment;
    }

    public void setPayment(Integer payment) {
        this.payment = payment;
    }

    public PlayerOrderStatus getStatus() {
        return status;
    }

    public void setStatus(PlayerOrderStatus status) {
        this.status = status;
    }

    public String getDifficulty() {
        return difficulty;
    }

    public void setDifficulty(String difficulty) {
        this.difficulty = difficulty;
    }

    public UUID getExecutorId() {
        return executorId;
    }

    public void setExecutorId(UUID executorId) {
        this.executorId = executorId;
    }

    public String getExecutorName() {
        return executorName;
    }

    public void setExecutorName(String executorName) {
        this.executorName = executorName;
    }

    public OffsetDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(OffsetDateTime createdAt) {
        this.createdAt = createdAt;
    }

    public OffsetDateTime getDeadline() {
        return deadline;
    }

    public void setDeadline(OffsetDateTime deadline) {
        this.deadline = deadline;
    }

    public Integer getViews() {
        return views;
    }

    public void setViews(Integer views) {
        this.views = views;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        PlayerOrder that = (PlayerOrder) o;
        return Objects.equals(orderId, that.orderId)
            && Objects.equals(creatorId, that.creatorId)
            && Objects.equals(creatorName, that.creatorName)
            && type == that.type
            && Objects.equals(title, that.title)
            && Objects.equals(description, that.description)
            && Objects.equals(payment, that.payment)
            && status == that.status
            && Objects.equals(difficulty, that.difficulty)
            && Objects.equals(executorId, that.executorId)
            && Objects.equals(executorName, that.executorName)
            && Objects.equals(createdAt, that.createdAt)
            && Objects.equals(deadline, that.deadline)
            && Objects.equals(views, that.views);
    }

    @Override
    public int hashCode() {
        return Objects.hash(orderId, creatorId, creatorName, type, title, description, payment, status, difficulty,
            executorId, executorName, createdAt, deadline, views);
    }

    @Override
    public String toString() {
        return "PlayerOrder{" +
            "orderId=" + orderId +
            ", creatorId=" + creatorId +
            ", creatorName='" + creatorName + '\'' +
            ", type=" + type +
            ", title='" + title + '\'' +
            ", description='" + description + '\'' +
            ", payment=" + payment +
            ", status=" + status +
            ", difficulty='" + difficulty + '\'' +
            ", executorId=" + executorId +
            ", executorName='" + executorName + '\'' +
            ", createdAt=" + createdAt +
            ", deadline=" + deadline +
            ", views=" + views +
            '}';
    }
}


