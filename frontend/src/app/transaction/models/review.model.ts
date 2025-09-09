export interface Review {
    id: number;
    authorName: string;
    authorId: number;
    materialTitle: string;
    rating: number;
    description: string;
    createdAt?: string;
}

export interface UserAverage {
    userId: number;
    averageRating: number;
    totalReviews: number;
}