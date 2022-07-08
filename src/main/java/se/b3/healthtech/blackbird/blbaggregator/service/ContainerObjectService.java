package se.b3.healthtech.blackbird.blbaggregator.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import se.b3.healthtech.blackbird.blbaggregator.domain.composite.Container;
import se.b3.healthtech.blackbird.blbaggregator.domain.composite.ContainerObject;
import se.b3.healthtech.blackbird.blbaggregator.integration.composition.ContainerObjectClient;

import java.util.ArrayList;
import java.util.List;

@Slf4j
@Service
public class ContainerObjectService {

    private final ContainerObjectClient containerObjectClient;

    public ContainerObjectService(ContainerObjectClient containerObjectClient) {
        this.containerObjectClient = containerObjectClient;
    }

    public void addContainerObjects(String key, List<ContainerObject> containerObjects) {
        containerObjectClient.postContainerObjects(key, containerObjects);
    }

    public List<ContainerObject> getLatestContainerObjects(String key) {
        return containerObjectClient.getLatestContainerObjects(key);
    }

    public ContainerObject createContainerObject(String uuid, long created, String userName) {
        ContainerObject newContainerObject = new ContainerObject();
        newContainerObject.setCreatedBy(userName);
        newContainerObject.setCreated(created);
        newContainerObject.setUuid(uuid);

        return newContainerObject;
    }

    public void addContainerObject(String publicationId, ContainerObject containerObject) {
        containerObjectClient.addContainerObject(publicationId, containerObject);
    }

    public ContainerObject getContainerObject(String publicationId, String containerObjectId) {
        ContainerObject latestContainerObject = containerObjectClient.getContainerObject(publicationId, containerObjectId);
        return latestContainerObject;
    }

    public void deleteContainerObject(String publicationId, String userName, List<ContainerObject> containerObjectList) {
        containerObjectClient.deleteContainerObject(publicationId, userName, containerObjectList);
    }

    // TODO: enhets test på den
    //Ta bort containerObjectId från listan av containObjectIds i ContainerObjektet
    public void deleteContainerObjectsIds(Container container, ContainerObject containerObject) {
        log.info("deleteContainerObjectsIds init");
        List<String> originalList = container.getContainerObjectsList();
        log.info("initial ContainerObjectsIds " + originalList);
        log.info("ContainerObjects.getUuid " + containerObject.getUuid());

        List<String> newList = new ArrayList<>();

        originalList.stream()
                .filter(containerObject.getUuid()::contentEquals)
                .forEach(newList::add);
        originalList.removeAll(newList);

            /*
        for (int i = 0; i < originalList.size(); i++) {
            if (originalList.get(i).equals(containerObject.getUuid())) {
                originalList.remove(i);
                log.info("i " + i);
            }

 */
        log.info("new list after stream " + originalList);

    }

}

