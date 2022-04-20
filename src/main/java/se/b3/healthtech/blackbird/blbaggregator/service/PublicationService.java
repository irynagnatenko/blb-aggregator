package se.b3.healthtech.blackbird.blbaggregator.service;

import lombok.extern.slf4j.Slf4j;
import org.mapstruct.factory.Mappers;
import org.springframework.stereotype.Service;
import se.b3.healthtech.blackbird.blbaggregator.domain.composite.Container;
import se.b3.healthtech.blackbird.blbaggregator.domain.composite.ContainerObject;
import se.b3.healthtech.blackbird.blbaggregator.domain.composite.Publication;
import se.b3.healthtech.blackbird.blbaggregator.mapper.TemplatePublicationMapper;
import se.b3.healthtech.blackbird.blbaggregator.template.model.Template;
import se.b3.healthtech.blackbird.blbaggregator.template.model.TemplateContainer;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Slf4j
@Service
public class PublicationService {

    private TemplatePublicationMapper mapper = Mappers.getMapper(TemplatePublicationMapper.class);

    private final TemplateService templateService;

    private List<Container> containerList = new ArrayList<>();
    private List<ContainerObject> containerObjectList = new ArrayList<>();

    public PublicationService(TemplateService templateService) {
        this.templateService = templateService;
    }

    public String createPublication(String userName, String templateId, String title) {
        log.info("init");

        Template template = templateService.getTemplate(templateId);
        log.info("Antal containers i template: {}", template.getTemplateContainerList().size());

        Publication publication = createPublication(template, userName, title);

        createContainers(publication, template.getTemplateContainerList(), userName);

        return null;
    }

    Publication createPublication(Template template, String userName, String title) {
        return mapper.mapToPublication(template, 1000L, userName,
                UUID.randomUUID().toString(), title);
    }

    // nollställer gamla och sätter nya id på containrar i publicationsobjekt
    void createContainers(Publication publication, List<TemplateContainer> templateContainerList, String userName) {
        publication.setContainersIdList(null);

        containerList = mapper.mapToContainerList(templateContainerList, 1000L, userName);

        containerList.forEach(c -> {
            c.setUuid(UUID.randomUUID().toString());
            publication.getContainersIdList().add(c.getUuid());
        });

    }
/*
    private final TemplateService templateService;

    public String createPublication(String userName, String templateId, String title) {
        log.info("init PublicationService");
        Template template = templateService.getTemplate(templateId);
        return null;
    }

 */

}
