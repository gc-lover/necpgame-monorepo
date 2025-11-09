package com.necpgame.backjava.entity;

import jakarta.persistence.EmbeddedId;
import jakarta.persistence.Entity;
import jakarta.persistence.Index;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;

import java.time.OffsetDateTime;

@Entity
@Table(name = "guild_members", indexes = {
    @Index(name = "idx_guild_members_guild", columnList = "guild_id"),
    @Index(name = "idx_guild_members_character", columnList = "character_id", unique = true)
})
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class GuildMemberEntity {

    @EmbeddedId
    private GuildMemberId id;

    @CreationTimestamp
    private OffsetDateTime joinedAt;

    private String rank;
}

