package se.b3.healthtech.blackbird.blbaggregator.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import se.b3.healthtech.blackbird.blbaggregator.domain.composite.Container;
import se.b3.healthtech.blackbird.blbaggregator.domain.composite.ContainerObject;
import se.b3.healthtech.blackbird.blbaggregator.integration.composition.ContainerObjectClient;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class ContainerObjectServiceTest {

    ContainerObjectService containerObjectService;
    ContainerObjectClient containerObjectClient;

    @BeforeEach
    public void setUp(){
        containerObjectClient = Mockito.mock(ContainerObjectClient.class);
        containerObjectService = new ContainerObjectService(containerObjectClient);

    }
    @Test
    public void deleteContainerObjectsIdsTest() {

        Container container = new Container();

        List<String> containnerObjectList = new ArrayList<>();
        containnerObjectList.add("71a3e3eb-6204-4103-a342-44dd50b6eeae");
        containnerObjectList.add("af5d9cff-059a-4182-afe5-0ea1fd57246c");
        containnerObjectList.add("ef12046d-bb21-4dc4-9a2e-2d636519d683");

        container.setContainerObjectsList(containnerObjectList);

        ContainerObject containerObject = new ContainerObject();
        containerObject.setUuid("ef12046d-bb21-4dc4-9a2e-2d636519d683");

        containerObjectService.deleteContainerObjectsIds(container, containerObject);

        assertEquals(2, containnerObjectList.size());

    }



}