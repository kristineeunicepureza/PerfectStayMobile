package com.android.theperfectstay

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView

class FoodActivity : AppCompatActivity() {

    private lateinit var foodRecyclerView: RecyclerView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_food)

        val toolbarTitle = findViewById<TextView>(R.id.toolbar_title)
        toolbarTitle.text = "Local Food"

        val backButton = findViewById<ImageView>(R.id.backButton)
        backButton.setOnClickListener {
            finish()
        }

        foodRecyclerView = findViewById(R.id.food_recycler_view)
        foodRecyclerView.layoutManager = LinearLayoutManager(this)

        val categorizedFoodItems = createCategorizedFoodItems()
        val flattenedList = buildFlattenedFoodList(categorizedFoodItems)

        val adapter = FoodAdapter(flattenedList)
        foodRecyclerView.adapter = adapter
    }

    // Data class for a food item
    data class FoodItem(
        val name: String,
        val description: String,
        val imageResourceId: Int,
        val category: FoodCategory // Add category
    )

    // Enum class for food categories
    enum class FoodCategory {
        BREAKFAST, LUNCH, DINNER, SNACKS
    }

    // Sealed class to represent items in the RecyclerView (either a header or a food item)
    sealed class FoodListItem {
        data class CategoryHeader(val category: FoodCategory) : FoodListItem()
        data class FoodItemEntry(val foodItem: FoodItem) : FoodListItem()
    }

    // Updated method to generate sample food data with categories
    private fun createDummyFoodItems(): List<FoodItem> {
        return listOf(
            FoodItem("Lechon", "Roasted pig with crispy skin", R.drawable.lechon, FoodCategory.LUNCH),
            FoodItem("Lechon", "Roasted pig with crispy skin", R.drawable.lechon, FoodCategory.DINNER),

            FoodItem("Sinigang", "Sour and savory soup", R.drawable.sinigang, FoodCategory.LUNCH),
            FoodItem("Sinigang", "Sour and savory soup", R.drawable.sinigang, FoodCategory.DINNER),

            FoodItem("Adobo", "Meat stewed in soy sauce, vinegar, garlic, and peppercorns", R.drawable.adobo, FoodCategory.LUNCH),
            FoodItem("Adobo", "Meat stewed in soy sauce, vinegar, garlic, and peppercorns", R.drawable.adobo, FoodCategory.DINNER),

            FoodItem("Kare-Kare", "Stew with thick peanut sauce, typically made with oxtail", R.drawable.kare_kare, FoodCategory.DINNER),

            FoodItem("Sisig", "Sizzling dish made from parts of pig's head and liver", R.drawable.sisig, FoodCategory.DINNER),

            FoodItem("Pancit", "Stir-fried noodles with meat and vegetables", R.drawable.pancit, FoodCategory.DINNER),
            FoodItem("Pancit", "Stir-fried noodles with meat and vegetables", R.drawable.pancit, FoodCategory.SNACKS),

            FoodItem("Lumpia", "Filipino spring rolls filled with ground meat and vegetables", R.drawable.lumpia, FoodCategory.DINNER),
            FoodItem("Lumpia", "Filipino spring rolls filled with ground meat and vegetables", R.drawable.lumpia, FoodCategory.SNACKS),

            FoodItem("Halo-Halo", "Cold dessert with shaved ice, sweet beans, fruits, and ice cream", R.drawable.halo_halo, FoodCategory.SNACKS),

            FoodItem("Bibingka", "Traditional rice cake cooked in clay pots lined with banana leaves", R.drawable.bibingka, FoodCategory.BREAKFAST),
            FoodItem("Bibingka", "Traditional rice cake cooked in clay pots lined with banana leaves", R.drawable.bibingka, FoodCategory.SNACKS),

            FoodItem("Longganisa", "Filipino sweet or garlicky sausage", R.drawable.longganisa, FoodCategory.BREAKFAST),

            FoodItem("Tinola", "Chicken soup with ginger, green papaya, and chili leaves", R.drawable.tinola, FoodCategory.LUNCH),
            FoodItem("Tinola", "Chicken soup with ginger, green papaya, and chili leaves", R.drawable.tinola, FoodCategory.DINNER),

            FoodItem("Bulalo", "Beef soup made with bone marrow and vegetables", R.drawable.bulalo, FoodCategory.LUNCH),
            FoodItem("Bulalo", "Beef soup made with bone marrow and vegetables", R.drawable.bulalo, FoodCategory.DINNER),

            FoodItem("Crispy Pata", "Deep-fried pork leg with crispy skin and tender meat", R.drawable.crispy_pata, FoodCategory.LUNCH),
            FoodItem("Crispy Pata", "Deep-fried pork leg with crispy skin and tender meat", R.drawable.crispy_pata, FoodCategory.DINNER),

            FoodItem("Leche Flan", "Filipino caramel custard dessert", R.drawable.leche_flan, FoodCategory.SNACKS)
        )
    }

    // Method to organize food items by category
    private fun createCategorizedFoodItems(): Map<FoodCategory, List<FoodItem>> {
        return createDummyFoodItems().groupBy { it.category }
    }

    // Method to build a single list including headers and food items for the adapter
    private fun buildFlattenedFoodList(categorizedItems: Map<FoodCategory, List<FoodItem>>): List<FoodListItem> {
        val flattenedList = mutableListOf<FoodListItem>()

        // Define the desired order of categories
        val categoryOrder = listOf(FoodCategory.BREAKFAST, FoodCategory.LUNCH, FoodCategory.DINNER, FoodCategory.SNACKS)

        for (category in categoryOrder) {
            val items = categorizedItems[category]
            if (!items.isNullOrEmpty()) {
                // Add the category header
                flattenedList.add(FoodListItem.CategoryHeader(category))
                // Add the food items for this category
                flattenedList.addAll(items.map { FoodListItem.FoodItemEntry(it) })
            }
        }

        return flattenedList
    }


    // Adapter for the RecyclerView, handling different view types
    private class FoodAdapter(private val foodListItems: List<FoodListItem>) :
        RecyclerView.Adapter<RecyclerView.ViewHolder>() {

        // Define view types
        companion object {
            private const val VIEW_TYPE_HEADER = 0
            private const val VIEW_TYPE_FOOD_ITEM = 1
        }

        // ViewHolder for Category Headers
        class CategoryViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
            val categoryTitleTextView: TextView = itemView.findViewById(R.id.category_title)
        }

        // ViewHolder for Food Items
        class FoodItemViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
            val nameTextView: TextView = itemView.findViewById(R.id.food_item_name)
            val descriptionTextView: TextView = itemView.findViewById(R.id.food_item_description)
            val imageView: ImageView = itemView.findViewById(R.id.food_item_image)
        }

        override fun getItemViewType(position: Int): Int {
            return when (foodListItems[position]) {
                is FoodListItem.CategoryHeader -> VIEW_TYPE_HEADER
                is FoodListItem.FoodItemEntry -> VIEW_TYPE_FOOD_ITEM
            }
        }

        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
            val inflater = LayoutInflater.from(parent.context)
            return when (viewType) {
                VIEW_TYPE_HEADER -> {
                    val view = inflater.inflate(R.layout.item_food_category_header, parent, false)
                    CategoryViewHolder(view)
                }
                VIEW_TYPE_FOOD_ITEM -> {
                    val view = inflater.inflate(R.layout.item_food, parent, false)
                    FoodItemViewHolder(view)
                }
                else -> throw IllegalArgumentException("Invalid view type")
            }
        }

        override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
            when (holder.itemViewType) {
                VIEW_TYPE_HEADER -> {
                    val headerHolder = holder as CategoryViewHolder
                    val headerItem = foodListItems[position] as FoodListItem.CategoryHeader
                    headerHolder.categoryTitleTextView.text = headerItem.category.name.capitalize() // Display category name
                }
                VIEW_TYPE_FOOD_ITEM -> {
                    val foodItemHolder = holder as FoodItemViewHolder
                    val foodItemEntry = foodListItems[position] as FoodListItem.FoodItemEntry
                    val foodItem = foodItemEntry.foodItem

                    foodItemHolder.nameTextView.text = foodItem.name
                    foodItemHolder.descriptionTextView.text = foodItem.description
                    foodItemHolder.imageView.setImageResource(foodItem.imageResourceId)
                }
            }
        }

        override fun getItemCount() = foodListItems.size
    }
}