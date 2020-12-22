import {Recipe} from './recipe';

export const RECIPES: Recipe[] = [
    {
        id: 1,
        title: 'Spaghetti',
        description: "Classic Italian Fav",
        category: "Beef",
        foodType: "Italian",
        prepTime: 10,
        cookTime: 30,
        servings: 4,
        publicRecipe: true,
        userID: 0,
        calories: 230,
        totalTime:40
    },
    {
        id: 2,
        title: 'Baked Salmon',
        description: "Delicious and Easy",
        category: "Salmon",
        foodType: "American",
        prepTime: 10,
        cookTime: 20,
        servings: 4,
        publicRecipe: true,
        userID: 0,
        calories: 200,
        totalTime:30
    },
    {
        id: 3,
        title: 'Waffles',
        description: "Breakfast foods",
        category: "",
        foodType: "Italian",
        prepTime: 10,
        cookTime: 30,
        servings: 4,
        publicRecipe: true,
        userID: 0,
        calories: 230,
        totalTime:40
    },
]