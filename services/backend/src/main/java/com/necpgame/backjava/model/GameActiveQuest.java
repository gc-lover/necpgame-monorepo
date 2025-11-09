package com.necpgame.backjava.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.*;

import java.util.Objects;

/**
 * GameActiveQuest - Р°РєС‚РёРІРЅС‹Р№ РєРІРµСЃС‚
 */
@Schema(description = "РђРєС‚РёРІРЅС‹Р№ РєРІРµСЃС‚")
public class GameActiveQuest {

    @JsonProperty("questId")
    private String questId;

    @JsonProperty("progress")
    private Integer progress;

    @Schema(description = "ID РєРІРµСЃС‚Р°")
    public String getQuestId() {
        return questId;
    }

    public void setQuestId(String questId) {
        this.questId = questId;
    }

    @Schema(description = "РџСЂРѕРіСЂРµСЃСЃ РєРІРµСЃС‚Р° РІ РїСЂРѕС†РµРЅС‚Р°С…")
    @Min(0)
    @Max(100)
    public Integer getProgress() {
        return progress;
    }

    public void setProgress(Integer progress) {
        this.progress = progress;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        GameActiveQuest that = (GameActiveQuest) o;
        return Objects.equals(questId, that.questId) &&
               Objects.equals(progress, that.progress);
    }

    @Override
    public int hashCode() {
        return Objects.hash(questId, progress);
    }

    @Override
    public String toString() {
        return "GameActiveQuest{" +
                "questId='" + questId + '\'' +
                ", progress=" + progress +
                '}';
    }
}

