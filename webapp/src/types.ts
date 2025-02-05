export type Settlement = {
  id: number;
  securityAmount: number;
  cashAmount: number;
  buyer: string;
  seller: string;
  creationDate: string;
  status: string;
  batchId?: string;
};

export type SettlementRequest = {
  securityAmount: number;
  cashAmount: number;
  buyerId: number;
  sellerId: number;
};

export type Batch = {
  id: number | undefined;
  settlements: Settlement[];
  aiResult?: string;
};
