package se.b3.healthtech.blackbird.blbaggregator.template.enums;

import lombok.Getter;
import lombok.Setter;
import se.b3.healthtech.blackbird.blbaggregator.template.model.TemplateHeaderContent;

@Getter
@Setter
public class TemplateContent {
    String id;
    String creator;
    ContentType contentType;
    TemplateHeaderContent templateHeaderContent;

    public TemplateContent(String id, String creator, ContentType header, TemplateHeaderContent templateHeaderContent) {
        this.id = id;
        this.creator = creator;
        contentType = header;
        this.templateHeaderContent = templateHeaderContent;
    }

}
