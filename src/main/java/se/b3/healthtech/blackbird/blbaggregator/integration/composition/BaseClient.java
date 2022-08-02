package se.b3.healthtech.blackbird.blbaggregator.integration.composition;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;

import java.util.List;

@Slf4j
@Service
public class BaseClient {

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

    MultiValueMap<String, String> createParameterKey(String key, List<String> uuids) {
        log.info("uuid" + uuids);

        MultiValueMap<String, String> parameters = new LinkedMultiValueMap<>();
        parameters.add("key", key);
        parameters.add("uuids", uuids.toString());

        log.info("uuid to string" + uuids.toString());
        return parameters;
    }

}
