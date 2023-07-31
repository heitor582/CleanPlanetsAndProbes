package com.study.application.probe.create;

import com.study.IntegrationTest;
import com.study.domain.exceptions.NotFoundException;
import com.study.domain.exceptions.NotificationException;
import com.study.domain.planet.Planet;
import com.study.domain.planet.PlanetGateway;
import com.study.domain.planet.PlanetID;
import com.study.domain.probe.Direction;
import com.study.domain.probe.ProbeGateway;
import com.study.infrastructure.planet.persistence.PlanetJpaEntity;
import com.study.infrastructure.planet.persistence.PlanetRepository;
import com.study.infrastructure.probe.persistence.ProbeRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.mock.mockito.SpyBean;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

class CreateProbeUseCaseIT implements IntegrationTest {
    @SpyBean
    private PlanetGateway planetGateway;
    @SpyBean
    private ProbeGateway probeGateway;
    @Autowired
    private CreateProbeUseCase useCase;
    @Autowired
    private ProbeRepository repository;
    @Autowired
    private PlanetRepository planetRepository;

    @Test
    public void givenAValidCommand_whenCallsCreateProbe_shouldReturnIt() {
        final var expectedName = "teste";
        final var expectedCordX = 3;
        final var expectedCordY = 3;
        final var expectedPlanet = planetRepository.saveAndFlush(PlanetJpaEntity.from(Planet.newPlanet(5,5,expectedName))).toAggregate();
        final var planetId = expectedPlanet.getId();

        final var command = CreateProbeCommand.with(expectedName, expectedCordX, expectedCordY, planetId.getValue());

        final var output = useCase.execute(command);
        final var probe = repository.findById(output.id()).get().toAggregate();

        assertNotNull(probe.getId());
        assertEquals(expectedName, probe.getName());
        assertEquals(expectedCordX, probe.getCordX());
        assertEquals(expectedCordY, probe.getCordY());
        assertEquals(Direction.UP, probe.getDirection());
        assertEquals(expectedPlanet, probe.getPlanet());
        assertEquals(expectedPlanet.getId(), probe.getPlanet().getId());
        assertNotNull(probe.getCreatedAt());
        assertNotNull(probe.getUpdatedAt());

        verify(planetGateway).findBy(planetId);
        verify(probeGateway).create(any());
    }

    @Test
    public void givenAValidCommandWithANonExistencePlanetId_whenCallsCreateProbe_shouldReturnNotFoundException() {
        final var expectedName = "teste";
        final var expectedCordX = 3;
        final var expectedCordY = 3;
        final var planetId = PlanetID.from(1L);
        final var expectedErrorMessage = "Planet with ID 1 was not found";
        final var command = CreateProbeCommand.with(expectedName, expectedCordX, expectedCordY, planetId.getValue());

        final var exception = assertThrows(NotFoundException.class, () -> useCase.execute(command));

        assertEquals(expectedErrorMessage, exception.getMessage());

        verify(planetGateway).findBy(planetId);
        verify(probeGateway, times(0)).create(any());
    }

    @ParameterizedTest
    @CsvSource({
            ",name should not be null",
            "'',name should not be empty",
            "aa,name must be between 3 and 255 characters",
            " a,name must be between 3 and 255 characters",
    })
    public void givenAnInvalidCommandWithInvalidName_whenCallsCreateProbe_shouldReturnNotificationException(
            final String name,
            final String errorMessage
    ) {
        final var expectedCordX = 3;
        final var expectedCordY = 3;
        final var expectedPlanet = planetRepository.saveAndFlush(PlanetJpaEntity.from(Planet.newPlanet(5,5,"teste"))).toAggregate();
        final var planetId = expectedPlanet.getId();
        final var expectedErrorCount = 1;
        final var command = CreateProbeCommand.with(name, expectedCordX, expectedCordY, planetId.getValue());

        final var exception = assertThrows(NotificationException.class, () -> useCase.execute(command));

        assertEquals(expectedErrorCount, exception.getErrors().size());
        assertEquals(errorMessage, exception.getErrors().get(0).message());

        verify(planetGateway).findBy(planetId);
        verify(probeGateway, times(0)).create(any());
    }
}