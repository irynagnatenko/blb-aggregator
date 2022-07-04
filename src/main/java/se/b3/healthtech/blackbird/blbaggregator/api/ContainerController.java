package se.b3.healthtech.blackbird.blbaggregator.api;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import se.b3.healthtech.blackbird.blbaggregator.service.ContainerService;

import java.util.Optional;

@Slf4j
@RestController
@RequestMapping(value = "/api-birdspecies/container")
public class ContainerController {

    private final ContainerService containerService;

    public ContainerController(ContainerService containerService) {
        this.containerService = containerService;
    }

    @Operation(summary = "Add container to the publication")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Successfully added a container",
                    content = {@Content(mediaType = "application/json",
                            schema = @Schema(implementation = Void.class))}),
            @ApiResponse(responseCode = "404", description = "Object not found", content = {@Content}),
            @ApiResponse(responseCode = "500", description = "Internal Server Error", content = {@Content})})
    @PostMapping(path = "/",
            headers = "userName",
            params = {"publicationId", "parentId"},
            produces = "application/json")
    @ResponseStatus(value = HttpStatus.OK)
    public void createContainer(@RequestHeader("userName") String userName,
                                  @RequestParam ("publicationId") String publicationId,
                                  @RequestParam ("parentId") Optional<String> parentId) {
        log.info("in the create container method");
        containerService.addContainer(publicationId, parentId, userName);

    }
}
