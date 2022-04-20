package se.b3.healthtech.blackbird.blbaggregator.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import lombok.Data;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import se.b3.healthtech.blackbird.blbaggregator.service.PublicationService;

@Data
@RestController
@RequestMapping(value = "/api-birdspecies/publication")
public class PublicationController {

    private PublicationService publicationService;

    @Operation(summary = "Create a publication from template")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Successfully created publication", content = {@Content}),
            @ApiResponse(responseCode = "404", description = "Object not found", content = {@Content}),
            @ApiResponse(responseCode = "500", description = "Internal Server Error", content = {@Content})   })
    @PostMapping(path= "/",
            headers = "userName",
            params = {"templateId", "title"},
            produces = "application/json")
    @ResponseStatus(value = HttpStatus.OK)
    public String createPublication(@RequestHeader("userName") String userName,
                                    @RequestParam String templateId,
                                    @RequestParam String title) {
        publicationService.createPublication(userName, templateId, title);
        return null;
    }

}

