package io.builders.demo.dtcc.application.service.addbalance

import groovy.util.logging.Slf4j
import io.builders.demo.core.command.CommandBus
import io.builders.demo.dtcc.application.dlt.command.minttokenuser.OrderMintTokenUserCommand
import io.builders.demo.dtcc.domain.user.User
import io.builders.demo.dtcc.domain.user.service.CheckUserExistsDomainService
import jakarta.validation.Valid
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service

@Service
@Slf4j
class AddBalanceAppService {

    @Autowired
    CommandBus commandBus

    @Autowired
    CheckUserExistsDomainService checkUserExistsDomainService

    void execute(@Valid AddBalanceAppServiceModel model) {
        User user = checkUserExistsDomainService.execute(model.userId)
        commandBus.executeAndWait(new OrderMintTokenUserCommand(
            userAddress: user.dltAddress,
            amount: model.amount,
            tokenAddress: model.tokenAddress
        ))
    }
}
