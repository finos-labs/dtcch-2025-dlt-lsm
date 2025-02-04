package io.builders.demo.integration

import com.fasterxml.jackson.databind.ObjectMapper
import groovy.json.JsonSlurper
import groovy.util.logging.Slf4j
import io.builders.demo.integration.model.IARequest
import jakarta.annotation.PostConstruct
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.beans.factory.annotation.Value
import org.springframework.stereotype.Component

@Component
@Slf4j
@SuppressWarnings(['NoDef', 'VariableTypeRequired', 'CatchException', 'MethodReturnTypeRequired'])
class GeminiIAAdapter implements IAPort {

    @Autowired
    IAConfiguration iaConfiguration

    HttpURLConnection connection

    @Value('${ia.url}')
    String url

    @Value('${ia.model}')
    String model

    @Value('${ia.apiKey}')
    String apiKey

    @Override
    List<String> obtainIACombination(IARequest request) {
        List<String> responseCombination = []
        ObjectMapper objectMapper = new ObjectMapper()
        String jsonString = objectMapper.writeValueAsString(request)
        def inputEscaped = jsonString.replaceAll('"', '\\\\"')
        def requestBodyString = "{ \"contents\": [ {\"parts\":[{\"text\":\"${inputEscaped}\"}]}]}"
        log.info("RequestBodyString ${requestBodyString}")
        try {
            connection.outputStream.withWriter('UTF-8') { it.write(requestBodyString) }
            def responseCode = connection.responseCode
            log.info("Response Code: ${responseCode}")
            log.debug("Response Message: ${connection.responseMessage}")
            def responseText = connection.inputStream.text
            log.debug("Response Text: ${responseText}")
            def jsonSlurper = new JsonSlurper()
            def responseJson = jsonSlurper.parseText(responseText)
            log.info("Response like Json: ${responseJson}")
            responseCombination = jsonSlurper.parseText(responseJson.candidates[0].content.parts[0].text)
        }
        catch (Exception e) {
            log.error("Exception when call to IA: ${e.cause} ${e.message}")
        }
        log.info("Response Combination: ${responseCombination}")
        return responseCombination
    }

    @PostConstruct
    init() {
        def apiUrl = "${this.url}/${this.model}:generateContent?key=${this.apiKey}"
        connection = (HttpURLConnection) new URL(apiUrl).openConnection()
        connection.requestMethod = 'POST'
        connection.doOutput = true
        connection.setRequestProperty('Content-Type', 'application/json')
    }

}
