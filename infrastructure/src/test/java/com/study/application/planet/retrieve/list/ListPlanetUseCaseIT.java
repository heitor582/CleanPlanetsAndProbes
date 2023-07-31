package com.study.application.planet.retrieve.list;

import com.study.IntegrationTest;
import com.study.domain.pagination.Pagination;
import com.study.domain.pagination.SearchQuery;
import com.study.domain.planet.Planet;
import com.study.domain.planet.PlanetGateway;
import com.study.infrastructure.planet.persistence.PlanetJpaEntity;
import com.study.infrastructure.planet.persistence.PlanetRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.mock.mockito.SpyBean;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.verify;

@IntegrationTest
class ListPlanetUseCaseIT {
    @SpyBean
    private PlanetGateway gateway;
    @Autowired
    private ListPlanetUseCase useCase;
    @Autowired
    private PlanetRepository repository;


    @Test
    public void givenAValidQuery_whenCallsListPlanets_shouldReturnAll() {
        // given
        final var members = List.of(
                Planet.newPlanet(1,1, "teste1"),
                Planet.newPlanet(1,1, "teste2")
        );

        final var savedItems = repository.saveAllAndFlush(members.stream().map(PlanetJpaEntity::from).toList());

        final var expectedPage = 0;
        final var expectedPerPage = 10;
        final var expectedTerms = "tes";
        final var expectedSort = "createdAt";
        final var expectedDirection = "asc";
        final var expectedTotal = 2;

        final var expectedItems = savedItems.stream()
                .map(PlanetJpaEntity::toAggregate)
                .map(PlanetListOutput::from)
                .toList();

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

    @Test
    public void givenAValidQuery_whenCallsListPlanetsAndIsEmpty_shouldReturn() {
        // given
        final var expectedPage = 0;
        final var expectedPerPage = 10;
        final var expectedTerms = "Algo";
        final var expectedSort = "createdAt";
        final var expectedDirection = "asc";
        final var expectedTotal = 0;

        final var members = List.<Planet>of();
        final var expectedItems = List.<PlanetListOutput>of();

        final var expectedPagination = new Pagination<>(
                expectedPage,
                expectedPerPage,
                expectedTotal,
                members
        );

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