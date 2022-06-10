package se.b3.healthtech.blackbird.blbaggregator.template.configuration;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import se.b3.healthtech.blackbird.blbaggregator.enums.ContentType;
import se.b3.healthtech.blackbird.blbaggregator.template.model.Template;
import se.b3.healthtech.blackbird.blbaggregator.template.model.TemplateContainer;
import se.b3.healthtech.blackbird.blbaggregator.template.model.TemplateContainerObject;

import java.util.*;

import static se.b3.healthtech.blackbird.blbaggregator.enums.CompositionType.*;


@Configuration
public class TemplateConfiguration {

    Logger logger = LoggerFactory.getLogger(TemplateConfiguration.class);

    public Template createTemplate() {

        Template template = new Template();
        logger.info("Creating a template");
        template.setId(UUID.randomUUID().toString());
        template.setTextName("ArtFakta");
        template.setContentType(ContentType.CONTENT);
        template.setCompositionType(COMPOSITION);
        template.setTemplateContainerList(createTemplateContainer());
        logger.info(template.toString());

        return template;

    }

    public List<TemplateContainer> createTemplateContainer() {

        List<TemplateContainer> templateContainerList = new ArrayList<>();

        TemplateContainer templateContainer1 = new TemplateContainer();
        logger.info("Creating a template container 1");
        templateContainer1.setId(UUID.randomUUID().toString());
        templateContainer1.setTextName("Taxoninformation");
        templateContainer1.setCreator("Iryna");
        templateContainer1.setCreated(1000L);
        templateContainer1.setOrdinal(1);
        templateContainer1.setContentType(ContentType.H2);
        templateContainer1.setCompositionType(CONTAINER);
        templateContainer1.setTemplateContainerObjectList(null);

        TemplateContainer templateContainer2 = new TemplateContainer();
        logger.info("Creating a template container 2");
        templateContainer2.setId(UUID.randomUUID().toString());
        templateContainer2.setTextName("Utseende och läte");
        templateContainer2.setCreator("Iryna");
        templateContainer2.setCreated(1000L);
        templateContainer2.setOrdinal(4);
        templateContainer2.setContentType(ContentType.H2);
        templateContainer2.setCompositionType(CONTAINER);
        templateContainer2.setTemplateContainerObjectList(createTemplateContainerObject());

        templateContainerList.add(templateContainer1);
        templateContainerList.add(templateContainer2);

        templateContainerList.sort(TemplateContainer.sortByOrdinal);

        return templateContainerList;
    }


    public List<TemplateContainerObject> createTemplateContainerObject() {

        List<TemplateContainerObject> templateContainerObjectList = new ArrayList<>();

        TemplateContainerObject templateContainerObject1 = new TemplateContainerObject();
        logger.info("Creating a template container object 1");
        templateContainerObject1.setId(UUID.randomUUID().toString());
        templateContainerObject1.setTextName("Utbredning i Sverige");
        templateContainerObject1.setCreator("Iryna");
        templateContainerObject1.setCreated(1000L);
        templateContainerObject1.setOrdinal(3);
        templateContainerObject1.setContentType(ContentType.H3);
        templateContainerObject1.setCompositionType(CONTAINER_OBJECT);

        TemplateContainerObject templateContainerObject2 = new TemplateContainerObject();
        logger.info("Creating a template container object 2");
        templateContainerObject2.setId(UUID.randomUUID().toString());
        templateContainerObject2.setTextName("Föda");
        templateContainerObject2.setCreator("Iryna");
        templateContainerObject2.setCreated(1000L);
        templateContainerObject2.setOrdinal(2);
        templateContainerObject2.setContentType(ContentType.H3);
        templateContainerObject2.setCompositionType(CONTAINER_OBJECT);

        TemplateContainerObject templateContainerObject3 = new TemplateContainerObject();
        logger.info("Creating a template container object 3");
        templateContainerObject3.setId(UUID.randomUUID().toString());
        templateContainerObject3.setCreator("Iryna");
        templateContainerObject3.setCreated(1000L);
        templateContainerObject3.setOrdinal(1);
        templateContainerObject3.setTextName("Häcking");
        templateContainerObject3.setContentType(ContentType.H3);
        templateContainerObject3.setCompositionType(CONTAINER_OBJECT);

        templateContainerObjectList.add(templateContainerObject1);
        templateContainerObjectList.add(templateContainerObject2);
        templateContainerObjectList.add(templateContainerObject3);

        templateContainerObjectList.sort(TemplateContainerObject.sortByOrdinal);

        return templateContainerObjectList;
    }


    @Bean("templateHashMap")
    public HashMap<String, Template> templateHashMap() {

        logger.info("Creating HashMap");
        HashMap<String, Template> templateHashmap = new HashMap<String, Template>();
        templateHashmap.put("1", createTemplate());

        return templateHashmap;

    }


}


