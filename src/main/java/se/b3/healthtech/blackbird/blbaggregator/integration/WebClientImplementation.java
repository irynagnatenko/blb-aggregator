package se.b3.healthtech.blackbird.blbaggregator.integration;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;
import se.b3.healthtech.blackbird.blbaggregator.domain.composite.CreatePublicationRequest;

@Slf4j
@Service
public class WebClientImplementation {

    private final WebClient webClient;

    public WebClientImplementation(WebClient webClient) {
        this.webClient = webClient;
    }

    public String postPublication(CreatePublicationRequest request) {
        log.info("Innan anrop: {}", request.getContainerList().size());
        String result =  webClient.post()
                .uri("/api-birdspecies/publication/")
                .body(Mono.just(request), CreatePublicationRequest.class)
                .retrieve()
                .bodyToMono(String.class)
                .log()
                .block();

        return result;

    }

}


