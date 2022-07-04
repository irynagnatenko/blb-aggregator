package se.b3.healthtech.blackbird.blbaggregator.integration.composition;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.util.MultiValueMap;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;
import se.b3.healthtech.blackbird.blbaggregator.domain.composite.Publication;

@Slf4j
@Service
public class PublicationClient extends BaseClient {

    private static final String URI_COMPOSITION_POST = "/api-birdspecies/publication/";
    private static final String URI_COMPOSITION_GET = "/api-birdspecies/publication/";

    private final WebClient compositionWebClient;

    public PublicationClient(WebClient compositionWebClient) {
        this.compositionWebClient = compositionWebClient;
    }

    public void postPublication(Publication publication) {

        compositionWebClient.post()
                .uri(URI_COMPOSITION_POST)
                .body(Mono.just(publication), Publication.class)
                .retrieve()
                    .onStatus(HttpStatus::is4xxClientError,
                            error -> Mono.error(new RuntimeException("Publication API not found")))
                    .onStatus(HttpStatus::is5xxServerError,
                            error -> Mono.error(new RuntimeException("Server is not responding")))
                .bodyToMono(Void.class)
                .log()
                .doOnError(error -> log.error("An error has occurred {}", error.getMessage()))
                .subscribe();
    }

    public Publication getLatestPublication(String key) {
        log.info("get latest publication with key:{}", key);
        MultiValueMap<String, String> parameters = createParameterKey(key);
        return compositionWebClient.get()
                .uri(uriBuilder -> uriBuilder
                        .path(URI_COMPOSITION_GET)
                        .queryParams(parameters)
                        .build())
                .retrieve()
                .onStatus(HttpStatus::is4xxClientError,
                        error -> Mono.error(new RuntimeException("Composition API not found getLatestPublication")))
                .onStatus(HttpStatus::is5xxServerError,
                        error -> Mono.error(new RuntimeException("Server is not responding")))
                .bodyToMono(Publication.class)
                .block();
    }

}


