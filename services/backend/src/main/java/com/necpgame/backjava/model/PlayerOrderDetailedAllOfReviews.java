package com.necpgame.backjava.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonTypeName;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.annotation.Generated;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import java.time.OffsetDateTime;
import java.util.Objects;
import java.util.UUID;
import org.springframework.format.annotation.DateTimeFormat;

@JsonTypeName("PlayerOrderDetailed_allOf_reviews")
@Generated(value = "org.openapitools.codegen.languages.SpringCodegen", comments = "Generator version: 7.17.0")
public class PlayerOrderDetailedAllOfReviews {

    @Schema(name = "reviewer_id")
    @JsonProperty("reviewer_id")
    private UUID reviewerId;

    @Min(1)
    @Max(5)
    @Schema(name = "rating")
    @JsonProperty("rating")
    private Integer rating;

    @Schema(name = "comment")
    @JsonProperty("comment")
    private String comment;

    @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME)
    @Schema(name = "created_at")
    @JsonProperty("created_at")
    private OffsetDateTime createdAt;

    public UUID getReviewerId() {
        return reviewerId;
    }

    public void setReviewerId(UUID reviewerId) {
        this.reviewerId = reviewerId;
    }

    public Integer getRating() {
        return rating;
    }

    public void setRating(Integer rating) {
        this.rating = rating;
    }

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }

    public OffsetDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(OffsetDateTime createdAt) {
        this.createdAt = createdAt;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        PlayerOrderDetailedAllOfReviews that = (PlayerOrderDetailedAllOfReviews) o;
        return Objects.equals(reviewerId, that.reviewerId)
            && Objects.equals(rating, that.rating)
            && Objects.equals(comment, that.comment)
            && Objects.equals(createdAt, that.createdAt);
    }

    @Override
    public int hashCode() {
        return Objects.hash(reviewerId, rating, comment, createdAt);
    }

    @Override
    public String toString() {
        return "PlayerOrderDetailedAllOfReviews{" +
            "reviewerId=" + reviewerId +
            ", rating=" + rating +
            ", comment='" + comment + '\'' +
            ", createdAt=" + createdAt +
            '}';
    }
}


