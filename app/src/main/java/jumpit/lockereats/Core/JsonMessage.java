import org.json.JSONObject;

public class JsonMessage {
	static String num_id = "Type";
	static String user_id = "User";
	static String num_args = "Args";
	static String arg1 = "Arg1";
	
	//numbers starting with 1 denote user to server
	//numbers starting with 2 denote server to user
	static int login = 10;
	static int restraunt = 11;
	static int menu = 12;
	static int order = 13;
	//==============================
	static int login_confirm = 20;
	static int restaurant_list = 21;
	static int send_menu = 22;
	static int order_confirm = 23;
	
	//User to client requests =============================================================================================================================
	
	/*
	 * request login
	 */
	public static JSONObject requestLogin(String user)
	{
		try
		{
			JSONObject json = new JSONObject();
			json.put(num_id, login);
			json.put(user_id, user);
			json.put(num_args, 0);
			return json;
		}
		catch(Exception e)
		{
			e.printStackTrace(System.out);
			return null;
		}
	}
	
	/*
	 * get the list of available restaurants
	 */
	public static JSONObject requestRestaurants(String user)
	{
		try
		{
			JSONObject json = new JSONObject();
			json.put(num_id, restraunt);
			json.put(user_id, user);
			json.put(num_args, 0);
			return json;
		}
		catch(Exception e)
		{
			e.printStackTrace(System.out);
			return null;
		}
	}
	
	/*
	 * get the menu for a restaurant
	 */
	public static JSONObject requestMenu(String user, String rest)
	{
		try
		{
			JSONObject json = new JSONObject();
			json.put(num_id, menu);
			json.put(user_id, user);
			json.put(num_args, 1);
			json.put(arg1, rest);
			return json;
		}
		catch(Exception e)
		{
			e.printStackTrace(System.out);
			return null;
		}
	}
	
	/*
	 * send order
	 */
	public static JSONObject requestOrder(String user, String ord)
	{
		try
		{
			JSONObject json = new JSONObject();
			json.put(num_id, order);
			json.put(user_id, user);
			json.put(num_args, 1);
			json.put(arg1, ord);
			return json;
		}
		catch(Exception e)
		{
			e.printStackTrace(System.out);
			return null;
		}
	}

	//Server to client =============================================================================================================================
	
	/*
	 * send login confirm
	 */
	public static JSONObject sendLogin(String user)
	{
		try
		{
			JSONObject json = new JSONObject();
			json.put(num_id, login_confirm);
			json.put(user_id, user);
			json.put(num_args, 0);
			return json;
		}
		catch(Exception e)
		{
			e.printStackTrace(System.out);
			return null;
		}
	}
	
	/*
	 * send restaurant list
	 */
	public static JSONObject sendRestaurant(String user,String list)
	{
		try
		{
			JSONObject json = new JSONObject();
			json.put(num_id, restaurant_list);
			json.put(user_id, user);
			json.put(num_args, 1);
			json.put(arg1, list);
			return json;
		}
		catch(Exception e)
		{
			e.printStackTrace(System.out);
			return null;
		}
	}
	
	/*
	 * send menu
	 */
	public static JSONObject sendMenu(String user, String menu)
	{
		try
		{
			JSONObject json = new JSONObject();
			json.put(num_id, send_menu);
			json.put(user_id, user);
			json.put(num_args, 1);
			json.put(arg1, menu);
			return json;
		}
		catch(Exception e)
		{
			e.printStackTrace(System.out);
			return null;
		}
	}
	
	/*
	 * order confirmation
	 */
	public static JSONObject sendOrderConfirm(String user)
	{
		try
		{
			JSONObject json = new JSONObject();
			json.put(num_id, order_confirm);
			json.put(user_id, user);
			json.put(num_args, 0);
			return json;
		}
		catch(Exception e)
		{
			e.printStackTrace(System.out);
			return null;
		}
	}
	
	//getter methods =============================================================================================================================
	
	/*
	 * get Type
	 */
	public static int getType(JSONObject json)
	{
		return json.getInt(num_id);
	}
	
	/*
	 * get user
	 */
	public static String getUser(JSONObject json)
	{
		return json.getString(user_id);
	}
	
	/*
	 * get num args
	 */
	public static int getNumArgs(JSONObject json)
	{
		return json.getInt(num_args);
	}
	
	/*
	 * get arg1
	 */
	public static Object getArgOne(JSONObject json)
	{
		return json.get(arg1);
	}
}
