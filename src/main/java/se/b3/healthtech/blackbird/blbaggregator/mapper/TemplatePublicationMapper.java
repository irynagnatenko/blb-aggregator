package se.b3.healthtech.blackbird.blbaggregator.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import se.b3.healthtech.blackbird.blbaggregator.domain.composite.Container;
import se.b3.healthtech.blackbird.blbaggregator.domain.composite.ContainerObject;
import se.b3.healthtech.blackbird.blbaggregator.domain.composite.Publication;
import se.b3.healthtech.blackbird.blbaggregator.domain.content.Content;
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


}
