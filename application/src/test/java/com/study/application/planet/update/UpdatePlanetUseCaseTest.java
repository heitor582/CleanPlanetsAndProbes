package com.study.application.planet.update;

import com.study.application.UseCaseTest;
import com.study.domain.exceptions.NotFoundException;
import com.study.domain.exceptions.NotificationException;
import com.study.domain.planet.Planet;
import com.study.domain.planet.PlanetGateway;
import com.study.domain.planet.PlanetID;
import org.junit.jupiter.api.Test;
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

        useCase.execute(command);

        verify(gateway).update(argThat(member ->
                Objects.equals(expectedId, member.getId())
                        && Objects.equals(expectedName, member.getName())
                        && Objects.equals(expectedCordX, planet.getCordX())
                        && Objects.equals(expectedCordY, planet.getCordY())
                        && Objects.equals(expectedCreatedAt, member.getCreatedAt())
                        && member.getUpdatedAt().isAfter(updatedAt)
        ));
    }

    @Test
    public void givenAnInvalidName_whenCallsUpdatePlanet_shouldReturnNotification(){
        final var planet = Planet.newPlanet(1,1,"teste");

        final var expectedId = planet.getId();
        final String expectedName = null;
        final var expectedCordX = 2;
        final var expectedCordY = 2;
        final var expectedErrorMessage = "name should not be null";
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
    public void givenAnInvalidCordX_whenCallsUpdatePlanet_shouldReturnNotification(){
        final var planet = Planet.newPlanet(1,1,"teste");

        final var expectedId = planet.getId();
        final var expectedName = "teste1";
        final var expectedCordX = 0;
        final var expectedCordY = 2;

        final var expectedErrorMessage = "coordinate X must be between 1 and 1000";
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
    public void givenAnInvalidCordY_whenCallsUpdatePlanet_shouldReturnNotification(){
        final var planet = Planet.newPlanet(1,1,"teste");

        final var expectedId = planet.getId();
        final var expectedName = "teste1";
        final var expectedCordX = 2;
        final var expectedCordY = 1002;

        final var expectedErrorMessage = "coordinate Y must be between 1 and 1000";
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