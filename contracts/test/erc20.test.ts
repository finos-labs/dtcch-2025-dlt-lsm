import { CustomERC20 } from '@contracts/index'
import { loadFixture } from '@nomicfoundation/hardhat-network-helpers'
import { deployErc20 } from '@scripts/deploy/deployErc20'
import { expect } from 'chai'
import { ethers } from 'hardhat'
import { Signer } from 'ethers'

describe(' Tests', () => {
	let erc20: CustomERC20
	let owner: Signer
	let spender: Signer
	let recipient: Signer

	async function deployFixture(): Promise<void> {
		erc20 = await deployErc20('Test', 'TEST')
		await erc20.addTrustedOperator(owner)
	}

	before(async () => {
		;[owner, spender, recipient] = await ethers.getSigners()
	})

	beforeEach(async () => {
		await loadFixture(deployFixture)
	})

	it('trustOperator have unlimited allowance', async () => {
		await erc20.mint(spender, 100)
		expect(await erc20.balanceOf(spender)).to.equal(100)
		expect(await erc20.balanceOf(recipient)).to.equal(0)
		await erc20.transferFrom(spender, recipient, 100)
		expect(await erc20.balanceOf(spender)).to.equal(0)
		expect(await erc20.balanceOf(recipient)).to.equal(100)
	})
})
