export type Settlement = {
  settlementId: string;
  securityAmount: number;
  cashAmount: number;
  buyerAlias: string;
  sellerAlias: string;
  datetime: string;
  status: string;
  batchId?: string;
};

export type Batch = {
  batchId: string;
  settlements: Settlement[];
};
