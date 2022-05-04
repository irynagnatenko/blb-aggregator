package se.b3.healthtech.blackbird.blbaggregator.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import se.b3.healthtech.blackbird.blbaggregator.domain.composite.Container;
import se.b3.healthtech.blackbird.blbaggregator.domain.composite.ContainerObject;
import se.b3.healthtech.blackbird.blbaggregator.domain.composite.Publication;
import se.b3.healthtech.blackbird.blbaggregator.template.model.Template;
import se.b3.healthtech.blackbird.blbaggregator.template.model.TemplateContainer;
import se.b3.healthtech.blackbird.blbaggregator.template.model.TemplateContainerObject;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

@Mapper(componentModel = "spring")
public interface TemplatePublicationMapper {
    List<String> getContainerIds(List<TemplateContainer> templateContainerList);
    List<String> getContainerObjectIds(List<TemplateContainerObject> templateContainerObjectList);

    default String map(TemplateContainer value) {
        return value.getId();
    }
    default String map(TemplateContainerObject value) {
        return value.getId();
    }

    @Mapping(target = "versionNumber", constant = "1")
    @Mapping(target = "commitNumber", constant = "1")
    @Mapping(target="createdBy", source="userName")
    @Mapping(target="templateId", source="source.id")
    @Mapping(target="containersIdList", source="source.templateContainerList")
    Publication mapToPublication(Template source, long created, String userName,
                                 String uuid, String title);

    default Map<Container, List<ContainerObject>> mapToContainerAndContainerObject(List<TemplateContainer> sourceList,
                                                                                   String userName, long created) {
        TreeMap<String, Container> containerMap = mapToContainers(sourceList, userName, created);
        TreeMap<String, ContainerObject> containerObjectMap = mapToContainerObjects(sourceList, userName, created);

        return mapContainerAndContainerObject(containerMap, containerObjectMap);
    }

    private TreeMap<String, ContainerObject> mapToContainerObjects(List<TemplateContainer> templateContainerList,
                                                                   String userName, long created) {
        TreeMap<String, TemplateContainer> templateContainerMap = new TreeMap<>();
        TreeMap<String, ContainerObject> containerObjectMap = new TreeMap<>();

        templateContainerList.forEach(tc -> templateContainerMap.put(tc.getId(), tc));

        templateContainerMap.forEach((k, v) -> {
                if(v.getTemplateContainerObjectList() != null) {
                    containerObjectMap.putAll(mapContainerObjects(v.getTemplateContainerObjectList(), userName, created));
                }
        });

        return containerObjectMap;
    }

    private TreeMap<String, Container> mapToContainers(List<TemplateContainer> source, String userName, long created) {
        TreeMap<String, Container> targetMap = new TreeMap<>();

        for(TemplateContainer templateContainer : source) {
            Container c = map(templateContainer, userName, created);
            targetMap.put(c.getUuid(),c);
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
                                                                 String userName, long created) {
        TreeMap<String, ContainerObject> targetMap = new TreeMap<>();

        for(TemplateContainerObject source : sourceList) {
            ContainerObject target = map(source, userName, created);
            targetMap.put(target.getUuid(),target);
        }
        return targetMap;
    }

    private Container map(TemplateContainer source, String userName, long created) {
        Container target = new Container();
        target.setCreatedBy(userName);
        target.setContainerObjectsList(getContainerObjectIds(source.getTemplateContainerObjectList()));
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
