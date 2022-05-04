package se.b3.healthtech.blackbird.blbaggregator.api;

import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.*;
import se.b3.healthtech.blackbird.blbaggregator.service.TemplateService;
import se.b3.healthtech.blackbird.blbaggregator.template.model.Template;

@RestController
@RequestMapping("/api/blackbird")
@RequiredArgsConstructor
public class TemplateController {
    Logger logger = LoggerFactory.getLogger(TemplateController.class);

    private final TemplateService templateService;

    @GetMapping("/template/{templateId}")
    public Template getTemplate(@RequestParam String templateId) {
        logger.info("getTemplate");
        return templateService.getTemplate(templateId);
    }
}
