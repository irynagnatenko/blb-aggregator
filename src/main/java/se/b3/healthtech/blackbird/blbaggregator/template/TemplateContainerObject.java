package se.b3.healthtech.blackbird.blbaggregator.template;

import lombok.Data;
import se.b3.healthtech.blackbird.blbaggregator.enums.CompositionType;
import se.b3.healthtech.blackbird.blbaggregator.enums.ContentType;

import java.time.LocalDateTime;
import java.util.Comparator;

@Data
public class TemplateContainerObject {

    private String id;
    private String name;
    private String creator;
    private LocalDateTime created;
    private int ordinal;
    private ContentType contentType;
    private CompositionType compositionType;

    public static Comparator<TemplateContainerObject> sortByOrdinal = new Comparator<>() {

        public int compare(TemplateContainerObject t1, TemplateContainerObject t2) {

            int ord1 = t1.getOrdinal();
            int ord2 = t2.getOrdinal();

            /*For ascending order*/
            return ord1-ord2;

            /*For descending order*/
            //ord2-ord1;
        }};

}
