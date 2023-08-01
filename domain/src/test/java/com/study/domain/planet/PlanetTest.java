package com.study.domain.planet;

import com.study.domain.UnitTest;
import com.study.domain.exceptions.NotificationException;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class PlanetTest extends UnitTest {

    @Test
    public void givenAValidParams_whenCallsNewPlanet_thenInstantiateIt() {
        final var expectedName = "teste";
        final int expectedCordX = 5;
        final int expectedCordY = 3;

        final var planet = Planet.newPlanet(expectedCordY, expectedCordX, expectedName);

        assertNotNull(planet);
        assertNotNull(planet.getId());

        assertEquals(expectedName, planet.getName());
        assertEquals(expectedCordX, planet.getCordX());
        assertEquals(expectedCordY, planet.getCordY());
        assertNotNull(planet.getCreatedAt());
        assertNotNull(planet.getUpdatedAt());
        assertEquals(planet.getCreatedAt(), planet.getUpdatedAt());
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
    public void givenAnInvalidParams_whenCallsNewPlanet_shouldReceivedANotificationException(
            final String expectedName,
            final int expectedCordX,
            final int expectedCordY,
            final String expectedErrorMessage
    ){
        final var expectedErrorCount = 1;

        final var exception = assertThrows(NotificationException.class,
                () -> Planet.newPlanet(expectedCordY, expectedCordX, expectedName));

        assertEquals(expectedErrorCount, exception.getErrors().size());
        assertEquals(expectedErrorMessage, exception.getErrors().get(0).message());
    }

    @Test
    public void givenAnInvalidNameWithMoreThan255ValidChars_whenCallsNewPlanet_shouldReceivedANotificationException(){
        final String expectedName = """
                Lorem ipsum dolor sit amet. Et cumque unde ex sint quis hic aliquid veritatis aut soluta quia et aperiam dignissimos aut error iusto. Est dolor dolore cum asperiores debitis eos voluptatem ipsum eum sint officia qui sunt ducimus. Aut voluptate quia eos laboriosam repellendus et quisquam nulla a doloribus sunt. Ex facere cumque ut laudantium voluptas ut culpa aspernatur nam rerum tenetur.
                                
                Et omnis error At adipisci quia qui velit optio ut repudiandae assumenda aut fugit iure eos numquam eligendi. Ut sint aperiam est praesentium eius aut voluptatem ipsa et enim aliquam a voluptates harum sed Quis quia aut minus eius. Aut quidem sunt qui dolor voluptas sit magnam porro ex blanditiis omnis est sequi voluptatem.
                                
                Qui ullam quas ut ipsa corrupti sed galisum Quis sed rerum placeat non quas nostrum vel illum atque ea animi provident. Sed obcaecati aliquam qui eveniet voluptatem ea enim voluptatem ex similique dolorem. Ut doloremque fugit est quidem eligendi eos voluptatem quos cum corrupti aspernatur aut doloribus odit non commodi saepe sed perferendis eius.
                """;
        final var expectedCordX = 3;
        final var expectedCordY = 3;
        final var expectedErrorMessage = "name must be between 3 and 255 characters";
        final var expectedErrorCount = 1;

        final var exception = assertThrows(NotificationException.class,
                () -> Planet.newPlanet(expectedCordY, expectedCordX, expectedName));

        assertEquals(expectedErrorCount, exception.getErrors().size());
        assertEquals(expectedErrorMessage, exception.getErrors().get(0).message());
    }

    @Test
    public void givenAValidParams_whenCallsUpdatePlanet_shouldReturnItUpdated() {
        final var expectedName = "teste";
        final var expectedCordX = 3;
        final var expectedCordY = 3;

        final var planet = Planet.newPlanet(1, 1, "tes");
        final var expectedCreatedAt = planet.getCreatedAt();
        final var expectedUpdatedAt = planet.getUpdatedAt();

        planet.update(expectedCordY, expectedCordX, expectedName);

        assertNotNull(planet);
        assertNotNull(planet.getId());

        assertEquals(expectedName, planet.getName());
        assertEquals(expectedCordX, planet.getCordX());
        assertEquals(expectedCordY, planet.getCordY());
        assertEquals(expectedCreatedAt, planet.getCreatedAt());
        assertTrue(planet.getUpdatedAt().isAfter(expectedUpdatedAt));
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
    public void givenAValidPlanet_whenCallsUpdatePlanetWithNullParams_shouldReturnANotificationException(
            final String expectedName,
            final int expectedCordX,
            final int expectedCordY,
            final String expectedErrorMessage
    ) {
        final var expectedErrorCount = 1;

        final var planet = Planet.newPlanet(1, 1, "tes");

        final var exception = assertThrows(NotificationException.class,
                () -> planet.update(expectedCordY, expectedCordX, expectedName));

        assertEquals(expectedErrorCount, exception.getErrors().size());
        assertEquals(expectedErrorMessage, exception.getErrors().get(0).message());
    }
}