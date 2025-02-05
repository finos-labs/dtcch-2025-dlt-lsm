package io.builders.demo.dtcc.infrastructure.endpoint.callbackai

import io.builders.demo.dtcc.infrastructure.endpoint.common.SettlementsRequestModel

interface CallbackAIPort {

    void execute(List<String> settlements)

}
