import { CustomERC20 } from '@contracts/index'
import { ethers } from 'hardhat'

export async function deployErc20(
	name: string,
	symbol: string
): Promise<CustomERC20> {
	const erc20 = await (
		await ethers.getContractFactory('CustomERC20')
	).deploy(name, symbol)
	await erc20.waitForDeployment()
	return erc20
}
