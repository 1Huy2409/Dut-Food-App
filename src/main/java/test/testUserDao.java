package test;

import DAO.Dao_User;
import Model.User;

public class testUserDao {
    public static void main (String[] args)
    {
        User user1 = new User(4, "0935520057", "Nguyen Huu Tien", "huutien2412@gmail.com", "tien1965", "123", 2, true);
        User user2 = new User(5, "0935333436", "Pham Thi Thu Suong", "thusuong0802@gmail.com", "suong1972", "123", 2, true);
        Dao_User.getInstance().getAll();
    }
}
