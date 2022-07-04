package se.b3.healthtech.blackbird.blbaggregator.api;

import se.b3.healthtech.blackbird.blbaggregator.domain.content.*;


public record CreateContentRequest(Content.ContentTypeEnum contentTypeEnum, HeaderContent headerContent,
                                   TextContent textContent, ListContent listContent, TableContent tableContent) {
}

