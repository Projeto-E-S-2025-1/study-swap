export interface Material {
    id?: number;
    title: string;
    description?: string;
    materialType: MaterialType;
    conservationStatus: ConservationStatus;
    transactionType: TransactionType;
    price?: number | null;
    photoUrl?: string;
    userId: number;
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