import { TransactionType } from "../../materials/models/material.model";

//src/app/transaction/models/transaction.model.ts
export interface Transaction {
    id: number;
    announcerId: number;
    announcerName: string;
    receiverId: number;
    receiverName: string;
    idMaterial: number;
    status: 'PENDING' | 'DENIED' | 'CONCLUDED';
    transactionType: 'DOACAO' | 'TROCA' | 'VENDA';
    transactionDate: string;
    offeredMaterial?: MaterialDTO;
}

export interface TransactionDTO {
    id: number;
    announcerId: number;
    announcerName: string;
    receiverId: number;
    receiverName: string;
    idMaterial: number;
    materialName: string;
    status: 'PENDING' | 'DENIED' | 'CONCLUDED';
    transactionType: TransactionType;
    transactionDate: string;
    offeredMaterial?: MaterialDTO;
}

export interface MaterialDTO {
    title?: string;
    description?: string;
    materialType?: string;
    conservationStatus?: string;
    transactionType: string;
    price?: number | null;
}