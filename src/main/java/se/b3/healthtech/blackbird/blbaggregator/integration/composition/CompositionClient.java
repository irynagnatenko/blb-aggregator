package se.b3.healthtech.blackbird.blbaggregator.integration.composition;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;
import se.b3.healthtech.blackbird.blbaggregator.domain.composite.CreatePublicationRequest;

@Slf4j
@Service
public class CompositionClient {

    private static final String URI_COMPOSITION_POST = "/api-birdspecies/publication/";

    private final WebClient compositionWebClient;

    public CompositionClient(WebClient compositionWebClient) {
        this.compositionWebClient = compositionWebClient;
    }

    public void postPublication(CreatePublicationRequest request) {
        log.info("Antal containers: {}", request.getContainerList().size());
        log.info("Antal containerObjects {}", request.getContainerObjectList().size());

        compositionWebClient.post()
                .uri(URI_COMPOSITION_POST)
                .body(Mono.just(request), CreatePublicationRequest.class)
                .retrieve()
                .onStatus(HttpStatus::is4xxClientError, error -> Mono.error(new RuntimeException("Composition API not found")))
                .onStatus(HttpStatus::is5xxServerError, error -> Mono.error(new RuntimeException("Server is not responding")))
                .bodyToMono(Void.class)
                .log()
                .doOnError(error -> log.error("An error has occurred {}", error.getMessage()))
                .subscribe();

    }


}


