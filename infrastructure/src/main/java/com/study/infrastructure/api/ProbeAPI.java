package com.study.infrastructure.api;

import com.study.application.probe.move.MoveProbeOutput;
import com.study.application.probe.update.UpdateProbeOutput;
import com.study.domain.pagination.Pagination;
import com.study.infrastructure.probe.models.CreateProbeRequest;
import com.study.infrastructure.probe.models.MoveProbeRequest;
import com.study.infrastructure.probe.models.ProbeListResponse;
import com.study.infrastructure.probe.models.ProbeResponse;
import com.study.infrastructure.probe.models.UpdateProbeRequest;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;

@RequestMapping(value = "probes")
@Tag(name = "Probes")
public interface ProbeAPI {
    @PostMapping(
            consumes = MediaType.APPLICATION_JSON_VALUE,
            produces = MediaType.APPLICATION_JSON_VALUE
    )
    @Operation(summary = "Create a new Probe")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Created successfully"),
            @ApiResponse(responseCode = "422", description = "A validation error was thrown"),
            @ApiResponse(responseCode = "500", description = "An internal server error was thrown"),
    })
    ResponseEntity<?> create(@RequestBody final CreateProbeRequest input);

    @GetMapping(
            produces = MediaType.APPLICATION_JSON_VALUE
    )
    @Operation(summary = "List all Probes paginated")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Listed successfully"),
            @ApiResponse(responseCode = "500", description = "An internal server error was thrown"),
    })
    Pagination<ProbeListResponse> list(
            @RequestParam(name = "search", required = false, defaultValue = "") final String search,
            @RequestParam(name = "page", required = false, defaultValue = "0") final int page,
            @RequestParam(name = "perPage", required = false, defaultValue = "10") final int perPage,
            @RequestParam(name = "sort", required = false, defaultValue = "name") final String sort,
            @RequestParam(name = "direction", required = false, defaultValue = "asc") final String direction
    );

    @GetMapping(value = "/{id}",
            produces = MediaType.APPLICATION_JSON_VALUE
    )
    @Operation(summary = "Get a Probe by id")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Probe retrieved successfully"),
            @ApiResponse(responseCode = "404", description = "Probe was not found"),
            @ApiResponse(responseCode = "500", description = "An internal server error was thrown"),
    })
    ProbeResponse getById(@PathVariable(name = "id") final Long id);

    @DeleteMapping(value = "/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @Operation(summary = "Delete a Probe by id")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = "Probe deleted successfully"),
            @ApiResponse(responseCode = "500", description = "An internal server error was thrown"),
    })
    void deleteById(@PathVariable(name = "id") final Long id);

    @PutMapping(value = "/{id}",
            consumes = MediaType.APPLICATION_JSON_VALUE,
            produces = MediaType.APPLICATION_JSON_VALUE
    )
    @Operation(summary = "Update a Probe by id")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Probe update successfully"),
            @ApiResponse(responseCode = "404", description = "Probe was not found"),
            @ApiResponse(responseCode = "422", description = "A validation error was thrown"),
            @ApiResponse(responseCode = "500", description = "An internal server error was thrown"),
    })
    ResponseEntity<UpdateProbeOutput> updateById(@PathVariable(name = "id") final Long id, @RequestBody final UpdateProbeRequest input);

    @PatchMapping(value = "/{id}",
            consumes = MediaType.APPLICATION_JSON_VALUE,
            produces = MediaType.APPLICATION_JSON_VALUE
    )

    @Operation(summary = "Move a Probe by id and command")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Probe moved successfully"),
            @ApiResponse(responseCode = "404", description = "Probe was not found"),
            @ApiResponse(responseCode = "422", description = "A validation error was thrown"),
            @ApiResponse(responseCode = "500", description = "An internal server error was thrown"),
    })
    ResponseEntity<MoveProbeOutput> move(@PathVariable(name = "id") final Long id, @RequestBody final MoveProbeRequest input);
}
