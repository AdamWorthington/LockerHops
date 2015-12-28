package jumpit.lockereats.server;

public class Item {
	String 		restaurant;		//Name of the restaurant (limit 40 characters)
	String 		item;			//Name of the item (limit 45 characters)
	String 		description;	//Description of the item (limit 280 characters)
	double 		cost;			//Cost of item (Max: 9999.99, Min: 0.00)
	String 		category;		//Small granularity category (breakfast, lunch, dinner)
	String 		subCategory;	//High granularity category (food type)
	
	//These are not mandatory and therefore have defaults
	String[]	ingredients	= null;		//List of ingredients in this item (OPTIONAL)
	boolean 	glutenFree  = false;	//Is this item gluten free? (OPTIONAL)
	boolean 	vegetarian  = false;	//Is this item vegetarian? (OPTIONAL)
	boolean 	vegan		= false;	//Is this item vegan? (OPTIONAL)
	
	public Item(
			String 		restaurant,
			String 		item,
			String 		description,
			double 		cost,
			String 		category,
			String 		subCategory,
			String[]	ingredients,
			boolean 	glutenFree,
			boolean 	vegetarian,
			boolean 	vegan
		) {
		this.restaurant 	= restaurant;
		this.item 			= item;
		this.description 	= description;
		this.cost 			= cost;
		this.category 		= category;
		this.subCategory 	= subCategory;
		this.ingredients 	= ingredients;
		this.glutenFree 	= glutenFree;
		this.vegetarian 	= vegetarian;
		this.vegan 			= vegan;
	}

	public String getRestaurant() {
		return this.restaurant;
	}

	public String getItem() {
		return this.item;
	}

	public String getDescription() {
		return this.description;
	}

	public double getCost() {
		return this.cost;
	}

	public String getCategory() {
		return this.category;
	}

	public String getSubCategory() {
		return this.subCategory;
	}

	public String[] getIngredients() {
		return this.ingredients;
	}

	public boolean isGlutenFree() {
		return this.glutenFree;
	}

	public boolean isVegetarian() {
		return this.vegetarian;
	}

	public boolean isVegan() {
		return this.vegan;
	}
}
