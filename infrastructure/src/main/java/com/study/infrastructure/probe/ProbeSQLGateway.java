package com.study.infrastructure.probe;

import com.study.domain.pagination.Pagination;
import com.study.domain.pagination.SearchQuery;
import com.study.domain.planet.PlanetID;
import com.study.domain.probe.Probe;
import com.study.domain.probe.ProbeGateway;
import com.study.domain.probe.ProbeID;
import com.study.infrastructure.probe.persistence.ProbeJpaEntity;
import com.study.infrastructure.probe.persistence.ProbeRepository;
import com.study.infrastructure.utils.SpecificationUtils;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Objects;
import java.util.Optional;

@Component
public class ProbeSQLGateway implements ProbeGateway {
    private final ProbeRepository repository;

    public ProbeSQLGateway(final ProbeRepository repository) {
        this.repository = Objects.requireNonNull(repository);
    }

    @Override
    public Optional<Probe> findBy(final ProbeID id) {
        return this.repository.findById(id.getValue()).map(ProbeJpaEntity::toAggregate);
    }

    @Override
    public Probe create(final Probe probe) {
        return this.save(probe);
    }

    @Override
    public void deleteBy(final ProbeID id) {
        final var probeId = id.getValue();
        if(this.repository.existsById(probeId)) {
            this.repository.deleteById(probeId);
        }
    }

    @Override
    public Probe update(final Probe probe) {
        return this.save(probe);
    }

    @Override
    public Pagination<Probe> findAll(final SearchQuery query) {
        final var page = PageRequest.of(
                query.page(),
                query.perPage(),
                Sort.by(Sort.Direction.fromString(query.direction()), query.sort())
        );

        final var specification = Optional.ofNullable(query.terms())
                .filter(str -> !str.isBlank())
                .map(terms -> SpecificationUtils
                        .<ProbeJpaEntity>like("name", terms))
                .orElse(null);

        final var pageResult = this.repository.findAll(Specification.where(specification), page);

        return new Pagination<>(
                pageResult.getNumber(),
                pageResult.getSize(),
                pageResult.getTotalElements(),
                pageResult.map(ProbeJpaEntity::toAggregate).toList()
        );
    }

    @Override
    public List<Probe> findAllByPlanetId(final PlanetID planetID) {
        return this.repository.findAllByPlanetId(planetID.getValue()).stream().map(ProbeJpaEntity::toAggregate).toList();
    }

    private Probe save(final Probe probe) {
        return this.repository.save(ProbeJpaEntity.from(probe)).toAggregate();
    }
}
