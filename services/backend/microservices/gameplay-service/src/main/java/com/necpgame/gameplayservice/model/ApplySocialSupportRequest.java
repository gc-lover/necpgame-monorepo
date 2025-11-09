package com.necpgame.gameplayservice.model;

import java.net.URI;
import java.util.Objects;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import org.openapitools.jackson.nullable.JsonNullable;
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
 * ApplySocialSupportRequest
 */


public class ApplySocialSupportRequest {

  /**
   * Тип социальной поддержки
   */
  public enum SupportTypeEnum {
    FAMILY_SESSION("family_session"),
    
    CLAN_INTERVENTION("clan_intervention"),
    
    MENTORSHIP("mentorship"),
    
    COMMUNITY_CIRCLE("community_circle"),
    
    THERAPY_GROUP("therapy_group");

    private final String value;

    SupportTypeEnum(String value) {
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
    public static SupportTypeEnum fromValue(String value) {
      for (SupportTypeEnum b : SupportTypeEnum.values()) {
        if (b.value.equals(value)) {
          return b;
        }
      }
      throw new IllegalArgumentException("Unexpected value '" + value + "'");
    }
  }

  private SupportTypeEnum supportType;

  /**
   * Роль ведущего социальной программы
   */
  public enum FacilitatorRoleEnum {
    MENTOR("mentor"),
    
    THERAPIST("therapist"),
    
    CLAN_LEADER("clan_leader"),
    
    FIXER("fixer"),
    
    PRIEST("priest");

    private final String value;

    FacilitatorRoleEnum(String value) {
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
    public static FacilitatorRoleEnum fromValue(String value) {
      for (FacilitatorRoleEnum b : FacilitatorRoleEnum.values()) {
        if (b.value.equals(value)) {
          return b;
        }
      }
      throw new IllegalArgumentException("Unexpected value '" + value + "'");
    }
  }

  private FacilitatorRoleEnum facilitatorRole;

  @Valid
  private List<String> participants = new ArrayList<>();

  private @Nullable Integer sessionCount;

  private JsonNullable<@Size(max = 500) String> expectedOutcome = JsonNullable.<String>undefined();

  public ApplySocialSupportRequest() {
    super();
  }

  /**
   * Constructor with only required parameters
   */
  public ApplySocialSupportRequest(SupportTypeEnum supportType, FacilitatorRoleEnum facilitatorRole) {
    this.supportType = supportType;
    this.facilitatorRole = facilitatorRole;
  }

  public ApplySocialSupportRequest supportType(SupportTypeEnum supportType) {
    this.supportType = supportType;
    return this;
  }

  /**
   * Тип социальной поддержки
   * @return supportType
   */
  @NotNull 
  @Schema(name = "support_type", example = "mentorship", description = "Тип социальной поддержки", requiredMode = Schema.RequiredMode.REQUIRED)
  @JsonProperty("support_type")
  public SupportTypeEnum getSupportType() {
    return supportType;
  }

  public void setSupportType(SupportTypeEnum supportType) {
    this.supportType = supportType;
  }

  public ApplySocialSupportRequest facilitatorRole(FacilitatorRoleEnum facilitatorRole) {
    this.facilitatorRole = facilitatorRole;
    return this;
  }

  /**
   * Роль ведущего социальной программы
   * @return facilitatorRole
   */
  @NotNull 
  @Schema(name = "facilitator_role", example = "mentor", description = "Роль ведущего социальной программы", requiredMode = Schema.RequiredMode.REQUIRED)
  @JsonProperty("facilitator_role")
  public FacilitatorRoleEnum getFacilitatorRole() {
    return facilitatorRole;
  }

  public void setFacilitatorRole(FacilitatorRoleEnum facilitatorRole) {
    this.facilitatorRole = facilitatorRole;
  }

  public ApplySocialSupportRequest participants(List<String> participants) {
    this.participants = participants;
    return this;
  }

  public ApplySocialSupportRequest addParticipantsItem(String participantsItem) {
    if (this.participants == null) {
      this.participants = new ArrayList<>();
    }
    this.participants.add(participantsItem);
    return this;
  }

  /**
   * Участники сессии поддержки
   * @return participants
   */
  
  @Schema(name = "participants", example = "[\"npc-mentor-jackie\",\"npc-therapist-river\"]", description = "Участники сессии поддержки", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("participants")
  public List<String> getParticipants() {
    return participants;
  }

  public void setParticipants(List<String> participants) {
    this.participants = participants;
  }

  public ApplySocialSupportRequest sessionCount(@Nullable Integer sessionCount) {
    this.sessionCount = sessionCount;
    return this;
  }

  /**
   * Количество назначенных сессий
   * minimum: 1
   * @return sessionCount
   */
  @Min(value = 1) 
  @Schema(name = "session_count", example = "6", description = "Количество назначенных сессий", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("session_count")
  public @Nullable Integer getSessionCount() {
    return sessionCount;
  }

  public void setSessionCount(@Nullable Integer sessionCount) {
    this.sessionCount = sessionCount;
  }

  public ApplySocialSupportRequest expectedOutcome(String expectedOutcome) {
    this.expectedOutcome = JsonNullable.of(expectedOutcome);
    return this;
  }

  /**
   * Ожидаемый результат программы
   * @return expectedOutcome
   */
  @Size(max = 500) 
  @Schema(name = "expected_outcome", description = "Ожидаемый результат программы", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("expected_outcome")
  public JsonNullable<@Size(max = 500) String> getExpectedOutcome() {
    return expectedOutcome;
  }

  public void setExpectedOutcome(JsonNullable<String> expectedOutcome) {
    this.expectedOutcome = expectedOutcome;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    ApplySocialSupportRequest applySocialSupportRequest = (ApplySocialSupportRequest) o;
    return Objects.equals(this.supportType, applySocialSupportRequest.supportType) &&
        Objects.equals(this.facilitatorRole, applySocialSupportRequest.facilitatorRole) &&
        Objects.equals(this.participants, applySocialSupportRequest.participants) &&
        Objects.equals(this.sessionCount, applySocialSupportRequest.sessionCount) &&
        equalsNullable(this.expectedOutcome, applySocialSupportRequest.expectedOutcome);
  }

  private static <T> boolean equalsNullable(JsonNullable<T> a, JsonNullable<T> b) {
    return a == b || (a != null && b != null && a.isPresent() && b.isPresent() && Objects.deepEquals(a.get(), b.get()));
  }

  @Override
  public int hashCode() {
    return Objects.hash(supportType, facilitatorRole, participants, sessionCount, hashCodeNullable(expectedOutcome));
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
    sb.append("class ApplySocialSupportRequest {\n");
    sb.append("    supportType: ").append(toIndentedString(supportType)).append("\n");
    sb.append("    facilitatorRole: ").append(toIndentedString(facilitatorRole)).append("\n");
    sb.append("    participants: ").append(toIndentedString(participants)).append("\n");
    sb.append("    sessionCount: ").append(toIndentedString(sessionCount)).append("\n");
    sb.append("    expectedOutcome: ").append(toIndentedString(expectedOutcome)).append("\n");
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

