package io.builders.demo.dtcc.application.service.resetbalance

import io.builders.demo.core.command.CommandBus
import io.builders.demo.dtcc.application.dlt.command.resetbalance.OrderResetBalanceCommand
import io.builders.demo.dtcc.domain.user.User
import io.builders.demo.dtcc.domain.user.UserRepository
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service

import groovy.util.logging.Slf4j

@Service
@Slf4j
class ResetBalanceAppService {

    @Autowired
    CommandBus commandBus

    @Autowired
    UserRepository userRepository

    void execute(ResetBalanceAppServiceModel appServiceModel) {
        List<User> users = userRepository.findAll()
        List<String> addresses = users.collect(it ->
            it.dltAddress
        )
        commandBus.executeAndWait(new OrderResetBalanceCommand(addresses: addresses, amount: appServiceModel.amount))
    }

}
