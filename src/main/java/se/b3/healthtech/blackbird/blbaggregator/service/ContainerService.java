package se.b3.healthtech.blackbird.blbaggregator.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import se.b3.healthtech.blackbird.blbaggregator.domain.composite.Container;
import se.b3.healthtech.blackbird.blbaggregator.integration.composition.ContainerClient;
import se.b3.healthtech.blackbird.blbaggregator.service.util.ServiceUtil;

import java.util.*;
import java.util.concurrent.CompletableFuture;

import static se.b3.healthtech.blackbird.blbaggregator.service.util.ServiceUtil.sortByOrdinal;

@Slf4j
@Service
public class ContainerService {

    private final ContainerClient containerClient;

    public ContainerService(ContainerClient containerClient) {
        this.containerClient = containerClient;
    }

    public void addContainers(String key, List<Container> containers) {
        containerClient.postContainers(key, containers);
    }

    public List<Container> getLatestContainers(String key) {
        List<Container> latestContainers = containerClient.getLatestContainers(key);
        latestContainers.sort(sortByOrdinal);
        return latestContainers;
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
        log.info("createPublication(): create new container");

        //Uppdatera ordinalNr för resten av containrar (ska räknas upp med ett för varje container)
        updatedContainers = updateOrdinal(latestContainers, newContainerOrdinalNr);

        //Anropa composite-tjänstens endpoint - addContainer för att lägga till den nya container
        CompletableFuture<Void> combinedFuture;
        CompletableFuture<Void> future1 = CompletableFuture.runAsync(() -> containerClient.addContainer(publicationId, container));

        //Anropa cosmpositetjänstens endpoint - addContainers för att uppdatera de underliggande containrar.
        CompletableFuture<Void> future2 = CompletableFuture.runAsync(() -> containerClient.postContainers(publicationId, updatedContainers));

        combinedFuture = CompletableFuture.allOf(future1, future2);

        // We wait for all threads to be ready
        log.info("createPublication(): waiting for threads to complete");
        combinedFuture.join();
        log.info("createPublication(): threads are complet");

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
    //* sätta latet på den nya containern

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


}

