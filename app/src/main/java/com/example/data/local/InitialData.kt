package com.example.data.local

import com.example.data.model.IngredientCategory
import com.example.data.model.IngredientEntity
import com.example.data.model.PantryItemEntity
import com.example.data.model.PantryStatus
import com.example.data.model.RecipeEntity
import com.example.data.model.RecipeIngredientEntity

object InitialData {

    fun getInitialIngredients(): List<IngredientEntity> = listOf(
        // Vegetables
        IngredientEntity("ing_potato", "Potato", IngredientCategory.VEGETABLES.displayName, "🥔", "Aloo, Batata, Potatoes", "pcs"),
        IngredientEntity("ing_onion", "Onion", IngredientCategory.VEGETABLES.displayName, "🧅", "Pyaz, Kanda, Red Onion, White Onion", "pcs"),
        IngredientEntity("ing_tomato", "Tomato", IngredientCategory.VEGETABLES.displayName, "🍅", "Tamatar, Cherry Tomato, Tomatoes", "pcs"),
        IngredientEntity("ing_garlic", "Garlic", IngredientCategory.VEGETABLES.displayName, "🧄", "Lehsun, Lasun, Garlic Cloves", "cloves"),
        IngredientEntity("ing_ginger", "Ginger", IngredientCategory.VEGETABLES.displayName, "🫚", "Adrak, Fresh Ginger", "inch"),
        IngredientEntity("ing_green_chili", "Green Chili", IngredientCategory.VEGETABLES.displayName, "🌶️", "Hari Mirch, Green Chilli, Jalapeno", "pcs"),
        IngredientEntity("ing_coriander", "Cilantro / Coriander", IngredientCategory.VEGETABLES.displayName, "🌿", "Dhaniya, Cilantro, Kothmir, Fresh Coriander", "bunch"),
        IngredientEntity("ing_spinach", "Spinach", IngredientCategory.VEGETABLES.displayName, "🥬", "Palak, Baby Spinach, Greens", "cup"),
        IngredientEntity("ing_bell_pepper", "Bell Pepper", IngredientCategory.VEGETABLES.displayName, "🫑", "Capsicum, Shimla Mirch, Sweet Pepper", "pcs"),
        IngredientEntity("ing_carrot", "Carrot", IngredientCategory.VEGETABLES.displayName, "🥕", "Gajar, Carrots", "pcs"),
        IngredientEntity("ing_peas", "Green Peas", IngredientCategory.VEGETABLES.displayName, "🟢", "Matar, Frozen Peas, Sweet Peas", "cup"),
        IngredientEntity("ing_cauliflower", "Cauliflower", IngredientCategory.VEGETABLES.displayName, "🥦", "Gobi, Phool Gobi", "head"),
        IngredientEntity("ing_eggplant", "Eggplant", IngredientCategory.VEGETABLES.displayName, "🍆", "Baingan, Brinjal, Aubergine", "pcs"),
        IngredientEntity("ing_lemon", "Lemon", IngredientCategory.VEGETABLES.displayName, "🍋", "Nimbu, Lime, Lemon Juice", "pcs"),

        // Dairy & Eggs
        IngredientEntity("ing_egg", "Egg", IngredientCategory.EGGS.displayName, "🥚", "Anda, Eggs, Egg White", "pcs"),
        IngredientEntity("ing_milk", "Milk", IngredientCategory.DAIRY.displayName, "🥛", "Doodh, Whole Milk, Soy Milk, Oat Milk", "cup"),
        IngredientEntity("ing_butter", "Butter", IngredientCategory.DAIRY.displayName, "🧈", "Makhan, Salted Butter, Unsalted Butter", "tbsp"),
        IngredientEntity("ing_paneer", "Paneer", IngredientCategory.DAIRY.displayName, "🧀", "Cottage Cheese, Tofu", "g"),
        IngredientEntity("ing_cheese", "Cheese", IngredientCategory.DAIRY.displayName, "🧀", "Cheddar, Mozzarella, Processed Cheese", "g"),
        IngredientEntity("ing_curd", "Curd / Yogurt", IngredientCategory.DAIRY.displayName, "🥣", "Dahi, Plain Yogurt, Greek Yogurt", "cup"),
        IngredientEntity("ing_cream", "Fresh Cream", IngredientCategory.DAIRY.displayName, "🥛", "Heavy Cream, Malai", "tbsp"),

        // Grains, Rice, Pasta, Bread
        IngredientEntity("ing_bread", "Bread", IngredientCategory.BREAD.displayName, "🍞", "White Bread, Brown Bread, Toast, Pav, Bun", "slices"),
        IngredientEntity("ing_rice", "Rice", IngredientCategory.RICE.displayName, "🍚", "Chawal, Basmati Rice, Jasmine Rice", "cup"),
        IngredientEntity("ing_pasta", "Pasta", IngredientCategory.PASTA.displayName, "🍝", "Penne, Spaghetti, Macaroni, Fusilli", "g"),
        IngredientEntity("ing_poha", "Poha", IngredientCategory.GRAINS.displayName, "🌾", "Flattened Rice, Aval, Flaked Rice", "cup"),
        IngredientEntity("ing_suji", "Semolina / Rava", IngredientCategory.GRAINS.displayName, "🌾", "Suji, Sooji, Rava, Semolina", "cup"),
        IngredientEntity("ing_wheat_flour", "Wheat Flour", IngredientCategory.GRAINS.displayName, "🌾", "Atta, Whole Wheat Flour, Gehun ka Atta", "cup"),
        IngredientEntity("ing_besan", "Besan / Chickpea Flour", IngredientCategory.PULSES.displayName, "🫘", "Gram Flour, Chickpea Flour, Besan", "cup"),
        IngredientEntity("ing_dal", "Yellow Lentils / Dal", IngredientCategory.PULSES.displayName, "🫘", "Toor Dal, Moong Dal, Masoor Dal, Lentils", "cup"),

        // Spices & Sauces
        IngredientEntity("ing_cumin", "Cumin Seeds", IngredientCategory.SPICES.displayName, "🌱", "Jeera, Zeera, Cumin", "tsp"),
        IngredientEntity("ing_mustard_seeds", "Mustard Seeds", IngredientCategory.SPICES.displayName, "🟡", "Rai, Sarson, Mustard", "tsp"),
        IngredientEntity("ing_turmeric", "Turmeric Powder", IngredientCategory.SPICES.displayName, "🟡", "Haldi, Turmeric", "tsp"),
        IngredientEntity("ing_red_chili_powder", "Red Chili Powder", IngredientCategory.SPICES.displayName, "🌶️", "Lal Mirch, Chili Flakes, Paprika", "tsp"),
        IngredientEntity("ing_garam_masala", "Garam Masala", IngredientCategory.SPICES.displayName, "🌿", "Curry Powder, Masala Spice Mix", "tsp"),
        IngredientEntity("ing_salt", "Salt", IngredientCategory.PANTRY_STAPLES.displayName, "🧂", "Namak, Sea Salt, Kosher Salt", "tsp"),
        IngredientEntity("ing_black_pepper", "Black Pepper", IngredientCategory.SPICES.displayName, "⚫", "Kali Mirch, Ground Pepper", "tsp"),
        IngredientEntity("ing_oil", "Cooking Oil", IngredientCategory.SAUCES.displayName, "🫒", "Tel, Vegetable Oil, Olive Oil, Mustard Oil", "tbsp"),
        IngredientEntity("ing_ghee", "Ghee", IngredientCategory.DAIRY.displayName, "🧈", "Clarified Butter, Desi Ghee", "tbsp"),
        IngredientEntity("ing_soy_sauce", "Soy Sauce", IngredientCategory.SAUCES.displayName, "🍶", "Shoyu, Dark Soy Sauce", "tbsp"),
        IngredientEntity("ing_tomato_ketchup", "Tomato Sauce", IngredientCategory.SAUCES.displayName, "🍅", "Ketchup, Tomato Paste, Marinara", "tbsp"),
        IngredientEntity("ing_curry_leaves", "Curry Leaves", IngredientCategory.SPICES.displayName, "🍃", "Kadi Patta, Sweet Neem", "sprig")
    )

    fun getInitialRecipes(): List<Pair<RecipeEntity, List<RecipeIngredientEntity>>> = listOf(
        // 1. Masala Egg Toast
        RecipeEntity(
            id = "rec_masala_egg_toast",
            name = "Masala Egg Toast",
            description = "Crispy spiced egg coated toast packed with onion, green chili and fresh coriander. The ultimate 10-minute comfort meal.",
            cuisine = "Indian",
            mealType = "Breakfast",
            prepTimeMinutes = 5,
            cookTimeMinutes = 5,
            difficulty = "Easy",
            servings = 2,
            calories = 310,
            proteinGrams = 14,
            carbsGrams = 28,
            fatGrams = 12,
            instructions = "Crack eggs into a bowl and whisk with finely chopped onion, green chili, turmeric, red chili powder, and salt.|Melt butter or heat oil in a non-stick pan over medium heat.|Dip bread slices into the egg mixture until thoroughly coated on both sides.|Place bread in the pan, pour any remaining egg mixture on top, and cook for 2 minutes until golden brown.|Flip and toast the other side for 2 minutes until fully set and crispy. Serve hot!",
            stepTimersMinutes = "0,0,0,2,2",
            imageUrl = "https://images.unsplash.com/photo-1525351484163-7529414344d8?w=800&q=80",
            requiredEquipment = "Pan,Stove",
            dietaryTags = "Eggetarian,High-protein,Quick,Under 15 Minutes,Budget",
            allergenTags = "Eggs,Gluten"
        ) to listOf(
            RecipeIngredientEntity(recipeId = "rec_masala_egg_toast", ingredientId = "ing_egg", ingredientName = "Egg", quantity = "2", unit = "pcs", isRequired = true),
            RecipeIngredientEntity(recipeId = "rec_masala_egg_toast", ingredientId = "ing_bread", ingredientName = "Bread", quantity = "2", unit = "slices", isRequired = true),
            RecipeIngredientEntity(recipeId = "rec_masala_egg_toast", ingredientId = "ing_onion", ingredientName = "Onion", quantity = "0.5", unit = "pcs", isRequired = true),
            RecipeIngredientEntity(recipeId = "rec_masala_egg_toast", ingredientId = "ing_green_chili", ingredientName = "Green Chili", quantity = "1", unit = "pcs", isRequired = false),
            RecipeIngredientEntity(recipeId = "rec_masala_egg_toast", ingredientId = "ing_butter", ingredientName = "Butter", quantity = "1", unit = "tbsp", isRequired = true, substitutions = "Ghee,Cooking Oil"),
            RecipeIngredientEntity(recipeId = "rec_masala_egg_toast", ingredientId = "ing_salt", ingredientName = "Salt", quantity = "0.5", unit = "tsp", isRequired = true)
        ),

        // 2. Creamy Tomato Pasta
        RecipeEntity(
            id = "rec_creamy_tomato_pasta",
            name = "Creamy Tomato Garlic Pasta",
            description = "Silky garlic tomato sauce tossed with al dente pasta and melted cheese.",
            cuisine = "Italian",
            mealType = "Dinner",
            prepTimeMinutes = 5,
            cookTimeMinutes = 15,
            difficulty = "Easy",
            servings = 2,
            calories = 490,
            proteinGrams = 16,
            carbsGrams = 68,
            fatGrams = 15,
            instructions = "Boil salted water in a pot. Cook pasta until al dente (approx 8-9 minutes). Reserve 1/2 cup pasta water.|Heat olive oil or butter in a pan over medium heat. Sauté minced garlic until fragrant.|Add finely chopped tomatoes or tomato sauce, salt, and black pepper. Simmer for 5 minutes until reduced and rich.|Stir in milk or cream and grated cheese until a silky sauce forms.|Add cooked pasta and a splash of reserved pasta water. Toss for 1 minute until coated. Garnish with pepper!",
            stepTimersMinutes = "9,2,5,2,1",
            imageUrl = "https://images.unsplash.com/photo-1621996346565-e3d5d6281055?w=800&q=80",
            requiredEquipment = "Pot,Pan,Stove",
            dietaryTags = "Vegetarian,Healthy",
            allergenTags = "Dairy,Gluten"
        ) to listOf(
            RecipeIngredientEntity(recipeId = "rec_creamy_tomato_pasta", ingredientId = "ing_pasta", ingredientName = "Pasta", quantity = "200", unit = "g", isRequired = true),
            RecipeIngredientEntity(recipeId = "rec_creamy_tomato_pasta", ingredientId = "ing_tomato", ingredientName = "Tomato", quantity = "2", unit = "pcs", isRequired = true, substitutions = "Tomato Puree,Tomato Sauce"),
            RecipeIngredientEntity(recipeId = "rec_creamy_tomato_pasta", ingredientId = "ing_garlic", ingredientName = "Garlic", quantity = "3", unit = "cloves", isRequired = true),
            RecipeIngredientEntity(recipeId = "rec_creamy_tomato_pasta", ingredientId = "ing_butter", ingredientName = "Butter", quantity = "1", unit = "tbsp", isRequired = true, substitutions = "Cooking Oil,Olive Oil"),
            RecipeIngredientEntity(recipeId = "rec_creamy_tomato_pasta", ingredientId = "ing_cheese", ingredientName = "Cheese", quantity = "40", unit = "g", isRequired = false, substitutions = "Paneer,Cream"),
            RecipeIngredientEntity(recipeId = "rec_creamy_tomato_pasta", ingredientId = "ing_salt", ingredientName = "Salt", quantity = "1", unit = "tsp", isRequired = true)
        ),

        // 3. Masala Omelette
        RecipeEntity(
            id = "rec_masala_omelette",
            name = "Classic Masala Omelette",
            description = "Fluffy spiced Indian omelette loaded with diced onions, tomatoes, green chilies, and fresh coriander.",
            cuisine = "Indian",
            mealType = "Breakfast",
            prepTimeMinutes = 4,
            cookTimeMinutes = 4,
            difficulty = "Easy",
            servings = 1,
            calories = 220,
            proteinGrams = 13,
            carbsGrams = 5,
            fatGrams = 16,
            instructions = "Whisk 2 eggs in a bowl with chopped onions, tomatoes, green chilies, turmeric, salt, and black pepper.|Heat oil or butter in a pan on medium-high heat.|Pour the egg mixture evenly into the pan and let it cook undisturbed for 2 minutes until bottom sets.|Flip gently and cook the other side for 1 minute until golden. Serve immediately!",
            stepTimersMinutes = "0,0,2,1",
            imageUrl = "https://images.unsplash.com/photo-1510693206972-df098062cb71?w=800&q=80",
            requiredEquipment = "Pan,Stove",
            dietaryTags = "Eggetarian,High-protein,Low-carb,Under 15 Minutes,Quick,Budget",
            allergenTags = "Eggs"
        ) to listOf(
            RecipeIngredientEntity(recipeId = "rec_masala_omelette", ingredientId = "ing_egg", ingredientName = "Egg", quantity = "2", unit = "pcs", isRequired = true),
            RecipeIngredientEntity(recipeId = "rec_masala_omelette", ingredientId = "ing_onion", ingredientName = "Onion", quantity = "0.5", unit = "pcs", isRequired = true),
            RecipeIngredientEntity(recipeId = "rec_masala_omelette", ingredientId = "ing_tomato", ingredientName = "Tomato", quantity = "0.5", unit = "pcs", isRequired = false),
            RecipeIngredientEntity(recipeId = "rec_masala_omelette", ingredientId = "ing_oil", ingredientName = "Cooking Oil", quantity = "1", unit = "tsp", isRequired = true, substitutions = "Butter,Ghee"),
            RecipeIngredientEntity(recipeId = "rec_masala_omelette", ingredientId = "ing_salt", ingredientName = "Salt", quantity = "0.5", unit = "tsp", isRequired = true)
        ),

        // 4. Quick Aloo Fry
        RecipeEntity(
            id = "rec_crispy_aloo_fry",
            name = "Crispy Jeera Aloo Fry",
            description = "Golden pan-fried spiced potato cubes with cumin seeds and aromatic spices. Perfect with rice or roti.",
            cuisine = "Indian",
            mealType = "Lunch",
            prepTimeMinutes = 5,
            cookTimeMinutes = 12,
            difficulty = "Easy",
            servings = 2,
            calories = 240,
            proteinGrams = 4,
            carbsGrams = 38,
            fatGrams = 8,
            instructions = "Peel and cut potatoes into small bite-sized cubes for fast cooking.|Heat oil in a pan and add cumin seeds. Let them splutter for 30 seconds.|Add potato cubes, turmeric powder, red chili powder, and salt. Stir well to coat evenly.|Cover with a lid and cook on medium-low flame for 7-8 minutes, stirring occasionally.|Uncover and fry on medium-high for 3 minutes until outer edges turn crispy and golden brown!",
            stepTimersMinutes = "0,1,0,8,3",
            imageUrl = "https://images.unsplash.com/photo-1589301760014-d929f3979dbc?w=800&q=80",
            requiredEquipment = "Pan,Stove",
            dietaryTags = "Vegetarian,Vegan,Gluten-free,Budget,Under 15 Minutes",
            allergenTags = ""
        ) to listOf(
            RecipeIngredientEntity(recipeId = "rec_crispy_aloo_fry", ingredientId = "ing_potato", ingredientName = "Potato", quantity = "2", unit = "pcs", isRequired = true),
            RecipeIngredientEntity(recipeId = "rec_crispy_aloo_fry", ingredientId = "ing_cumin", ingredientName = "Cumin Seeds", quantity = "1", unit = "tsp", isRequired = true),
            RecipeIngredientEntity(recipeId = "rec_crispy_aloo_fry", ingredientId = "ing_oil", ingredientName = "Cooking Oil", quantity = "1.5", unit = "tbsp", isRequired = true, substitutions = "Ghee,Butter"),
            RecipeIngredientEntity(recipeId = "rec_crispy_aloo_fry", ingredientId = "ing_turmeric", ingredientName = "Turmeric Powder", quantity = "0.5", unit = "tsp", isRequired = false),
            RecipeIngredientEntity(recipeId = "rec_crispy_aloo_fry", ingredientId = "ing_salt", ingredientName = "Salt", quantity = "1", unit = "tsp", isRequired = true)
        ),

        // 5. Paneer Bhurji
        RecipeEntity(
            id = "rec_paneer_bhurji",
            name = "Spiced Paneer Bhurji",
            description = "Scrambled cottage cheese sautéed with onions, tomatoes, ginger-garlic, and warming aromatic spices.",
            cuisine = "Indian",
            mealType = "Dinner",
            prepTimeMinutes = 5,
            cookTimeMinutes = 10,
            difficulty = "Easy",
            servings = 2,
            calories = 360,
            proteinGrams = 22,
            carbsGrams = 10,
            fatGrams = 24,
            instructions = "Crumble the paneer by hand into a bowl.|Heat butter or oil in a pan, add cumin seeds, chopped onion, and ginger-garlic paste. Sauté until translucent.|Add finely chopped tomatoes, turmeric, red chili powder, and salt. Cook until tomatoes soften.|Fold in the crumbled paneer and toss well on medium flame for 2-3 minutes.|Turn off flame, squeeze fresh lemon juice, and garnish with fresh coriander!",
            stepTimersMinutes = "0,3,3,3,0",
            imageUrl = "https://images.unsplash.com/photo-1567188040759-fb8a883dc6d8?w=800&q=80",
            requiredEquipment = "Pan,Stove",
            dietaryTags = "Vegetarian,High-protein,Low-carb,Under 15 Minutes",
            allergenTags = "Dairy"
        ) to listOf(
            RecipeIngredientEntity(recipeId = "rec_paneer_bhurji", ingredientId = "ing_paneer", ingredientName = "Paneer", quantity = "200", unit = "g", isRequired = true, substitutions = "Tofu"),
            RecipeIngredientEntity(recipeId = "rec_paneer_bhurji", ingredientId = "ing_onion", ingredientName = "Onion", quantity = "1", unit = "pcs", isRequired = true),
            RecipeIngredientEntity(recipeId = "rec_paneer_bhurji", ingredientId = "ing_tomato", ingredientName = "Tomato", quantity = "1", unit = "pcs", isRequired = true),
            RecipeIngredientEntity(recipeId = "rec_paneer_bhurji", ingredientId = "ing_butter", ingredientName = "Butter", quantity = "1", unit = "tbsp", isRequired = true, substitutions = "Cooking Oil,Ghee"),
            RecipeIngredientEntity(recipeId = "rec_paneer_bhurji", ingredientId = "ing_salt", ingredientName = "Salt", quantity = "1", unit = "tsp", isRequired = true)
        ),

        // 6. Quick Tomato Rice
        RecipeEntity(
            id = "rec_tomato_rice",
            name = "Tangy South Indian Tomato Rice",
            description = "Flavorful spiced rice infused with ripe tomatoes, mustard seeds, curry leaves, and aromatic ginger.",
            cuisine = "South Indian",
            mealType = "Lunch",
            prepTimeMinutes = 5,
            cookTimeMinutes = 10,
            difficulty = "Easy",
            servings = 2,
            calories = 340,
            proteinGrams = 6,
            carbsGrams = 62,
            fatGrams = 7,
            instructions = "Heat oil in a pan and temper mustard seeds and curry leaves until fragrant.|Add chopped onions and ginger, sautéing until light golden.|Add chopped tomatoes, turmeric, chili powder, and salt. Cook until tomatoes become mushy and oil separates.|Add cooked rice (leftover rice works best!) and gently mix until every grain is infused with the tomato masala.|Cover and steam on low heat for 2 minutes. Serve hot with curd!",
            stepTimersMinutes = "1,3,4,2,2",
            imageUrl = "https://images.unsplash.com/photo-1546833999-b9f581a1996d?w=800&q=80",
            requiredEquipment = "Pan,Stove",
            dietaryTags = "Vegetarian,Vegan,Gluten-free,Under 15 Minutes,Budget",
            allergenTags = ""
        ) to listOf(
            RecipeIngredientEntity(recipeId = "rec_tomato_rice", ingredientId = "ing_rice", ingredientName = "Rice", quantity = "2", unit = "cup", isRequired = true),
            RecipeIngredientEntity(recipeId = "rec_tomato_rice", ingredientId = "ing_tomato", ingredientName = "Tomato", quantity = "2", unit = "pcs", isRequired = true),
            RecipeIngredientEntity(recipeId = "rec_tomato_rice", ingredientId = "ing_onion", ingredientName = "Onion", quantity = "1", unit = "pcs", isRequired = false),
            RecipeIngredientEntity(recipeId = "rec_tomato_rice", ingredientId = "ing_oil", ingredientName = "Cooking Oil", quantity = "1", unit = "tbsp", isRequired = true),
            RecipeIngredientEntity(recipeId = "rec_tomato_rice", ingredientId = "ing_salt", ingredientName = "Salt", quantity = "1", unit = "tsp", isRequired = true)
        ),

        // 7. Comforting Dal Tadka
        RecipeEntity(
            id = "rec_dal_tadka",
            name = "Homestyle Yellow Dal Tadka",
            description = "Hearty yellow lentils simmered to perfection and tempered with sizzling cumin, garlic, and red chilies in ghee.",
            cuisine = "Indian",
            mealType = "Dinner",
            prepTimeMinutes = 5,
            cookTimeMinutes = 15,
            difficulty = "Easy",
            servings = 3,
            calories = 230,
            proteinGrams = 12,
            carbsGrams = 32,
            fatGrams = 6,
            instructions = "Boil yellow lentils in a pot with water, turmeric, and salt until soft and creamy.|In a small pan, heat ghee or oil for the tadka (tempering).|Add cumin seeds, minced garlic, and chopped green chilies until garlic turns aromatic golden.|Pour the sizzling tadka immediately over the cooked dal with a lid to trap the aromas.|Stir gently, squeeze fresh lemon juice, and serve warm with rice or roti!",
            stepTimersMinutes = "12,1,2,0,0",
            imageUrl = "https://images.unsplash.com/photo-1546833999-b9f581a1996d?w=800&q=80",
            requiredEquipment = "Pot,Pan,Stove",
            dietaryTags = "Vegetarian,Gluten-free,High-protein,Healthy,Budget",
            allergenTags = ""
        ) to listOf(
            RecipeIngredientEntity(recipeId = "rec_dal_tadka", ingredientId = "ing_dal", ingredientName = "Yellow Lentils / Dal", quantity = "1", unit = "cup", isRequired = true),
            RecipeIngredientEntity(recipeId = "rec_dal_tadka", ingredientId = "ing_garlic", ingredientName = "Garlic", quantity = "4", unit = "cloves", isRequired = true),
            RecipeIngredientEntity(recipeId = "rec_dal_tadka", ingredientId = "ing_cumin", ingredientName = "Cumin Seeds", quantity = "1", unit = "tsp", isRequired = true),
            RecipeIngredientEntity(recipeId = "rec_dal_tadka", ingredientId = "ing_ghee", ingredientName = "Ghee", quantity = "1", unit = "tbsp", isRequired = true, substitutions = "Butter,Cooking Oil"),
            RecipeIngredientEntity(recipeId = "rec_dal_tadka", ingredientId = "ing_salt", ingredientName = "Salt", quantity = "1", unit = "tsp", isRequired = true)
        ),

        // 8. Vegetable Poha
        RecipeEntity(
            id = "rec_vegetable_poha",
            name = "Kanda Batata Poha",
            description = "Light, wholesome flattened rice tossed with sauteed onions, potatoes, turmeric, and crunchy mustard seeds.",
            cuisine = "Indian",
            mealType = "Breakfast",
            prepTimeMinutes = 5,
            cookTimeMinutes = 8,
            difficulty = "Easy",
            servings = 2,
            calories = 270,
            proteinGrams = 5,
            carbsGrams = 48,
            fatGrams = 6,
            instructions = "Rinse poha gently in a colander under running water for 30 seconds. Drain and let rest.|Heat oil in a pan, add mustard seeds and cumin until popping.|Add finely diced potatoes and onions. Sauté on medium flame for 5 minutes until soft.|Add turmeric and salt. Stir in the softened poha gently so the flakes do not break.|Cover with lid and steam on lowest heat for 2 minutes. Finish with a squeeze of fresh lemon juice!",
            stepTimersMinutes = "0,1,5,1,2",
            imageUrl = "https://images.unsplash.com/photo-1626777552726-4a6b54c97e46?w=800&q=80",
            requiredEquipment = "Pan,Stove",
            dietaryTags = "Vegetarian,Vegan,Gluten-free,Under 15 Minutes,Healthy,Budget",
            allergenTags = ""
        ) to listOf(
            RecipeIngredientEntity(recipeId = "rec_vegetable_poha", ingredientId = "ing_poha", ingredientName = "Poha", quantity = "2", unit = "cup", isRequired = true),
            RecipeIngredientEntity(recipeId = "rec_vegetable_poha", ingredientId = "ing_onion", ingredientName = "Onion", quantity = "1", unit = "pcs", isRequired = true),
            RecipeIngredientEntity(recipeId = "rec_vegetable_poha", ingredientId = "ing_potato", ingredientName = "Potato", quantity = "1", unit = "pcs", isRequired = false),
            RecipeIngredientEntity(recipeId = "rec_vegetable_poha", ingredientId = "ing_oil", ingredientName = "Cooking Oil", quantity = "1", unit = "tbsp", isRequired = true),
            RecipeIngredientEntity(recipeId = "rec_vegetable_poha", ingredientId = "ing_turmeric", ingredientName = "Turmeric Powder", quantity = "0.5", unit = "tsp", isRequired = false),
            RecipeIngredientEntity(recipeId = "rec_vegetable_poha", ingredientId = "ing_lemon", ingredientName = "Lemon", quantity = "0.5", unit = "pcs", isRequired = false)
        ),

        // 9. Garlic Butter Egg Fried Rice
        RecipeEntity(
            id = "rec_egg_fried_rice",
            name = "Garlic Butter Egg Fried Rice",
            description = "High-heat stir-fried rice with scrambled eggs, fragrant garlic, and soy sauce. Perfect empty-fridge meal.",
            cuisine = "Chinese",
            mealType = "Dinner",
            prepTimeMinutes = 5,
            cookTimeMinutes = 7,
            difficulty = "Easy",
            servings = 2,
            calories = 410,
            proteinGrams = 15,
            carbsGrams = 52,
            fatGrams = 14,
            instructions = "Melt butter and a splash of oil in a wok or large pan over high heat.|Add minced garlic and sauté for 30 seconds until golden and aromatic.|Push garlic to the side, crack in eggs and scramble vigorously for 1 minute.|Add cold cooked rice, soy sauce, and black pepper. Toss vigorously on high heat for 3-4 minutes.|Toss in green onions or bell pepper if available and serve piping hot!",
            stepTimersMinutes = "0,1,1,3,0",
            imageUrl = "https://images.unsplash.com/photo-1603133872878-684f208fb84b?w=800&q=80",
            requiredEquipment = "Pan,Stove",
            dietaryTags = "Eggetarian,High-protein,Under 15 Minutes",
            allergenTags = "Eggs,Soy"
        ) to listOf(
            RecipeIngredientEntity(recipeId = "rec_egg_fried_rice", ingredientId = "ing_rice", ingredientName = "Rice", quantity = "2", unit = "cup", isRequired = true),
            RecipeIngredientEntity(recipeId = "rec_egg_fried_rice", ingredientId = "ing_egg", ingredientName = "Egg", quantity = "2", unit = "pcs", isRequired = true),
            RecipeIngredientEntity(recipeId = "rec_egg_fried_rice", ingredientId = "ing_garlic", ingredientName = "Garlic", quantity = "3", unit = "cloves", isRequired = true),
            RecipeIngredientEntity(recipeId = "rec_egg_fried_rice", ingredientId = "ing_butter", ingredientName = "Butter", quantity = "1.5", unit = "tbsp", isRequired = true, substitutions = "Cooking Oil,Ghee"),
            RecipeIngredientEntity(recipeId = "rec_egg_fried_rice", ingredientId = "ing_soy_sauce", ingredientName = "Soy Sauce", quantity = "1", unit = "tbsp", isRequired = false)
        ),

        // 10. Besan Chilla
        RecipeEntity(
            id = "rec_besan_chilla",
            name = "Protein Besan Chilla (Savory Crepe)",
            description = "Crispy gram flour savory pancakes mixed with onions, tomatoes, and spices. Naturally gluten-free and packed with plant protein.",
            cuisine = "Indian",
            mealType = "Breakfast",
            prepTimeMinutes = 5,
            cookTimeMinutes = 6,
            difficulty = "Easy",
            servings = 2,
            calories = 210,
            proteinGrams = 11,
            carbsGrams = 26,
            fatGrams = 7,
            instructions = "In a mixing bowl, combine besan (chickpea flour), water, salt, turmeric, and chili powder to make a smooth pourable batter.|Fold in finely diced onions, tomatoes, and green chilies.|Heat a flat pan, drizzle a few drops of oil, and pour a ladle of batter spreading into a round crepe.|Cook on medium heat for 2 minutes until bubbles pop and bottom turns golden brown.|Flip, drizzle a touch of oil, and cook the other side for 2 minutes until crisp. Enjoy with green chutney or ketchup!",
            stepTimersMinutes = "0,0,0,2,2",
            imageUrl = "https://images.unsplash.com/photo-1546833999-b9f581a1996d?w=800&q=80",
            requiredEquipment = "Pan,Stove",
            dietaryTags = "Vegetarian,Vegan,Gluten-free,High-protein,Healthy,Under 15 Minutes,Budget",
            allergenTags = ""
        ) to listOf(
            RecipeIngredientEntity(recipeId = "rec_besan_chilla", ingredientId = "ing_besan", ingredientName = "Besan / Chickpea Flour", quantity = "1", unit = "cup", isRequired = true),
            RecipeIngredientEntity(recipeId = "rec_besan_chilla", ingredientId = "ing_onion", ingredientName = "Onion", quantity = "1", unit = "pcs", isRequired = true),
            RecipeIngredientEntity(recipeId = "rec_besan_chilla", ingredientId = "ing_tomato", ingredientName = "Tomato", quantity = "0.5", unit = "pcs", isRequired = false),
            RecipeIngredientEntity(recipeId = "rec_besan_chilla", ingredientId = "ing_oil", ingredientName = "Cooking Oil", quantity = "1", unit = "tbsp", isRequired = true),
            RecipeIngredientEntity(recipeId = "rec_besan_chilla", ingredientId = "ing_salt", ingredientName = "Salt", quantity = "1", unit = "tsp", isRequired = true)
        )
    )

    fun getInitialPantryItems(): List<PantryItemEntity> {
        val now = System.currentTimeMillis()
        val twoDaysAhead = now + (2 * 24 * 60 * 60 * 1000L) // Expiring soon!
        val sevenDaysAhead = now + (7 * 24 * 60 * 60 * 1000L)

        return listOf(
            PantryItemEntity(
                ingredientId = "ing_tomato",
                ingredientName = "Tomato",
                category = IngredientCategory.VEGETABLES.displayName,
                emoji = "🍅",
                quantity = 4.0,
                unit = "pcs",
                expiryTimestamp = twoDaysAhead,
                status = PantryStatus.EXPIRING_SOON.name,
                notes = "About to get soft - use in sauce or curries!"
            ),
            PantryItemEntity(
                ingredientId = "ing_onion",
                ingredientName = "Onion",
                category = IngredientCategory.VEGETABLES.displayName,
                emoji = "🧅",
                quantity = 5.0,
                unit = "pcs",
                expiryTimestamp = sevenDaysAhead,
                status = PantryStatus.AVAILABLE.name
            ),
            PantryItemEntity(
                ingredientId = "ing_potato",
                ingredientName = "Potato",
                category = IngredientCategory.VEGETABLES.displayName,
                emoji = "🥔",
                quantity = 3.0,
                unit = "pcs",
                expiryTimestamp = sevenDaysAhead,
                status = PantryStatus.AVAILABLE.name
            ),
            PantryItemEntity(
                ingredientId = "ing_egg",
                ingredientName = "Egg",
                category = IngredientCategory.EGGS.displayName,
                emoji = "🥚",
                quantity = 6.0,
                unit = "pcs",
                expiryTimestamp = sevenDaysAhead,
                status = PantryStatus.AVAILABLE.name
            ),
            PantryItemEntity(
                ingredientId = "ing_bread",
                ingredientName = "Bread",
                category = IngredientCategory.BREAD.displayName,
                emoji = "🍞",
                quantity = 4.0,
                unit = "slices",
                expiryTimestamp = twoDaysAhead,
                status = PantryStatus.EXPIRING_SOON.name,
                notes = "Finish within 2 days"
            )
        )
    }
}
