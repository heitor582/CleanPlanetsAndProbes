package com.study.infrastructure.api;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.study.ControllerTest;
import com.study.application.probe.create.CreateProbeOutput;
import com.study.application.probe.create.CreateProbeUseCase;
import com.study.application.probe.delete.DeleteProbeUseCase;
import com.study.application.probe.move.MoveProbeOutput;
import com.study.application.probe.move.MoveProbeUseCase;
import com.study.application.probe.retrieve.get.GetProbeByIdUseCase;
import com.study.application.probe.retrieve.get.ProbeOutput;
import com.study.application.probe.retrieve.list.ListProbeUseCase;
import com.study.application.probe.retrieve.list.ProbeListOutput;
import com.study.application.probe.update.UpdateProbeOutput;
import com.study.application.probe.update.UpdateProbeUseCase;
import com.study.domain.exceptions.NotFoundException;
import com.study.domain.exceptions.NotificationException;
import com.study.domain.pagination.Pagination;
import com.study.domain.planet.Planet;
import com.study.domain.planet.PlanetID;
import com.study.domain.probe.Direction;
import com.study.domain.probe.Probe;
import com.study.domain.validation.Error;
import com.study.infrastructure.probe.models.CreateProbeRequest;
import com.study.infrastructure.probe.models.MoveProbeRequest;
import com.study.infrastructure.probe.models.UpdateProbeRequest;
import org.hamcrest.Matchers;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import java.util.List;
import java.util.Objects;

import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.Matchers.nullValue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.argThat;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.patch;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.header;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ControllerTest(controllers = ProbeAPI.class)
public class ProbeApiIT {
    @Autowired
    private MockMvc mvc;
    @Autowired
    private ObjectMapper mapper;
    
    @MockBean
    private DeleteProbeUseCase deleteProbeUseCase;
    @MockBean
    private CreateProbeUseCase createProbeUseCase;
    @MockBean
    private MoveProbeUseCase moveProbeUseCase;
    @MockBean
    private ListProbeUseCase listProbeUseCase;
    @MockBean
    private GetProbeByIdUseCase getProbeByIdUseCase;
    @MockBean
    private UpdateProbeUseCase updateProbeUseCase;

    private final static String API_URL = "/probes";

    @Test
    public void givenAValidCommand_whenCallsCreateProbe_shouldReturnItsIdentifier() throws Exception {
        //given
        final Planet planet = Planet.newPlanet(5,5,"name");
        final var planetId = planet.getId().getValue();

        final var expectedName = "teste";
        final var expectedCordX = 3;
        final var expectedCordY = 3;
        final Long expectedId = 1L;

        final var command = new CreateProbeRequest(expectedName, expectedCordX, expectedCordY, planetId);

        when(createProbeUseCase.execute(any())).thenReturn(CreateProbeOutput.from(expectedId));

        //then
        final var request = MockMvcRequestBuilders.post(API_URL)
                .contentType(MediaType.APPLICATION_JSON)
                .content(mapper.writeValueAsString(command));

        final var response = this.mvc.perform(request);

        //then
        response.andExpect(MockMvcResultMatchers.status().isCreated())
                .andExpect(header().string("Location", "%s/%s".formatted(API_URL,expectedId)))
                .andExpect(header().string("Content-Type", MediaType.APPLICATION_JSON_VALUE))
                .andExpect(jsonPath("$.id", equalTo(expectedId.intValue())));

        verify(createProbeUseCase).execute(argThat(cmd ->
                Objects.equals(expectedName, cmd.name())
                        && Objects.equals(expectedCordX, cmd.cordX())
                        && Objects.equals(expectedCordY, cmd.cordY())
                        && Objects.equals(planetId, cmd.planetId())
        ));
    }

    @ParameterizedTest
    @CsvSource({
            ",3,3,name should not be null",
            "'',3,3,name should not be empty",
            "aa,3,3,name must be between 3 and 255 characters",
            " a,3,3,name must be between 3 and 255 characters",
            "tes,1001,3,coordinate X must be between 1 and 1000",
            "tes,3,1001,coordinate Y must be between 1 and 1000",
    })
    public void givenAnInvalidRequest_whenCallsCreateProbe_shouldReturnNotificationException(
            final String expectedName,
            final int expectedCordX,
            final int expectedCordY,
            final String expectedErrorMessage
    ) throws Exception {
        //given
        final Planet planet = Planet.newPlanet(5,5,"name");
        final var planetId = planet.getId().getValue();

        final var command = new CreateProbeRequest(expectedName, expectedCordX, expectedCordY, planetId);

        when(createProbeUseCase.execute(any())).thenThrow(NotificationException.with(new Error(expectedErrorMessage)));

        //then
        final var request = MockMvcRequestBuilders.post(API_URL)
                .contentType(MediaType.APPLICATION_JSON)
                .content(mapper.writeValueAsString(command));

        final var response = this.mvc.perform(request);

        //then
        response.andExpect(MockMvcResultMatchers.status().isUnprocessableEntity())
                .andExpect(header().string("Location", nullValue()))
                .andExpect(header().string("Content-Type", MediaType.APPLICATION_JSON_VALUE))
                .andExpect(jsonPath("$.errors", hasSize(1)))
                .andExpect(jsonPath("$.errors[0].message", equalTo(expectedErrorMessage)));

        verify(createProbeUseCase).execute(argThat(cmd ->
                Objects.equals(expectedName, cmd.name())
                        && Objects.equals(expectedCordX, cmd.cordX())
                        && Objects.equals(expectedCordY, cmd.cordY())
                        && Objects.equals(planetId, cmd.planetId())
        ));
    }

    @Test
    public void givenAValidId_whenCallsGetProbeById_shouldReturnIt() throws Exception{
        //given
        final Planet planet = Planet.newPlanet(5,5,"name");

        final var expectedName = "teste";
        final var expectedCordX = 3;
        final var expectedCordY = 3;
        final var expectedDirection = Direction.UP;

        final var probe = Probe.newProbe(expectedName, expectedCordX, expectedCordY, planet);
        final var expectedId = probe.getId().getValue();

        when(getProbeByIdUseCase.execute(any())).thenReturn(ProbeOutput.from(probe));

        //when
        final var request = MockMvcRequestBuilders.get("%s/{id}".formatted(API_URL), expectedId)
                .accept(MediaType.APPLICATION_JSON);

        final var response = this.mvc.perform(request);

        //then
        response.andExpect(status().isOk())
                .andExpect(header().string("Content-Type", MediaType.APPLICATION_JSON_VALUE))
                .andExpect(jsonPath("$.id", equalTo(expectedId.intValue())))
                .andExpect(jsonPath("$.name", equalTo(expectedName)))
                .andExpect(jsonPath("$.cord_x", equalTo(expectedCordX)))
                .andExpect(jsonPath("$.cord_y", equalTo(expectedCordY)))
                .andExpect(jsonPath("$.direction", equalTo(expectedDirection.toString())))
                .andExpect(jsonPath("$.planet_id", equalTo(planet.getId().getValue().intValue())))
                .andExpect(jsonPath("$.created_at", equalTo(probe.getCreatedAt().toString())))
                .andExpect(jsonPath("$.updated_at", equalTo(probe.getUpdatedAt().toString())));

        verify(getProbeByIdUseCase).execute(eq(expectedId));
    }

    @Test
    public void givenAInvalidId_whenCallsGetProbeById_shouldReturnNotFound() throws Exception{
        //given
        final var expectedId = PlanetID.from(1L);
        final var expectedErrorMessage = "Probe with ID %s was not found".formatted(expectedId.getValue());

        when(getProbeByIdUseCase.execute(any())).thenThrow(NotFoundException.with(Probe.class, expectedId));

        //when
        final var request = MockMvcRequestBuilders.get("%s/{id}".formatted(API_URL), expectedId.getValue())
                .accept(MediaType.APPLICATION_JSON);

        final var response = this.mvc.perform(request);

        //then
        response.andExpect(status().isNotFound())
                .andExpect(header().string("Content-Type", MediaType.APPLICATION_JSON_VALUE))
                .andExpect(jsonPath("$.message", equalTo(expectedErrorMessage)));

        verify(getProbeByIdUseCase).execute(eq(expectedId.getValue()));
    }

    @Test
    public void givenAValidCommand_whenCallsUpdateProbe_shouldReturnGenreId() throws Exception {
        //given
        final Planet planet = Planet.newPlanet(5,5,"name");

        final var expectedName = "teste";
        final var expectedCordX = 3;
        final var expectedCordY = 3;
        final var expectedDirection = Direction.DOWN;

        final var probe = Probe.newProbe("teste", 1, 1, planet);
        final var expectedId = probe.getId().getValue();

        final var command = new UpdateProbeRequest(expectedName, expectedCordX, expectedCordY, expectedDirection);

        when(updateProbeUseCase.execute(any())).thenReturn(UpdateProbeOutput.from(expectedId));

        // when
        final var request = put("%s/{id}".formatted(API_URL), expectedId)
                .contentType(MediaType.APPLICATION_JSON)
                .content(this.mapper.writeValueAsString(command));

        final var response = this.mvc.perform(request);

        //then
        response
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.header().string("Content-Type", MediaType.APPLICATION_JSON_VALUE))
                .andExpect(MockMvcResultMatchers.jsonPath("$.id", Matchers.equalTo(expectedId.intValue())));

        verify(updateProbeUseCase).execute(argThat(cmd ->
                Objects.equals(expectedName, cmd.name())
                        && Objects.equals(expectedCordX, cmd.cordX())
                        && Objects.equals(expectedCordY, cmd.cordY())
                        && Objects.equals(expectedDirection, cmd.direction())
        ));
    }

    @ParameterizedTest
    @CsvSource({
            "LRM",
            "RLM",
            "MM",
            "RMM",
            "LMM",
            "RRMM",
    })
    public void givenAValidCommand_whenCallsMoveProbe_shouldReturnProbeId(
            final String requestCommand
    ) throws Exception {
        //given
        final Planet planet = Planet.newPlanet(5,5,"name");

        final var probe = Probe.newProbe("teste", 1, 1, planet);
        final var expectedId = probe.getId().getValue();

        final var command = new MoveProbeRequest(requestCommand);

        when(moveProbeUseCase.execute(any())).thenReturn(MoveProbeOutput.from(expectedId));

        // when
        final var request = patch("%s/{id}".formatted(API_URL), expectedId)
                .contentType(MediaType.APPLICATION_JSON)
                .content(this.mapper.writeValueAsString(command));

        final var response = this.mvc.perform(request);

        //then
        response
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.header().string("Content-Type", MediaType.APPLICATION_JSON_VALUE))
                .andExpect(MockMvcResultMatchers.jsonPath("$.id", Matchers.equalTo(expectedId.intValue())));

        verify(moveProbeUseCase).execute(argThat(cmd ->
                Objects.equals(requestCommand, cmd.command())
        ));
    }

    @ParameterizedTest
    @CsvSource({
            ",3,3,name should not be null",
            "'',3,3,name should not be empty",
            "aa,3,3,name must be between 3 and 255 characters",
            " a,3,3,name must be between 3 and 255 characters",
            "tes,1001,3,coordinate X must be between 1 and 1000",
            "tes,3,1001,coordinate Y must be between 1 and 1000",
    })
    public void givenAInvalidRequest_whenCallsUpdatePlanet_NotificationException(
            final String expectedName,
            final int expectedCordX,
            final int expectedCordY,
            final String expectedErrorMessage
    ) throws Exception{
        //given
        final Planet planet = Planet.newPlanet(5,5,"name");

        final Direction expectedDirection = Direction.UP;

        final var probe = Probe.newProbe("teste", 1, 1, planet);
        final var expectedId = probe.getId().getValue();

        final var command = new UpdateProbeRequest(expectedName, expectedCordX, expectedCordY, expectedDirection);

        when(updateProbeUseCase.execute(any())).thenThrow(NotificationException.with(new Error(expectedErrorMessage)));

        // when
        final var request = put("%s/{id}".formatted(API_URL), expectedId)
                .contentType(MediaType.APPLICATION_JSON)
                .content(this.mapper.writeValueAsString(command));

        final var response = this.mvc.perform(request);

        //then
        response
                .andExpect(MockMvcResultMatchers.status().isUnprocessableEntity())
                .andExpect(MockMvcResultMatchers.header().string("Content-Type", MediaType.APPLICATION_JSON_VALUE))
                .andExpect(MockMvcResultMatchers.jsonPath("$.errors", hasSize(1)))
                .andExpect(MockMvcResultMatchers.jsonPath("$.errors[0].message", equalTo(expectedErrorMessage)));

        verify(updateProbeUseCase).execute(argThat(cmd ->
                Objects.equals(expectedName, cmd.name())
                        && Objects.equals(expectedCordX, cmd.cordX())
                        && Objects.equals(expectedCordY, cmd.cordY())
                        && Objects.equals(expectedDirection, cmd.direction())
        ));
    }

    @Test
    public void givenAValidId_whenCallsDeletePlanetById_shouldBeOk() throws Exception {
        //given
        final Planet planet = Planet.newPlanet(5,5,"name");

        final var probe = Probe.newProbe("teste", 1, 1, planet);
        final var expectedId = probe.getId().getValue();

        //when
        final var request = delete("%s/{id}".formatted(API_URL), expectedId)
                .accept(MediaType.APPLICATION_JSON);

        final var response = this.mvc.perform(request);

        // then

        response.andExpect(MockMvcResultMatchers.status().isNoContent());

        verify(deleteProbeUseCase).execute(eq(expectedId));
    }

    @Test
    public void givenValidParams_whenCallsListPlanets_shouldReturnCategories() throws Exception {
        final var expectedPage = 0;
        final var expectedPerPage = 10;
        final var expectedTerms = "tes";
        final var expectedSort = "name";
        final var expectedDirection = "asc";
        final var expectedTotal = 1;
        final Planet planet = Planet.newPlanet(5,5,"name");

        final var probe = Probe.newProbe("teste", 1, 1, planet);
        final var expectedId = probe.getId().getValue();

        when(listProbeUseCase.execute(any())).thenReturn(new Pagination(expectedPage, expectedPerPage, expectedTotal, List.of(ProbeListOutput.from(probe))));

        final var request = MockMvcRequestBuilders.get(API_URL)
                .queryParam("page", String.valueOf(expectedPage))
                .queryParam("perPage", String.valueOf(expectedPerPage))
                .queryParam("sort", expectedSort)
                .queryParam("direction", expectedDirection)
                .queryParam("search", expectedTerms)
                .accept(MediaType.APPLICATION_JSON)
                .contentType(MediaType.APPLICATION_JSON);

        final var response = this.mvc.perform(request).andDo(print());

        response.andExpect(status().isOk())
                .andExpect(jsonPath("$.current_page", equalTo(expectedPage)))
                .andExpect(jsonPath("$.per_page", equalTo(expectedPerPage)))
                .andExpect(jsonPath("$.total", equalTo(expectedTotal)))
                .andExpect(jsonPath("$.items", hasSize(expectedTotal)))
                .andExpect(jsonPath("$.items[0].id", equalTo(expectedId.intValue())))
                .andExpect(jsonPath("$.items[0].name", equalTo(probe.getName())))
                .andExpect(jsonPath("$.items[0].cord_x", equalTo(probe.getCordX())))
                .andExpect(jsonPath("$.items[0].cord_y", equalTo(probe.getCordY())))
                .andExpect(jsonPath("$.items[0].direction", equalTo(probe.getDirection().toString())))
                .andExpect(jsonPath("$.items[0].created_at", equalTo(probe.getCreatedAt().toString())));

        verify(listProbeUseCase, times(1)).execute(argThat(query ->
                Objects.equals(expectedPage, query.page())
                        && Objects.equals(expectedPerPage, query.perPage())
                        && Objects.equals(expectedDirection, query.direction())
                        && Objects.equals(expectedSort, query.sort())
                        && Objects.equals(expectedTerms, query.terms())
        ));
    }
}
