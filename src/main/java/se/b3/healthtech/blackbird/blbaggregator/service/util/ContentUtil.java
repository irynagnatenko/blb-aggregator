package se.b3.healthtech.blackbird.blbaggregator.service.util;

import lombok.extern.slf4j.Slf4j;
import se.b3.healthtech.blackbird.blbaggregator.api.CreateContentRequest;
import se.b3.healthtech.blackbird.blbaggregator.domain.content.Content;

@Slf4j
public class ContentUtil {

    public static void setContentByContentType(Content content, CreateContentRequest contentRequest) {
        switch (content.getContentType()) {
            case HEADER -> setHeaderContent(content, contentRequest);
            case TEXT -> setTextContent(content, contentRequest);
            case LIST -> setListContent(content, contentRequest);
            case TABLE -> setTableContent(content, contentRequest);
        }
    }
    private static void setHeaderContent(Content content, CreateContentRequest contentRequest){
            content.setHeaderContent(contentRequest.headerContent());
            content.setTextContent(null);
            content.setListContent(null);
            content.setTableContent(null);
    }

    private static void setTextContent(Content content, CreateContentRequest contentRequest){
        content.setHeaderContent(null);
        content.setTextContent(contentRequest.textContent());
        content.setListContent(null);
        content.setTableContent(null);
    }

    private static void setListContent(Content content, CreateContentRequest contentRequest){
        content.setHeaderContent(null);
        content.setTextContent(null);
        content.setListContent(contentRequest.listContent());
        content.setTableContent(null);
    }

    private static void setTableContent(Content content, CreateContentRequest contentRequest){
        content.setHeaderContent(null);
        content.setTextContent(null);
        content.setListContent(null);
        content.setTableContent(contentRequest.tableContent());
    }



}
