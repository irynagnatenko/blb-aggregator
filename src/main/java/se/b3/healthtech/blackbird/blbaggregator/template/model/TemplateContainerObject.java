package se.b3.healthtech.blackbird.blbaggregator.template.model;

import lombok.Data;
import se.b3.healthtech.blackbird.blbaggregator.template.enums.CompositionType;
import se.b3.healthtech.blackbird.blbaggregator.template.enums.ObjectTypeEnum;
import se.b3.healthtech.blackbird.blbaggregator.template.enums.TemplateContent;

@Data
public class TemplateContainerObject {

    String id;
    String creator;
    CompositionType compositionType;
    ObjectTypeEnum objectTypeEnum;
    TemplateContent templateContent;


}
