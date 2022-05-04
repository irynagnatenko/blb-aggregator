package se.b3.healthtech.blackbird.blbaggregator.service;

import lombok.extern.slf4j.Slf4j;
import org.mapstruct.factory.Mappers;
import org.springframework.stereotype.Service;
import se.b3.healthtech.blackbird.blbaggregator.domain.composite.Container;
import se.b3.healthtech.blackbird.blbaggregator.domain.composite.ContainerObject;
import se.b3.healthtech.blackbird.blbaggregator.domain.composite.CreatePublicationRequest;
import se.b3.healthtech.blackbird.blbaggregator.domain.composite.Publication;
import se.b3.healthtech.blackbird.blbaggregator.integration.WebClientImplementation;
import se.b3.healthtech.blackbird.blbaggregator.mapper.TemplatePublicationMapper;
import se.b3.healthtech.blackbird.blbaggregator.template.model.Template;

import java.util.*;
import java.util.stream.Collectors;

@Service
@Slf4j
public class PublicationService {
    private final TemplatePublicationMapper mapper = Mappers.getMapper(TemplatePublicationMapper.class);
    private final TemplateService templateService;
    private final WebClientImplementation compositionWebClient;
//    private static final String URL_PATH_CREATE = "/api/composition/";

    public PublicationService(TemplateService templateService, WebClientImplementation compositionWebClient) {
        this.templateService = templateService;
        this.compositionWebClient = compositionWebClient;
    }

    public String createPublication(String userName, String templateId, String title) {
        log.info("init");
        Map<Container, List<ContainerObject>> containerContainerObjectMap;

        Template template = templateService.getTemplate(templateId);
        log.info("Antal containers i template: {}", template.getTemplateContainerList().size());

        //Skapa upp compositionsobjekten
        log.info("create publication");
        Publication publication = createPublication(template, userName, title);
        log.info("create container and container objects");
        containerContainerObjectMap = createContainersAndContainerObjects(template, userName, 1000L);

        //Skapa id
        log.info("Set uuid");
        setUuid(publication, containerContainerObjectMap);

        //Create RequestObject
        log.info("Create request");
        CreatePublicationRequest request = createCompositionRequest(publication, containerContainerObjectMap);

        //Invoke compositionService
        log.info("Invoke compositionService");
        compositionWebClient.postPublication(request);


        return null;
    }

    Publication createPublication(Template template, String userName, String title) {
        return mapper.mapToPublication(template, 1000L, userName,
                UUID.randomUUID().toString(), title);
    }

    Map<Container, List<ContainerObject>> createContainersAndContainerObjects(Template template, String userName, long created) {
        return mapper.mapToContainerAndContainerObject(template.getTemplateContainerList(), userName, created);
    }

    void setUuid(Publication publication, Map<Container, List<ContainerObject>> containerContainerObjectMap) {
        //Set Container UUID
        containerContainerObjectMap.forEach((k, v) -> {
            k.setUuid(UUID.randomUUID().toString());
            publication.getContainersIdList().add(k.getUuid());
        });

        Map<Container, List<ContainerObject>> map = new HashMap<>();
        map.putAll(containerContainerObjectMap);

        map.values().removeAll(Collections.singleton(null));

        map.forEach((k, v) -> v.forEach(containerObject -> {
            {
                containerObject.setUuid(UUID.randomUUID().toString());
                k.getContainerObjectsList().add(containerObject.getUuid());
                containerContainerObjectMap.replace(k, v);
            }
        }));
    }

    CreatePublicationRequest createCompositionRequest(Publication publication, Map<Container, List<ContainerObject>> map) {
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
}
