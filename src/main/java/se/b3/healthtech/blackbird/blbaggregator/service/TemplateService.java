package se.b3.healthtech.blackbird.blbaggregator.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import se.b3.healthtech.blackbird.blbaggregator.template.exception.TemplateNotFoundException;
import se.b3.healthtech.blackbird.blbaggregator.template.model.Template;

import java.util.Map;

@Slf4j
@Service
public class TemplateService {

    private final Map<String, Template> template;
    public TemplateService(Map<String, Template> template) {
        this.template = template;
    }

    public Template getTemplate(String templateId) {
        log.info("Service - TemplateService, getTemplate by Id: {}", templateId);
        if(template.containsKey(templateId)) {
            return template.get(templateId);
        } else {
            throw new TemplateNotFoundException("TemplateId "+templateId+" does not exist in TemplateMap");
        }
    }


}
