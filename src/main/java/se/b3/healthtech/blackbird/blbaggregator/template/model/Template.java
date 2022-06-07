package se.b3.healthtech.blackbird.blbaggregator.template.model;

import lombok.Data;
import se.b3.healthtech.blackbird.blbaggregator.enums.CompositionType;
import se.b3.healthtech.blackbird.blbaggregator.enums.ContentType;
import se.b3.healthtech.blackbird.blbaggregator.enums.PublicationType;

import java.util.List;

@Data
public class Template {

    private String id;
    private String textName;
    private CompositionType compositionType;
    private PublicationType publicationType;
    private ContentType contentType;
    private List<TemplateContainer> templateContainerList;


}
