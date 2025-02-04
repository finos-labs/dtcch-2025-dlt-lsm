import * as path from 'path'
import { network } from 'hardhat'

export const DEPLOYED_PATH = path.join(__dirname, '../deployed')
export const FILE_NAME_DEPLOYED_CONTRACTS = `contracts-${network.name}`
