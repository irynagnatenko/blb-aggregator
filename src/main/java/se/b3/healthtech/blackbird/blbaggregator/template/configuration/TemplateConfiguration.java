package se.b3.healthtech.blackbird.blbaggregator.template.configuration;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import se.b3.healthtech.blackbird.blbaggregator.template.model.Template;

import java.util.*;

@Configuration
public class TemplateConfiguration {

    Logger logger = LoggerFactory.getLogger(TemplateConfiguration.class);
    private final Map<String, Template> templateMap = new HashMap<String, Template>();

    @Bean("template")
    public Map<String, Template> template() {
        logger.info("Loading templateMap with default data");
        templateMap.put("1", TemplateDataCreator.createTemplateV2());
        logger.info("Loaded {} templateRows ", templateMap.size());
        return templateMap;
    }

}


