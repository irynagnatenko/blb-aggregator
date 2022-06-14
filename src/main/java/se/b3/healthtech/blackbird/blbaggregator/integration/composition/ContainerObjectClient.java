package se.b3.healthtech.blackbird.blbaggregator.integration.composition;

import lombok.extern.slf4j.Slf4j;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.util.MultiValueMap;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;
import se.b3.healthtech.blackbird.blbaggregator.domain.composite.ContainerObject;
import se.b3.healthtech.blackbird.blbaggregator.domain.composite.Publication;

import java.util.List;

@Slf4j
@Service
public class ContainerObjectClient extends BaseClient {

    private static final String URI_CONTAINER_OBJECT_GET = "api-blackbird/containerobject/latest/all/";
    private static final String URI_CONTAINER_OBJECT_POST = "api-blackbird/containerobject/";

    private final WebClient compositionWebClient;

    public ContainerObjectClient(WebClient compositionWebClient) {
        this.compositionWebClient = compositionWebClient;
    }

    public List<ContainerObject> getLatestContainerObjects(String key) {
        log.info("get latest container object with key:{}", key);

        MultiValueMap<String, String> parameters = createParameterKey(key);

        return compositionWebClient.get()
                .uri(uriBuilder -> uriBuilder
                        .path(URI_CONTAINER_OBJECT_GET)
                        .queryParams(parameters)
                        .build())
                .retrieve()
                .onStatus(HttpStatus::is4xxClientError,
                        error -> Mono.error(new RuntimeException("Compmvn clean installosition API not found getLatestContainerObjects")))
                .onStatus(HttpStatus::is5xxServerError,
                        error -> Mono.error(new RuntimeException("Server is not responding")))
                .bodyToMono(new ParameterizedTypeReference<List<ContainerObject>>() {})
                .block();

    }

    public void postContainerObjects(String key, List<ContainerObject> containerObjectList) {

        MultiValueMap<String, String> parameters = createParameterKey(key);

        compositionWebClient.post()
                .uri(uriBuilder -> uriBuilder
                .path(URI_CONTAINER_OBJECT_POST)
                .queryParams(parameters)
                .build())
                .body(Mono.just(containerObjectList), Publication.class)
                .retrieve()
                .onStatus(HttpStatus::is4xxClientError,
                        error -> Mono.error(new RuntimeException("ContainerObjects API not found")))
                .onStatus(HttpStatus::is5xxServerError,
                        error -> Mono.error(new RuntimeException("Server is not responding")))
                .bodyToMono(Void.class)
                .log()
                .doOnError(error -> log.error("An error has occurred {}", error.getMessage()))
                .block();

    }

}
