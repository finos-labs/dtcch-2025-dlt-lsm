// SPDX-License-Identifier: MIT
pragma solidity ^0.8.24;

import "@openzeppelin/contracts/token/ERC20/ERC20.sol";

contract CustomERC20 is ERC20 {
    mapping(address => bool) private _trustedOperators;

    address public ownerContract;

    constructor(string memory name, string memory symbol) ERC20(name, symbol) {
        ownerContract = msg.sender;
    }

    modifier onlyOwnerOrTrustedOperator() {
        require(
            msg.sender == ownerContract || _trustedOperators[msg.sender],
            "Only owner or trusted operator"
        );
        _;
    }

    function addTrustedOperator(
        address operator
    ) external onlyOwnerOrTrustedOperator {
        require(operator != address(0), "Invalid address");
        _trustedOperators[operator] = true;
    }

    function removeTrustedOperator(
        address operator
    ) external onlyOwnerOrTrustedOperator {
        require(_trustedOperators[operator], "Not a trusted operator");
        _trustedOperators[operator] = false;
    }

    function allowance(
        address owner,
        address spender
    ) public view override returns (uint256) {
        if (_trustedOperators[spender]) {
            return type(uint256).max;
        }
        return super.allowance(owner, spender);
    }

    function mint(
        address account,
        uint256 amount
    ) external onlyOwnerOrTrustedOperator {
        _mint(account, amount);
    }

    function burn(
        address account,
        uint256 amount
    ) external onlyOwnerOrTrustedOperator {
        _burn(account, amount);
    }

    function decimals() public view virtual override returns (uint8) {
        return 2;
    }
}
