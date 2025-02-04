import * as fs from 'fs'
import * as path from 'path'
import * as crypto from 'crypto'
import { ethers } from 'hardhat'
import { promisify } from 'util'
import {
	ContractTransactionReceipt,
	ContractTransactionResponse,
	BaseContract,
} from 'ethers'

const readFileAsync = promisify(fs.readFile)

export function saveJsonToFile(
	filePath: string,
	fileName: string,
	data: unknown
): void {
	if (!fileName.endsWith('.json')) {
		fileName += '.json'
	}

	const fullPath = path.join(filePath, fileName)

	if (!fs.existsSync(filePath)) {
		fs.mkdirSync(filePath, { recursive: true })
	}

	const jsonData = JSON.stringify(data, null, 2)
	fs.writeFileSync(fullPath, jsonData, 'utf-8')
	console.log(
		`\x1b[32mData has been saved to the file \x1b[1m"${fullPath}"\x1b[0m.`
	)
}

export async function getJsonFile<T>(
	filePath: string,
	fileName: string
): Promise<T> {
	if (!fileName.endsWith('.json')) {
		fileName += '.json'
	}
	const fullPath = path.join(filePath, fileName)
	return JSON.parse(await readFileAsync(fullPath, { encoding: 'utf-8' }))
}

export function generateBytes(size: number, hexFormat = true): string {
	const bytes = crypto.randomBytes(size).toString('hex')
	return hexFormat ? `0x${bytes}` : bytes
}

export function generateUUID(hexFormat = true): string {
	return generateBytes(16, hexFormat)
}

export async function getProxyAdminOwnerEnv() {
	return await ethers.getSigner(
		process.env.PROXY_ADMIN_ADDRESS ??
			(await ethers.getSigners())[0].address
	)
}

export function generateRandomWallet() {
	return ethers.Wallet.createRandom(ethers.provider)
}

export function stringToKeccak256(data: string): string {
	return ethers.keccak256(ethers.toUtf8Bytes(data))
}

export function generateZeroBytes(size: number): string {
	return '0x'.padEnd(size * 2 + 2, '0')
}

export function decodeEvent<T extends BaseContract>(
	contract: T,
	eventName: string,
	transactionReceipt: ContractTransactionReceipt | null
) {
	if (transactionReceipt == null) {
		throw new Error('Transaction receipt is empty')
	}
	const eventFragment = contract.interface.getEvent(eventName)

	if (eventFragment === null) {
		throw new Error(`Event "${eventName}" doesn't exist in the contract`)
	}
	const topic = eventFragment.topicHash
	const contractAddress = contract.target
	if (typeof contractAddress !== 'string') {
		throw new Error(
			`The contract target should be a string. Check your contract`
		)
	}
	const log = transactionReceipt.logs
		.filter((filteredLog) => filteredLog.topics.includes(topic))
		.filter(
			(filteredLog) =>
				filteredLog.address.toLowerCase() ===
				contractAddress.toLowerCase()
		)

	return contract.interface.decodeEventLog(
		eventName,
		log[0].data,
		log[0].topics
	)
}
export const getEventFromContractTx = async <T extends BaseContract>(
	contract: T,
	contractTx: ContractTransactionResponse,
	eventName: string
) => {
	const contractReceipt = await contractTx.wait()
	return decodeEvent(contract, eventName, contractReceipt)
}
