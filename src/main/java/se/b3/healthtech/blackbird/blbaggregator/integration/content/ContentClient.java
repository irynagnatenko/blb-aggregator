package se.b3.healthtech.blackbird.blbaggregator.integration.content;

import lombok.extern.slf4j.Slf4j;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;
import se.b3.healthtech.blackbird.blbaggregator.domain.composite.Container;
import se.b3.healthtech.blackbird.blbaggregator.domain.content.Content;
import se.b3.healthtech.blackbird.blbaggregator.domain.content.CreateContentRequest;

import java.util.List;

@Slf4j
@Service
public class ContentClient {

    private static final String URI_CONTENT_POST = "/api-birdspecies/content/";
    private static final String URI_CONTENT_GET = "/api-birdspecies/content/latest/all/";


    private WebClient contentWebClient;

    public ContentClient(WebClient contentWebClient) {
        this.contentWebClient = contentWebClient;
    }

    public void postContent(String key, List<Content> contentList) {
        MultiValueMap<String, String> parameters = createParameterKey(key);

        contentWebClient.post()
                .uri(uriBuilder -> uriBuilder
                        .path(URI_CONTENT_POST)
                        .queryParams(parameters)
                        .build())
                .body(Mono.just(contentList), Content.class)
                .retrieve()
                .onStatus(HttpStatus::is4xxClientError, error -> Mono.error(new RuntimeException("Content API not found")))
                .onStatus(HttpStatus::is5xxServerError, error -> Mono.error(new RuntimeException("Content Server is not responding")))
                .bodyToMono(Void.class)
                .log()
                .doOnError(error -> log.error("An error has occurred {}", error.getMessage()))
                .subscribe();

    }
    MultiValueMap<String, String> createParameterKey(String key) {
        MultiValueMap<String, String> parameters = new LinkedMultiValueMap<>();
        parameters.add("key", key);
        return parameters;
    }

    public List<Content> getLatestContent(String key) {
        log.info("get latest content with key:{}", key);

        MultiValueMap<String, String> parameters = createParameterKey(key);

        return contentWebClient.get()
                .uri(uriBuilder -> uriBuilder
                        .path(URI_CONTENT_GET)
                        .queryParams(parameters)
                        .build())
                .retrieve()
                .onStatus(HttpStatus::is4xxClientError,
                        error -> Mono.error(new RuntimeException("Content API not found")))
                .onStatus(HttpStatus::is5xxServerError,
                        error -> Mono.error(new RuntimeException("Content Server is not responding")))
                .bodyToMono(new ParameterizedTypeReference<List<Content>>() {})
                .block();

    }
}
