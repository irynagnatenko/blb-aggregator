package se.b3.healthtech.blackbird.blbaggregator.api.response;

import se.b3.healthtech.blackbird.blbaggregator.domain.composite.Container;
import se.b3.healthtech.blackbird.blbaggregator.domain.composite.ContainerObject;
import se.b3.healthtech.blackbird.blbaggregator.domain.composite.Publication;
import se.b3.healthtech.blackbird.blbaggregator.domain.content.Content;

import java.util.List;


public record PublicationResponse(Publication publication,
                                  List<Container> containerList,
                                  List<ContainerObject> containerObjectList,
                                  List<Content> contentList){

}
