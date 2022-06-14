package se.b3.healthtech.blackbird.blbaggregator.service;

import org.springframework.stereotype.Service;
import se.b3.healthtech.blackbird.blbaggregator.domain.composite.Container;
import se.b3.healthtech.blackbird.blbaggregator.integration.composition.ContainerClient;

import java.util.List;

@Service
public class ContainerService {

    private final ContainerClient containerClient;

    public ContainerService(ContainerClient containerClient) {
        this.containerClient = containerClient;
    }

    public void addContainers(String key, List<Container> containers) {
        containerClient.postContainers(key, containers);
    }

    public List<Container> getLatestContainers(String key){
       return containerClient.getLatestContainers(key);
    }
}
