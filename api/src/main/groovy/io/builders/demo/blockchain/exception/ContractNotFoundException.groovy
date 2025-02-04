package io.builders.demo.blockchain.exception

import org.web3j.tx.Contract

class ContractNotFoundException<T extends Contract> extends RuntimeException {

    static final String DEFAULT_MESSAGE = 'Contract with class %s not found. #CONTRACT_NOT_FOUND#'

    ContractNotFoundException(Class<T> contractClass) {
        super(String.format(DEFAULT_MESSAGE, contractClass.simpleName))
    }

}
