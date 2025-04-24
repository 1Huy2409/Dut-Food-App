package Config;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import io.github.cdimascio.dotenv.Dotenv;
import java.security.MessageDigest;
import java.util.*;

public class VNPayPayment {
    private static Dotenv dotenv = Dotenv.load();
    private static final String vnp_TmnCode = dotenv.get("vnp_TmnCode");
    private static final String vnp_HashSecret = dotenv.get("vnp_HashSecret");
    private static final String vnp_Url = dotenv.get("vnp_Url");
    private static final String vnp_Returnurl = dotenv.get("vnp_Returnurl");

    public static String createPaymentUrl(int amount, String orderId) throws Exception {
        Map<String, String> vnp_Params = new HashMap<>();
        vnp_Params.put("vnp_Version", "2.1.0");
        vnp_Params.put("vnp_Command", "pay");
        vnp_Params.put("vnp_TmnCode", vnp_TmnCode);
        vnp_Params.put("vnp_Amount", String.valueOf(amount * 100));
        vnp_Params.put("vnp_CurrCode", "VND");
        vnp_Params.put("vnp_TxnRef", orderId);
        vnp_Params.put("vnp_OrderInfo", "Thanh toan don hang " + orderId);
        vnp_Params.put("vnp_OrderType", "other");
        vnp_Params.put("vnp_Locale", "vn");
        vnp_Params.put("vnp_ReturnUrl", vnp_Returnurl);
        vnp_Params.put("vnp_IpAddr", "127.0.0.1");

        String vnp_CreateDate = new java.text.SimpleDateFormat("yyyyMMddHHmmss").format(new Date());
        vnp_Params.put("vnp_CreateDate", vnp_CreateDate);

        List<String> fieldNames = new ArrayList<>(vnp_Params.keySet());
        Collections.sort(fieldNames);

        StringBuilder hashData = new StringBuilder();
        StringBuilder query = new StringBuilder();
        for (String fieldName : fieldNames) {
            String value = vnp_Params.get(fieldName);
            if (hashData.length() > 0) hashData.append('&');
            hashData.append(fieldName).append('=').append(value);

            query.append(fieldName).append('=').append(URLEncoder.encode(value, StandardCharsets.US_ASCII)).append('&');
        }

        String secureHash = hmacSHA512(vnp_HashSecret, hashData.toString());
        query.append("vnp_SecureHash=").append(secureHash);
        return vnp_Url + "?" + query.toString();
    }

    public static String hmacSHA512(String key, String data) throws Exception {
        javax.crypto.Mac hmac512 = javax.crypto.Mac.getInstance("HmacSHA512");
        javax.crypto.spec.SecretKeySpec secretKeySpec = new javax.crypto.spec.SecretKeySpec(key.getBytes(StandardCharsets.UTF_8), "HmacSHA512");
        hmac512.init(secretKeySpec);
        byte[] bytes = hmac512.doFinal(data.getBytes(StandardCharsets.UTF_8));
        StringBuilder hash = new StringBuilder();
        for (byte b : bytes) {
            hash.append(String.format("%02x", b));
        }
        return hash.toString();
    }
}
