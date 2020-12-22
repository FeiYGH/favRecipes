export interface Recipe{
    id: number;
    title: string;
    description: string;
    category: string;
    foodType: string;
    prepTime: number;
    cookTime: number;
    servings: number;
    publicRecipe: boolean;
    userID: number;
    calories: number;
    totalTime: number;

}