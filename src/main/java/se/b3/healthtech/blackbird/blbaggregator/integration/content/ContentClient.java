package se.b3.healthtech.blackbird.blbaggregator.integration.content;

import lombok.extern.slf4j.Slf4j;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.util.MultiValueMap;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;
import se.b3.healthtech.blackbird.blbaggregator.domain.content.Content;

import java.util.List;

@Slf4j
@Service
public class ContentClient extends BaseClientContent {

    private static final String URI_CONTENT_POST_ALL = "/api-birdspecies/content/all/";
    private static final String URI_CONTENT_GET_ALL = "/api-birdspecies/content/all/";
    private static final String URI_CONTENT_ADD_ONE = "/api-birdspecies/content/";
    private static final String URI_CONTENT_GET_ONE = "/api-birdspecies/content/";
    private static final String URI_CONTENT_DELETE = "/api-birdspecies/content/";

    private final WebClient contentWebClient;

    public ContentClient(WebClient contentWebClient) {
        this.contentWebClient = contentWebClient;
    }

    public void postContent(String key, List<Content> contentList) {
        MultiValueMap<String, String> parameters = createParameterKey(key);

        contentWebClient.post()
                .uri(uriBuilder -> uriBuilder
                        .path(URI_CONTENT_POST_ALL)
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

    public List<Content> getLatestContentList(String key) {
        log.info("get latest content with key:{}", key);

        MultiValueMap<String, String> parameters = createParameterKey(key);

        return contentWebClient.get()
                .uri(uriBuilder -> uriBuilder
                        .path(URI_CONTENT_GET_ALL)
                        .queryParams(parameters)
                        .build())
                .retrieve()
                .onStatus(HttpStatus::is4xxClientError,
                        error -> Mono.error(new RuntimeException("Get content list API not found")))
                .onStatus(HttpStatus::is5xxServerError,
                        error -> Mono.error(new RuntimeException("Content Server is not responding")))
                .bodyToMono(new ParameterizedTypeReference<List<Content>>() {
                })
                .block();
    }

    public void addContentObject(String key, Content content) {
        log.info("add content with key:{}", key);

        MultiValueMap<String, String> parameters = createParameterKey(key);

        contentWebClient.post()
                .uri(uriBuilder -> uriBuilder
                        .path(URI_CONTENT_ADD_ONE)
                        .queryParams(parameters)
                        .build())
                .body(Mono.just(content), Content.class)
                .retrieve()
                .onStatus(HttpStatus::is4xxClientError,
                        error -> Mono.error(new RuntimeException("Add content API not found")))
                .onStatus(HttpStatus::is5xxServerError,
                        error -> Mono.error(new RuntimeException("Content server is not responding")))
                .bodyToMono(Void.class)
                .log()
                .doOnError(error -> log.error("An error has occurred {}", error.getMessage()))
                .block();
    }

    // for deleteContent
    // get one content-object
    public Content getContent(String key, String contentId) {
        log.info("get latest content with key:{}", key);

        MultiValueMap<String, String> parameters = createParameterKey(key, contentId);

        return contentWebClient.get()
                .uri(uriBuilder -> uriBuilder
                        .path(URI_CONTENT_GET_ONE)
                        .queryParams(parameters)
                        .build())
                .retrieve()
                .onStatus(HttpStatus::is4xxClientError,
                        error -> Mono.error(new RuntimeException("getContent API not found getContent")))
                .onStatus(HttpStatus::is5xxServerError,
                        error -> Mono.error(new RuntimeException("Server is not responding")))
                .bodyToMono(Content.class)
                .block();
    }

    public void deleteContent(String key, String userName, List<Content> contentList) {
        log.info("deleteContentClient with a key {}: ", key);

        MultiValueMap<String, String> parameters = createParameterKey(key);

        contentWebClient.post()
                .uri(uriBuilder -> uriBuilder
                        .path(URI_CONTENT_DELETE)
                        .queryParams(parameters)
                        .build())
                .header("userName", userName)
                .body(Mono.just(contentList), List.class)
                .retrieve()
                .onStatus(HttpStatus::is4xxClientError,
                        error -> Mono.error(new RuntimeException("Delete content API not found")))
                .onStatus(HttpStatus::is5xxServerError,
                        error -> Mono.error(new RuntimeException("Server is not responding")))
                .bodyToMono(Void.class)
                .log()
                .doOnError(error -> log.error("An error has occurred {}", error.getMessage()))
                .block();

    }
}
