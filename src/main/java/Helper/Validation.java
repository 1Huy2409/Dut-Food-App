package Helper;
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
            AlertMessage.showAlertErrorMessage("Vui lòng nhập đầy đủ thông tin!");
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
}
