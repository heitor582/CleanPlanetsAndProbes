package com.study.domain.probe;

import com.study.domain.UnitTest;
import com.study.domain.exceptions.NotificationException;
import com.study.domain.planet.Planet;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.junit.jupiter.params.provider.EnumSource;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class ProbeTest extends UnitTest {
    @Test
    public void givenAValidParams_whenCallsNewProbe_thenInstantiateIt() {
        final String expectedName = "teste";
        final int expectedCordX = 5;
        final int expectedCordY = 3;
        final Direction expectedDirection = Direction.UP;
        final Planet planet = Planet.newPlanet(3, 5, "tes");

        final var probe = Probe.newProbe(expectedName, expectedCordX, expectedCordY, planet);

        assertNotNull(probe);
        assertNotNull(probe.getId());

        assertEquals(expectedName, probe.getName());
        assertEquals(expectedCordX, probe.getCordX());
        assertEquals(expectedCordY, probe.getCordY());
        assertEquals(expectedDirection, probe.getDirection());
        assertEquals(planet.getId(), probe.getPlanet().getId());
        assertNotNull(probe.getCreatedAt());
        assertNotNull(probe.getUpdatedAt());
        assertEquals(probe.getCreatedAt(), probe.getUpdatedAt());
    }

    @ParameterizedTest
    @CsvSource({"4,5,LEFT", "5,-5,LEFT", "-5,5,RIGHT", "5,4,RIGHT"})
    public void givenAValidParams_whenCallsMoveProbe_shouldMoveCordX(final int expectedCordX, final int initialCordX, final Direction direction) {
        final String expectedName = "teste";
        final int expectedCordY = 3;
        final Planet planet = Planet.newPlanet(3, 5, "tes");

        final var probe = Probe.newProbe(expectedName, initialCordX, expectedCordY, planet);
        probe.update(probe.getName(), probe.getCordX(), probe.getCordY(), direction);

        final var expectedCreatedAt = probe.getCreatedAt();
        final var expectedUpdatedAt = probe.getUpdatedAt();

        probe.move(planet.getCordX(), planet.getCordY(), List.of(), List.of());

        assertNotNull(probe);
        assertNotNull(probe.getId());

        assertEquals(expectedName, probe.getName());
        assertEquals(expectedCordX, probe.getCordX());
        assertEquals(expectedCordY, probe.getCordY());
        assertEquals(direction, probe.getDirection());
        assertEquals(planet.getId(), probe.getPlanet().getId());
        assertEquals(expectedCreatedAt, probe.getCreatedAt());
        assertNotNull(probe.getCreatedAt());
        assertNotNull(probe.getUpdatedAt());
        assertTrue(probe.getUpdatedAt().isAfter(expectedUpdatedAt));
        assertNotEquals(probe.getCreatedAt(), probe.getUpdatedAt());
    }

    @ParameterizedTest
    @CsvSource({"RIGHT, 4", "UP, 4", "LEFT, 2", "DOWN, 2"})
    public void givenAValidParams_whenCallsMoveProbeButHasOtherProbesInTheWay_shouldNotUpdated(final Direction expectedDirection, final int cordInTheWay) {
        final String expectedName = "teste";
        final int expectedCordY = 3;
        final int expectedCordX = 3;
        final Planet planet = Planet.newPlanet(5, 5, "tes");

        final var probe = Probe.newProbe(expectedName, expectedCordX, expectedCordY, planet);
        probe.update(probe.getName(), probe.getCordX(), probe.getCordY(), expectedDirection);

        final var expectedCreatedAt = probe.getCreatedAt();
        final var expectedUpdatedAt = probe.getUpdatedAt();

        probe.move(planet.getCordX(), planet.getCordY(), List.of(cordInTheWay), List.of(cordInTheWay));

        assertNotNull(probe);
        assertNotNull(probe.getId());

        assertEquals(expectedName, probe.getName());
        assertEquals(expectedCordX, probe.getCordX());
        assertEquals(expectedCordY, probe.getCordY());
        assertEquals(expectedDirection, probe.getDirection());
        assertEquals(planet.getId(), probe.getPlanet().getId());
        assertEquals(expectedCreatedAt, probe.getCreatedAt());
        assertNotNull(probe.getCreatedAt());
        assertNotNull(probe.getUpdatedAt());
        assertTrue(probe.getUpdatedAt().isAfter(expectedUpdatedAt));
        assertNotEquals(probe.getCreatedAt(), probe.getUpdatedAt());
    }

    @ParameterizedTest
    @CsvSource({"-5,5,UP", "3,2,UP", "2,3,DOWN", "5,-5,DOWN"})
    public void givenAValidParams_whenCallsMoveProbe_shouldMoveCordY(final int expectedCordY, final int initialCordY, final Direction direction) {
        final String expectedName = "teste";
        final int expectedCordX = 5;
        final Planet planet = Planet.newPlanet(5, 5, "tes");

        final var probe = Probe.newProbe(expectedName, expectedCordX, initialCordY, planet);
        probe.update(probe.getName(), probe.getCordX(), probe.getCordY(), direction);

        final var expectedCreatedAt = probe.getCreatedAt();
        final var expectedUpdatedAt = probe.getUpdatedAt();

        probe.move(planet.getCordX(), planet.getCordY(), List.of(), List.of());

        assertNotNull(probe);
        assertNotNull(probe.getId());

        assertEquals(expectedName, probe.getName());
        assertEquals(expectedCordX, probe.getCordX());
        assertEquals(expectedCordY, probe.getCordY());
        assertEquals(direction, probe.getDirection());
        assertEquals(planet.getId(), probe.getPlanet().getId());
        assertEquals(expectedCreatedAt, probe.getCreatedAt());
        assertNotNull(probe.getCreatedAt());
        assertNotNull(probe.getUpdatedAt());
        assertTrue(probe.getUpdatedAt().isAfter(expectedUpdatedAt));
        assertNotEquals(probe.getCreatedAt(), probe.getUpdatedAt());
    }

    @ParameterizedTest
    @EnumSource(Direction.class)
    public void givenAValidProbe_whenCallsTurnLeft_shouldUpdate(final Direction direction) {
        final String expectedName = "teste";
        final int expectedCordX = 5;
        final int expectedCordY = 3;
        final Direction expectedDirection = direction.turnLeft();
        final Planet planet = Planet.newPlanet(3, 5, "tes");

        final var probe = Probe.newProbe(expectedName, expectedCordX, expectedCordY, planet);
        probe.update(probe.getName(), probe.getCordX(), probe.getCordY(), direction);
        final var expectedCreatedAt = probe.getCreatedAt();
        final var expectedUpdatedAt = probe.getUpdatedAt();

        probe.turnLeft();

        assertNotNull(probe);
        assertNotNull(probe.getId());

        assertEquals(expectedName, probe.getName());
        assertEquals(expectedCordX, probe.getCordX());
        assertEquals(expectedCordY, probe.getCordY());
        assertEquals(expectedDirection, probe.getDirection());
        assertEquals(planet.getId(), probe.getPlanet().getId());
        assertEquals(expectedCreatedAt, probe.getCreatedAt());
        assertNotNull(probe.getCreatedAt());
        assertNotNull(probe.getUpdatedAt());
        assertTrue(probe.getUpdatedAt().isAfter(expectedUpdatedAt));
        assertNotEquals(probe.getCreatedAt(), probe.getUpdatedAt());
    }

    @ParameterizedTest
    @EnumSource(Direction.class)
    public void givenAValidProbe_whenCallsTurnRight_shouldUpdate(final Direction direction) {
        final String expectedName = "teste";
        final int expectedCordX = 5;
        final int expectedCordY = 3;
        final Direction expectedDirection = direction.turnRight();
        final Planet planet = Planet.newPlanet(3, 5, "tes");

        final var probe = Probe.newProbe(expectedName, expectedCordX, expectedCordY, planet);
        probe.update(probe.getName(), probe.getCordX(), probe.getCordY(), direction);
        final var expectedCreatedAt = probe.getCreatedAt();
        final var expectedUpdatedAt = probe.getUpdatedAt();

        probe.turnRight();

        assertNotNull(probe);
        assertNotNull(probe.getId());

        assertEquals(expectedName, probe.getName());
        assertEquals(expectedCordX, probe.getCordX());
        assertEquals(expectedCordY, probe.getCordY());
        assertEquals(expectedDirection, probe.getDirection());
        assertEquals(planet.getId(), probe.getPlanet().getId());
        assertEquals(expectedCreatedAt, probe.getCreatedAt());
        assertNotNull(probe.getCreatedAt());
        assertNotNull(probe.getUpdatedAt());
        assertTrue(probe.getUpdatedAt().isAfter(expectedUpdatedAt));
        assertNotEquals(probe.getCreatedAt(), probe.getUpdatedAt());
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
    public void givenAnInvalidName_whenCallsNewProbe_shouldReceivedANotificationException(
            final String expectedName,
            final int expectedCordX,
            final int expectedCordY,
            final String expectedErrorMessage
    ){
        final Planet planet = Planet.newPlanet(5, 5, "tes");
        final var expectedErrorCount = 1;

        final var exception = assertThrows(NotificationException.class,
                () -> Probe.newProbe(expectedName, expectedCordX, expectedCordY, planet));

        assertEquals(expectedErrorCount, exception.getErrors().size());
        assertEquals(expectedErrorMessage, exception.getErrors().get(0).message());
    }

    @Test
    public void givenAnInvalidNameWithMoreThan255ValidChars_whenCallsNewProbe_shouldReceivedANotificationException(){
        final String expectedName = """
                Lorem ipsum dolor sit amet. Et cumque unde ex sint quis hic aliquid veritatis aut soluta quia et aperiam dignissimos aut error iusto. Est dolor dolore cum asperiores debitis eos voluptatem ipsum eum sint officia qui sunt ducimus. Aut voluptate quia eos laboriosam repellendus et quisquam nulla a doloribus sunt. Ex facere cumque ut laudantium voluptas ut culpa aspernatur nam rerum tenetur.
                                
                Et omnis error At adipisci quia qui velit optio ut repudiandae assumenda aut fugit iure eos numquam eligendi. Ut sint aperiam est praesentium eius aut voluptatem ipsa et enim aliquam a voluptates harum sed Quis quia aut minus eius. Aut quidem sunt qui dolor voluptas sit magnam porro ex blanditiis omnis est sequi voluptatem.
                                
                Qui ullam quas ut ipsa corrupti sed galisum Quis sed rerum placeat non quas nostrum vel illum atque ea animi provident. Sed obcaecati aliquam qui eveniet voluptatem ea enim voluptatem ex similique dolorem. Ut doloremque fugit est quidem eligendi eos voluptatem quos cum corrupti aspernatur aut doloribus odit non commodi saepe sed perferendis eius.
                """;
        final int expectedCordX = 3;
        final int expectedCordY = 3;
        final Planet planet = Planet.newPlanet(5, 5, "tes");

        final var expectedErrorMessage = "name must be between 3 and 255 characters";
        final var expectedErrorCount = 1;

        final var exception = assertThrows(NotificationException.class,
                () -> Probe.newProbe(expectedName, expectedCordX, expectedCordY, planet));

        assertEquals(expectedErrorCount, exception.getErrors().size());
        assertEquals(expectedErrorMessage, exception.getErrors().get(0).message());
    }


    @Test
    public void givenAValidParams_whenCallsUpdateProbe_shouldReturnItUpdated() {
        final String expectedName = "teste";
        final int expectedCordX = 3;
        final int expectedCordY = 3;
        final Planet planet = Planet.newPlanet(3, 3, "tes");
        final Direction direction = Direction.DOWN;

        final var probe = Probe.newProbe("tes", 1, 2, planet);
        final var expectedCreatedAt = probe.getCreatedAt();
        final var expectedUpdatedAt = probe.getUpdatedAt();

        probe.update(expectedName, expectedCordX, expectedCordY, direction);

        assertNotNull(probe);
        assertNotNull(probe.getId());

        assertEquals(expectedName, probe.getName());
        assertEquals(expectedCordX, probe.getCordX());
        assertEquals(expectedCordY, probe.getCordY());
        assertEquals(planet.getId(), probe.getPlanet().getId());
        assertEquals(direction, probe.getDirection());
        assertEquals(expectedCreatedAt, probe.getCreatedAt());
        assertTrue(probe.getUpdatedAt().isAfter(expectedUpdatedAt));
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
    public void givenAValidProbe_whenCallsUpdateProbeWithInvalidName_shouldReturnANotificationException(
            final String expectedName,
            final int expectedCordX,
            final int expectedCordY,
            final String expectedErrorMessage
    ) {
        final Planet planet = Planet.newPlanet(3, 3, "tes");
        final Direction direction = Direction.DOWN;

        final var expectedErrorCount = 1;

        final var probe = Probe.newProbe("tes", 1, 2, planet);

        final var exception = assertThrows(NotificationException.class,
                () -> probe.update(expectedName, expectedCordX, expectedCordY, direction));

        assertEquals(expectedErrorCount, exception.getErrors().size());
        assertEquals(expectedErrorMessage, exception.getErrors().get(0).message());
    }

    @Test
    public void givenAValidProbe_whenCallsUpdateProbeWithNullDirection_shouldReturnANotificationException() {
        final String expectedName = "teste";
        final var expectedCordX = 3;
        final var expectedCordY = 3;
        final Planet planet = Planet.newPlanet(3, 3, "tes");
        final Direction direction = null;

        final var expectedErrorMessage =  "direction should not be null";
        final var expectedErrorCount = 1;

        final var probe = Probe.newProbe("tes", 1, 2, planet);

        final var exception = assertThrows(NotificationException.class,
                () -> probe.update(expectedName, expectedCordX, expectedCordY, direction));

        assertEquals(expectedErrorCount, exception.getErrors().size());
        assertEquals(expectedErrorMessage, exception.getErrors().get(0).message());
    }

    @ParameterizedTest
    @CsvSource({
            "5,5,4,5",
            "-5,5,4,5",
            "-5,5,5,4",
            "5,5,5,4",
            "5,5,4,4",
            "-5,-5,4,4",
    })
    public void givenAValidProbe_whenCallsCreateProbeWithCordsGreaterThanPlanet_shouldReturnANotificationException(
            final int expectedCordX,
            final int expectedCordY,
            final int cordYPlanet,
            final int cordXPlanet
    ) {
        final String expectedName = "teste";
        final Planet planet = Planet.newPlanet(cordYPlanet, cordXPlanet, "tes");

        final var expectedErrorMessage = "does not have this position to land the ship";
        final var expectedErrorCount = 1;

        final var exception = assertThrows(NotificationException.class,
                () -> Probe.newProbe(expectedName, expectedCordX, expectedCordY, planet));

        assertEquals(expectedErrorCount, exception.getErrors().size());
        assertEquals(expectedErrorMessage, exception.getErrors().get(0).message());
    }

    @Test
    public void givenAValidProbeWithNegativeCords_whenCallsCreateProbe_shouldReturnIt() {
        final String expectedName = "teste";
        final var expectedCordX = -3;
        final var expectedCordY = -3;
        final Planet planet = Planet.newPlanet(3, 3, "tes");
        final Direction expectedDirection = Direction.UP;

        final var probe = Probe.newProbe(expectedName, expectedCordX, expectedCordY, planet);

        assertNotNull(probe);
        assertNotNull(probe.getId());

        assertEquals(expectedName, probe.getName());
        assertEquals(expectedCordX, probe.getCordX());
        assertEquals(expectedCordY, probe.getCordY());
        assertEquals(expectedDirection, probe.getDirection());
        assertEquals(planet.getId(), probe.getPlanet().getId());
        assertNotNull(probe.getCreatedAt());
        assertNotNull(probe.getUpdatedAt());
        assertEquals(probe.getCreatedAt(), probe.getUpdatedAt());
    }
}
