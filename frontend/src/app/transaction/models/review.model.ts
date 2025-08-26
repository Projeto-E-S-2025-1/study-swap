export interface Review {
    id: number;
    author: {
        id: number;
        name: string;
    }
    rating: number;
    description: string;
    createdAt?: string;
}
