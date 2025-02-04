package io.builders.demo.dtcc.infrastructure.endpoint.common

import groovy.transform.EqualsAndHashCode
import groovy.transform.ToString

@EqualsAndHashCode
@ToString
class SettlementsRequestModel {

    List<SettlementRequestModel> settlements

}
