import { DEPLOYED_PATH, FILE_NAME_DEPLOYED_CONTRACTS } from '@scripts/constants'

import { saveJsonToFile } from '@scripts/utils'
import { deployErc20 } from './deployErc20'
import { deployDvpOrchestrator } from './deployDvpOrchestrator'

async function main() {
	const cashToken = await deployErc20('CashToken', 'CASH')
	const securityToken = await deployErc20('SecurityToken', 'SECURITY')

	const orchestrator = await deployDvpOrchestrator(cashToken, securityToken)

	await cashToken.addTrustedOperator(orchestrator)
	await securityToken.addTrustedOperator(orchestrator)

	saveJsonToFile(DEPLOYED_PATH, FILE_NAME_DEPLOYED_CONTRACTS, {
		orchestrator: await orchestrator.getAddress(),
		cashToken: await cashToken.getAddress(),
		securityToken: await securityToken.getAddress(),
	})
}

main().catch((error) => {
	console.log(error)
	process.exit(1)
})
