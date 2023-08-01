package com.study.application.planet.update;

import com.study.application.UseCaseTest;
import com.study.domain.exceptions.NotFoundException;
import com.study.domain.exceptions.NotificationException;
import com.study.domain.planet.Planet;
import com.study.domain.planet.PlanetGateway;
import com.study.domain.planet.PlanetID;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.mockito.InjectMocks;
import org.mockito.Mock;

import java.util.List;
import java.util.Objects;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.AdditionalAnswers.returnsFirstArg;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.argThat;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

class UpdatePlanetUseCaseTest extends UseCaseTest {
    @Mock
    private PlanetGateway gateway;

    @InjectMocks
    private DefaultUpdatePlanetUseCase useCase;

    @Override
    protected List<Object> getMocks() {
        return List.of(gateway);
    }

    @Test
    public void givenAValidCommand_whenCallsUpdatePlanet_shouldUReturnIt(){
        final var planet = Planet.newPlanet(1,1,"teste");

        final var expectedId = planet.getId();
        final var expectedName = "teste1";
        final var expectedCordX = 2;
        final var expectedCordY = 2;
        final var expectedCreatedAt = planet.getCreatedAt();
        final var updatedAt = planet.getUpdatedAt();

        final var command = UpdatePlanetCommand.with(expectedId.getValue(), expectedName, expectedCordX, expectedCordY);

        when(gateway.findBy(expectedId)).thenReturn(Optional.of(planet));
        when(gateway.update(any())).thenAnswer(returnsFirstArg());

        final var output = useCase.execute(command);

        assertEquals(output.id(), expectedId.getValue());

        verify(gateway).update(argThat(cmd ->
                Objects.equals(expectedId, cmd.getId())
                        && Objects.equals(expectedName, cmd.getName())
                        && Objects.equals(expectedCordX, cmd.getCordX())
                        && Objects.equals(expectedCordY, cmd.getCordY())
                        && Objects.equals(expectedCreatedAt, cmd.getCreatedAt())
                        && cmd.getUpdatedAt().isAfter(updatedAt)
        ));
    }

    @ParameterizedTest
    @CsvSource({
            ",3,3,name should not be null",
            "'',3,3,name should not be empty",
            "aa,3,3,name must be between 3 and 255 characters",
            " a,3,3,name must be between 3 and 255 characters",
            "tes,0,3,coordinate X must be between 1 and 1000",
            "tes,3,0,coordinate Y must be between 1 and 1000",
            "tes,1001,3,coordinate X must be between 1 and 1000",
            "tes,3,1001,coordinate Y must be between 1 and 1000",
    })
    public void givenAnInvalidName_whenCallsUpdatePlanet_shouldReturnNotification(
            final String expectedName,
            final int expectedCordX,
            final int expectedCordY,
            final String expectedErrorMessage
    ){
        final var planet = Planet.newPlanet(1,1,"teste");

        final var expectedId = planet.getId();
        final var expectedErrorCount = 1;

        final var command = UpdatePlanetCommand.with(expectedId.getValue(), expectedName, expectedCordX, expectedCordY);


        when(gateway.findBy(expectedId)).thenReturn(Optional.of(planet));

        final var exception = assertThrows(NotificationException.class, () -> useCase.execute(command));

        assertEquals(expectedErrorCount, exception.getErrors().size());
        assertEquals(expectedErrorMessage, exception.getErrors().get(0).message());

        verify(gateway).findBy(eq(expectedId));
        verify(gateway, times(0)).update(any());
    }

    @Test
    public void givenAValidCommand_whenCallsUpdatePlanetWithNonexistentId_shouldReturnNotFoundException (){
        final var expectedId = PlanetID.from(123L);
        final var expectedName = "teste1";
        final var expectedCordX = 2;
        final var expectedCordY = 2;

        final var expectedErrorMessage = "Planet with ID 123 was not found";

        final var command = UpdatePlanetCommand.with(expectedId.getValue(), expectedName, expectedCordX, expectedCordY);


        when(gateway.findBy(expectedId)).thenReturn(Optional.empty());

        final var exception = assertThrows(NotFoundException.class, () -> useCase.execute(command));

        assertEquals(expectedErrorMessage, exception.getMessage());

        verify(gateway).findBy(eq(expectedId));
        verify(gateway, times(0)).update(any());
    }
}