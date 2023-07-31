package com.study.application.probe.delete;

import com.study.application.UseCaseTest;
import com.study.domain.planet.Planet;
import com.study.domain.probe.Probe;
import com.study.domain.probe.ProbeGateway;
import com.study.domain.probe.ProbeID;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.doThrow;

class DeleteProbeUseCaseTest extends UseCaseTest {
    @Mock
    private ProbeGateway gateway;
    @InjectMocks
    private DefaultDeleteProbeUseCase useCase;

    @Override
    protected List<Object> getMocks() {
        return List.of(gateway);
    }

    @Test
    public void givenAValidId_whenCallsDeletePlanet_shouldBeOk() {
        final var planet = Planet.newPlanet(1,2,"teste");
        final var probe = Probe.newProbe("teste",1,1, planet);
        final var id = probe.getId();

        doNothing().when(gateway).deleteBy(id);

        assertDoesNotThrow(() -> useCase.execute(id.getValue()));

        verify(gateway).deleteBy(eq(id));
    }

    @Test
    public void givenAnInvalidId_whenCallsDeletePlanet_shouldBeOk() {
        final var id = ProbeID.from(123L);

        doNothing().when(gateway).deleteBy(id);

        assertDoesNotThrow(() -> useCase.execute(id.getValue()));

        verify(gateway).deleteBy(eq(id));
    }

    @Test
    public void givenAValidId_whenCallsDeleteProbeAndGatewayThrowsException_shouldReceiveException() {
        final var planet = Planet.newPlanet(1,2,"teste");
        final var probe = Probe.newProbe("teste",1,1,  planet);
        final var id = probe.getId();

        doThrow(new IllegalStateException("Error")).when(gateway).deleteBy(id);

        assertThrows(IllegalStateException.class, () -> useCase.execute(id.getValue()));

        verify(gateway).deleteBy(eq(id));
    }
}