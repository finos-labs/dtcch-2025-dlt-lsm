import { ethers } from 'hardhat'
import { DEPLOYED_PATH, FILE_NAME_DEPLOYED_CONTRACTS } from './constants'

import { decodeEvent, getJsonFile } from './utils'

const DECIMALS = 2
const amountToToken = (amount: number) => 10 ** DECIMALS * amount
interface Contracts {
	orchestrator: string
	cashToken: string
	securityToken: string
}

async function main() {
	const testWalletAddress = '0xba2bfd3d2e5263f98b62bdd9941d9bcf3dcc7abd'
	const otherWalletAddress = '0x165330a1b4383bb2a0a197db53984535e44c7aa8'
	const contracts = await getJsonFile<Contracts>(
		DEPLOYED_PATH,
		FILE_NAME_DEPLOYED_CONTRACTS
	)
	const operator = await ethers.getContractAt(
		'DvpOrchestrator',
		contracts.orchestrator
	)
	console.log(await operator.getBalances([testWalletAddress]))
	// await operator.resetBalances([testWalletAddress], amountToToken(100))

	const tx = await operator.executeLsm([
		{
			from: testWalletAddress,
			amount: amountToToken(1),
			to: otherWalletAddress,
			token: contracts.securityToken,
		},
	])
	console.log(tx.blockNumber)
	console.log(decodeEvent(operator, 'LsmExecuted', await tx.wait()))
	const cashToken = await ethers.getContractAt(
		'CustomERC20',
		contracts.cashToken
	)
	const securityToken = await ethers.getContractAt(
		'CustomERC20',
		contracts.securityToken
	)
	console.log(
		'CashToken balance: ',
		await cashToken.balanceOf(testWalletAddress)
	)
	console.log(
		'SecurityToken balance: ',
		await securityToken.balanceOf(testWalletAddress)
	)
	console.log('----------------------------')
	console.log(
		'CashToken balance: ',
		await cashToken.balanceOf(otherWalletAddress)
	)
	console.log(
		'SecurityToken balance: ',
		await securityToken.balanceOf(otherWalletAddress)
	)
}

main().catch((error) => {
	console.log(error)
	process.exit(1)
})
