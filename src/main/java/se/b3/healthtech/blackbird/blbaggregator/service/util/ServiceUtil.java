package se.b3.healthtech.blackbird.blbaggregator.service.util;

import lombok.extern.slf4j.Slf4j;
import se.b3.healthtech.blackbird.blbaggregator.domain.composite.Container;
import se.b3.healthtech.blackbird.blbaggregator.domain.composite.ContainerObject;
import se.b3.healthtech.blackbird.blbaggregator.domain.content.Content;
import se.b3.healthtech.blackbird.blbaggregator.exception.ContainerNotFoundException;
import se.b3.healthtech.blackbird.blbaggregator.exception.ContainerObjectNotFoundException;
import se.b3.healthtech.blackbird.blbaggregator.exception.ContentNotFoundException;
import se.b3.healthtech.blackbird.blbaggregator.template.model.TemplateContainer;

import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.Comparator;
import java.util.List;

@Slf4j
public class ServiceUtil {

    public static Container findContainerById(List<Container> containers, String id) {
        return containers.stream()
                .filter(c -> id.equals(c.getUuid()))
                .findAny()
                .orElseThrow(() -> new ContainerNotFoundException("Container with id "+id +" not exist"));
    }

    public static Content findContentById(List<Content> contents, String id) {
        return contents.stream()
                .filter(c -> id.equals(c.getUuid()))
                .findAny()
                .orElseThrow(() -> new ContentNotFoundException("Content with id "+id +" not exist"));
    }

    public static ContainerObject findContainerObjectById(List<ContainerObject> containerObjects, String id) {
        return containerObjects.stream()
                .filter(c -> id.equals(c.getUuid()))
                .findAny()
                .orElseThrow(() -> new ContainerObjectNotFoundException("ContainerObject with id "+id +" not exist"));
    }

    public static Comparator<Container> sortByOrdinal = new Comparator<>() {

        @Override
        public int compare(Container c1, Container c2) {
            int ord1 = c1.getOrdinal();
            int ord2 = c2.getOrdinal();
            return ord1 - ord2;
        }
    };

    public static long setCreatedTime() {
        ZoneId zone = ZoneId.of("Europe/Stockholm");
        ZonedDateTime date = ZonedDateTime.now(zone);
        log.info("ZonedDateTime with zone : {}", date);
        return date.toInstant().toEpochMilli();
    }

/*
    public Long dateCreated() {
        ZonedDateTime nowEuropeStockholm = ZonedDateTime.now(ZoneId.of("Europe/Stockholm"));
        return nowEuropeStockholm.toInstant().toEpochMilli();
    }

 */

}