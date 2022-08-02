package se.b3.healthtech.blackbird.blbaggregator.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import se.b3.healthtech.blackbird.blbaggregator.api.CreateContentRequest;
import se.b3.healthtech.blackbird.blbaggregator.domain.composite.Container;
import se.b3.healthtech.blackbird.blbaggregator.domain.composite.ContainerObject;
import se.b3.healthtech.blackbird.blbaggregator.domain.content.Content;
import se.b3.healthtech.blackbird.blbaggregator.integration.content.ContentClient;
import se.b3.healthtech.blackbird.blbaggregator.service.util.ContentUtil;
import se.b3.healthtech.blackbird.blbaggregator.service.util.ServiceUtil;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.concurrent.CompletableFuture;

@Slf4j
@Service
public class ContentService {

    private final ContentClient contentClient;
    private final ContainerService containerService;
    private final ContainerObjectService containerObjectService;

    public ContentService(ContentClient contentClient, ContainerService containerService, ContainerObjectService containerObjectService) {
        this.contentClient = contentClient;
        this.containerService = containerService;
        this.containerObjectService = containerObjectService;
    }

    public void createContent(String key, List<Content> contents) {

        contentClient.postContent(key, contents);
    }

    public List<Content> getLatestContentList(String key){
        return contentClient.getLatestContentList(key);
    }

    public void addContent(String userName, String publicationId, String containerId, Optional<String> parentId, CreateContentRequest contentRequest) {
        String newUUID = UUID.randomUUID().toString();
        Long created = ServiceUtil.setCreatedTime();

        //Anropa containerService för att hämta upp latest containerobjektet med angivet containerId.
        Container latestContainer = containerService.getLatestContainer(publicationId, containerId);

        //Anropa ny metod i containerService.updateContainerWithNewContainerObjectRef(container, uuid, parentId)
        // för att uppdatera containerObjectListan med nytt ContainerObjectId
        // (om parentId finns ska det nya ContainerObjectId placeras efter parentId i listan av containerObjectId).
        containerService.updateContainerWithNewContainerObjectRef(latestContainer, newUUID, parentId);

        //Anropa ny metod i containerObjectService.create(publicationId, uuid, created, userName) för att skapa upp ett nytt containerObject.
        // Metoden ska returnera det skapade containerObject-objektet.
        ContainerObject containerObject = containerObjectService.createContainerObject(newUUID, created, userName);

        //Anropa ny private metod i contentSerice - create(uuid, created, userName, request)
        // för att skapa upp ett nytt ContentObjekt, objektet ska hantera att skapa olika typer av contents.
        // Metoden ska returnera det skapade content-objektet.
        Content content = createContent(newUUID, created, userName, contentRequest);

        //Anropa de underliggande mikro-tjänsterna - CompositeTjänsten och ContentTjänsten med CompletableFuture för att:
        CompletableFuture<Void> combinedFuture;
        //Uppdatera container med nytt containerObjectId i listan - containerService.addContainer(String publicationId, Container container)
        CompletableFuture<Void> future1 = CompletableFuture.runAsync(() -> containerService.addContainer(publicationId, latestContainer));
        //Skapa nytt containerObject - containerObjectService.addContainerObject(String publicationId, ContainerObject containerObject)
        CompletableFuture<Void> future2 = CompletableFuture.runAsync(() ->containerObjectService.addContainerObject(publicationId,containerObject));
        //Skapa nytt content - contentClient.addContent(String publicationId, Content content)
        CompletableFuture<Void> future3 = CompletableFuture.runAsync(() -> addContent(publicationId,content));

        combinedFuture = CompletableFuture.allOf(future1, future2, future3);

        // We wait for all threads to be ready
        log.info("createContent(): waiting for threads to complete");
        combinedFuture.join();
        log.info("createContent(): threads are complete");

    }

    private Content createContent(String uuid, long created, String userName, CreateContentRequest request){
        Content newContent = new Content();
        newContent.setUuid(uuid);
        newContent.setCreatedBy(userName);
        newContent.setCreated(created);
        newContent.setContentType(request.contentTypeEnum());
        newContent.setHeaderContent(request.headerContent());
        newContent.setListContent(request.listContent());
        newContent.setTableContent(request.tableContent());
        newContent.setTextContent(request.textContent());

        return newContent;
    }

    public void addContent(String publicationId,Content content){
        contentClient.addContentObject(publicationId, content);
    }

    public void updateContent(String userName, String publicationId, CreateContentRequest contentRequest) {
        long created = ServiceUtil.setCreatedTime();
        // get latest content-objekt
        List<Content> latestContentList = getLatestContentList(publicationId);
        //List<Content> latestContentList = getLatestContent(publicationId, contentRequest.id());
        Content contentToUpdate = latestContentList.get(0);

        //Uppdatera uppläst content-objekt med data från ContentRequest. OBS! Befintligt content ska ersättas med content från ContentRequest.
        contentToUpdate.setContentType(contentRequest.contentTypeEnum());
        contentToUpdate.setCreatedBy(userName);
        contentToUpdate.setCreated(created);
        ContentUtil.setContentByContentType(contentToUpdate, contentRequest);

        //insert content
        contentClient.addContentObject(publicationId, contentToUpdate);
    }

    public void deleteContent(String userName, String publicationId, String containerId, String contentId) {
        // Hämta med asynkrona anrop(CompletableFuture) upp en instans av varje:
        //Container via anrop containerServie.getContainer
        log.info("deleteContent: getLatestContainer init");
        Container container = containerService.getLatestContainer(publicationId, containerId);
        log.info("deleteContent: getLatestContainer finish");
        log.info("deleteContent: getLatestContainerObject init");

        //ContainerObject via anrop containerObjectService.getContainerObject
        ContainerObject containerObject = containerObjectService.getContainerObject(publicationId, contentId);
        List<ContainerObject> containerObjectList = new ArrayList<>();
        containerObjectList.add(containerObject);
        log.info("deleteContent: getLatestContainerObject finish");
        log.info("deleteContent: getContent init");

        //Content getContent
        Content content = contentClient.getContent(publicationId, contentId);
        List<Content> contentList= new ArrayList<>();
        contentList.add(content);

        log.info("deleteContent: getContent finish");

        log.info("deleteContent: deleteContainerObjectsIds init");
        //Ta bort containerObjectId från listan av containObjectIds i ContainerObjektet
        containerObjectService.deleteContainerObjectsIds(container, containerObject);
        log.info("deleteContent: deleteContainerObjectsIds finish");

        log.info("deleteContent: combinedFuture init");
        CompletableFuture<Void> combinedFuture;
        log.info("deleteContent: before combinedFuture 1");
        //Anropa Compositiontjänsten för att uppdatera Container-objektet
        CompletableFuture<Void> future1 = CompletableFuture.runAsync(() -> containerService.addContainer(publicationId, container));
        log.info("deleteContent: before combinedFuture 2");
        //Anropa Compostiontjänsten för att ta bort ContainerObject-objektet
        CompletableFuture<Void> future2 = CompletableFuture.runAsync(() ->containerObjectService.deleteContainerObject(publicationId, userName, containerObjectList));
        log.info("deleteContent: before combinedFuture 3");
        //Anropa Contenttjänsten för att ta bort Content-tjänsten
        CompletableFuture<Void> future3 = CompletableFuture.runAsync(() -> deleteContent(publicationId, userName, contentList));

        combinedFuture = CompletableFuture.allOf(future1, future2, future3);

        // We wait for all threads to be ready
        log.info("deleteContent(): waiting for threads to complete");
        combinedFuture.join();
        log.info("deleteContent(): threads are complete");
    }

    public void deleteContent(String publicationId,String userName, List<Content> contentList){
        log.info("In deleteContent method");
        contentClient.deleteContent(publicationId, userName, contentList);
    }
}
