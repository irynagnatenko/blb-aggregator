package se.b3.healthtech.blackbird.blbaggregator.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import se.b3.healthtech.blackbird.blbaggregator.api.response.PublicationResponse;
import se.b3.healthtech.blackbird.blbaggregator.domain.composite.Container;
import se.b3.healthtech.blackbird.blbaggregator.domain.composite.ContainerObject;
import se.b3.healthtech.blackbird.blbaggregator.domain.composite.Publication;
import se.b3.healthtech.blackbird.blbaggregator.domain.content.Content;
import se.b3.healthtech.blackbird.blbaggregator.integration.composition.ContainerClient;
import se.b3.healthtech.blackbird.blbaggregator.integration.composition.ContainerObjectClient;
import se.b3.healthtech.blackbird.blbaggregator.integration.content.ContentClient;
import se.b3.healthtech.blackbird.blbaggregator.service.util.ServiceUtil;
import se.b3.healthtech.blackbird.blbaggregator.template.enums.TemplateContent;
import se.b3.healthtech.blackbird.blbaggregator.integration.composition.PublicationClient;
import se.b3.healthtech.blackbird.blbaggregator.template.model.Template;
import se.b3.healthtech.blackbird.blbaggregator.template.model.TemplateContainer;
import se.b3.healthtech.blackbird.blbaggregator.template.model.TemplateContainerObject;

import java.util.*;
import java.util.concurrent.CompletableFuture;

@Service
@Slf4j
public class PublicationService {

    private final TemplateService templateService;
    private final PublicationClient publicationClient;
    private final TemplateConverterService templateConverterService;
    private final ContainerService containerService;
    private final ContainerObjectService containerObjectService;
    private final ContentService contentService;
    private final ContainerClient containerClient;
    private final ContainerObjectClient containerObjectClient;
    private final ContentClient contentClient;

    public PublicationService(TemplateService templateService, PublicationClient publicationClient, TemplateConverterService templateConverterService, ContainerService containerService, ContainerObjectService containerObjectService, ContentService contentService, ContainerClient containerClient, ContainerObjectClient containerObjectClient, ContentClient contentClient) {
        this.templateService = templateService;
        this.publicationClient = publicationClient;
        this.templateConverterService = templateConverterService;
        this.containerService = containerService;
        this.containerObjectService = containerObjectService;
        this.contentService = contentService;
        this.containerClient = containerClient;
        this.containerObjectClient = containerObjectClient;
        this.contentClient = contentClient;
    }

    public String createPublication(String userName, String templateId, String title) {
        log.info("createPublication:init");
        String key = UUID.randomUUID().toString();
        long created =ServiceUtil.setCreatedTime();

        Template template = templateService.getTemplate(templateId);
        //Publication
        Publication publication = templateConverterService.mapToPublication(template, key, userName, title, created);

        //Set Content Uuid
        List<TemplateContainer> containerV2s = template.getTemplateContainerList();
        List<TemplateContainerObject> allTemplateContainerObjects = templateConverterService.getAllTemplateContainerObjectV2(containerV2s);
        setContentId(allTemplateContainerObjects);

        //Container
        List<Container> containers = templateConverterService.mapToContainers(containerV2s, userName,  created);

        //ContainerObject
        List<ContainerObject> containerObjects = templateConverterService.mapToContainerObjectList(containerV2s, userName, created);

        //Content
        List<Content> contents = templateConverterService.mapToContents(allTemplateContainerObjects,  userName, created);

        setUuid(containers, containerObjects, contents);

        CompletableFuture<Void> combinedFuture;
        CompletableFuture<Void> future1 = CompletableFuture.runAsync(() -> publicationClient.postPublication(publication));
        CompletableFuture<Void> future2 = CompletableFuture.runAsync(() -> containerService.addContainers(key, containers));
        CompletableFuture<Void> future3 = CompletableFuture.runAsync(() -> containerObjectService.addContainerObjects(key, containerObjects));
        CompletableFuture<Void> future4 = CompletableFuture.runAsync(() -> contentService.createContent(key, contents));

        combinedFuture = CompletableFuture.allOf(future1, future2, future3, future4);

        // We wait for all threads to be ready
        log.info("createPublication(): waiting for threads to complete");
        combinedFuture.join();
        log.info("createPublication(): threads are complete");

        return publication.getUuid();
    }

    void setContentId(List<TemplateContainerObject> templateContainerObjects) {
        templateContainerObjects.forEach(objectV2 -> {
            TemplateContent o = objectV2.getTemplateContent();
            o.setId(objectV2.getId());
        });
    }

    void setUuid(List<Container> containerList, List<ContainerObject> containerObjectList, List<Content> contentList) {
        List<String> newContainerObjectList = new ArrayList<>();

        containerList.forEach(container -> {
            container.getContainerObjectsList().forEach(s -> {
                String uuid = UUID.randomUUID().toString();
                ContainerObject co = ServiceUtil.findContainerObjectById(containerObjectList, s);
                Content content = ServiceUtil.findContentById(contentList, co.getUuid());
                co.setUuid(uuid);
                content.setUuid(uuid);
                newContainerObjectList.add(uuid);
            });
            container.setContainerObjectsList(new ArrayList<>());
            container.getContainerObjectsList().addAll(newContainerObjectList);
            newContainerObjectList.clear();
        });
    }
    public PublicationResponse getLatestPublication(String key) {
        Publication publication = publicationClient.getLatestPublication(key);
        List<Container> containerList = containerService.getLatestContainers(key);
        List<ContainerObject> containerObjectsList = containerObjectService.getLatestContainerObjects(key);
        List<Content> contentList = contentService.getLatestContentList(key);

        PublicationResponse latestPublicationResponse = new PublicationResponse(publication, containerList, containerObjectsList, contentList);
        return latestPublicationResponse;
    }

    public void deletePublication(String userName, String publicationId) {
        log.info("In the deletePublication method ");
        // Läs upp Latest-publication med publicationId
        List<Publication> publicationList = new ArrayList<>();
        Publication publication = publicationClient.getLatestPublication(publicationId);
        publicationList.add(publication);

        //Läs upp alla Latest-containers med publicationId
        List<Container> containerList = containerClient.getLatestContainers(publicationId);

        //Läs upp alla Latest containerObjects med publicationId
        List<ContainerObject> containerObjectList = containerObjectService.getLatestContainerObjects(publicationId);

        //Läs upp alla Latest contents med publicationId
        List<Content> contentList = contentService.getLatestContentList(publicationId);


        //Hämtning av alla LatestObjekt ska ske asynkront
        //Anropa asynkront deletePublication (denna tjänst finns inte implementerad i compositionservice och måste implementeras. Görs på samma sätt som för de andra tjänsterna.
        //Anropa asynkront deleteContainer
        //Anropa asynkront deleteContainerObjects
        //Anropa asynkront deleteContent

        CompletableFuture<Void> combinedFuture;
        CompletableFuture<Void> future1 = CompletableFuture.runAsync(() -> publicationClient.deletePublication(publicationId, userName, publicationList));
        CompletableFuture<Void> future2 = CompletableFuture.runAsync(() -> containerClient.deleteContainer(publicationId, userName, containerList));
        CompletableFuture<Void> future3 = CompletableFuture.runAsync(() -> containerObjectClient.deleteContainerObject(publicationId, userName, containerObjectList));
        CompletableFuture<Void> future4 = CompletableFuture.runAsync(() -> contentClient.deleteContent(publicationId, userName, contentList));

        combinedFuture = CompletableFuture.allOf(future1, future2, future3, future4);

        log.info("deletePublication(): waiting for threads to complete");
        combinedFuture.join();
        log.info("deletePublication(): threads are complete");

    }
}
