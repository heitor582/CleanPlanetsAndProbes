package com.study.application.probe.retrieve.list;

import com.study.application.UseCaseTest;
import com.study.application.planet.retrieve.list.PlanetListOutput;
import com.study.domain.pagination.Pagination;
import com.study.domain.pagination.SearchQuery;
import com.study.domain.planet.Planet;
import com.study.domain.probe.Probe;
import com.study.domain.probe.ProbeGateway;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

class ListProbeUseCaseTest extends UseCaseTest {
    @Mock
    private ProbeGateway gateway;
    @InjectMocks
    private DefaultListProbeUseCase useCase;

    @Override
    protected List<Object> getMocks() {
        return List.of(gateway);
    }
    @Test
    public void givenAValidQuery_whenCallsListProbes_shouldReturnAll() {
        // given
        final var planet = Planet.newPlanet(1,1, "teste2");
        final var members = List.of(
                Probe.newProbe("teste1",1,1, planet),
                Probe.newProbe("teste2",1,1, planet)
        );

        final var expectedPage = 0;
        final var expectedPerPage = 10;
        final var expectedTerms = "Algo";
        final var expectedSort = "createdAt";
        final var expectedDirection = "asc";
        final var expectedTotal = 2;

        final var expectedItems = members.stream()
                .map(ProbeListOutput::from)
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
        final var expectedTerms = "Algo";
        final var expectedSort = "createdAt";
        final var expectedDirection = "asc";
        final var expectedTotal = 0;

        final var members = List.<Probe>of();
        final var expectedItems = List.<ProbeListOutput>of();

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