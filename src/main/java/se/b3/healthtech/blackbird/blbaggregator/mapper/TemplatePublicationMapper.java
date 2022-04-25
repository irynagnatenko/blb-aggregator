package se.b3.healthtech.blackbird.blbaggregator.mapper;

import org.mapstruct.Mapper;
import se.b3.healthtech.blackbird.blbaggregator.domain.composite.Container;
import se.b3.healthtech.blackbird.blbaggregator.domain.composite.ContainerObject;
import se.b3.healthtech.blackbird.blbaggregator.domain.composite.Publication;
import se.b3.healthtech.blackbird.blbaggregator.template.model.Template;
import se.b3.healthtech.blackbird.blbaggregator.template.model.TemplateContainer;
import se.b3.healthtech.blackbird.blbaggregator.template.model.TemplateContainerObject;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Mapper(componentModel = "spring")
public abstract class TemplatePublicationMapper {

    abstract List<String> getContainersIdList(List<TemplateContainer> templateContainerList);

    abstract List<String> getContainerObjectsList(List<TemplateContainerObject> templateContainerObjectList);

    String map(TemplateContainer value) {
        return value.getId();
    }

    String map(TemplateContainerObject value) {
        return value.getId();
    }

    public Publication mapTemplateToPublication(Template source, long created, String userName,
                                                String uuid, String title) {

        Publication target = new Publication();

        target.setUuid(uuid);
        target.setCreatedBy(userName);
        target.setCreated(created);
        target.setTemplateId(source.getId());
        target.setTitle(title);
        target.setContainersIdList(getContainersIdList(source.getTemplateContainerList()));

        return target;
    }

    public Container mapTemplateContainerToContainer(TemplateContainer source, long created, String userName) {

        Container targetContainer = new Container();

        if (source != null) {
            targetContainer.setUuid(UUID.randomUUID().toString());
            targetContainer.setCreated(created);
            targetContainer.setOrdinal(source.getOrdinal());
            targetContainer.setContainerObjectsList(getContainerObjectsList(source.getTemplateContainerObjectList()));

        }
        if (userName != null) {
            targetContainer.setCreatedBy(userName);
        }
        targetContainer.setVersionNumber(1);
        targetContainer.setCommitNumber(1);

        return targetContainer;
    }


    public ContainerObject mapTemplateContainerObjectToContainerObject(TemplateContainerObject source, long created, String userName) {

        ContainerObject targetContainerObject = new ContainerObject();

        if (source != null) {
            targetContainerObject.setUuid(UUID.randomUUID().toString());
            targetContainerObject.setCreated(created);
            targetContainerObject.setOrdinal(source.getOrdinal());

        }
        if (userName != null) {
            targetContainerObject.setCreatedBy(userName);
        }
        targetContainerObject.setVersionNumber(1);
        targetContainerObject.setCommitNumber(1);

        return targetContainerObject;
    }


    public List<Container> mapToContainerList(List<TemplateContainer> templateContainerList, long created, String userName) {
        return templateContainerList.stream()
                .map((TemplateContainer source) -> mapTemplateContainerToContainer(source, created, userName)).collect(Collectors.toList());

    }


    public List<ContainerObject> mapToContainerObjectList(List<TemplateContainerObject> containerObjectList, long created, String userName) {
        return containerObjectList.stream()
                .map((TemplateContainerObject source) -> mapTemplateContainerObjectToContainerObject(source, created, userName)).collect(Collectors.toList());
    }
}
