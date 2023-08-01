package com.study.application.probe.update;

import com.study.IntegrationTest;
import com.study.domain.exceptions.NotFoundException;
import com.study.domain.exceptions.NotificationException;
import com.study.domain.planet.Planet;
import com.study.domain.probe.Direction;
import com.study.domain.probe.Probe;
import com.study.domain.probe.ProbeGateway;
import com.study.domain.probe.ProbeID;
import com.study.infrastructure.planet.persistence.PlanetJpaEntity;
import com.study.infrastructure.planet.persistence.PlanetRepository;
import com.study.infrastructure.probe.persistence.ProbeJpaEntity;
import com.study.infrastructure.probe.persistence.ProbeRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.mock.mockito.SpyBean;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

class UpdateProbeUseCaseIT extends IntegrationTest {
    @SpyBean
    private ProbeGateway gateway;
    @Autowired
    private ProbeRepository repository;
    @Autowired
    private PlanetRepository planetRepository;
    @Autowired
    private UpdateProbeUseCase useCase;

    @Test
    public void givenAValidCommand_whenCallsUpdateProbe_shouldReturnIt() {
        final var expectedName = "teste";
        final var expectedCordX = 3;
        final var expectedCordY = 3;
        final var expectedDirection = Direction.DOWN;
        final var planet = planetRepository.saveAndFlush(PlanetJpaEntity.from(Planet.newPlanet(5,5,"teste"))).toAggregate();
        final var probe = repository.saveAndFlush(ProbeJpaEntity.from(Probe.newProbe("teste",1,1, planet))).toAggregate();
        final var expectedId = probe.getId();

        final var command = UpdateProbeCommand.with(expectedId.getValue(), expectedName, expectedCordX, expectedCordY, expectedDirection);

        final var output = useCase.execute(command);

        final var foundProbe = repository.findById(output.id()).get().toAggregate();

        assertEquals(output.id(), probe.getId().getValue());

        assertEquals(expectedId, foundProbe.getId());
        assertEquals(probe.getName(), foundProbe.getName());
        assertEquals(expectedCordX, foundProbe.getCordX());
        assertEquals(expectedCordY, foundProbe.getCordY());
        assertEquals(expectedDirection, foundProbe.getDirection());
        assertEquals(planet, foundProbe.getPlanet());
        assertEquals(planet.getId(), foundProbe.getPlanet().getId());
        assertEquals(probe.getCreatedAt(), foundProbe.getCreatedAt());
        assertTrue(foundProbe.getUpdatedAt().isAfter(foundProbe.getCreatedAt()));

        verify(gateway).findBy(expectedId);
        verify(gateway).update(any());
    }

    @Test
    public void givenAnInvalidId_whenCallsUpdateProbe_shouldThrowsNotFoundException() {
        final var id = ProbeID.from(123L);
        final var expectedErrorMessage = "Probe with ID 123 was not found";
        final var command = UpdateProbeCommand.with(id.getValue(), "teste", 5, 5, Direction.UP);

        final var exception = assertThrows(NotFoundException.class, () -> useCase.execute(command));

        assertEquals(expectedErrorMessage, exception.getMessage());

        verify(gateway).findBy(eq(id));
        verify(gateway, times(0)).update(any());
    }

    @ParameterizedTest
    @CsvSource({
            ",name should not be null",
            "'',name should not be empty",
            "aa,name must be between 3 and 255 characters",
            " a,name must be between 3 and 255 characters",
    })
    public void givenAnInvalidCommandWithInvalidName_whenCallsUpdateProbe_shouldReturnNotificationException(
            final String name,
            final String errorMessage
    ) {
        final var expectedCordX = 3;
        final var expectedCordY = 3;
        final var expectedErrorCount = 1;
        final var planet = planetRepository.saveAndFlush(PlanetJpaEntity.from(Planet.newPlanet(5,5,"teste"))).toAggregate();
        final var probe = repository.saveAndFlush(ProbeJpaEntity.from(Probe.newProbe("teste",1,1, planet))).toAggregate();
        final var expectedId = probe.getId();
        final var command = UpdateProbeCommand.with(expectedId.getValue(), name, expectedCordX, expectedCordY, Direction.UP);

        when(gateway.findBy(expectedId)).thenReturn(Optional.of(probe));

        final var exception = assertThrows(NotificationException.class, () -> useCase.execute(command));

        assertEquals(expectedErrorCount, exception.getErrors().size());
        assertEquals(errorMessage, exception.getErrors().get(0).message());

        verify(gateway).findBy(eq(expectedId));
        verify(gateway, times(0)).update(any());
    }

}