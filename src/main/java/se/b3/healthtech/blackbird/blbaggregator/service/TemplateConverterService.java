package se.b3.healthtech.blackbird.blbaggregator.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import se.b3.healthtech.blackbird.blbaggregator.domain.composite.Container;
import se.b3.healthtech.blackbird.blbaggregator.domain.composite.ContainerObject;
import se.b3.healthtech.blackbird.blbaggregator.domain.composite.Publication;
import se.b3.healthtech.blackbird.blbaggregator.domain.content.Content;
import se.b3.healthtech.blackbird.blbaggregator.domain.content.HeaderContent;
import se.b3.healthtech.blackbird.blbaggregator.template.enums.TemplateContent;
import se.b3.healthtech.blackbird.blbaggregator.template.model.Template;
import se.b3.healthtech.blackbird.blbaggregator.template.model.TemplateContainer;
import se.b3.healthtech.blackbird.blbaggregator.template.model.TemplateContainerObject;
import se.b3.healthtech.blackbird.blbaggregator.template.model.TemplateHeaderContent;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@Slf4j
public class TemplateConverterService {

    Publication mapToPublication(Template template, String key, String userName, String title, long created) {
        Publication publication = new Publication();
        publication.setCreated(created);
        publication.setCreatedBy(userName);
        publication.setUuid(key);
        publication.setTemplateId(template.getId());
        publication.setTitle(title);
        publication.setPublicationType(Publication.PublicationTypeEnum.fromValue(template.getTemplatePublicationType().name()));

        return publication;
    }

    List<TemplateContainerObject> getAllTemplateContainerObjectV2(List<TemplateContainer> templateContainers) {
        return templateContainers.stream()
                .flatMap(e -> e.getTemplateContainerObjectList().stream())
                .collect(Collectors.toList());
    }

    List<Container> mapToContainers(List<TemplateContainer> containerV2s,
                                    String userName, long created) {
        return containerV2s
                .stream()
                .map(templateContainer -> mapToContainer(templateContainer, userName, created))
                .toList();
    }

    List<ContainerObject> mapToContainerObjectList(List<TemplateContainer> templateContainers,
                                                   String userName, long created) {

        List<TemplateContainerObject> templateContainerObjects = getAllTemplateContainerObjectV2(templateContainers);

        return templateContainerObjects
                .stream()
                .map(object -> mapToContainerObject(object, userName, created))
                .toList();
    }

    List<Content> mapToContents(List<TemplateContainerObject> templateContainerObjects,
                                String userName, long created) {
        return templateContainerObjects
                .stream()
                .map(objectV2 -> mapToContent(objectV2.getTemplateContent(), userName, created))
                .toList();
    }

    private ContainerObject mapToContainerObject(TemplateContainerObject objectV2, String userName, long created) {
        ContainerObject containerObject = new ContainerObject();
        containerObject.setUuid(objectV2.getId());
        containerObject.setCreated(created);
        containerObject.setCreatedBy(userName);
        containerObject.setObjectType(ContainerObject.ObjectTypeEnum.fromValue(objectV2.getObjectTypeEnum().name()));

        return containerObject;
    }

    private Container mapToContainer(TemplateContainer templateContainer, String userName, long created) {
        Container container = new Container();
        container.setOrdinal(templateContainer.getOrdinal());
        container.setCreatedBy(userName);
        container.setUuid(UUID.randomUUID().toString());
        container.setCreated(created);
        container.setContainerObjectsList(mapContainerObjectListToStringList(templateContainer.getTemplateContainerObjectList()));
        return container;
    }

    private Content mapToContent(TemplateContent templateContent, String userName, long created) {
        Content content = new Content();
        content.setContentType(Content.ContentTypeEnum.valueOf(templateContent.getContentType().name()));
        content.setUuid(templateContent.getId());
        content.setCreated(created);
        content.setCreatedBy(userName);
        setContentObject(templateContent, content);

        return content;
    }

    private List<String> mapContainerObjectListToStringList(List<TemplateContainerObject> list) {
        return list.stream().map(TemplateContainerObject::getId).toList();
    }

    private void setContentObject(TemplateContent templateContent, Content content) {
        switch(templateContent.getContentType()) {
            case HEADER -> setHeaderContent(templateContent.getTemplateHeaderContent(), content);
        }
    }

    private void setHeaderContent(TemplateHeaderContent templateHeaderContent, Content content) {
        HeaderContent headerContent = new HeaderContent();
        headerContent.setHeaderType(HeaderContent.HeaderTypeEnum.fromValue(
                templateHeaderContent.getTemplateHeaderType().name()));
        headerContent.setText(templateHeaderContent.getText());

        content.setHeaderContent(headerContent);
    }

}
