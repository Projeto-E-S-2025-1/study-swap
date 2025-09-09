//src/app/transaction/models/transaction.model.ts
export interface Transaction {
    id: number;
    announcerId: number;
    announcerName: string;
    receiverId: number;
    receiverName: string;
    idMaterial: number;
    status: 'PENDING' | 'COMPLETED' | 'CANCELLED';
    transactionType: 'DOACAO' | 'TROCA' | 'VENDA';
    transactionDate: string;
    offeredMaterial?: MaterialDTO;
}

export interface MaterialDTO {
    id: number | null;
    title: string | null;
    materialType: string | null;
    conservationStatus: string | null;
}