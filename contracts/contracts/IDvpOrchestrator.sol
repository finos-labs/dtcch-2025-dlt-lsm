// SPDX-License-Identifier: MIT
pragma solidity ^0.8.24;

interface IDvpOrchestrator {
    function issueTokens(
        address _token,
        address _account,
        uint256 _amount
    ) external returns (bool);
    function addTokensBalance(
        address[] calldata _acccounts,
        uint256 _amount
    ) external returns (bool);

    event BalanceAdded(address token, address user, uint256 amount);

    struct BalanceAccount {
        uint256 cashToken;
        uint256 securityToken;
        address user;
    }

    function getBalances(
        address[] calldata _acccounts
    ) external returns (BalanceAccount[] memory);

    struct Transaction {
        address from;
        address to;
        uint256 amount;
        address token;
    }
    event LsmExecuted(Transaction[] lsmTransactions);

    function executeLsm(
        Transaction[] calldata _lsmTransactions
    ) external returns (bool);

    event AccountsReset(address accounts, uint256 amount);

    function resetBalances(
        address[] calldata _accounts,
        uint256 _amount
    ) external returns (bool);
}
