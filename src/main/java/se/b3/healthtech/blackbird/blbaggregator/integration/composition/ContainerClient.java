package se.b3.healthtech.blackbird.blbaggregator.integration.composition;

import lombok.extern.slf4j.Slf4j;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.util.MultiValueMap;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;
import se.b3.healthtech.blackbird.blbaggregator.domain.composite.Container;
import se.b3.healthtech.blackbird.blbaggregator.domain.composite.Publication;

import java.util.List;

@Slf4j
@Service
public class ContainerClient extends BaseClient {

    private static final String URI_CONTAINER_GET_ALL = "/api-birdspecies/container/all/";
    private static final String URI_CONTAINER_ADD_ALL = "/api-birdspecies/container/all/";
    private static final String URI_CONTAINER_ADD_ONE = "/api-birdspecies/container/";
    private static final String URI_CONTAINER_GET_ONE = "/api-birdspecies/container/";


    private final WebClient compositionWebClient;

    public ContainerClient(WebClient compositionWebClient) {
        this.compositionWebClient = compositionWebClient;
    }

    public List<Container> getLatestContainers(String key) {
        log.info("get latest container with key:{}", key);
        MultiValueMap<String, String> parameters = createParameterKey(key);

        return compositionWebClient.get()
                .uri(uriBuilder -> uriBuilder
                        .path(URI_CONTAINER_GET_ALL)
                        .queryParams(parameters)
                        .build())
                .retrieve()
                .onStatus(HttpStatus::is4xxClientError,
                        error -> Mono.error(new RuntimeException("Container API not found getLatestContainers")))
                .onStatus(HttpStatus::is5xxServerError,
                        error -> Mono.error(new RuntimeException("Server is not responding")))
                .bodyToMono(new ParameterizedTypeReference<List<Container>>() {
                })
                .block();

    }

    public void postContainers(String key, List<Container> containerList) {

        MultiValueMap<String, String> parameters = createParameterKey(key);

        compositionWebClient.post()
                .uri(uriBuilder -> uriBuilder
                        .path(URI_CONTAINER_ADD_ALL)
                        .queryParams(parameters)
                        .build())
                .body(Mono.just(containerList), Publication.class)
                .retrieve()
                .onStatus(HttpStatus::is4xxClientError,
                        error -> Mono.error(new RuntimeException("Add containers API not found")))
                .onStatus(HttpStatus::is5xxServerError,
                        error -> Mono.error(new RuntimeException("Server is not responding")))
                .bodyToMono(Void.class)
                .log()
                .doOnError(error -> log.error("An error has occurred {}", error.getMessage()))
                .block();

    }

    public void addContainer(String key, Container container) {

        MultiValueMap<String, String> parameters = createParameterKey(key);

        compositionWebClient.post()
                .uri(uriBuilder -> uriBuilder
                        .path(URI_CONTAINER_ADD_ONE)
                        .queryParams(parameters)
                        .build())
                .body(Mono.just(container), Container.class)
                .retrieve()
                .onStatus(HttpStatus::is4xxClientError,
                        error -> Mono.error(new RuntimeException("Add container API not found")))
                .onStatus(HttpStatus::is5xxServerError,
                        error -> Mono.error(new RuntimeException("Server is not responding")))
                .bodyToMono(Void.class)
                .log()
                .doOnError(error -> log.error("An error has occurred {}", error.getMessage()))
                .block();
    }

    // for addContent
    public Container getLatestContainer(String key, String containerId) {
        log.info("get latest container with key:{}", key);

        MultiValueMap<String, String> parameters = createParameterKey(key, containerId);

        return compositionWebClient.get()
                .uri(uriBuilder -> uriBuilder
                        .path(URI_CONTAINER_GET_ONE)
                        .queryParams(parameters)
                        .build())
                .retrieve()
                .onStatus(HttpStatus::is4xxClientError,
                        error -> Mono.error(new RuntimeException("Container API not found getLatestContainer")))
                .onStatus(HttpStatus::is5xxServerError,
                        error -> Mono.error(new RuntimeException("Server is not responding")))
                .bodyToMono(new ParameterizedTypeReference<Container>() {
                })
                .block();
    }


}
