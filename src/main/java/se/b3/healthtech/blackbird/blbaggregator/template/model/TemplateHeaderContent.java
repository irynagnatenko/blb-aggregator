package se.b3.healthtech.blackbird.blbaggregator.template.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import se.b3.healthtech.blackbird.blbaggregator.template.enums.TemplateHeaderTypeEnum;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class TemplateHeaderContent {
    TemplateHeaderTypeEnum templateHeaderType;
    String text;

}
