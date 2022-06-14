package se.b3.healthtech.blackbird.blbaggregator.integration.composition;

import org.springframework.stereotype.Service;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;

@Service
public class BaseClient {

    MultiValueMap<String, String> createParameterKey(String key) {
        MultiValueMap<String, String> parameters = new LinkedMultiValueMap<>();
        parameters.add("key", key);
        return parameters;
    }
}
