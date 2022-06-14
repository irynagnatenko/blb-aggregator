package se.b3.healthtech.blackbird.blbaggregator.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import se.b3.healthtech.blackbird.blbaggregator.api.response.PublicationResponse;
import se.b3.healthtech.blackbird.blbaggregator.domain.composite.Container;
import se.b3.healthtech.blackbird.blbaggregator.domain.composite.ContainerObject;
import se.b3.healthtech.blackbird.blbaggregator.domain.composite.Publication;
import se.b3.healthtech.blackbird.blbaggregator.domain.content.Content;
import se.b3.healthtech.blackbird.blbaggregator.service.util.ServiceUtil;
import se.b3.healthtech.blackbird.blbaggregator.template.enums.TemplateContent;
import se.b3.healthtech.blackbird.blbaggregator.integration.composition.PublicationClient;
import se.b3.healthtech.blackbird.blbaggregator.template.model.Template;
import se.b3.healthtech.blackbird.blbaggregator.template.model.TemplateContainer;
import se.b3.healthtech.blackbird.blbaggregator.template.model.TemplateContainerObject;


import java.time.ZoneId;
import java.time.ZonedDateTime;

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

    public PublicationService(TemplateService templateService, PublicationClient publicationClient, TemplateConverterService templateConverterService, ContainerService containerService, ContainerObjectService containerObjectService, ContentService contentService) {
        this.templateService = templateService;
        this.publicationClient = publicationClient;
        this.templateConverterService = templateConverterService;
        this.containerService = containerService;
        this.containerObjectService = containerObjectService;
        this.contentService = contentService;
    }

    public Long dateCreated() {
        ZonedDateTime nowEuropeStockholm = ZonedDateTime.now(ZoneId.of("Europe/Stockholm"));
        return nowEuropeStockholm.toInstant().toEpochMilli();
    }

    public String createPublication(String userName, String templateId, String title) {
        log.info("createPublication:init");
        String key = UUID.randomUUID().toString();
        long created =dateCreated();

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
        CompletableFuture<Void> future4 = CompletableFuture.runAsync(() -> contentService.addContent(key, contents));

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
        List<Content> contentList = contentService.getLatestContent(key);

        PublicationResponse latestPublicationResponse = new PublicationResponse(publication, containerList, containerObjectsList, contentList);
        return latestPublicationResponse;
    }


}
