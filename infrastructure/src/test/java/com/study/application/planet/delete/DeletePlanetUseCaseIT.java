package com.study.application.planet.delete;

import com.study.IntegrationTest;
import com.study.domain.planet.Planet;
import com.study.domain.planet.PlanetGateway;
import com.study.domain.planet.PlanetID;
import com.study.infrastructure.planet.persistence.PlanetJpaEntity;
import com.study.infrastructure.planet.persistence.PlanetRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.mock.mockito.SpyBean;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.verify;

class DeletePlanetUseCaseIT implements IntegrationTest{
    @SpyBean
    private PlanetGateway gateway;
    @Autowired
    private DeletePlanetUseCase useCase;
    @Autowired
    private PlanetRepository repository;

    @Test
    public void givenAValidId_whenCallsDeletePlanet_shouldBeOk() {
        final var planet = Planet.newPlanet(1,1, "teste");
        final var id = PlanetID.from(repository.saveAndFlush(PlanetJpaEntity.from(planet)).getId());

        assertDoesNotThrow(() -> useCase.execute(id.getValue()));

        verify(gateway).deleteBy(eq(id));
    }

    @Test
    public void givenAnInvalidId_whenCallsDeletePlanet_shouldBeOk() {
        final var id = PlanetID.from(123L);

        assertDoesNotThrow(() -> useCase.execute(id.getValue()));

        verify(gateway).deleteBy(eq(id));
    }
}