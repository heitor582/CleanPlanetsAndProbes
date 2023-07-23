package com.study.infrastructure.planet;

import com.study.domain.pagination.Pagination;
import com.study.domain.pagination.SearchQuery;
import com.study.domain.planet.Planet;
import com.study.domain.planet.PlanetGateway;
import com.study.domain.planet.PlanetID;
import com.study.infrastructure.planet.persistence.PlanetJpaEntity;
import com.study.infrastructure.planet.persistence.PlanetRepository;
import com.study.infrastructure.utils.SpecificationUtils;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Component;

import java.util.Objects;
import java.util.Optional;

@Component
public class PlanetSQLGateway implements PlanetGateway {
    private final PlanetRepository repository;

    public PlanetSQLGateway(final PlanetRepository repository) {
        this.repository = Objects.requireNonNull(repository);
    }

    @Override
    public Optional<Planet> findBy(final PlanetID id) {
        return this.repository.findById(id.getValue()).map(PlanetJpaEntity::toAggregate);
    }

    @Override
    public void deleteBy(final PlanetID id) {
        final Long planetId = id.getValue();
        if(this.repository.existsById(planetId)){
            this.repository.deleteById(planetId);
        }
    }

    @Override
    public Planet create(final Planet planet) {
        return this.save(planet);
    }

    @Override
    public Planet update(final Planet planet) {
        return this.save(planet);
    }

    @Override
    public Pagination<Planet> findAll(final SearchQuery query) {
        final var page = PageRequest.of(
                query.page(),
                query.perPage(),
                Sort.by(Sort.Direction.fromString(query.direction()), query.sort())
        );

        final var specification = Optional.ofNullable(query.terms())
                .filter(str -> !str.isBlank())
                .map(terms -> SpecificationUtils
                        .<PlanetJpaEntity>like("name", terms))
                .orElse(null);

        final var pageResult = this.repository.findAll(Specification.where(specification), page);

        return new Pagination<>(
                pageResult.getNumber(),
                pageResult.getSize(),
                pageResult.getTotalElements(),
                pageResult.map(PlanetJpaEntity::toAggregate).toList()
        );
    }

    private Planet save(final Planet planet){
        return this.repository.save(PlanetJpaEntity.from(planet)).toAggregate();
    }
}
