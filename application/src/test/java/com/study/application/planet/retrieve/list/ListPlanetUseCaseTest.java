package com.study.application.planet.retrieve.list;

import com.study.application.UseCaseTest;
import com.study.domain.pagination.Pagination;
import com.study.domain.pagination.SearchQuery;
import com.study.domain.planet.Planet;
import com.study.domain.planet.PlanetGateway;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

class ListPlanetUseCaseTest extends UseCaseTest {
    @Mock
    private PlanetGateway gateway;

    @InjectMocks
    private DefaultListPlanetUseCase useCase;

    @Override
    protected List<Object> getMocks() {
        return List.of(gateway);
    }

    @Test
    public void givenAValidQuery_whenCallsListPlanets_shouldReturnAll() {
        // given
        final var members = List.of(
                Planet.newPlanet(1,1, "teste1"),
                Planet.newPlanet(1,1, "teste2")
        );

        final var expectedPage = 0;
        final var expectedPerPage = 10;
        final var expectedTerms = "Algo";
        final var expectedSort = "createdAt";
        final var expectedDirection = "asc";
        final var expectedTotal = 2;

        final var expectedItems = members.stream()
                .map(PlanetListOutput::from)
                .toList();

        final var expectedPagination = new Pagination<>(
                expectedPage,
                expectedPerPage,
                expectedTotal,
                members
        );

        when(gateway.findAll(any()))
                .thenReturn(expectedPagination);

        final var aQuery =
                new SearchQuery(expectedPage, expectedPerPage, expectedTerms, expectedSort, expectedDirection);

        // when
        final var actualOutput = useCase.execute(aQuery);

        // then
        assertEquals(expectedPage, actualOutput.currentPage());
        assertEquals(expectedPerPage, actualOutput.perPage());
        assertEquals(expectedTotal, actualOutput.total());
        assertEquals(expectedItems, actualOutput.items());
        assertEquals(expectedItems.get(0).id(), actualOutput.items().get(0).id());
        assertEquals(expectedItems.get(0).cordX(), actualOutput.items().get(0).cordX());
        assertEquals(expectedItems.get(0).cordY(), actualOutput.items().get(0).cordY());
        assertEquals(expectedItems.get(0).createdAt(), actualOutput.items().get(0).createdAt());
        assertEquals(expectedItems.get(0).name(), actualOutput.items().get(0).name());
        assertEquals(expectedItems.get(1).id(), actualOutput.items().get(1).id());
        assertEquals(expectedItems.get(1).cordX(), actualOutput.items().get(1).cordX());
        assertEquals(expectedItems.get(1).cordY(), actualOutput.items().get(1).cordY());
        assertEquals(expectedItems.get(1).createdAt(), actualOutput.items().get(1).createdAt());
        assertEquals(expectedItems.get(1).name(), actualOutput.items().get(1).name());

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

        final var members = List.<Planet>of();
        final var expectedItems = List.<PlanetListOutput>of();

        final var expectedPagination = new Pagination<>(
                expectedPage,
                expectedPerPage,
                expectedTotal,
                members
        );

        when(gateway.findAll(any()))
                .thenReturn(expectedPagination);

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
    public void givenAValidQuery_whenCallsListPlanetsAndGatewayThrowsRandomException_shouldException() {
        // given
        final var expectedPage = 0;
        final var expectedPerPage = 10;
        final var expectedTerms = "Algo";
        final var expectedSort = "createdAt";
        final var expectedDirection = "asc";

        final var expectedErrorMessage = "Gateway error";

        when(gateway.findAll(any()))
                .thenThrow(new IllegalStateException(expectedErrorMessage));

        final var aQuery =
                new SearchQuery(expectedPage, expectedPerPage, expectedTerms, expectedSort, expectedDirection);

        // when
        final var actualException = assertThrows(IllegalStateException.class, () -> useCase.execute(aQuery));

        // then
        assertEquals(expectedErrorMessage, actualException.getMessage());

        verify(gateway).findAll(eq(aQuery));
    }

}