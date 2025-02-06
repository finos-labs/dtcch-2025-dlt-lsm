package io.builders.demo.integration

import com.fasterxml.jackson.databind.ObjectMapper
import io.builders.demo.integration.model.IARequest
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.beans.factory.annotation.Value
import org.springframework.stereotype.Component

import groovy.json.JsonOutput
import groovy.json.JsonSlurper
import groovy.util.logging.Slf4j

@Component
@Slf4j
@SuppressWarnings(['NoDef', 'VariableTypeRequired', 'CatchException', 'MethodReturnTypeRequired', 'LineLength', 'ThrowException', 'UnnecessaryGString', 'SpaceAroundMapEntryColon'])
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
        def requestBodyMap = [
            contents: [
                [
                    parts: [[text: "Only return the settlement IDs, " + inputEscaped]]
                ]
            ],
            systemInstruction: [
                role : "user",
                parts: [[
                            text: """You are an expert in solving optimization problems. You need to determine the optimal combination of settlements to process, given liquidity constraints in two currencies (CASH and TOKEN).

                    I want to maximize the number of settlements processed while minimizing needed liquidity balance. Always keep in mind that if a settlement in the combination provides liquidity to a client, that has to be taken into account and the balance may not need to be above the amount of either cash or token.

                    The data is structured as a JSON object with two main attributes: balances and settlements.

                    1. balances: This attribute holds an array of client balance objects. Each object within this array represents the balances for a single client and has the following attributes:

                    - client: A string representing the client's identifier (e.g., "1", "2", "3").
                    - cash: A number (integer or float) representing the client's cash balance.
                    - token: A number (integer or float) representing the client's token balance.

                    2. settlements: This attribute holds an array of settlement objects. Each object within this array represents a single settlement transaction and has the following attributes:

                    - id: A string representing the unique identifier for the settlement (e.g., "1", "2", "3").
                    - seller: A string representing the client identifier of the seller in the transaction.
                    - buyer: A string representing the client identifier of the buyer in the transaction.
                    - token: A number (integer or float) representing the number of tokens transferred in the settlement.
                    - cash: A number (integer or float) representing the amount of cash transferred in the settlement.

                    Always provide the combination of settlements as a JSON list with the ID of each settlement, for example: [1, 2]. Never return the list of settlements with all properties, only a list of the IDs without being nested in any property or attribute.
            """
                        ]]
            ],
            generationConfig: [
                temperature       : 0.7,
                topK             : 64,
                topP             : 0.95,
                maxOutputTokens  : 65536,
                responseMimeType : "text/plain"
            ]
        ]

        def requestBodyString = JsonOutput.toJson(requestBodyMap)
        log.info("RequestBodyString ${requestBodyString}")
        try {
            initialize()
            connection.outputStream.withWriter('UTF-8') { it.write(requestBodyString) }
            def responseCode = connection.responseCode
            log.info("Response Code: ${responseCode}")
            log.debug("Response Message: ${connection.responseMessage}")
            def responseText = connection.inputStream.text
            log.debug("Response Text: ${responseText}")
            def jsonSlurper = new JsonSlurper()
            def responseJson = jsonSlurper.parseText(responseText)
            log.info("Response like Json: ${responseJson}")
            if (responseJson.candidates[0]?.content) {
                def res = responseJson.candidates[0]?.content?.parts[0]?.text?.replaceAll('```', '')?.replaceAll('json', '')
                responseCombination = jsonSlurper.parseText(res)
            } else {
                throw new Exception("No content in response")
            }
        }
        catch (Exception e) {
            log.error("Exception when call to IA: ${e.cause} ${e.message}")
            throw e
        } finally {
            connection.disconnect()
        }
        log.info("Response Combination: ${responseCombination}")
        return responseCombination
    }

    void initialize() {
        def apiUrl = "${this.url}/${this.model}:generateContent?key=${this.apiKey}"
        connection = (HttpURLConnection) new URL(apiUrl).openConnection()
        connection.requestMethod = 'POST'
        connection.doOutput = true
        connection.setRequestProperty('Content-Type', 'application/json')
    }

}
