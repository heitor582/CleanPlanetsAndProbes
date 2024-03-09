package com.study.application.planet.create;

import com.study.IntegrationTest;
import com.study.domain.exceptions.NotificationException;
import com.study.domain.planet.PlanetGateway;
import com.study.infrastructure.planet.persistence.PlanetRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.mock.mockito.SpyBean;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

class CreatePlanetUseCaseIT extends IntegrationTest {
    @SpyBean
    private PlanetGateway gateway;
    @Autowired
    private CreatePlanetUseCase useCase;
    @Autowired
    private PlanetRepository repository;

    @Test
    public void givenAValidCommand_whenCallsCreatePlanet_shouldReturnIt() {
        //given
        final var expectedName = "teste";
        final var expectedCordX = 3;
        final var expectedCordY = 3;

        final var command = CreatePlanetCommand.with(expectedName, expectedCordX, expectedCordY);

        //when
        final var output = useCase.execute(command);
        
        //then
        final var planet = repository.findById(output.id()).get();

        assertNotNull(planet.getId());
        assertEquals(expectedName, planet.getName());
        assertEquals(expectedCordX, planet.getCordX());
        assertEquals(expectedCordY, planet.getCordY());
        assertEquals(planet.getCreatedAt(), planet.getUpdatedAt());
        assertNotNull(planet.getCreatedAt());
        assertNotNull(planet.getUpdatedAt());

        verify(gateway).create(any());
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
    public void givenAnInvalidName_whenCallsCreatePlanet_shouldThrowsNotificationException(
            final String expectedName,
            final int expectedCordX,
            final int expectedCordY,
            final String expectedErrorMessage
    ) {
        final var expectedErrorCount = 1;

        final var command = CreatePlanetCommand.with(expectedName, expectedCordX, expectedCordY);

        final var exception = assertThrows(NotificationException.class, () -> useCase.execute(command));

        assertEquals(expectedErrorCount, exception.getErrors().size());
        assertEquals(expectedErrorMessage, exception.getErrors().get(0).message());

        verify(gateway, times(0)).create(any());
    }
}
