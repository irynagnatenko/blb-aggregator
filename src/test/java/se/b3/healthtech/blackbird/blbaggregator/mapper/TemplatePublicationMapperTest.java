package se.b3.healthtech.blackbird.blbaggregator.mapper;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.mapstruct.factory.Mappers;
import se.b3.healthtech.blackbird.blbaggregator.domain.composite.Container;
import se.b3.healthtech.blackbird.blbaggregator.domain.composite.ContainerObject;
import se.b3.healthtech.blackbird.blbaggregator.domain.composite.Publication;
import se.b3.healthtech.blackbird.blbaggregator.enums.CompositionType;
import se.b3.healthtech.blackbird.blbaggregator.enums.ContentType;
import se.b3.healthtech.blackbird.blbaggregator.template.model.Template;
import se.b3.healthtech.blackbird.blbaggregator.template.model.TemplateContainer;
import se.b3.healthtech.blackbird.blbaggregator.template.model.TemplateContainerObject;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static se.b3.healthtech.blackbird.blbaggregator.enums.CompositionType.*;

class TemplatePublicationMapperTest {

    private TemplatePublicationMapper mapper = Mappers.getMapper(TemplatePublicationMapper.class);

    private final String templateId = "1";
    private final String title = "Bird";
    private final int templateOrdinal = 1;
    private final String userName = "userName";
    private final int versionNumber = 1;
    private final int commitNumber = 1;
    private final long created = 10000L;


    @Test
    void mapTemplateToPublication() {
        String uuid = UUID.randomUUID().toString();

        Template template = new Template();

        template.setId(templateId);
        template.setTextName(title);
        template.setCompositionType(CompositionType.COMPOSITION);
        template.setContentType(ContentType.CONTENT);
        template.setTemplateContainerList(createTemplateContainer());

        Publication publication = mapper.mapTemplateToPublication(template, created, userName, uuid, title);

        assertEquals(uuid, publication.getUuid());
        assertEquals(template.getId(), publication.getTemplateId());
        assertEquals(template.getTextName(), publication.getTitle());
        assertEquals(userName, publication.getCreatedBy());
        assertEquals(created, publication.getCreated());
        assertEquals(template.getTemplateContainerList().size(), publication.getContainersIdList().size());

        List<String> idList = publication.getContainersIdList();
        idList.forEach(Assertions::assertNotNull);

    }

    @Test
    void mapTemplateContainerToContainer() {

        TemplateContainer templateContainer = new TemplateContainer();

        templateContainer.setId(templateId);
        templateContainer.setTextName(title);
        templateContainer.setCreator(userName);
        templateContainer.setCreated(created);
        templateContainer.setOrdinal(templateOrdinal);
        templateContainer.setCompositionType(CompositionType.CONTAINER);
        templateContainer.setContentType(ContentType.H2);
        templateContainer.setTemplateContainerObjectList(createTemplateContainerObject());

        Container container = mapper.mapTemplateContainerToContainer(templateContainer, created, userName);

        assertEquals(templateContainer.getOrdinal(), container.getOrdinal());
        assertEquals(templateContainer.getCreator(), container.getCreatedBy());
        assertEquals(templateContainer.getCreated(), container.getCreated());
        assertEquals(templateContainer.getTemplateContainerObjectList().size(), container.getContainerObjectsList().size());

        assertEquals(commitNumber, container.getCommitNumber());
        assertEquals(versionNumber, container.getVersionNumber());

        List<String> idList = container.getContainerObjectsList();
        idList.forEach(Assertions::assertNotNull);

    }


    @Test
    void mapTemplateContainerObjectToContainerObject() {

        TemplateContainerObject templateContainerObject = new TemplateContainerObject();

        templateContainerObject.setId(templateId);
        templateContainerObject.setTextName(title);
        templateContainerObject.setCreator(userName);
        templateContainerObject.setCreated(created);
        templateContainerObject.setOrdinal(templateOrdinal);
        templateContainerObject.setCompositionType(CompositionType.CONTAINER_OBJECT);
        templateContainerObject.setContentType(ContentType.H3);

        ContainerObject containerObject = mapper.mapTemplateContainerObjectToContainerObject(templateContainerObject, created, userName);

        assertEquals(templateContainerObject.getOrdinal(), containerObject.getOrdinal());
        assertEquals(templateContainerObject.getCreator(), containerObject.getCreatedBy());
        assertEquals(templateContainerObject.getCreated(), containerObject.getCreated());

        assertEquals(commitNumber, containerObject.getCommitNumber());
        assertEquals(versionNumber, containerObject.getVersionNumber());

    }

    private List<TemplateContainer> createTemplateContainer() {
        List<TemplateContainer> templateContainerList = new ArrayList<>();

        TemplateContainer templateContainer1 = new TemplateContainer();
        templateContainer1.setId(UUID.randomUUID().toString());
        templateContainer1.setTextName("Taxoninformation");
        templateContainer1.setCreator("Iryna");
        templateContainer1.setCreated(1000L);
        templateContainer1.setOrdinal(1);
        templateContainer1.setContentType(ContentType.H2);
        templateContainer1.setCompositionType(CONTAINER);
        templateContainer1.setTemplateContainerObjectList(null);

        TemplateContainer templateContainer2 = new TemplateContainer();
        templateContainer2.setId(UUID.randomUUID().toString());
        templateContainer2.setTextName("Utseende och läte");
        templateContainer2.setCreator("Iryna");
        templateContainer2.setCreated(1000L);
        templateContainer2.setOrdinal(4);
        templateContainer2.setContentType(ContentType.H2);
        templateContainer2.setCompositionType(CONTAINER);
        templateContainer2.setTemplateContainerObjectList(createTemplateContainerObject());

        TemplateContainer templateContainer3 = new TemplateContainer();
        templateContainer3.setId(UUID.randomUUID().toString());
        templateContainer3.setTextName("Systematik");
        templateContainer3.setCreator("Iryna");
        templateContainer3.setCreated(1000L);
        templateContainer3.setOrdinal(5);
        templateContainer3.setContentType(ContentType.H2);
        templateContainer3.setCompositionType(CONTAINER);
        templateContainer3.setTemplateContainerObjectList(null);

        TemplateContainer templateContainer4 = new TemplateContainer();
        templateContainer4.setId(UUID.randomUUID().toString());
        templateContainer4.setTextName("Ekologi");
        templateContainer4.setCreator("Iryna");
        templateContainer4.setCreated(1000L);
        templateContainer4.setOrdinal(2);
        templateContainer4.setContentType(ContentType.H2);
        templateContainer4.setCompositionType(CONTAINER);
        templateContainer4.setTemplateContainerObjectList(createTemplateContainerObject());

        TemplateContainer templateContainer5 = new TemplateContainer();
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
        templateContainerObject1.setId(UUID.randomUUID().toString());
        templateContainerObject1.setTextName("Utbredning i Sverige");
        templateContainerObject1.setCreator("Iryna");
        templateContainerObject1.setCreated(1000L);
        templateContainerObject1.setOrdinal(3);
        templateContainerObject1.setContentType(ContentType.H3);
        templateContainerObject1.setCompositionType(CONTAINER_OBJECT);

        TemplateContainerObject templateContainerObject2 = new TemplateContainerObject();
        templateContainerObject2.setId(UUID.randomUUID().toString());
        templateContainerObject2.setTextName("Föda");
        templateContainerObject2.setCreator("Iryna");
        templateContainerObject2.setCreated(1000L);
        templateContainerObject2.setOrdinal(2);
        templateContainerObject2.setContentType(ContentType.H3);
        templateContainerObject2.setCompositionType(CONTAINER_OBJECT);

        TemplateContainerObject templateContainerObject3 = new TemplateContainerObject();
        templateContainerObject3.setId(UUID.randomUUID().toString());
        templateContainerObject3.setCreator("Iryna");
        templateContainerObject3.setCreated(1000L);
        templateContainerObject3.setOrdinal(1);
        templateContainerObject3.setTextName("Häcking");
        templateContainerObject3.setContentType(ContentType.H3);
        templateContainerObject3.setCompositionType(CONTAINER_OBJECT);

        TemplateContainerObject templateContainerObject4 = new TemplateContainerObject();
        templateContainerObject4.setId(UUID.randomUUID().toString());
        templateContainerObject4.setTextName("Populationsutveckling, status och hot");
        templateContainerObject4.setCreator("Iryna");
        templateContainerObject4.setCreated(1000L);
        templateContainerObject4.setOrdinal(4);
        templateContainerObject4.setContentType(ContentType.H3);
        templateContainerObject4.setCompositionType(CONTAINER_OBJECT);

        TemplateContainerObject templateContainerObject5 = new TemplateContainerObject();
        templateContainerObject5.setId(UUID.randomUUID().toString());
        templateContainerObject5.setTextName("Folktro");
        templateContainerObject5.setCreator("Iryna");
        templateContainerObject5.setCreated(1000L);
        templateContainerObject5.setOrdinal(5);
        templateContainerObject5.setContentType(ContentType.H3);
        templateContainerObject5.setCompositionType(CONTAINER_OBJECT);

        TemplateContainerObject templateContainerObject6 = new TemplateContainerObject();
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


}