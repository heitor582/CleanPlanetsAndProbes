package com.study.application.planet.retrieve.get;

import com.study.IntegrationTest;
import com.study.domain.exceptions.NotFoundException;
import com.study.domain.planet.Planet;
import com.study.domain.planet.PlanetGateway;
import com.study.domain.planet.PlanetID;
import com.study.infrastructure.planet.persistence.PlanetJpaEntity;
import com.study.infrastructure.planet.persistence.PlanetRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.mock.mockito.SpyBean;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.verify;

class GetPlanetByIdUseCaseIT implements IntegrationTest{
    @SpyBean
    private PlanetGateway gateway;
    @Autowired
    private GetPlanetByIdUseCase useCase;
    @Autowired
    private PlanetRepository repository;

    @Test
    public void givenAValidId_whenCallsGetPlanet_shouldReturnIt() {
        final var planet = Planet.newPlanet(1,1, "teste");
        final var id = PlanetID.from(repository.saveAndFlush(PlanetJpaEntity.from(planet)).getId());

        final var output = useCase.execute(id.getValue());

        assertEquals(id.getValue(), output.id());
        assertEquals(planet.getName(), output.name());
        assertEquals(planet.getCordX(), output.cordX());
        assertEquals(planet.getCordY(), output.cordY());
        assertEquals(planet.getCreatedAt(), output.createdAt());
        assertEquals(planet.getUpdatedAt(), output.updatedAt());

        verify(gateway).findBy(eq(id));
    }

    @Test
    public void givenAnInvalidId_whenCallsGetPlanet_shouldThrowsNotFoundException() {
        final var id = PlanetID.from(123L);
        final var expectedErrorMessage = "Planet with ID 123 was not found";

        final var exception = assertThrows(NotFoundException.class, () -> useCase.execute(id.getValue()));

        assertEquals(expectedErrorMessage, exception.getMessage());

        verify(gateway).findBy(eq(id));
    }
}