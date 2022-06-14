package se.b3.healthtech.blackbird.blbaggregator.service;

import org.springframework.stereotype.Service;
import se.b3.healthtech.blackbird.blbaggregator.domain.composite.ContainerObject;
import se.b3.healthtech.blackbird.blbaggregator.integration.composition.ContainerObjectClient;

import java.util.List;

@Service
public class ContainerObjectService {
    private final ContainerObjectClient containerObjectClient;

    public ContainerObjectService(ContainerObjectClient containerObjectClient) {
        this.containerObjectClient = containerObjectClient;
    }

    public void addContainerObjects(String key, List<ContainerObject> containerObjects) {
        containerObjectClient.postContainerObjects(key, containerObjects);
    }

    public List<ContainerObject> getLatestContainerObjects(String key){
        return containerObjectClient.getLatestContainerObjects(key);
    }
}
