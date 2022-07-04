package se.b3.healthtech.blackbird.blbaggregator.api;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import se.b3.healthtech.blackbird.blbaggregator.service.ContentService;

import java.util.Optional;

@Slf4j
@RestController
@RequestMapping(value = "/api-birdspecies/content")
public class ContentController {

    private final ContentService contentService;

    public ContentController(ContentService contentService) {
        this.contentService = contentService;
    }

    @Operation(summary = "Add content to the publication")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Successfully added a content",
                    content = {@Content(mediaType = "application/json",
                            schema = @Schema(implementation = Void.class))}),
            @ApiResponse(responseCode = "404", description = "Object not found", content = {@Content}),
            @ApiResponse(responseCode = "500", description = "Internal Server Error", content = {@Content})})
    @PostMapping(path = "/",
            produces = "application/json")
    @ResponseStatus(value = HttpStatus.OK)
    public void createContent( @RequestHeader String userName,
                                @RequestParam String publicationId,
                                @RequestParam String containerId,
                                @RequestParam Optional<String> parentId,
                                @RequestBody CreateContentRequest contentRequest) {
        log.info("in the create container method");
        contentService.addContent(userName, publicationId, containerId, parentId, contentRequest );

    }

    @Operation(summary = "Update content")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Successfully updated the content",
                    content = {@Content(mediaType = "application/json",
                            schema = @Schema(implementation = Void.class))}),
            @ApiResponse(responseCode = "404", description = "Object not found", content = {@Content}),
            @ApiResponse(responseCode = "500", description = "Internal Server Error", content = {@Content})})
    @PutMapping(path = "/update/",
            produces = "application/json")
    @ResponseStatus(value = HttpStatus.OK)
    public void updateContent(@RequestHeader String userName,
                              @RequestParam String publicationId,
                              @RequestBody CreateContentRequest contentRequest) {
        log.info("in the update content method");
        contentService.updateContent(userName, publicationId, contentRequest);

    }
}
