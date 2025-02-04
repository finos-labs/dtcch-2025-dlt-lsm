// SPDX-License-Identifier: MIT
pragma solidity ^0.8.24;

import "./CustomERC20.sol";
import "./IDvpOrchestrator.sol";

contract DvpOrchestrator is IDvpOrchestrator {
    CustomERC20 private cashToken;
    CustomERC20 private securityToken;

    constructor(address _cashToken, address _securityToken) {
        cashToken = CustomERC20(_cashToken);
        securityToken = CustomERC20(_securityToken);
    }
    function _isToken(address _token) internal view returns (bool) {
        return _token == address(cashToken) || _token == address(securityToken);
    }

    function _checkAmountGreaterThanZero(
        uint256 _amount
    ) internal pure returns (bool) {
        require(_amount > 0, "Amount must be greater than 0");
        return true;
    }

    function issueTokens(
        address _token,
        address _account,
        uint256 _amount
    ) external override returns (bool) {
        _checkAmountGreaterThanZero(_amount);
        _isToken(_token);
        _addTokenBalance(_token, _account, _amount);
        return true;
    }
    function _addTokenBalance(
        address _token,
        address _account,
        uint256 _amount
    ) internal {
        CustomERC20(_token).mint(_account, _amount);
        emit BalanceAdded(_token, _account, _amount);
    }

    function addTokensBalance(
        address[] calldata _accounts,
        uint256 _amount
    ) external override returns (bool) {
        _checkAmountGreaterThanZero(_amount);
        for (uint256 i = 0; i < _accounts.length; i++) {
            _addTokenBalance(address(cashToken), _accounts[i], _amount);
            _addTokenBalance(address(securityToken), _accounts[i], _amount);
        }
        return true;
    }

    function getBalances(
        address[] calldata _accounts
    ) external view override returns (BalanceAccount[] memory) {
        uint256 accountsLength = _accounts.length;
        BalanceAccount[] memory results = new BalanceAccount[](accountsLength);
        for (uint256 i = 0; i < accountsLength; i++) {
            results[i] = BalanceAccount({
                cashToken: cashToken.balanceOf(_accounts[i]),
                securityToken: securityToken.balanceOf(_accounts[i]),
                user: _accounts[i]
            });
        }
        return results;
    }

    function executeLsm(
        Transaction[] calldata _lsmTransactions
    ) external override returns (bool) {
        uint256 transactionsLength = _lsmTransactions.length;
        for (uint256 i = 0; i < transactionsLength; i++) {
            Transaction memory txn = _lsmTransactions[i];
            _isToken(txn.token);
            CustomERC20 token = CustomERC20(txn.token);
            require(
                token.balanceOf(txn.from) >= txn.amount,
                "Insufficient balance for token"
            );
            token.transferFrom(txn.from, txn.to, txn.amount);
        }

        emit LsmExecuted(_lsmTransactions);
        return true;
    }

    function adjustBalance(
        CustomERC20 token,
        address account,
        uint256 targetAmount
    ) internal {
        uint256 currentBalance = token.balanceOf(account);
        if (currentBalance > targetAmount) {
            token.burn(account, currentBalance - targetAmount);
        } else if (currentBalance < targetAmount) {
            token.mint(account, targetAmount - currentBalance);
        }
    }

    function resetBalances(
        address[] calldata _accounts,
        uint256 _amount
    ) external override returns (bool) {
        uint256 accountsLength = _accounts.length;
        for (uint256 i = 0; i < accountsLength; i++) {
            adjustBalance(cashToken, _accounts[i], _amount);
            adjustBalance(securityToken, _accounts[i], _amount);
            emit AccountsReset(_accounts[i], _amount);
        }
        return true;
    }
}
