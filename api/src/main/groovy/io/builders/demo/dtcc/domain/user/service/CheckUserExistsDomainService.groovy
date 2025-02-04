package io.builders.demo.dtcc.domain.user.service

import io.builders.demo.dtcc.domain.user.User
import io.builders.demo.dtcc.domain.user.UserRepository
import io.builders.demo.dtcc.domain.user.exception.UserNotFoundDomainException
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service

@Service
class CheckUserExistsDomainService {

    @Autowired
    UserRepository repository

    User execute(Integer id) throws UserNotFoundDomainException {
        repository.findById(id).orElseThrow {
            new UserNotFoundDomainException(id)
        }
    }
}
