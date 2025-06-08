package Helper;
import DAO.Dao_Food;
import DAO.Dao_OrderItems;
import DAO.Dao_User;
import Model.FoodItem;
import Model.Order;
import Model.OrderInfo;
import Model.OrderItem;
import com.itextpdf.text.*;
import com.itextpdf.text.pdf.*;
import java.io.*;
import java.util.List;
public class InvoiceGenerator {
    public static void exportInvoiceAsPDF(Order order, OrderInfo orderInfo) {
        String pdfPath = "D:/HuyCoding/JavaCode/Invoices/invoice_order_" + order.getId() + ".pdf";
        Document document = new Document();
        try {
            PdfWriter.getInstance(document, new FileOutputStream(pdfPath));
            document.open();

            // Title
            Font unicodeFont = FontFactory.getFont("D:/HuyCoding/JavaCode/LoginApp/Fonts/arial.ttf", BaseFont.IDENTITY_H, BaseFont.EMBEDDED, 12);
            Paragraph title = new Paragraph("HÓA ĐƠN", unicodeFont);
            document.add(title);

            document.add(new Paragraph("Order ID: " + order.getId(), unicodeFont));
            document.add(new Paragraph("Customer Name: " + Dao_User.getInstance().selectedById(order.getUserId()).getFullName(), unicodeFont));
            document.add(new Paragraph("Order Date: " + order.getOrderDate(), unicodeFont));
            document.add(new Paragraph("Status: " + order.getStatus(), unicodeFont));
            document.add(new Paragraph("\n", unicodeFont));

            // Table
            PdfPTable table = new PdfPTable(4);
            table.setWidthPercentage(100);
            table.addCell("Food Name");
            table.addCell("Quantity");
            table.addCell("Price (VND)");
            table.addCell("Total (VND)");

            List<OrderItem> items = Dao_OrderItems.getInstance().getOrderItemsByOrderId(order.getId());
            for (OrderItem item : items) {
                FoodItem food = Dao_Food.getInstance().selectedById(item.getFoodItemId());
                PdfPCell foodNameCell = new PdfPCell(new Phrase(food.getFoodName(), unicodeFont));
                table.addCell(foodNameCell);

                PdfPCell quantityCell = new PdfPCell(new Phrase(String.valueOf(item.getQuantity()), unicodeFont));
                table.addCell(quantityCell);

                PdfPCell priceCell = new PdfPCell(new Phrase(String.format("%,.0f", food.getPrice()), unicodeFont));
                table.addCell(priceCell);

                PdfPCell totalCell = new PdfPCell(new Phrase(String.format("%,.0f", item.getPrice()), unicodeFont));
                table.addCell(totalCell);
            }
            document.add(table);
            document.add(new Paragraph("\n"));
            // Customer Information
            document.add(new Paragraph("Receiver Information:", unicodeFont));
            document.add(new Paragraph("Full Name: " + orderInfo.getFullname(), unicodeFont));
            document.add(new Paragraph("Phone: " + orderInfo.getPhone(), unicodeFont));
            document.add(new Paragraph("Address: " + orderInfo.getAddress(), unicodeFont));
            // Total
            Font totalFont = new Font(Font.FontFamily.HELVETICA, 12, Font.BOLD);
            document.add(new Paragraph("\nTotal: " + String.format("%,.0f VND", order.getTotalPrice()), totalFont));

            document.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
