package io.builders.demo.integration.gemini

import io.builders.demo.integration.model.IARequest

interface IAPort {

    List<String> obtainIACombination(IARequest request)

}
