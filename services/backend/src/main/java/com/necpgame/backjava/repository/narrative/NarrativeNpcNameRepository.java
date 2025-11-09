package com.necpgame.backjava.repository.narrative;

import com.necpgame.backjava.entity.narrative.NarrativeNpcNameEntity;
import java.util.List;
import java.util.UUID;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface NarrativeNpcNameRepository extends JpaRepository<NarrativeNpcNameEntity, UUID> {

    @Query("""
        select n from NarrativeNpcNameEntity n
        where (:region is null or lower(n.region) = lower(:region))
          and (:role is null or lower(n.role) = lower(:role))
        order by n.weight desc, n.name asc
        """)
    List<NarrativeNpcNameEntity> findPreferredNames(
        @Param("region") String region,
        @Param("role") String role
    );
}



