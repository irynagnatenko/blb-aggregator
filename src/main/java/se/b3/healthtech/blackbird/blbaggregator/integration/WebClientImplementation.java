package se.b3.healthtech.blackbird.blbaggregator.integration;

import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.BodyInserter;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;
import se.b3.healthtech.blackbird.blbaggregator.domain.composite.CreatePublicationRequest;

@Service
public class WebClientImplementation {

    private final WebClient webClient;

    public WebClientImplementation(WebClient webClient) {
        this.webClient = webClient;
    }

    public String postPublication(CreatePublicationRequest request) {

        String result =  webClient.post()
                .uri("/api-birdspecies/publication/")
                .body(Mono.just(request), CreatePublicationRequest.class)
                .retrieve()
                .bodyToMono(String.class)
                .log()
                .block();

        return result;

    }

/*
        WebClient.UriSpec<WebClient.RequestBodySpec> uriSpec = webClient.post();
        WebClient.RequestBodySpec bodySpec = uriSpec.uri(
                uriBuilder -> uriBuilder.pathSegment("/api-birdspecies/publication/").build());


 */


/*
    public void postPublication2(CreatePublicationRequest createPublicationRequest) {

        WebClient webClient = WebClient.create("http://localhost:8086");

        Mono<String> postResponse = webClient.post()
                .uri(uriBuilder -> uriBuilder
                        .path("/api-birdspecies/publication/")
                        .build()
                        .bodyValue(createPublicationRequest)
//                                .body(BodyInserter.fromValue(createPublicationRequest))
                        .retrieve()
                        .bodyToMono(String.class)
                );


 */

}


