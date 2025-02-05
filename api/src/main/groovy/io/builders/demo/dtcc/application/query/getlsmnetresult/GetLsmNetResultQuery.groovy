package io.builders.demo.dtcc.application.query.getlsmnetresult

import io.builders.demo.core.query.Query
import io.builders.demo.dtcc.application.query.common.GetLsmBatchesQueryModel
import io.builders.demo.integration.model.Balance
import io.builders.demo.integration.model.IASettlement

class GetLsmNetResultQuery extends Query<List<IASettlement>> {
    List<IASettlement> settlements
    Map<String, Balance> balances
}
