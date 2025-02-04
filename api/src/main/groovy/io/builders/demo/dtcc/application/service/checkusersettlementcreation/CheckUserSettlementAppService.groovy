package io.builders.demo.dtcc.application.service.checkusersettlementcreation

import groovy.util.logging.Slf4j
import io.builders.demo.dtcc.application.command.createsettlement.CreateSettlementCommand
import io.builders.demo.dtcc.domain.user.User
import io.builders.demo.dtcc.domain.user.service.CheckUserExistsDomainService
import io.builders.demo.core.command.CommandBus
import io.builders.demo.core.domain.exception.EntityNotFoundDomainException
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service

@Service
@Slf4j
class CheckUserSettlementAppService {

    @Autowired
    CommandBus commandBus

    @Autowired
    CheckUserExistsDomainService checkUserExistsDomainService

    void execute(List<CheckUserSettlementAppServiceModel> settlements) {
        settlements.forEach {
            try {
                User buyer = checkUserExistsDomainService.execute(it.buyerId)
                User seller = checkUserExistsDomainService.execute(it.sellerId)
                this.commandBus.execute(new CreateSettlementCommand(
                    buyer: buyer,
                    seller: seller,
                    cashAmount: it.cashAmount,
                    securityAmount: it.securityAmount
                ))
            }
            catch (EntityNotFoundDomainException e) {
                log.error(e.message)
            }

        }
    }
}
