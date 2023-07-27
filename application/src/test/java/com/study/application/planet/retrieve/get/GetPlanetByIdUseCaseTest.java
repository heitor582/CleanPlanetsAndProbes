package com.study.application.planet.retrieve.get;

import com.study.application.UseCaseTest;
import com.study.domain.exceptions.NotFoundException;
import com.study.domain.planet.Planet;
import com.study.domain.planet.PlanetGateway;
import com.study.domain.planet.PlanetID;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

class GetPlanetByIdUseCaseTest extends UseCaseTest {
    @Mock
    private PlanetGateway gateway;

    @InjectMocks
    private DefaultGetPlanetByIdUseCase useCase;

    @Override
    protected List<Object> getMocks() {
        return List.of(gateway);
    }

    @Test
    public void givenAValidId_whenCallsGetPlanet_shouldReturnIt() {
        final var planet = Planet.newPlanet(1,1, "teste");
        final var id = planet.getId();

        when(gateway.findBy(id)).thenReturn(Optional.of(planet));

        final var output = useCase.execute(id.getValue());

        assertEquals(planet.getId().getValue(), output.id());
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

        when(gateway.findBy(id)).thenReturn(Optional.empty());

        final var exception = assertThrows(NotFoundException.class, () -> useCase.execute(id.getValue()));

        assertEquals(expectedErrorMessage, exception.getMessage());

        verify(gateway).findBy(eq(id));
    }

}