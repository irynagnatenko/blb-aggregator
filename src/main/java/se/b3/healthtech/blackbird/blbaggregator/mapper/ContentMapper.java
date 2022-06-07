package se.b3.healthtech.blackbird.blbaggregator.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.ValueMapping;
import se.b3.healthtech.blackbird.blbaggregator.domain.content.Content;
import se.b3.healthtech.blackbird.blbaggregator.template.model.Template;
import se.b3.healthtech.blackbird.blbaggregator.template.model.TemplateContainer;
import se.b3.healthtech.blackbird.blbaggregator.template.model.TemplateContainerObject;

@Mapper(componentModel = "spring")
public interface ContentMapper {

    @Mapping(target = "versionNumber", constant = "1")
    @Mapping(target = "commitNumber", constant = "1")
    @Mapping(target="createdBy", source="userName")
    @Mapping(target="contentType", constant="HEADER")
    Content mapTemplateToContent(Template source, Long created, String userName, String uuid, String title);

    @Mapping(target = "versionNumber", constant = "1")
    @Mapping(target = "commitNumber", constant = "1")
    @Mapping(target="createdBy", source="userName")
    @Mapping(target="contentType", constant="HEADER")
    Content mapTemplateContainerToContent(TemplateContainer source, Long created, String userName, String uuid);

    @Mapping(target = "versionNumber", constant = "1")
    @Mapping(target = "commitNumber", constant = "1")
    @Mapping(target="createdBy", source="userName")
    @Mapping(target="contentType", constant="HEADER")
    Content mapTemplateContainerObjectToContent(TemplateContainerObject source, Long created, String userName, String uuid);
}
