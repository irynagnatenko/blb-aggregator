package se.b3.healthtech.blackbird.blbaggregator.template.configuration;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import se.b3.healthtech.blackbird.blbaggregator.enums.ContentType;
import se.b3.healthtech.blackbird.blbaggregator.template.model.Template;
import se.b3.healthtech.blackbird.blbaggregator.template.model.TemplateContainer;
import se.b3.healthtech.blackbird.blbaggregator.template.model.TemplateContainerObject;

import java.time.LocalDateTime;
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
        template.setContentType(ContentType.H2);
        template.setCompositionType(COMPOSITION);
        template.setTemplateContainerList(createContainers());
        logger.info(template.toString());

        return template;

    }

    public List<TemplateContainer> createContainers() {

        List<TemplateContainer> templateContainerList = new ArrayList<>();

        TemplateContainer templateContainer1 = new TemplateContainer();
        logger.info("Creating a template container 1");
        templateContainer1.setId(UUID.randomUUID().toString());
        templateContainer1.setTextName("1 Taxoninformation");
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

        TemplateContainer templateContainer3 = new TemplateContainer();
        logger.info("Creating a template container 3");
        templateContainer3.setId(UUID.randomUUID().toString());
        templateContainer3.setTextName("Systematik");
        templateContainer3.setCreator("Iryna");
        templateContainer3.setCreated(1000L);
        templateContainer3.setOrdinal(5);
        templateContainer3.setContentType(ContentType.H2);
        templateContainer3.setCompositionType(CONTAINER);
        templateContainer3.setTemplateContainerObjectList(null);

        TemplateContainer templateContainer4 = new TemplateContainer();
        logger.info("Creating a template container 4");
        templateContainer4.setId(UUID.randomUUID().toString());
        templateContainer4.setTextName("Ekologi");
        templateContainer4.setCreator("Iryna");
        templateContainer4.setCreated(1000L);
        templateContainer4.setOrdinal(2);
        templateContainer4.setContentType(ContentType.H2);
        templateContainer4.setCompositionType(CONTAINER);
        templateContainer4.setTemplateContainerObjectList(createTemplateContainerObject());

        TemplateContainer templateContainer5 = new TemplateContainer();
        logger.info("Creating a template container 5");
        templateContainer5.setId(UUID.randomUUID().toString());
        templateContainer5.setTextName("Relation till människa");
        templateContainer5.setCreator("Iryna");
        templateContainer5.setCreated(1000L);
        templateContainer5.setOrdinal(3);
        templateContainer5.setContentType(ContentType.H2);
        templateContainer5.setCompositionType(CONTAINER);
        templateContainer5.setTemplateContainerObjectList(createTemplateContainerObject());

        templateContainerList.add(templateContainer1);
        templateContainerList.add(templateContainer2);
        templateContainerList.add(templateContainer3);
        templateContainerList.add(templateContainer4);
        templateContainerList.add(templateContainer5);

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

        TemplateContainerObject templateContainerObject4 = new TemplateContainerObject();
        logger.info("Creating a template container object 4");
        templateContainerObject4.setId(UUID.randomUUID().toString());
        templateContainerObject4.setTextName("Populationsutveckling, status och hot");
        templateContainerObject4.setCreator("Iryna");
        templateContainerObject4.setCreated(1000L);
        templateContainerObject4.setOrdinal(4);
        templateContainerObject4.setContentType(ContentType.H3);
        templateContainerObject4.setCompositionType(CONTAINER_OBJECT);

        TemplateContainerObject templateContainerObject5 = new TemplateContainerObject();
        logger.info("Creating a template container object 5");
        templateContainerObject5.setId(UUID.randomUUID().toString());
        templateContainerObject5.setTextName("Folktro");
        templateContainerObject5.setCreator("Iryna");
        templateContainerObject5.setCreated(1000L);
        templateContainerObject5.setOrdinal(5);
        templateContainerObject5.setContentType(ContentType.H3);
        templateContainerObject5.setCompositionType(CONTAINER_OBJECT);

        TemplateContainerObject templateContainerObject6 = new TemplateContainerObject();
        logger.info("Creating a template container object 6");
        templateContainerObject6.setId(UUID.randomUUID().toString());
        templateContainerObject6.setTextName("Namn");
        templateContainerObject6.setCreator("Iryna");
        templateContainerObject6.setCreated(1000L);
        templateContainerObject6.setOrdinal(6);
        templateContainerObject6.setContentType(ContentType.H3);
        templateContainerObject6.setCompositionType(CONTAINER_OBJECT);

        templateContainerObjectList.add(templateContainerObject1);
        templateContainerObjectList.add(templateContainerObject2);
        templateContainerObjectList.add(templateContainerObject3);
        templateContainerObjectList.add(templateContainerObject4);
        templateContainerObjectList.add(templateContainerObject5);
        templateContainerObjectList.add(templateContainerObject6);

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


