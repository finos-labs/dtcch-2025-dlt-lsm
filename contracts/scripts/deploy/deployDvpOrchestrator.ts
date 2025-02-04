import { CustomERC20, DvpOrchestrator } from '@contracts/index'
import { ethers } from 'hardhat'

export async function deployDvpOrchestrator(
	cashToken: CustomERC20,
	securityToken: CustomERC20
): Promise<DvpOrchestrator> {
	const orchestrator = await (
		await ethers.getContractFactory('DvpOrchestrator')
	).deploy(cashToken, securityToken)
	await orchestrator.waitForDeployment()

	return orchestrator
}
