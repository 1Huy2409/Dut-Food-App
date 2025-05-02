package Config;
import com.stripe.Stripe;
import com.stripe.exception.StripeException;
import com.stripe.model.checkout.Session;
import com.stripe.param.checkout.SessionCreateParams;
import io.github.cdimascio.dotenv.Dotenv;

public class StripeService {
    static Dotenv dotenv = Dotenv.load();
    private static final String STRIPE_SECRET_KEY = dotenv.get("STRIPE_SECRET_KEY");

    static {
        Stripe.apiKey = STRIPE_SECRET_KEY;
    }

    public static String createCheckoutSession(int amount, String orderName) throws StripeException {
        SessionCreateParams params =
                SessionCreateParams.builder()
                        .setMode(SessionCreateParams.Mode.PAYMENT)
                        .setSuccessUrl("http://localhost:5173/success")
                        .setCancelUrl("http://localhost:5173/cancel")
                        .addLineItem(
                                SessionCreateParams.LineItem.builder()
                                        .setQuantity(1L)
                                        .setPriceData(
                                                SessionCreateParams.LineItem.PriceData.builder()
                                                        .setCurrency("vnd")
                                                        .setUnitAmount((long) amount)
                                                        .setProductData(
                                                                SessionCreateParams.LineItem.PriceData.ProductData.builder()
                                                                        .setName(orderName)
                                                                        .build())
                                                        .build())
                                        .build())
                        .build();

        Session session = Session.create(params);
        return session.getUrl();
    }
}

