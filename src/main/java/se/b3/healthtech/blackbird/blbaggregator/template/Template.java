package se.b3.healthtech.blackbird.blbaggregator.template;

import lombok.Data;
import se.b3.healthtech.blackbird.blbaggregator.enums.CompositionType;
import se.b3.healthtech.blackbird.blbaggregator.enums.ContentType;

import java.util.List;

@Data
public class Template {

    private String id;
    private String name;
    private CompositionType compositionType;
    private ContentType contentType;
    private List<TemplateContainer> templateContainerList;



}
