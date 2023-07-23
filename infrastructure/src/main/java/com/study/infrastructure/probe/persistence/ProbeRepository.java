package com.study.infrastructure.probe.persistence;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ProbeRepository extends JpaRepository<ProbeJpaEntity, Long> {
    Page<ProbeJpaEntity> findAll(Specification<ProbeJpaEntity> whereClause, Pageable page);
    List<ProbeJpaEntity> findAllByPlanetId(Long planetId);
}
