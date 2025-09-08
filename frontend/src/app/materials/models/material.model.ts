//src/app/materials/models/material.model.ts
export interface Material {
    id?: number;
    title: string;
    description?: string;
    materialType: MaterialType;
    conservationStatus: ConservationStatus;
    transactionType: TransactionType;
    price?: number | null;
    photo?: string;
    userId?: number;
    userName?: string;
    available?: boolean;
}

export enum MaterialType {
    LIVRO = 'LIVRO',
    APOSTILA = 'APOSTILA',
    EQUIPAMENTO = 'EQUIPAMENTO',
    MOBILIARIO = 'MOBILIARIO'
}

export enum ConservationStatus {
    NOVO = 'NOVO',
    BOM = 'BOM',
    RAZOAVEL = 'RAZOAVEL',
    VELHO = 'VELHO'
}

export enum TransactionType {
    DOACAO = 'DOACAO',
    TROCA = 'TROCA',
    VENDA = 'VENDA'
}