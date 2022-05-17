package se.b3.healthtech.blackbird.blbaggregator.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import se.b3.healthtech.blackbird.blbaggregator.domain.composite.Container;
import se.b3.healthtech.blackbird.blbaggregator.domain.composite.ContainerObject;
import se.b3.healthtech.blackbird.blbaggregator.domain.composite.Publication;
import se.b3.healthtech.blackbird.blbaggregator.integration.WebClientImplementation;
import se.b3.healthtech.blackbird.blbaggregator.template.configuration.TemplateConfiguration;

import java.util.*;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;

class PublicationServiceTest {

    private static final String UUID1 = "1";
    private static final String UUID2 = "2";
    PublicationService publicationService;
    TemplateConfiguration templateConfiguration;
    TemplateService templateService;
    WebClientImplementation webClientImplementationMock;


    @BeforeEach
    public void setUp() {
        templateService = new TemplateService();
        templateConfiguration = new TemplateConfiguration();
        webClientImplementationMock = Mockito.mock(WebClientImplementation.class);

        publicationService = new PublicationService(templateService, webClientImplementationMock);

    }

    @Test
    public void setUuidTest() {
        Publication publication = new Publication();

        Map<Container, List<ContainerObject>> containerContainerObjectMap = new HashMap<>();

        Container container1 = new Container();
        container1.setUuid(UUID1);
        Container container2 = new Container();
        container2.setUuid(UUID2);

        publication.setContainersIdList(new ArrayList<String>(Arrays.asList("1", "2")));
        containerContainerObjectMap.put(container1, Arrays.asList(new ContainerObject(), new ContainerObject()));
        containerContainerObjectMap.put(container2, Arrays.asList(new ContainerObject(), new ContainerObject()));

        publicationService.setUuid(publication, containerContainerObjectMap);

        assertEquals(2, publication.getContainersIdList().size());
        assertEquals(2, container1.getContainerObjectsList().size());
        assertEquals(2, container2.getContainerObjectsList().size());

        assertNotEquals(UUID1, container1.getUuid());

    }


}