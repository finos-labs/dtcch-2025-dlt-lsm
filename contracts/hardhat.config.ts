import { HardhatUserConfig } from 'hardhat/config'
import '@nomicfoundation/hardhat-toolbox'
import 'hardhat-contract-sizer'
import 'hardhat-contract-signatures'
import 'tsconfig-paths'
import 'hardhat-dependency-compiler'
import * as dotenv from 'dotenv'
import 'hardhat-tx-decoder'

dotenv.config()

const config: HardhatUserConfig = {
	solidity: {
		version: '0.8.24',
		settings: {
			optimizer: {
				enabled: true,
				runs: 100,
			},
		},
	},
	networks: {
		// Defines the configuration settings for connecting to Hedera local node
		hedera_testnet: {
			// Specifies URL endpoint for Hedera local node pulled from the .env file
			url: process.env.LOCAL_NODE_ENDPOINT,
			// Your local node operator private key pulled from the .env file
			accounts: [process.env.LOCAL_NODE_OPERATOR_PRIVATE_KEY!],
		},
	},
	mocha: {
		timeout: 1000000,
	},
	dependencyCompiler: {
		paths: [
			'@openzeppelin/contracts/proxy/transparent/ProxyAdmin.sol',
			'@openzeppelin/contracts/proxy/transparent/TransparentUpgradeableProxy.sol',
		],
	},
	contractSignature: {
		functionsColumns: ['selector'],
		eventsColumns: ['sign:minimal', 'selector'],
		exclude: ['contracts/testWrappers/**', '@openzeppelin/**'],
	},
}

if (process.env.IS_SLITHER) {
	config.dependencyCompiler = {}
}

export default config
