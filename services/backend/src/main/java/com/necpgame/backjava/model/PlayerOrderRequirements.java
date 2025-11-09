package com.necpgame.backjava.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonTypeName;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.annotation.Generated;
import jakarta.validation.constraints.Min;
import java.util.List;
import java.util.Map;
import java.util.Objects;

@JsonTypeName("PlayerOrderRequirements")
@Generated(value = "org.openapitools.codegen.languages.SpringCodegen", comments = "Generator version: 7.17.0")
public class PlayerOrderRequirements {

    @Schema(name = "min_level")
    @JsonProperty("min_level")
    @Min(0)
    private Integer minLevel;

    @Schema(name = "required_skills")
    @JsonProperty("required_skills")
    private Map<String, Integer> requiredSkills;

    @Schema(name = "required_equipment")
    @JsonProperty("required_equipment")
    private List<String> requiredEquipment;

    public Integer getMinLevel() {
        return minLevel;
    }

    public void setMinLevel(Integer minLevel) {
        this.minLevel = minLevel;
    }

    public Map<String, Integer> getRequiredSkills() {
        return requiredSkills;
    }

    public void setRequiredSkills(Map<String, Integer> requiredSkills) {
        this.requiredSkills = requiredSkills;
    }

    public List<String> getRequiredEquipment() {
        return requiredEquipment;
    }

    public void setRequiredEquipment(List<String> requiredEquipment) {
        this.requiredEquipment = requiredEquipment;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        PlayerOrderRequirements that = (PlayerOrderRequirements) o;
        return Objects.equals(minLevel, that.minLevel)
            && Objects.equals(requiredSkills, that.requiredSkills)
            && Objects.equals(requiredEquipment, that.requiredEquipment);
    }

    @Override
    public int hashCode() {
        return Objects.hash(minLevel, requiredSkills, requiredEquipment);
    }

    @Override
    public String toString() {
        return "PlayerOrderRequirements{" +
            "minLevel=" + minLevel +
            ", requiredSkills=" + requiredSkills +
            ", requiredEquipment=" + requiredEquipment +
            '}';
    }
}


