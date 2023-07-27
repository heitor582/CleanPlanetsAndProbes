package com.study.application.planet.delete;

import com.study.application.UseCaseTest;
import com.study.domain.planet.Planet;
import com.study.domain.planet.PlanetGateway;
import com.study.domain.planet.PlanetID;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.doThrow;

class DeletePlanetUseCaseTest extends UseCaseTest {
    @Mock
    private PlanetGateway gateway;

    @InjectMocks
    private DefaultDeletePlanetUseCase useCase;

    @Override
    protected List<Object> getMocks() {
        return List.of(gateway);
    }

    @Test
    public void givenAValidId_whenCallsDeletePlanet_shouldBeOk() {
        final var planet = Planet.newPlanet(1,1, "teste");
        final var id = planet.getId();

        doNothing().when(gateway).deleteBy(id);

        assertDoesNotThrow(() -> useCase.execute(id.getValue()));

        Mockito.verify(gateway).deleteBy(eq(id));
    }

    @Test
    public void givenAnInvalidId_whenCallsDeletePlanet_shouldBeOk() {
        final var id = PlanetID.from(123L);

        doNothing().when(gateway).deleteBy(id);

        assertDoesNotThrow(() -> useCase.execute(id.getValue()));

        Mockito.verify(gateway).deleteBy(eq(id));
    }

    @Test
    public void givenAValidId_whenCallsDeletePlanetAndGatewayThrowsException_shouldReceiveException() {
        final var planet = Planet.newPlanet(1,1, "teste");
        final var id = planet.getId();

        doThrow(new IllegalStateException("Error")).when(gateway).deleteBy(id);

        assertThrows(IllegalStateException.class, () -> useCase.execute(id.getValue()));

        Mockito.verify(gateway).deleteBy(eq(id));
    }
}