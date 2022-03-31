package se.b3.healthtech.blackbird.blbaggregator;

import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
@RequestMapping("/api/blackbird")
public class TestController {

    @GetMapping("/all")
    public String getAll() {
        log.info("test");
        log.error("error");
        return "utskrift från get-metod";
    }


}
