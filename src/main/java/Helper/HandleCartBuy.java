package Helper;

import DAO.Dao_CartItem;
import Model.CartItem;
import Model.FoodItem;

public class HandleCartBuy {
    public static HandleCartBuy getInstance() {
        return new HandleCartBuy();
    }
    public void handleAddToCart(FoodItem item, Integer quantity)
    {
        CartItem findItem = Dao_CartItem.getInstance().checkItemExist(UserSession.getInstance().getCartId(), item.getId());
        if (findItem != null)
        {
            // update quantity
            int newQuantity = findItem.getQuantity();
            newQuantity++;
            findItem.setQuantity(newQuantity);
            Dao_CartItem.getInstance().updateQuantity(findItem);
        }
        else
        {
            if (quantity == null)
            {
                quantity = 1;
            }
            CartItem cartItem = new CartItem();
            cartItem.setCartId(UserSession.getInstance().getCartId());
            cartItem.setFoodItemId(item.getId());
            cartItem.setQuantity(quantity);
            Dao_CartItem.getInstance().create(cartItem);
        }
    }
}
