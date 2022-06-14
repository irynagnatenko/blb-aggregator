package se.b3.healthtech.blackbird.blbaggregator.template.configuration;

import se.b3.healthtech.blackbird.blbaggregator.template.enums.*;
import se.b3.healthtech.blackbird.blbaggregator.template.model.Template;
import se.b3.healthtech.blackbird.blbaggregator.template.model.TemplateContainer;
import se.b3.healthtech.blackbird.blbaggregator.template.model.TemplateContainerObject;
import se.b3.healthtech.blackbird.blbaggregator.template.model.TemplateHeaderContent;

import java.util.ArrayList;
import java.util.List;

public class TemplateDataCreator {

    public static Template createTemplateV2() {
        Template template = new Template();
        template.setAuthor("TemplateAuthor");
        template.setTemplateContainerList(createTemplateContainerV2());
        template.setId("1");
        template.setTitle("mall");
        template.setCompositionType(CompositionType.COMPOSITION);
        template.setTemplatePublicationType(TemplatePublicationType.ART_FAKTA);

        return template;
    }

    private static List<TemplateContainer> createTemplateContainerV2() {
        List<TemplateContainer> containerV2s = new ArrayList<>();

        TemplateContainer tc1 = new TemplateContainer();
        tc1.setCreator("ContainerAuthor");
        tc1.setCompositionType(CompositionType.CONTAINER);
        tc1.setId("c1");
        tc1.setOrdinal(1);
        tc1.setTemplateContainerObjectList(createTemplateContainerObjectForContainer1());
        containerV2s.add(tc1);

        TemplateContainer tc2 = new TemplateContainer();
        tc2.setCreator("ContainerAuthor");
        tc2.setCompositionType(CompositionType.CONTAINER);
        tc2.setId("c2");
        tc2.setOrdinal(2);
        tc2.setTemplateContainerObjectList(createTemplateContainerObjectForContainer2());
        containerV2s.add(tc2);

        TemplateContainer tc3 = new TemplateContainer();
        tc3.setCreator("ContainerAuthor");
        tc3.setCompositionType(CompositionType.CONTAINER);
        tc3.setId("c3");
        tc3.setOrdinal(3);
        tc3.setTemplateContainerObjectList(createTemplateContainerObjectForContainer3());
        containerV2s.add(tc3);

        return containerV2s;
    }

    private static List<TemplateContainerObject> createTemplateContainerObjectForContainer1() {
        List<TemplateContainerObject> list = new ArrayList<>();
        TemplateContainerObject tco1 = new TemplateContainerObject();
        tco1.setId("co1");
        tco1.setCreator("ContainerObjectAuthor");
        tco1.setCompositionType(CompositionType.CONTAINER_OBJECT);
        tco1.setObjectTypeEnum(ObjectTypeEnum.CONTENT);
        tco1.setTemplateContent(new TemplateContent(
                                "C1",
                            "ContentAuthor",
                                    ContentType.HEADER,
                                    new TemplateHeaderContent(
                                            TemplateHeaderTypeEnum.TWO,
                                            "Fältkännetecken")));
        list.add(tco1);

        TemplateContainerObject tco2 = new TemplateContainerObject();
        tco2.setId("co2");
        tco2.setCreator("ContainerObjectAuthor");
        tco2.setCompositionType(CompositionType.CONTAINER_OBJECT);
        tco2.setObjectTypeEnum(ObjectTypeEnum.CONTENT);
        tco2.setTemplateContent(new TemplateContent(
                                "C2",
                                "ContentAuthor",
                                ContentType.HEADER, new TemplateHeaderContent(
                                        TemplateHeaderTypeEnum.THREE, "Utseende")));
        list.add(tco2);

        TemplateContainerObject tco3 = new TemplateContainerObject();
        tco3.setId("co3");
        tco3.setCreator("ContainerObjectAuthor");
        tco3.setCompositionType(CompositionType.CONTAINER_OBJECT);
        tco3.setObjectTypeEnum(ObjectTypeEnum.CONTENT);
        tco3.setTemplateContent(new TemplateContent(
                                "C3",
                                "ContentAuthor",
                                ContentType.HEADER,
                                new TemplateHeaderContent(
                                    TemplateHeaderTypeEnum.THREE, "Läte")));
        list.add(tco3);

        return list;
    }

    private static List<TemplateContainerObject> createTemplateContainerObjectForContainer2() {
        List<TemplateContainerObject> list = new ArrayList<>();
        TemplateContainerObject tco4 = new TemplateContainerObject();
        tco4.setId("tco4");
        tco4.setCreator("ContainerObjectAuthor");
        tco4.setCompositionType(CompositionType.CONTAINER_OBJECT);
        tco4.setObjectTypeEnum(ObjectTypeEnum.CONTENT);
        tco4.setTemplateContent(new TemplateContent(
                                "C4",
                                "ContentAuthor",
                                ContentType.HEADER,
                                new TemplateHeaderContent(
                                    TemplateHeaderTypeEnum.TWO, "Utbredning")));
        list.add(tco4);

        TemplateContainerObject tco5 = new TemplateContainerObject();
        tco5.setId("tco5");
        tco5.setCreator("ContainerObjectAuthor");
        tco5.setCompositionType(CompositionType.CONTAINER_OBJECT);
        tco5.setObjectTypeEnum(ObjectTypeEnum.CONTENT);
        tco5.setTemplateContent(new TemplateContent(
                                "C5",
                                "ContentAuthor",
                                ContentType.HEADER,
                                new TemplateHeaderContent(
                                    TemplateHeaderTypeEnum.THREE, "Allmänt")));
        list.add(tco5);

        TemplateContainerObject tco6 = new TemplateContainerObject();
        tco6.setId("tco6");
        tco6.setCreator("ContainerObjectAuthor");
        tco6.setCompositionType(CompositionType.CONTAINER_OBJECT);
        tco6.setObjectTypeEnum(ObjectTypeEnum.CONTENT);
        tco6.setTemplateContent(new TemplateContent(
                                "C6",
                                "ContentAuthor",
                                ContentType.HEADER,
                                new TemplateHeaderContent(
                                    TemplateHeaderTypeEnum.THREE, "Svensk utbredning")));
        list.add(tco6);

        TemplateContainerObject tco7 = new TemplateContainerObject();
        tco7.setId("tco7");
        tco7.setCreator("ContainerObjectAuthor");
        tco7.setCompositionType(CompositionType.CONTAINER_OBJECT);
        tco7.setObjectTypeEnum(ObjectTypeEnum.CONTENT);
        tco7.setTemplateContent(new TemplateContent(
                            "C7",
                            "ContentAuthor",
                            ContentType.HEADER,
                            new TemplateHeaderContent(
                                TemplateHeaderTypeEnum.THREE, "Svensk migration")));
        list.add(tco7);

        TemplateContainerObject tco8 = new TemplateContainerObject();
        tco8.setId("tco8");
        tco8.setCreator("ContainerObjectAuthor");
        tco8.setCompositionType(CompositionType.CONTAINER_OBJECT);
        tco8.setObjectTypeEnum(ObjectTypeEnum.CONTENT);
        tco8.setTemplateContent(new TemplateContent(
                            "C8",
                            "ContentAuthor",
                            ContentType.HEADER,
                            new TemplateHeaderContent(
                                TemplateHeaderTypeEnum.THREE, "Habitat")));
        list.add(tco8);

        return list;
    }

    private static List<TemplateContainerObject> createTemplateContainerObjectForContainer3() {
        List<TemplateContainerObject> list = new ArrayList<>();
        TemplateContainerObject tco9 = new TemplateContainerObject();
        tco9.setId("tco9");
        tco9.setCreator("ContainerObjectAuthor");
        tco9.setCompositionType(CompositionType.CONTAINER_OBJECT);
        tco9.setObjectTypeEnum(ObjectTypeEnum.CONTENT);
        tco9.setTemplateContent(new TemplateContent(
                            "C9",
                            "ContentAuthor",
                            ContentType.HEADER,
                            new TemplateHeaderContent(
                              TemplateHeaderTypeEnum.TWO, "Systematik")));
        list.add(tco9);

        return list;
    }


}
