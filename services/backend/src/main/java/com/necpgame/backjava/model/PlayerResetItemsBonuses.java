package com.necpgame.backjava.model;

import java.net.URI;
import java.util.Objects;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonTypeName;
import org.springframework.lang.Nullable;
import org.openapitools.jackson.nullable.JsonNullable;
import java.time.OffsetDateTime;
import jakarta.validation.Valid;
import jakarta.validation.constraints.*;
import io.swagger.v3.oas.annotations.media.Schema;


import java.util.*;
import jakarta.annotation.Generated;

/**
 * PlayerResetItemsBonuses
 */

@JsonTypeName("PlayerResetItems_bonuses")
@Generated(value = "org.openapitools.codegen.languages.SpringCodegen", comments = "Generator version: 7.17.0")
public class PlayerResetItemsBonuses {

  private @Nullable Boolean firstWinClaimed;

  private @Nullable Boolean dailyLoginClaimed;

  public PlayerResetItemsBonuses firstWinClaimed(@Nullable Boolean firstWinClaimed) {
    this.firstWinClaimed = firstWinClaimed;
    return this;
  }

  /**
   * Get firstWinClaimed
   * @return firstWinClaimed
   */
  
  @Schema(name = "first_win_claimed", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("first_win_claimed")
  public @Nullable Boolean getFirstWinClaimed() {
    return firstWinClaimed;
  }

  public void setFirstWinClaimed(@Nullable Boolean firstWinClaimed) {
    this.firstWinClaimed = firstWinClaimed;
  }

  public PlayerResetItemsBonuses dailyLoginClaimed(@Nullable Boolean dailyLoginClaimed) {
    this.dailyLoginClaimed = dailyLoginClaimed;
    return this;
  }

  /**
   * Get dailyLoginClaimed
   * @return dailyLoginClaimed
   */
  
  @Schema(name = "daily_login_claimed", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("daily_login_claimed")
  public @Nullable Boolean getDailyLoginClaimed() {
    return dailyLoginClaimed;
  }

  public void setDailyLoginClaimed(@Nullable Boolean dailyLoginClaimed) {
    this.dailyLoginClaimed = dailyLoginClaimed;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    PlayerResetItemsBonuses playerResetItemsBonuses = (PlayerResetItemsBonuses) o;
    return Objects.equals(this.firstWinClaimed, playerResetItemsBonuses.firstWinClaimed) &&
        Objects.equals(this.dailyLoginClaimed, playerResetItemsBonuses.dailyLoginClaimed);
  }

  @Override
  public int hashCode() {
    return Objects.hash(firstWinClaimed, dailyLoginClaimed);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class PlayerResetItemsBonuses {\n");
    sb.append("    firstWinClaimed: ").append(toIndentedString(firstWinClaimed)).append("\n");
    sb.append("    dailyLoginClaimed: ").append(toIndentedString(dailyLoginClaimed)).append("\n");
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

