package se.b3.healthtech.blackbird.blbaggregator.template.model;

import lombok.*;
import se.b3.healthtech.blackbird.blbaggregator.template.enums.CompositionType;
import se.b3.healthtech.blackbird.blbaggregator.template.enums.TemplatePublicationType;

import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Template {
    String id;
    String title;
    TemplatePublicationType templatePublicationType;
    String author;
    CompositionType compositionType;
    List<TemplateContainer> templateContainerList;
}
