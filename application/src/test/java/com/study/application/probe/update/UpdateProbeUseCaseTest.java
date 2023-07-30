package com.study.application.probe.update;

import com.study.application.UseCaseTest;
import com.study.domain.exceptions.NotFoundException;
import com.study.domain.exceptions.NotificationException;
import com.study.domain.planet.Planet;
import com.study.domain.probe.Direction;
import com.study.domain.probe.Probe;
import com.study.domain.probe.ProbeGateway;
import com.study.domain.probe.ProbeID;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.mockito.InjectMocks;
import org.mockito.Mock;

import java.util.List;
import java.util.Objects;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.AdditionalAnswers.returnsFirstArg;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.argThat;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

class UpdateProbeUseCaseTest extends UseCaseTest {
    @Mock
    private ProbeGateway gateway;
    @InjectMocks
    private DefaultUpdateProbeUseCase useCase;

    @Override
    protected List<Object> getMocks() {
        return List.of(gateway);
    }

    @Test
    public void givenAValidCommand_whenCallsUpdateProbe_shouldReturnIt() {
        final var expectedName = "teste";
        final var expectedCordX = 3;
        final var expectedCordY = 3;
        final var expectedDirection = Direction.DOWN;
        final var planet = Planet.newPlanet(5,5,"teste");
        final var probe = Probe.newProbe("teste",1,1,  planet);
        final var expectedId = probe.getId();

        final var command = UpdateProbeCommand.with(expectedId.getValue(), expectedName, expectedCordX, expectedCordY, expectedDirection);

        when(gateway.findBy(expectedId)).thenReturn(Optional.of(probe));
        when(gateway.update(any())).thenAnswer(returnsFirstArg());

        final var output = useCase.execute(command);

        assertEquals(output.id(), probe.getId().getValue());

        verify(gateway).update(argThat(cmd ->
                Objects.nonNull(cmd.getId())
                        && Objects.equals(expectedName, cmd.getName())
                        && Objects.equals(expectedCordX, cmd.getCordX())
                        && Objects.equals(expectedCordY, cmd.getCordY())
                        && Objects.equals(expectedDirection, cmd.getDirection())
                        && cmd.getUpdatedAt().isAfter(probe.getCreatedAt())
                        && Objects.nonNull(cmd.getCreatedAt())
                        && Objects.nonNull(cmd.getUpdatedAt())
        ));
    }

    @Test
    public void givenAnInvalidId_whenCallsUpdateProbe_shouldThrowsNotFoundException() {
        final var id = ProbeID.from(123L);
        final var expectedErrorMessage = "Probe with ID 123 was not found";
        final var command = UpdateProbeCommand.with(id.getValue(), "teste", 5, 5, Direction.UP);

        when(gateway.findBy(id)).thenReturn(Optional.empty());

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
        final var planet = Planet.newPlanet(5,5,"teste");
        final var probe = Probe.newProbe("teste",1,1,  planet);
        final var expectedId = probe.getId();
        final var command = UpdateProbeCommand.with(expectedId.getValue(), name, expectedCordX, expectedCordY, Direction.UP);

        when(gateway.findBy(expectedId)).thenReturn(Optional.of(probe));

        final var exception = assertThrows(NotificationException.class, () -> useCase.execute(command));

        assertEquals(expectedErrorCount, exception.getErrors().size());
        assertEquals(errorMessage, exception.getErrors().get(0).message());

        verify(gateway, times(0)).update(any());
    }

}