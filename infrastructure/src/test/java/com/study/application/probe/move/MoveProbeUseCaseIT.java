package com.study.application.probe.move;

import com.study.IntegrationTest;
import com.study.domain.exceptions.NotFoundException;
import com.study.domain.exceptions.NotificationException;
import com.study.domain.planet.Planet;
import com.study.domain.probe.Probe;
import com.study.domain.probe.ProbeGateway;
import com.study.domain.probe.ProbeID;
import com.study.infrastructure.planet.persistence.PlanetJpaEntity;
import com.study.infrastructure.planet.persistence.PlanetRepository;
import com.study.infrastructure.probe.persistence.ProbeJpaEntity;
import com.study.infrastructure.probe.persistence.ProbeRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.mock.mockito.SpyBean;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

class MoveProbeUseCaseIT extends IntegrationTest{
    @SpyBean
    private ProbeGateway gateway;
    @Autowired
    private MoveProbeUseCase useCase;
    @Autowired
    private ProbeRepository repository;
    @Autowired
    private PlanetRepository planetRepository;

    @Test
    public void givenAValidCommand_whenCallsMoveProbe_shouldReturnIt() {
        final var planet = planetRepository.saveAndFlush(PlanetJpaEntity.from(Planet.newPlanet(5,5,"teste"))).toAggregate();
        final var probe = repository.saveAndFlush(ProbeJpaEntity.from(Probe.newProbe("teste",1,1, planet))).toAggregate();
        final var expectedId = probe.getId();

        final var expectedCordY = 2;
        final var expectedCordX = 1;

        final var command = MoveProbeCommand.with(expectedId.getValue(), "LRM");
        final var output = useCase.execute(command);

        final var foundProbe = repository.findById(output.id()).get().toAggregate();

        assertEquals(output.id(), probe.getId().getValue());

        assertEquals(expectedId, foundProbe.getId());
        assertEquals(probe.getName(), foundProbe.getName());
        assertEquals(expectedCordX, foundProbe.getCordX());
        assertEquals(expectedCordY, foundProbe.getCordY());
        assertEquals(probe.getDirection(), foundProbe.getDirection());
        assertEquals(planet, foundProbe.getPlanet());
        assertEquals(planet.getId(), foundProbe.getPlanet().getId());
        assertEquals(probe.getCreatedAt(), foundProbe.getCreatedAt());
        assertTrue(foundProbe.getUpdatedAt().isAfter(foundProbe.getCreatedAt()));

        verify(gateway).findBy(expectedId);
        verify(gateway).update(any());
    }

    @Test
    public void givenAnInvalidId_whenCallsMoveProbe_shouldThrowsNotFoundException() {
        final var id = ProbeID.from(123L);
        final var expectedErrorMessage = "Probe with ID 123 was not found";

        final var command = MoveProbeCommand.with(id.getValue(), "LRM");

        final var exception = assertThrows(NotFoundException.class, () -> useCase.execute(command));

        assertEquals(expectedErrorMessage, exception.getMessage());

        verify(gateway).findBy(eq(id));
        verify(gateway, times(0)).update(any());
    }

    @Test
    public void givenAnInvalidCommand_whenCallsMoveProbe_shouldThrowsNotificationException() {
        final var expectedErrorMessage = "The command passed did not work";
        final var expectedErrorCount = 1;

        final var planet = planetRepository.saveAndFlush(PlanetJpaEntity.from(Planet.newPlanet(5,5,"teste"))).toAggregate();
        final var probe = repository.saveAndFlush(ProbeJpaEntity.from(Probe.newProbe("teste",1,1, planet))).toAggregate();
        final var expectedId = probe.getId();

        final var command = MoveProbeCommand.with(expectedId.getValue(), "HE");

        final var exception = assertThrows(NotificationException.class, () -> useCase.execute(command));

        assertEquals(expectedErrorCount, exception.getErrors().size());
        assertEquals(expectedErrorMessage, exception.getErrors().get(0).message());

        verify(gateway).findBy(eq(expectedId));
        verify(gateway, times(0)).update(any());
    }
}