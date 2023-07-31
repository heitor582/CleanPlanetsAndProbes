package com.study.application.probe.retrieve.get;

import com.study.IntegrationTest;
import com.study.domain.exceptions.NotFoundException;
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
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.verify;

class GetProbeByIdUseCaseIT implements IntegrationTest{
    @SpyBean
    private ProbeGateway gateway;
    @Autowired
    private GetProbeByIdUseCase useCase;
    @Autowired
    private ProbeRepository repository;
    @Autowired
    private PlanetRepository planetRepository;

    @Test
    public void givenAValidId_whenCallsGetProbe_shouldReturnIt() {
        final var planet = planetRepository.saveAndFlush(PlanetJpaEntity.from(Planet.newPlanet(5,5,"teste"))).toAggregate();
        final var probe = repository.saveAndFlush(ProbeJpaEntity.from(Probe.newProbe("teste",1,1, planet.getId()))).toAggregate();
        final var id = probe.getId();

        final var output = useCase.execute(id.getValue());

        assertEquals(probe.getId().getValue(), output.id());
        assertEquals(probe.getName(), output.name());
        assertEquals(probe.getCordX(), output.cordX());
        assertEquals(probe.getCordY(), output.cordY());
        assertEquals(probe.getDirection(), output.direction());
        assertEquals(probe.getPlanetId().getValue(), output.planetId());
        assertEquals(probe.getPlanetId(), planet.getId());
        assertEquals(probe.getCreatedAt(), output.createdAt());
        assertEquals(probe.getUpdatedAt(), output.updatedAt());

        verify(gateway).findBy(eq(id));
    }

    @Test
    public void givenAnInvalidId_whenCallsGetPlanet_shouldThrowsNotFoundException() {
        final var id = ProbeID.from(123L);
        final var expectedErrorMessage = "Probe with ID 123 was not found";

        final var exception = assertThrows(NotFoundException.class, () -> useCase.execute(id.getValue()));

        assertEquals(expectedErrorMessage, exception.getMessage());

        verify(gateway).findBy(eq(id));
    }
}