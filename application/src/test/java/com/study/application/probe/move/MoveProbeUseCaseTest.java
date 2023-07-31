package com.study.application.probe.move;

import com.study.application.UseCaseTest;
import com.study.domain.exceptions.NotFoundException;
import com.study.domain.exceptions.NotificationException;
import com.study.domain.planet.Planet;
import com.study.domain.probe.Direction;
import com.study.domain.probe.Probe;
import com.study.domain.probe.ProbeGateway;
import com.study.domain.probe.ProbeID;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.internal.matchers.Not;

import javax.management.NotificationFilter;
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

class MoveProbeUseCaseTest extends UseCaseTest {
    @Mock
    private ProbeGateway gateway;
    @InjectMocks
    private DefaultMoveProbeUseCase useCase;

    @Override
    protected List<Object> getMocks() {
        return List.of(gateway);
    }

    @Test
    public void givenAValidCommand_whenCallsMoveProbe_shouldReturnIt() {
        final var planet = Planet.newPlanet(5,5,"teste");
        final var probe = Probe.newProbe("teste",1,1,  planet);
        final var expectedId = probe.getId();
        final var command = MoveProbeCommand.with(expectedId.getValue(), "LRM");

        when(gateway.findBy(expectedId)).thenReturn(Optional.of(probe));
        when(gateway.update(any())).thenAnswer(returnsFirstArg());

        final var output = useCase.execute(command);

        assertEquals(output.id(), probe.getId().getValue());

        verify(gateway).update(argThat(cmd ->
                Objects.nonNull(cmd.getId())
                        && Objects.equals(probe.getName(), cmd.getName())
                        && Objects.equals(probe.getCordX(), cmd.getCordX())
                        && Objects.equals(probe.getCordY(), cmd.getCordY())
                        && Objects.equals(Direction.UP, cmd.getDirection())
                        && Objects.equals(planet, cmd.getPlanet())
                        && Objects.equals(planet.getId(), cmd.getPlanet().getId())
                        && Objects.nonNull(cmd.getCreatedAt())
                        && Objects.nonNull(cmd.getUpdatedAt())
        ));
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
    public void givenAnInvaliCommand_whenCallsMoveProbe_shouldThrowsNotificationException() {
        final var expectedErrorMessage = "The command passed did not work";
        final var expectedErrorCount = 1;
        final var planet = Planet.newPlanet(5,5,"teste");
        final var probe = Probe.newProbe("teste",1,1,  planet);
        final var expectedId = probe.getId();
        final var command = MoveProbeCommand.with(expectedId.getValue(), "HE");

        when(gateway.findBy(expectedId)).thenReturn(Optional.of(probe));

        final var exception = assertThrows(NotificationException.class, () -> useCase.execute(command));

        assertEquals(expectedErrorCount, exception.getErrors().size());
        assertEquals(expectedErrorMessage, exception.getErrors().get(0).message());

        verify(gateway).findBy(eq(expectedId));
        verify(gateway, times(0)).update(any());
    }
}