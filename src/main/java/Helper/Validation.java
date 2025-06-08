package Helper;
import Config.JDBC;
import DAO.Dao_Category;
import DAO.Dao_Food;
import DAO.Dao_User;
import Model.Category;
import Model.FoodItem;
import Model.User;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.text.Normalizer;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Pattern;
import java.util.regex.Matcher;
public class Validation {
    private static Validation _instance;
    public static Validation getInstance()
    {
        if (_instance == null)
        {
            _instance = new Validation();
        }
        return _instance;
    }
    // check valid email
    public static boolean isValidEmail(String email) {
        String regex = "^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+$";
        Pattern pattern = Pattern.compile(regex);
        Matcher matcher = pattern.matcher(email);
        return matcher.matches();
    }
    // check valid phone
    public static boolean isValidPhone(String phone) {
        String regex = "^(0|\\+84)(3|5|7|8|9)[0-9]{8}$";
        return Pattern.matches(regex, phone);
    }
    // validate register form
    public boolean registerValidate(String fullName, String email, String userName, String password, String phone)
    {
        if (fullName.isEmpty() || email.isEmpty() || userName.isEmpty() || password.isEmpty() || phone.isEmpty())
        {
            AlertMessage.showAlertErrorMessage("Cần nhập đầy đủ thông tin để tạo tài khoản!");
            return false;
        }
        if (fullName.length() < 5)
        {
            AlertMessage.showAlertErrorMessage("Họ và tên có độ dài tối thiểu 5 ký tự!");
            return false;
        }
        if (!isValidEmail(email))
        {
            AlertMessage.showAlertErrorMessage("Email này không hợp lệ với chuẩn email!");
            return false;
        }
        if (userName.length() < 3)
        {
            AlertMessage.showAlertErrorMessage("Tên người dùng có độ dài tối thiểu 3 ký tự!");
            return false;
        }
        if (password.length() < 3)
        {
            AlertMessage.showAlertErrorMessage("Mật khẩu có độ dài tối thiểu 3 ký tự!");
            return false;
        }
        if (!isValidPhone(phone))
        {
            AlertMessage.showAlertErrorMessage("Số điện thoại này không hợp lệ!");
            return false;
        }
        return true;
    }
    public boolean loginValidation(String email, String password)
    {
        if (email.isEmpty() && password.isEmpty())
        {
            AlertMessage.showAlertErrorMessage("Vui lòng nhập email và mật khẩu!");
            return false;
        }
        if (email.isEmpty())
        {
            AlertMessage.showAlertErrorMessage("Vui lòng nhập email!");
            return false;
        }
        if (password.isEmpty())
        {
            AlertMessage.showAlertErrorMessage("Vui lòng nhập mật khẩu!");
            return false;
        }
        if (!isValidEmail(email))
        {
            AlertMessage.showAlertErrorMessage("Email không đúng định dạng!");
            return false;
        }
        return true;
    }
    public static boolean isCategoryExists(String nameInput, Integer categoryId) {
        String normalizedInput = normalizeName(nameInput);
        List<Category> categoryList = Dao_Category.getInstance().getAll();

        for (Category c : categoryList) {
            String normalizedExisting = normalizeName(c.getCategoryName());
            // Nếu categoryId không null và trùng với ID của category hiện tại, bỏ qua so sánh
            if (categoryId != null && c.getId() == categoryId) {
                continue;
            }
            if (normalizedInput.equals(normalizedExisting)) {
                return true;
            }
        }
        return false;
    }
    public static boolean isProductsExists(String nameInput, Integer ProductId) {
        String normalizedInput = normalizeName(nameInput);

        // Lấy toàn bộ danh mục từ database
        List<FoodItem> foodItemsList = Dao_Food.getInstance().getAll();

        for (FoodItem c : foodItemsList) {
            if(ProductId != null && c.getId() == ProductId) {
                continue;
            }
            String normalizedExisting = normalizeName(c.getFoodName());
            if (normalizedInput.equals(normalizedExisting)) {
                return true; // Đã tồn tại
            }
        }
        return false; // Không tồn tại
    }
    // Hàm phụ trợ xóa dấu tiếng Việt
    private static String normalizeName(String s) {
        String temp = Normalizer.normalize(s, Normalizer.Form.NFD);
        return temp.replaceAll("\\p{InCombiningDiacriticalMarks}+", "")
                .toLowerCase()
                .trim();
    }
    public static boolean isValidPrice(String priceText) {
        return priceText.matches("\\d+(\\.\\d+)?");
    }
    public static boolean isValidPassword(String password) {
        String regex = "^(?=.*[A-Za-z])(?=.*\\d)(?=.*[@#$%^&+=!])[A-Za-z\\d@#$%^&+=!]{6,}$";
        return password.matches(regex);
    }
    public static String checkUserExists(String username, String email, String phone, Integer UserId) {
        List<User> userList = Dao_User.getInstance().getAll();

        List<String> conflicts = new ArrayList<>();

        for (User u : userList) {
            if (UserId != null && u.getId() == UserId) {
                continue;
            }

            if (u.getUserName().equalsIgnoreCase(username)) {
                conflicts.add("Username");
            }
            if (u.getEmail().equalsIgnoreCase(email)) {
                conflicts.add("Email");
            }
            if (u.getPhone().equals(phone)) {
                conflicts.add("Số điện thoại");
            }
        }

        if (conflicts.isEmpty()) {
            return null;
        }

        return String.join(", ", conflicts) + " đã có người sử dụng";
    }

}
