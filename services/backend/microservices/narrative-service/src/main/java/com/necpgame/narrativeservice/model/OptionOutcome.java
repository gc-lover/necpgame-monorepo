package com.necpgame.narrativeservice.model;

import java.net.URI;
import java.util.Objects;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.necpgame.narrativeservice.model.Cost;
import com.necpgame.narrativeservice.model.Debuff;
import com.necpgame.narrativeservice.model.Grant;
import com.necpgame.narrativeservice.model.Reward;
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
 * OptionOutcome
 */


public class OptionOutcome {

  @Valid
  private List<String> setFlags = new ArrayList<>();

  @Valid
  private List<String> clearFlags = new ArrayList<>();

  @Valid
  private List<@Valid Reward> rewards = new ArrayList<>();

  @Valid
  private List<@Valid Grant> grants = new ArrayList<>();

  @Valid
  private List<@Valid Cost> costs = new ArrayList<>();

  @Valid
  private List<@Valid Debuff> debuffs = new ArrayList<>();

  @Valid
  private List<String> triggers = new ArrayList<>();

  private @Nullable String narrativeLog;

  public OptionOutcome setFlags(List<String> setFlags) {
    this.setFlags = setFlags;
    return this;
  }

  public OptionOutcome addSetFlagsItem(String setFlagsItem) {
    if (this.setFlags == null) {
      this.setFlags = new ArrayList<>();
    }
    this.setFlags.add(setFlagsItem);
    return this;
  }

  /**
   * Get setFlags
   * @return setFlags
   */
  
  @Schema(name = "setFlags", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("setFlags")
  public List<String> getSetFlags() {
    return setFlags;
  }

  public void setSetFlags(List<String> setFlags) {
    this.setFlags = setFlags;
  }

  public OptionOutcome clearFlags(List<String> clearFlags) {
    this.clearFlags = clearFlags;
    return this;
  }

  public OptionOutcome addClearFlagsItem(String clearFlagsItem) {
    if (this.clearFlags == null) {
      this.clearFlags = new ArrayList<>();
    }
    this.clearFlags.add(clearFlagsItem);
    return this;
  }

  /**
   * Get clearFlags
   * @return clearFlags
   */
  
  @Schema(name = "clearFlags", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("clearFlags")
  public List<String> getClearFlags() {
    return clearFlags;
  }

  public void setClearFlags(List<String> clearFlags) {
    this.clearFlags = clearFlags;
  }

  public OptionOutcome rewards(List<@Valid Reward> rewards) {
    this.rewards = rewards;
    return this;
  }

  public OptionOutcome addRewardsItem(Reward rewardsItem) {
    if (this.rewards == null) {
      this.rewards = new ArrayList<>();
    }
    this.rewards.add(rewardsItem);
    return this;
  }

  /**
   * Get rewards
   * @return rewards
   */
  @Valid 
  @Schema(name = "rewards", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("rewards")
  public List<@Valid Reward> getRewards() {
    return rewards;
  }

  public void setRewards(List<@Valid Reward> rewards) {
    this.rewards = rewards;
  }

  public OptionOutcome grants(List<@Valid Grant> grants) {
    this.grants = grants;
    return this;
  }

  public OptionOutcome addGrantsItem(Grant grantsItem) {
    if (this.grants == null) {
      this.grants = new ArrayList<>();
    }
    this.grants.add(grantsItem);
    return this;
  }

  /**
   * Get grants
   * @return grants
   */
  @Valid 
  @Schema(name = "grants", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("grants")
  public List<@Valid Grant> getGrants() {
    return grants;
  }

  public void setGrants(List<@Valid Grant> grants) {
    this.grants = grants;
  }

  public OptionOutcome costs(List<@Valid Cost> costs) {
    this.costs = costs;
    return this;
  }

  public OptionOutcome addCostsItem(Cost costsItem) {
    if (this.costs == null) {
      this.costs = new ArrayList<>();
    }
    this.costs.add(costsItem);
    return this;
  }

  /**
   * Get costs
   * @return costs
   */
  @Valid 
  @Schema(name = "costs", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("costs")
  public List<@Valid Cost> getCosts() {
    return costs;
  }

  public void setCosts(List<@Valid Cost> costs) {
    this.costs = costs;
  }

  public OptionOutcome debuffs(List<@Valid Debuff> debuffs) {
    this.debuffs = debuffs;
    return this;
  }

  public OptionOutcome addDebuffsItem(Debuff debuffsItem) {
    if (this.debuffs == null) {
      this.debuffs = new ArrayList<>();
    }
    this.debuffs.add(debuffsItem);
    return this;
  }

  /**
   * Get debuffs
   * @return debuffs
   */
  @Valid 
  @Schema(name = "debuffs", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("debuffs")
  public List<@Valid Debuff> getDebuffs() {
    return debuffs;
  }

  public void setDebuffs(List<@Valid Debuff> debuffs) {
    this.debuffs = debuffs;
  }

  public OptionOutcome triggers(List<String> triggers) {
    this.triggers = triggers;
    return this;
  }

  public OptionOutcome addTriggersItem(String triggersItem) {
    if (this.triggers == null) {
      this.triggers = new ArrayList<>();
    }
    this.triggers.add(triggersItem);
    return this;
  }

  /**
   * Get triggers
   * @return triggers
   */
  
  @Schema(name = "triggers", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("triggers")
  public List<String> getTriggers() {
    return triggers;
  }

  public void setTriggers(List<String> triggers) {
    this.triggers = triggers;
  }

  public OptionOutcome narrativeLog(@Nullable String narrativeLog) {
    this.narrativeLog = narrativeLog;
    return this;
  }

  /**
   * Get narrativeLog
   * @return narrativeLog
   */
  
  @Schema(name = "narrativeLog", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("narrativeLog")
  public @Nullable String getNarrativeLog() {
    return narrativeLog;
  }

  public void setNarrativeLog(@Nullable String narrativeLog) {
    this.narrativeLog = narrativeLog;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    OptionOutcome optionOutcome = (OptionOutcome) o;
    return Objects.equals(this.setFlags, optionOutcome.setFlags) &&
        Objects.equals(this.clearFlags, optionOutcome.clearFlags) &&
        Objects.equals(this.rewards, optionOutcome.rewards) &&
        Objects.equals(this.grants, optionOutcome.grants) &&
        Objects.equals(this.costs, optionOutcome.costs) &&
        Objects.equals(this.debuffs, optionOutcome.debuffs) &&
        Objects.equals(this.triggers, optionOutcome.triggers) &&
        Objects.equals(this.narrativeLog, optionOutcome.narrativeLog);
  }

  @Override
  public int hashCode() {
    return Objects.hash(setFlags, clearFlags, rewards, grants, costs, debuffs, triggers, narrativeLog);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class OptionOutcome {\n");
    sb.append("    setFlags: ").append(toIndentedString(setFlags)).append("\n");
    sb.append("    clearFlags: ").append(toIndentedString(clearFlags)).append("\n");
    sb.append("    rewards: ").append(toIndentedString(rewards)).append("\n");
    sb.append("    grants: ").append(toIndentedString(grants)).append("\n");
    sb.append("    costs: ").append(toIndentedString(costs)).append("\n");
    sb.append("    debuffs: ").append(toIndentedString(debuffs)).append("\n");
    sb.append("    triggers: ").append(toIndentedString(triggers)).append("\n");
    sb.append("    narrativeLog: ").append(toIndentedString(narrativeLog)).append("\n");
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

