package se.b3.healthtech.blackbird.blbaggregator.api;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import se.b3.healthtech.blackbird.blbaggregator.api.response.PublicationResponse;
import se.b3.healthtech.blackbird.blbaggregator.service.PublicationService;

@Slf4j
@Data
@RestController
@RequestMapping(value = "/api-birdspecies/publication")
public class PublicationController {

    private PublicationService publicationService;

    public PublicationController(PublicationService publicationService) {
        this.publicationService = publicationService;
    }

    @Operation(summary = "Create a publication from template")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Successful created publication by templateId",
                    content = {@Content(mediaType = "application/json",
                            schema = @Schema(implementation = String.class))}),
            @ApiResponse(responseCode = "404", description = "Object not found", content = {@Content}),
            @ApiResponse(responseCode = "500", description = "Internal Server Error", content = {@Content})})
    @PostMapping(path = "/",
            headers = "userName",
            params = {"templateId", "title"},
            produces = "application/json")
    @ResponseStatus(value = HttpStatus.OK)
    public String createPublication(@RequestHeader("userName") String userName,
                                    @RequestParam String templateId,
                                    @RequestParam String title) {
        return publicationService.createPublication(userName, templateId, title);

    }

    @Operation(summary = "Get latest publication for a specific partition key")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Successfully found publication", content = {@Content}),
            @ApiResponse(responseCode = "404", description = "Object not found", content = {@Content}),
            @ApiResponse(responseCode = "500", description = "Internal Server Error", content = {@Content})})
    @GetMapping(value = "/",
            params = "key",
            produces = {"application/json"})
    @ResponseStatus(value = HttpStatus.OK)
    public PublicationResponse getLatestPublication(@RequestParam("key") String key) {
        log.info("in PublicationController - getLatestPublication");
        return publicationService.getLatestPublication(key);
    }

    @Operation(summary = "Delete a publication")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Successfully deleted publication",
                    content = {@Content(mediaType = "application/json",
                            schema = @Schema(implementation = String.class))}),
            @ApiResponse(responseCode = "404", description = "Object not found", content = {@Content}),
            @ApiResponse(responseCode = "500", description = "Internal Server Error", content = {@Content})})
    @DeleteMapping(path = "/",
            headers = "userName",
            params = "key",
            produces = "application/json")
    @ResponseStatus(value = HttpStatus.OK)
    public void deletePublication(@RequestHeader("userName") String userName,
                                  @RequestParam("key") String publicationId) {
        publicationService.deletePublication(userName, publicationId);

    }

}

