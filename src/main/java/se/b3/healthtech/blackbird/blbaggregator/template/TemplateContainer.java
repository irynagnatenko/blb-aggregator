package se.b3.healthtech.blackbird.blbaggregator.template;

import lombok.AllArgsConstructor;
import lombok.Data;
import se.b3.healthtech.blackbird.blbaggregator.enums.CompositionType;
import se.b3.healthtech.blackbird.blbaggregator.enums.ContentType;

import java.time.LocalDateTime;
import java.util.Comparator;
import java.util.List;

@Data
public class TemplateContainer {

    private String id;
    private String name;
    private String creator;
    private LocalDateTime created;
    private int ordinal;
    private ContentType contentType;
    private CompositionType compositionType;
    private List<TemplateContainerObject> templateContainerObjectList;

    public static Comparator<TemplateContainer> sortByOrdinal = new Comparator<>() {

        public int compare(TemplateContainer t1, TemplateContainer t2) {

            int ord1 = t1.getOrdinal();
            int ord2 = t2.getOrdinal();

            /*For ascending order*/
            return ord1-ord2;

            /*For descending order*/
            //ord2-ord1;
        }};

}