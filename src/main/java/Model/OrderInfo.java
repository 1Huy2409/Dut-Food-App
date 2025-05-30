package Model;

public class OrderInfo {
    private int id;
    private int order_id;
    private String fullname;
    private String phone;
    private String address;

    public OrderInfo() {}
    public OrderInfo(int id, int order_id, String fullname, String phone, String address) {
        this.id = id;
        this.order_id = order_id;
        this.fullname = fullname;
        this.phone = phone;
        this.address = address;
    }
    public OrderInfo(String fullname, String phone, String address) {
        this.fullname = fullname;
        this.phone = phone;
        this.address = address;
    }
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getOrder_id() {
        return order_id;
    }

    public void setOrder_id(int order_id) {
        this.order_id = order_id;
    }

    public String getFullname() {
        return fullname;
    }

    public void setFullname(String fullname) {
        this.fullname = fullname;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }
}
