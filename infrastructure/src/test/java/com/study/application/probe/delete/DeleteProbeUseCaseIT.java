package com.study.application.probe.delete;

import com.study.IntegrationTest;
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

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.verify;

class DeleteProbeUseCaseIT implements IntegrationTest{
    @SpyBean
    private ProbeGateway gateway;
    @Autowired
    private DeleteProbeUseCase useCase;
    @Autowired
    private ProbeRepository repository;
    @Autowired
    private PlanetRepository planetRepository;

    @Test
    public void givenAValidId_whenCallsDeletePlanet_shouldBeOk() {
        final var planet = planetRepository.saveAndFlush(PlanetJpaEntity.from(Planet.newPlanet(5,5,"teste"))).toAggregate();
        final var probe = repository.saveAndFlush(ProbeJpaEntity.from(Probe.newProbe("teste",1,1, planet))).toAggregate();
        final var id = probe.getId();

        assertDoesNotThrow(() -> useCase.execute(id.getValue()));

        verify(gateway).deleteBy(eq(id));
    }

    @Test
    public void givenAnInvalidId_whenCallsDeletePlanet_shouldBeOk() {
        final var id = ProbeID.from(123L);

        assertDoesNotThrow(() -> useCase.execute(id.getValue()));

        verify(gateway).deleteBy(eq(id));
    }
}