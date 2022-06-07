package se.b3.healthtech.blackbird.blbaggregator.integration.content;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;
import se.b3.healthtech.blackbird.blbaggregator.domain.content.CreateContentRequest;

@Slf4j
@Service
public class ContentClient {

    private static final String URI_CONTENT_POST = "/api-birdspecies/content/";

    private WebClient contentWebClient;

    public ContentClient(WebClient contentWebClient) {
        this.contentWebClient = contentWebClient;
    }

    public void postContent(CreateContentRequest request) {
        log.info("Antal containers: {}", request.getContentList().size());

        contentWebClient.post()
                .uri(URI_CONTENT_POST)
                .body(Mono.just(request), CreateContentRequest.class)
                .retrieve()
                .onStatus(HttpStatus::is4xxClientError, error -> Mono.error(new RuntimeException("Composition API not found")))
                .onStatus(HttpStatus::is5xxServerError, error -> Mono.error(new RuntimeException("Server is not responding")))
                .bodyToMono(Void.class)
                .log()
                .doOnError(error -> log.error("An error has occurred {}", error.getMessage()))
                .subscribe();

    }

}
