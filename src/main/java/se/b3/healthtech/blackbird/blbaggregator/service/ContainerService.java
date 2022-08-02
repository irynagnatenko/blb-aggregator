package se.b3.healthtech.blackbird.blbaggregator.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import se.b3.healthtech.blackbird.blbaggregator.domain.composite.Container;
import se.b3.healthtech.blackbird.blbaggregator.domain.composite.ContainerObject;
import se.b3.healthtech.blackbird.blbaggregator.domain.content.Content;
import se.b3.healthtech.blackbird.blbaggregator.integration.composition.ContainerClient;
import se.b3.healthtech.blackbird.blbaggregator.integration.composition.ContainerObjectClient;
import se.b3.healthtech.blackbird.blbaggregator.integration.content.ContentClient;
import se.b3.healthtech.blackbird.blbaggregator.service.util.ServiceUtil;

import java.util.*;
import java.util.concurrent.CompletableFuture;

import static se.b3.healthtech.blackbird.blbaggregator.service.util.ServiceUtil.sortByOrdinal;

@Slf4j
@Service
public class ContainerService {

    private final ContainerClient containerClient;
    private final ContainerObjectClient containerObjectClient;
    private final ContentClient contentClient;

    public ContainerService(ContainerClient containerClient, ContainerObjectClient containerObjectClient, ContentClient contentClient) {
        this.containerClient = containerClient;
        this.containerObjectClient = containerObjectClient;
        this.contentClient = contentClient;
    }

    public void addContainers(String key, List<Container> containers) {
        containerClient.postContainers(key, containers);
    }

    public List<Container> getLatestContainers(String key) {
        List<Container> latestContainers = containerClient.getLatestContainers(key);
        latestContainers.sort(sortByOrdinal);
        return latestContainers;
    }
    // fot addContent method
    public Container getLatestContainer(String publicationId, String containerId){
        log.info("ContainerService - getLatestContainer");
        Container latestContainer = containerClient.getLatestContainer(publicationId, containerId);
        return latestContainer;
    }

    //Metoden ska kunna hantera att parentId är null. I det fallet ska den nya containern ha ordinalNr 1
    // och befintliga containrar ska uppdatera sitt ordinalNr med 1.
    public void addContainer(String publicationId, Optional<String> parentId, String userName) {

        Container container;
        List<Container> updatedContainers;
        int newContainerOrdinalNr = 1;
        long created = ServiceUtil.setCreatedTime();

        //Hämta upp en lista av alla latest containrar för aktuell publicationId genom att
        // anropa befintlig endpoint - getLatestContainers i composite-tjänsten.
        List<Container> latestContainers = containerClient.getLatestContainers(publicationId);
        log.info("ContainerService - addContainer");
        //Ur listan av latest containrar hämta ut “parent Container” dvs. den container
        // som har matchande uuid med det som har skickats in till tjänsten (parentId)
        if (parentId.isPresent()) {
            Container parentContainer = ServiceUtil.findContainerById(latestContainers, parentId.get());
            //Räkna ut vilket ordinalNr som den nya container ska ha och skapa en ny container med ett nytt commitnummer
            newContainerOrdinalNr = parentContainer.getOrdinal() + 1;
        }

        container = createNewContainer(userName, created, newContainerOrdinalNr);
        log.info("addContainer(): create new container");

        //Uppdatera ordinalNr för resten av containrar (ska räknas upp med ett för varje container)
        updatedContainers = updateOrdinal(latestContainers, newContainerOrdinalNr);

        //Anropa composite-tjänstens endpoint - addContainer för att lägga till den nya container
        CompletableFuture<Void> combinedFuture;
        CompletableFuture<Void> future1 = CompletableFuture.runAsync(() -> containerClient.addContainer(publicationId, container));

        //Anropa cosmpositetjänstens endpoint - addContainers för att uppdatera de underliggande containrar.
        CompletableFuture<Void> future2 = CompletableFuture.runAsync(() -> containerClient.postContainers(publicationId, updatedContainers));

        combinedFuture = CompletableFuture.allOf(future1, future2);

        // We wait for all threads to be ready
        log.info("addContainer(): waiting for threads to complete");
        combinedFuture.join();
        log.info("addContainer(): threads are complete");

    }

    public Container createNewContainer(String userName, Long created, int ordinalNr) {
        Container newContainer = new Container();
        newContainer.setCreatedBy(userName);
        newContainer.setCreated(created);
        newContainer.setOrdinal(ordinalNr);
        newContainer.setUuid(UUID.randomUUID().toString());
        newContainer.setContainerObjectsList(new ArrayList<>());

        return newContainer;

    }
    //* sätta latest på den nya containern

    public List<Container> updateOrdinal(List<Container> containersList, int newContainerOrdinalNr) {
        List<Container> updatedContainerList = new ArrayList<>();
        for (Container container : containersList) {
            if (container.getOrdinal() >= newContainerOrdinalNr) {
                updatedContainerList.add(container);
            }
        }
        for (Container container : updatedContainerList) {
            container.setOrdinal(container.getOrdinal() + 1);
        }

        return updatedContainerList;
    }

    public void updateContainerWithNewContainerObjectRef(Container container, String uuid, Optional<String> parentId) {

        int index = 0;

        List<String> containerObjectListToUpdate = container.getContainerObjectsList();

        if (parentId.isPresent()) {
            index = containerObjectListToUpdate.indexOf(parentId)+1;
        }

        containerObjectListToUpdate.add(index, uuid);
        container.setContainerObjectsList(containerObjectListToUpdate);

        log.info("Container Service: updateContainerWithNewContainerObjectRef");

    }

    public void addContainer(String publicationId, Container container) {
        containerClient.addContainer(publicationId, container);
    }

    public void deleteContainers(String userName, String publicationId, String containerId) {
        //TODO: menar du container eller containerObject???
        //Hämta upp ett containerObjekt från CompositionTjänsten med skicka in key och containerId. Asynkront anrop
        //Lägg till ContainerObjektet i en List<Container>
        //GET-TJÄNSTER
        Container container = containerClient.getLatestContainer(publicationId, containerId);
        List<Container> containerList = new ArrayList<>();
        containerList.add(container);

        //Hämta ut listan av containerObjectIds från ContainerObjektet
        List<String> containerObjectIds = container.getContainerObjectsList();
        log.info("listan av containerObjectIds från ContainerObjektet " + containerObjectIds);

        //Anropa compositiontjänsten för att hämta upp alla ContainerObject som finns i listan av containerObjectIds (Ett asynkront anrop till tjänsten)
        List<ContainerObject> containerObjectsByUuids = containerObjectClient.getContainerObjectsByUuids(publicationId, containerObjectIds);
        //Anropa contentjänsten för att hämta upp alla Content som finns i listan av containerObjectIds (Ett asynkront anrop till tjänsten)
        List<Content> contentsByUuids = contentClient.getContentsByUuids(publicationId, containerObjectIds);
        //Alla anrop för att hämta upp objekt ska ske med asynkron logik enligt tidigare implementationer.

        //Ta bort container genom att anropa endpoint - deleteContainers i compositetjänsten (finns inte klienten för detta ska en delete-metod skapas i klienten
        //Ta bort alla upplästa containerObjects genom att anropa endpoint - deleteContainerObjects i compositetjänsten
        //Ta bort alla upplästa content genom att anropa endpoint- deleteContents i Contenttjänsten.
        //Alla anrop för att ta bort objekt ska ske med asynkron logik enligt tidigare implementationer.
        CompletableFuture<Void> combinedFuture;
        CompletableFuture<Void> future1 = CompletableFuture.runAsync(() -> containerClient.deleteContainer(publicationId, userName, containerList));
        CompletableFuture<Void> future2 = CompletableFuture.runAsync(() -> containerObjectClient.deleteContainerObject(publicationId, userName, containerObjectsByUuids));
        CompletableFuture<Void> future3 = CompletableFuture.runAsync(() -> contentClient.deleteContent(publicationId, userName, contentsByUuids));

        combinedFuture = CompletableFuture.allOf(future1, future2, future3);

        // We wait for all threads to be ready
        log.info("deleteContainers(): waiting for threads to complete a delete");
        combinedFuture.join();
        log.info("deleteContainers(): delete threads are complete");

    }

}

