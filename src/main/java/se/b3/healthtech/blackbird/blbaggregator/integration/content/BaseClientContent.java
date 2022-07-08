package se.b3.healthtech.blackbird.blbaggregator.integration.content;

import org.springframework.stereotype.Service;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;

@Service
public class BaseClientContent {
    MultiValueMap<String, String> createParameterKey(String key) {
        MultiValueMap<String, String> parameters = new LinkedMultiValueMap<>();
        parameters.add("key", key);
        return parameters;
    }

    MultiValueMap<String, String> createParameterKey(String key, String id) {
        MultiValueMap<String, String> parameters = new LinkedMultiValueMap<>();
        parameters.add("key", key);
        parameters.add("id", id);

        return parameters;
    }


}
