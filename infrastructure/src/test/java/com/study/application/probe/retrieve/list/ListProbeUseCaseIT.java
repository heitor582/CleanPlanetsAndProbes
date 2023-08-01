package com.study.application.probe.retrieve.list;

import com.study.IntegrationTest;
import com.study.domain.pagination.SearchQuery;
import com.study.domain.planet.Planet;
import com.study.domain.probe.Probe;
import com.study.domain.probe.ProbeGateway;
import com.study.infrastructure.planet.persistence.PlanetJpaEntity;
import com.study.infrastructure.planet.persistence.PlanetRepository;
import com.study.infrastructure.probe.persistence.ProbeJpaEntity;
import com.study.infrastructure.probe.persistence.ProbeRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.mock.mockito.SpyBean;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.verify;

class ListProbeUseCaseIT extends IntegrationTest {
    @SpyBean
    private ProbeGateway gateway;
    @Autowired
    private ListProbeUseCase useCase;
    @Autowired
    private ProbeRepository repository;
    @Autowired
    private PlanetRepository planetRepository;

    @Test
    public void givenAValidQuery_whenCallsListProbes_shouldReturnAll() {
        // given
        final var planet = planetRepository.saveAndFlush(PlanetJpaEntity.from(Planet.newPlanet(5,5,"teste"))).toAggregate();
        final var members = List.of(
                Probe.newProbe("teste1",1,1, planet.getId()),
                Probe.newProbe("teste2",1,1, planet.getId())
        );

        final var savedItems = repository.saveAllAndFlush(members.stream().map(ProbeJpaEntity::from).toList());

        final var expectedPage = 0;
        final var expectedPerPage = 10;
        final var expectedTerms = "tes";
        final var expectedSort = "createdAt";
        final var expectedDirection = "asc";
        final var expectedTotal = 2;

        final var expectedItems = savedItems.stream()
                .map(ProbeJpaEntity::toAggregate)
                .map(ProbeListOutput::from)
                .toList();

        final var aQuery =
                new SearchQuery(expectedPage, expectedPerPage, expectedTerms, expectedSort, expectedDirection);

        // when
        final var actualOutput = useCase.execute(aQuery);

        // then
        final var firstExpectedItem = expectedItems.get(0);
        final var firstItem = actualOutput.items().get(0);

        assertEquals(expectedPage, actualOutput.currentPage());
        assertEquals(expectedPerPage, actualOutput.perPage());
        assertEquals(expectedTotal, actualOutput.total());
        assertEquals(expectedItems, actualOutput.items());
        assertEquals(firstExpectedItem.id(), firstItem.id());
        assertEquals(firstExpectedItem.planetId(), firstItem.planetId());
        assertEquals(firstExpectedItem.cordX(), firstItem.cordX());
        assertEquals(firstExpectedItem.cordY(), firstItem.cordY());
        assertEquals(firstExpectedItem.direction(), firstItem.direction());
        assertEquals(firstExpectedItem.createdAt(), firstItem.createdAt());
        assertEquals(firstExpectedItem.updatedAt(), firstItem.updatedAt());
        assertEquals(firstExpectedItem.name(), firstItem.name());

        verify(gateway).findAll(eq(aQuery));
    }

    @Test
    public void givenAValidQuery_whenCallsListPlanetsAndIsEmpty_shouldReturn() {
        // given
        final var expectedPage = 0;
        final var expectedPerPage = 10;
        final var expectedTerms = "";
        final var expectedSort = "createdAt";
        final var expectedDirection = "asc";
        final var expectedTotal = 0;

        final var expectedItems = List.<ProbeListOutput>of();

        final var aQuery =
                new SearchQuery(expectedPage, expectedPerPage, expectedTerms, expectedSort, expectedDirection);

        // when
        final var actualOutput = useCase.execute(aQuery);

        // then
        assertEquals(expectedPage, actualOutput.currentPage());
        assertEquals(expectedPerPage, actualOutput.perPage());
        assertEquals(expectedTotal, actualOutput.total());
        assertEquals(expectedItems, actualOutput.items());

        verify(gateway).findAll(eq(aQuery));
    }
}