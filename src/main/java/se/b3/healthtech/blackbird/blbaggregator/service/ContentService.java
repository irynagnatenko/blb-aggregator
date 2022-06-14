package se.b3.healthtech.blackbird.blbaggregator.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import se.b3.healthtech.blackbird.blbaggregator.domain.composite.Container;
import se.b3.healthtech.blackbird.blbaggregator.domain.content.Content;
import se.b3.healthtech.blackbird.blbaggregator.integration.composition.ContainerClient;
import se.b3.healthtech.blackbird.blbaggregator.integration.content.ContentClient;

import java.util.List;

@Slf4j
@Service
public class ContentService {

    private final ContentClient contentClient;

    public ContentService(ContentClient contentClient) {
        this.contentClient = contentClient;
    }

    public void addContent(String key, List<Content> contents) {
        contentClient.postContent(key, contents);
    }

    public List<Content> getLatestContent(String key){
        return contentClient.getLatestContent(key);
    }
}
