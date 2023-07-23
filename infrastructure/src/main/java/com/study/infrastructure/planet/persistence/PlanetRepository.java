package com.study.infrastructure.planet.persistence;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Repository;
import org.springframework.data.jpa.repository.JpaRepository;

@Repository
public interface PlanetRepository extends JpaRepository<PlanetJpaEntity,Long> {
    Page<PlanetJpaEntity> findAll(Specification<PlanetJpaEntity> whereClause, Pageable page);
}
