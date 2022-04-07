package se.b3.healthtech.blackbird.blbaggregator.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import se.b3.healthtech.blackbird.blbaggregator.exception.TemplateNotFoundException;
import se.b3.healthtech.blackbird.blbaggregator.template.Template;

import java.util.HashMap;

@Service
public class TemplateService {

    Logger logger = LoggerFactory.getLogger(TemplateService.class);

    @Autowired
    private HashMap<String, Template> templateHashMap;

    public Template getTemplate(String templateId) {
        logger.info("getTemplate Service");

        if (templateHashMap.containsKey(templateId)) {
            return templateHashMap.get(templateId);
        } else {
            throw new TemplateNotFoundException("Template not found in the HashMap");
        }
    }


}
