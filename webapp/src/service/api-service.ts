import { Batch, Settlement } from "@/types";
import axios from "axios";

const BASE_URL =
  import.meta.env.VITE_API_BASE_URL ?? "http://localhost:8080/api/v1";

const getUrl = (path: string) => new URL(BASE_URL + path).toString();

export const getBatches = async (): Promise<Batch[]> => {
  return (await axios.get(new URL(BASE_URL + "/batches").toString())).data;
};

export const getLooseSettlements = async (): Promise<Batch> => {
  const data = (await axios.get(getUrl("/pending-settlements").toString()))
    .data;
  return {
    settlements: data,
    batchId: "",
  };
};

export const getSettlementDetails = async (id: string) => {
  return (await axios.get(getUrl("/settlements/" + id).toString())).data;
};

export const createRandomSettlements = async (amount: number) => {
  const elements: Partial<Settlement>[] = [];
  for (let i = 0; i < amount; i++) {
    elements.push({
      securityAmount: Math.floor(Math.random() * 15),
      cashAmount: Math.floor(Math.random() * 15),
      buyerAlias: "buyer" + Math.floor(Math.random() * 5),
      sellerAlias: "seller" + Math.floor(Math.random() * 5),
    });
  }
  console.log("Settlements:", elements);
  return (await axios.post(getUrl("/settlements").toString(), elements)).data;
};

export const executeLsm = async () => {
  return (await axios.post(getUrl("/settlements/process").toString())).data;
};
