package com.necpgame.backjava.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.Valid;
import jakarta.validation.constraints.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

/**
 * TutorialStepsResponse - С€Р°РіРё С‚СѓС‚РѕСЂРёР°Р»Р°
 */
@Schema(description = "РЁР°РіРё С‚СѓС‚РѕСЂРёР°Р»Р°")
public class TutorialStepsResponse {

    @JsonProperty("steps")
    private List<TutorialStep> steps = new ArrayList<>();

    @JsonProperty("currentStep")
    private Integer currentStep;

    @JsonProperty("totalSteps")
    private Integer totalSteps;

    @JsonProperty("canSkip")
    private Boolean canSkip;

    @Schema(description = "РЎРїРёСЃРѕРє С€Р°РіРѕРІ С‚СѓС‚РѕСЂРёР°Р»Р°", required = true)
    @NotNull
    @Valid
    public List<TutorialStep> getSteps() {
        return steps;
    }

    public void setSteps(List<TutorialStep> steps) {
        this.steps = steps;
    }

    @Schema(description = "РўРµРєСѓС‰РёР№ С€Р°Рі С‚СѓС‚РѕСЂРёР°Р»Р° (0-based РёРЅРґРµРєСЃ)", required = true)
    @NotNull
    @Min(0)
    public Integer getCurrentStep() {
        return currentStep;
    }

    public void setCurrentStep(Integer currentStep) {
        this.currentStep = currentStep;
    }

    @Schema(description = "РћР±С‰РµРµ РєРѕР»РёС‡РµСЃС‚РІРѕ С€Р°РіРѕРІ", required = true)
    @NotNull
    @Min(1)
    public Integer getTotalSteps() {
        return totalSteps;
    }

    public void setTotalSteps(Integer totalSteps) {
        this.totalSteps = totalSteps;
    }

    @Schema(description = "РњРѕР¶РЅРѕ Р»Рё РїСЂРѕРїСѓСЃС‚РёС‚СЊ С‚СѓС‚РѕСЂРёР°Р»", required = true)
    @NotNull
    public Boolean getCanSkip() {
        return canSkip;
    }

    public void setCanSkip(Boolean canSkip) {
        this.canSkip = canSkip;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        TutorialStepsResponse that = (TutorialStepsResponse) o;
        return Objects.equals(steps, that.steps) &&
               Objects.equals(currentStep, that.currentStep) &&
               Objects.equals(totalSteps, that.totalSteps) &&
               Objects.equals(canSkip, that.canSkip);
    }

    @Override
    public int hashCode() {
        return Objects.hash(steps, currentStep, totalSteps, canSkip);
    }

    @Override
    public String toString() {
        return "TutorialStepsResponse{" +
                "steps=" + steps +
                ", currentStep=" + currentStep +
                ", totalSteps=" + totalSteps +
                ", canSkip=" + canSkip +
                '}';
    }
}

