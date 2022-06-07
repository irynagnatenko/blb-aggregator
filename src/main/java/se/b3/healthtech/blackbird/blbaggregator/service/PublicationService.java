package se.b3.healthtech.blackbird.blbaggregator.service;

import lombok.extern.slf4j.Slf4j;
import org.mapstruct.factory.Mappers;
import org.springframework.stereotype.Service;
import se.b3.healthtech.blackbird.blbaggregator.domain.composite.Container;
import se.b3.healthtech.blackbird.blbaggregator.domain.composite.ContainerObject;
import se.b3.healthtech.blackbird.blbaggregator.domain.composite.CreatePublicationRequest;
import se.b3.healthtech.blackbird.blbaggregator.domain.composite.Publication;
import se.b3.healthtech.blackbird.blbaggregator.domain.content.Content;
import se.b3.healthtech.blackbird.blbaggregator.domain.content.CreateContentRequest;
import se.b3.healthtech.blackbird.blbaggregator.integration.composition.CompositionClient;
import se.b3.healthtech.blackbird.blbaggregator.integration.content.ContentClient;
import se.b3.healthtech.blackbird.blbaggregator.mapper.ContentMapper;
import se.b3.healthtech.blackbird.blbaggregator.mapper.TemplatePublicationMapper;
import se.b3.healthtech.blackbird.blbaggregator.template.model.Template;
import se.b3.healthtech.blackbird.blbaggregator.template.model.TemplateContainer;
import se.b3.healthtech.blackbird.blbaggregator.template.model.TemplateContainerObject;


import java.time.ZoneId;
import java.time.ZonedDateTime;

import java.util.*;
import java.util.stream.Collectors;

@Service
@Slf4j
public class PublicationService {
    private final TemplatePublicationMapper mapper = Mappers.getMapper(TemplatePublicationMapper.class);
    private final ContentMapper contentMapper =Mappers.getMapper(ContentMapper.class);
    private final TemplateService templateService;
    private final CompositionClient compositionClient;
    private final ContentClient contentClient;

    public PublicationService(TemplateService templateService, CompositionClient compositionClient, ContentClient contentClient) {
        this.templateService = templateService;
        this.compositionClient = compositionClient;
        this.contentClient = contentClient;
    }

    public Long dateCreated() {
        ZonedDateTime nowEuropeStockholm = ZonedDateTime.now(ZoneId.of("Europe/Stockholm"));
        return nowEuropeStockholm.toInstant().toEpochMilli();
    }

    public String createPublication(String userName, String templateId, String title) {
        log.info("init");
        List<Content> contents = new ArrayList<>();
        Long created = dateCreated();

        Map<Container, List<ContainerObject>> containerContainerObjectMap;

        Template template = templateService.getTemplate(templateId);
        log.info("Antal containers i template: {}", template.getTemplateContainerList().size());

        //Skapa upp publikationsobjekten
        log.info("create publication");
        Publication publication = createPublication(template, userName, title, created, contents);
        log.info("create container and container objects");
        containerContainerObjectMap = createContainersAndContainerObjects(template, userName, dateCreated(), contents);

        //Skapa id
        log.info("Set uuid");
        setUuid(publication, containerContainerObjectMap);

        //Create publication RequestObject
        log.info("Create request");
        CreatePublicationRequest createPublicationRequest = createPublicationRequest(publication, containerContainerObjectMap);

        // Create Content Request
        CreateContentRequest createContentRequest = createContentRequest(publication.getUuid(), contents);

        //Invoke composition WebClient
        log.info("Invoke compositionService");
        compositionClient.postPublication(createPublicationRequest);

        //Invoke content WebClient
        contentClient.postContent(createContentRequest);


        return publication.getUuid();
    }

    //NEW
    //add content to create publication
    Publication createPublication(Template template, String userName, String title, long created, List<Content> contents) {
        String uuid = UUID.randomUUID().toString();
        Publication publication = mapper.mapToPublication(template, dateCreated(), userName, uuid, title);

        Content content = contentMapper.mapTemplateToContent(template, created, userName, uuid, title);
        contents.add(content);

        return publication;
    }

    Map<Container, List<ContainerObject>> createContainersAndContainerObjects(Template template, String userName, long created, List<Content> contents) {
        Map<Container, List<ContainerObject>> containerListMap = mapToContainerAndContainerObject(template.getTemplateContainerList(), userName, created, contents);

        return containerListMap;

    }

    void setUuid(Publication publication, Map<Container, List<ContainerObject>> containerContainerObjectMap) {
        //Set Container UUID
        setUuidOnPublication(publication, containerContainerObjectMap);

        Map<Container, List<ContainerObject>> map = new HashMap<>();
        map.putAll(containerContainerObjectMap);

        map.values().removeAll(Collections.singleton(null));

        map.forEach((k, v) -> v.forEach(containerObject -> {
            k.getContainerObjectsList().remove(containerObject.getUuid());
            containerObject.setUuid(UUID.randomUUID().toString());
            k.getContainerObjectsList().add(containerObject.getUuid());

        }));
    }

    private void setUuidOnPublication(Publication publication, Map<Container, List<ContainerObject>> containerContainerObjectMap) {
        publication.setContainersIdList(new ArrayList<>());

        containerContainerObjectMap.forEach((k, v) -> {
            k.setUuid(UUID.randomUUID().toString());
            publication.getContainersIdList().add(k.getUuid());

        });
    }

    public CreatePublicationRequest createPublicationRequest(Publication publication, Map<Container, List<ContainerObject>> map) {
        CreatePublicationRequest request = new CreatePublicationRequest();
        request.setPublication(publication);

        log.info("Antal containers som ska sättas i request-objektet: {}", map.size());

        request.containerList(map.keySet().stream().toList());

        log.info("Antal containers satta i request-objektet: {}", request.getContainerList().size());
        List<ContainerObject> containerObjectListWithoutNull = map.values().stream()
                .filter(Objects::nonNull)
                .flatMap(x -> x.stream())
                .collect(Collectors.toList());

        request.setContainerObjectList(containerObjectListWithoutNull);

        return request;
    }

    // NEW mapper
    public CreateContentRequest createContentRequest(String uuid, List<Content> contents){
        CreateContentRequest request = new CreateContentRequest();
        request.setPartitionKey(uuid);
        request.setContentList(contents);

        return request;
    }

    public Content mapContent(String userName, String uuid, long created){
        Content content = new Content();
        content.setCommitNumber(1);
        content.setContentType(Content.ContentTypeEnum.CONTENT);
        content.setCreated(created);
        content.setCreatedBy(userName);
        content.setUuid(uuid);
        content.setVersionNumber(1);
        content.setText("test");

        return content;
    }

    private Map<Container, List<ContainerObject>> mapToContainerAndContainerObject(List<TemplateContainer> sourceList,
                                                                                   String userName, long created, List<Content> contents) {
        TreeMap<String, Container> containerMap = mapToContainers(sourceList, userName, created, contents);
        TreeMap<String, ContainerObject> containerObjectMap = mapToContainerObjects(sourceList, userName, created, contents);

        return mapContainerAndContainerObject(containerMap, containerObjectMap);
    }

    private TreeMap<String, ContainerObject> mapToContainerObjects(List<TemplateContainer> templateContainerList,
                                                                   String userName, long created, List<Content> contents) {
        TreeMap<String, TemplateContainer> templateContainerMap = new TreeMap<>();
        TreeMap<String, ContainerObject> containerObjectMap = new TreeMap<>();

        templateContainerList.forEach(tc -> templateContainerMap.put(tc.getId(), tc));

        templateContainerMap.forEach((k, v) -> {
            if(v.getTemplateContainerObjectList() != null) {
                containerObjectMap.putAll(mapContainerObjects(v.getTemplateContainerObjectList(), userName, created, contents));
            }
        });

        return containerObjectMap;
    }

    private TreeMap<String, Container> mapToContainers(List<TemplateContainer> source, String userName, long created, List<Content> contents) {
        TreeMap<String, Container> targetMap = new TreeMap<>();

        for(TemplateContainer templateContainer : source) {
            Container container = map(templateContainer, userName, created);
            Content content = contentMapper.mapTemplateContainerToContent(templateContainer, created, userName, container.getUuid());
            targetMap.put(container.getUuid(),container);
            contents.add(content);
        }
        return targetMap;
    }

    private Map<Container, List<ContainerObject>> mapContainerAndContainerObject(TreeMap<String, Container> containerMap,
                                                                                 TreeMap<String, ContainerObject> containerObjectMap) {
        Map<Container, List<ContainerObject>> map = new HashMap<>();

        containerMap.values().forEach(v -> {
            if(v.getContainerObjectsList() == null) {
                map.put(v,null);
            } else {
                List<String> objectKeys = v.getContainerObjectsList();
                List<ContainerObject> objectList = objectKeys
                        .stream()
                        .map(containerObjectMap::get)
                        .toList();
                objectKeys.stream().map(containerObjectMap::get).forEachOrdered(co -> map.put(v, objectList));
            }
        });
        return map;
    }

    private TreeMap<String, ContainerObject> mapContainerObjects(List<TemplateContainerObject> sourceList,
                                                                 String userName, long created, List<Content> contents) {
        TreeMap<String, ContainerObject> targetMap = new TreeMap<>();

        for(TemplateContainerObject source : sourceList) {
            ContainerObject target = map(source, userName, created);
            Content content = contentMapper.mapTemplateContainerObjectToContent(source, created, userName, target.getUuid());
            targetMap.put(target.getUuid(),target);
            contents.add(content);
        }
        return targetMap;
    }

    private Container map(TemplateContainer source, String userName, long created) {
        Container target = new Container();
        target.setCreatedBy(userName);
        target.setContainerObjectsList(mapper.getContainerObjectIds(source.getTemplateContainerObjectList()));
        target.setCommitNumber(1);
        target.setOrdinal(source.getOrdinal());
        target.setVersionNumber(1);
        target.setCreated(created);
        target.setUuid(source.getId());

        return target;
    }

    private ContainerObject map(TemplateContainerObject source, String userName, long created) {
        ContainerObject target = new ContainerObject();
        target.setCreated(created);
        target.setCreatedBy(userName);
        target.setOrdinal(source.getOrdinal());
        target.setVersionNumber(1);
        target.setCommitNumber(1);
        target.setUuid(source.getId());

        return target;
    }

}
