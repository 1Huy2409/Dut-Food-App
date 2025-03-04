package Model;

<<<<<<< HEAD
import java.sql.Timestamp;

public class Payment {
    private int id;
    private int roleId;
    private String paymentMethod;
    private double amount;
    private Timestamp paymentDate;
    private String status;

    public Payment() {}

    public Payment(int id, int roleId, String paymentMethod, double amount, Timestamp paymentDate, String status) {
        this.id = id;
        this.roleId = roleId;
        this.paymentMethod = paymentMethod;
        this.amount = amount;
        this.paymentDate = paymentDate;
        this.status = status;
=======
public class Payment {
    private int id;
    private int orderId;
    private String paymentMethod;
    private double amount;
    private String paymentDate;
    private String status;

    public Payment(int id, int orderId, String paymentMethod, double amount, String status, String paymentDate) {
        this.id = id;
        this.orderId = orderId;
        this.paymentMethod = paymentMethod;
        this.amount = amount;
        this.status = status;
        this.paymentDate = paymentDate;
>>>>>>> phihihi
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

<<<<<<< HEAD
    public int getRoleId() {
        return roleId;
    }

    public void setRoleId(int roleId) {
        this.roleId = roleId;
=======
    public int getOrderId() {
        return orderId;
    }

    public void setOrderId(int orderId) {
        this.orderId = orderId;
>>>>>>> phihihi
    }

    public String getPaymentMethod() {
        return paymentMethod;
    }

    public void setPaymentMethod(String paymentMethod) {
        this.paymentMethod = paymentMethod;
    }

    public double getAmount() {
        return amount;
    }

    public void setAmount(double amount) {
        this.amount = amount;
    }

<<<<<<< HEAD
    public Timestamp getPaymentDate() {
        return paymentDate;
    }

    public void setPaymentDate(Timestamp paymentDate) {
=======
    public String getPaymentDate() {
        return paymentDate;
    }

    public void setPaymentDate(String paymentDate) {
>>>>>>> phihihi
        this.paymentDate = paymentDate;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }
}
