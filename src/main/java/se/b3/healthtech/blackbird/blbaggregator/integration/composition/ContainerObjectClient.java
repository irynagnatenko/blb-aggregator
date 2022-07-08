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
import se.b3.healthtech.blackbird.blbaggregator.domain.content.Content;

import java.util.List;

@Slf4j
@Service
public class ContainerObjectClient extends BaseClient {

    private static final String URI_CONTAINER_OBJECT_GET_ALL = "api-blackbird/containerobject/all/";
    private static final String URI_CONTAINER_OBJECT_POST_ALL = "api-blackbird/containerobject/all";
    private static final String URI_CONTAINER_OBJECT_ADD_ONE = "api-blackbird/containerobject/";
    private static final String URI_CONTAINER_OBJECT_GET_ONE = "api-blackbird/containerobject/";
    private static final String URI_CONTAINER_OBJECT_DELETE = "api-blackbird/containerobject/";


    private final WebClient compositionWebClient;

    public ContainerObjectClient(WebClient compositionWebClient) {
        this.compositionWebClient = compositionWebClient;
    }

    public List<ContainerObject> getLatestContainerObjects(String key) {
        log.info("get latest container object with key:{}", key);

        MultiValueMap<String, String> parameters = createParameterKey(key);

        return compositionWebClient.get()
                .uri(uriBuilder -> uriBuilder
                        .path(URI_CONTAINER_OBJECT_GET_ALL)
                        .queryParams(parameters)
                        .build())
                .retrieve()
                .onStatus(HttpStatus::is4xxClientError,
                        error -> Mono.error(new RuntimeException("getLatestContainerObjects API not found getLatestContainerObjects")))
                .onStatus(HttpStatus::is5xxServerError,
                        error -> Mono.error(new RuntimeException("Server is not responding")))
                .bodyToMono(new ParameterizedTypeReference<List<ContainerObject>>() {
                })
                .block();

    }

    public void postContainerObjects(String key, List<ContainerObject> containerObjectList) {
        log.info("post container object with key:{}", key);

        MultiValueMap<String, String> parameters = createParameterKey(key);

        compositionWebClient.post()
                .uri(uriBuilder -> uriBuilder
                        .path(URI_CONTAINER_OBJECT_POST_ALL)
                        .queryParams(parameters)
                        .build())
                .body(Mono.just(containerObjectList), Publication.class)
                .retrieve()
                .onStatus(HttpStatus::is4xxClientError,
                        error -> Mono.error(new RuntimeException("postContainerObjects API not found")))
                .onStatus(HttpStatus::is5xxServerError,
                        error -> Mono.error(new RuntimeException("Server is not responding")))
                .bodyToMono(Void.class)
                .log()
                .doOnError(error -> log.error("An error has occurred {}", error.getMessage()))
                .block();
    }

    public void addContainerObject(String key, ContainerObject containerObject) {

        MultiValueMap<String, String> parameters = createParameterKey(key);

        compositionWebClient.post()
                .uri(uriBuilder -> uriBuilder
                        .path(URI_CONTAINER_OBJECT_ADD_ONE)
                        .queryParams(parameters)
                        .build())
                .body(Mono.just(containerObject), ContainerObject.class)
                .retrieve()
                .onStatus(HttpStatus::is4xxClientError,
                        error -> Mono.error(new RuntimeException("Add container object API not found")))
                .onStatus(HttpStatus::is5xxServerError,
                        error -> Mono.error(new RuntimeException("Server is not responding")))
                .bodyToMono(Void.class)
                .log()
                .doOnError(error -> log.error("An error has occurred {}", error.getMessage()))
                .block();
    }

    public ContainerObject getContainerObject(String key, String containerObjectId ) {
        log.info("get latest container object with key:{}", key);

        MultiValueMap<String, String> parameters = createParameterKey(key, containerObjectId);

        return compositionWebClient.get()
                .uri(uriBuilder -> uriBuilder
                        .path(URI_CONTAINER_OBJECT_GET_ONE)
                        .queryParams(parameters)
                        .build())
                .retrieve()
                .onStatus(HttpStatus::is4xxClientError,
                        error -> Mono.error(new RuntimeException("getContainerObject API not found ")))
                .onStatus(HttpStatus::is5xxServerError,
                        error -> Mono.error(new RuntimeException("Server is not responding")))
                .bodyToMono(ContainerObject.class)
                .block();
    }

    // Ny metod - deleteContainerObjects
    //Inparameter till metoden:
    //String publicationId
    //String userName
    //List<ContainerObjects>
    //Anropar endpoint - deleteContainerObjects
    //returnerar void
    public void deleteContainerObject(String key, String userName, List<ContainerObject> containerObjectList) {
        log.info("delete latest container object with key:{}", key);

        MultiValueMap<String, String> parameters = createParameterKey(key);

        compositionWebClient.post()
                .uri(uriBuilder -> uriBuilder
                        .path(URI_CONTAINER_OBJECT_DELETE)
                        .queryParams(parameters)
                        .build())
                .header("userName", userName)
                .body(Mono.just(containerObjectList), List.class)
                .retrieve()
                .onStatus(HttpStatus::is4xxClientError,
                        error -> Mono.error(new RuntimeException("Delete container object API not found")))
                .onStatus(HttpStatus::is5xxServerError,
                        error -> Mono.error(new RuntimeException("Server is not responding")))
                .bodyToMono(Void.class)
                .log()
                .doOnError(error -> log.error("An error has occurred {}", error.getMessage()))
                .block();
    }

}


