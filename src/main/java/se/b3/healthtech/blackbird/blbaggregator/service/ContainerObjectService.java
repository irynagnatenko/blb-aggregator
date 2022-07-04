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

    public ContainerObject createContainerObject(String uuid, long created, String userName){
    ContainerObject newContainerObject = new ContainerObject();
    newContainerObject.setCreatedBy(userName);
    newContainerObject.setCreated(created);
    newContainerObject.setUuid(uuid);

    return newContainerObject;
    }

    //Metoden ska anropa ny metod i containerObjectClient
    // som tar ett publicationId och ett ContainerObject och anropar motsvarande tjänst i CompositeTjänsten.
    //Metoden returnerar void
    public void addContainerObject(String publicationId, ContainerObject containerObject) {
        containerObjectClient.addContainerObject(publicationId, containerObject);
    }
}
