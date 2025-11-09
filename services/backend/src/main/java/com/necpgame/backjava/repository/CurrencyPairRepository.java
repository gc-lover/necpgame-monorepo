package com.necpgame.backjava.repository;

import com.necpgame.backjava.entity.CurrencyPairEntity;
import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CurrencyPairRepository extends JpaRepository<CurrencyPairEntity, String> {

    List<CurrencyPairEntity> findAllByOrderByPairAsc();

    List<CurrencyPairEntity> findAllByPairTypeOrderByPairAsc(CurrencyPairEntity.PairType pairType);

    Optional<CurrencyPairEntity> findByPair(String pair);
}


