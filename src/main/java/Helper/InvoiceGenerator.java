package Helper;
import DAO.Dao_Food;
import DAO.Dao_OrderItems;
import Model.FoodItem;
import Model.Order;
import Model.OrderItem;
import com.itextpdf.text.*;
import com.itextpdf.text.pdf.*;
import java.io.*;
import java.util.List;
public class InvoiceGenerator {
    public static void exportInvoiceAsPDF(Order order) {
        String pdfPath = "invoice_order_" + order.getId() + ".pdf";
        Document document = new Document();
        try {
            PdfWriter.getInstance(document, new FileOutputStream(pdfPath));
            document.open();

            // Title
            Font boldFont = new Font(Font.FontFamily.HELVETICA, 20, Font.BOLD);
            Paragraph title = new Paragraph("INVOICE", boldFont);
            document.add(title);

            document.add(new Paragraph("Order ID: " + order.getId()));
            document.add(new Paragraph("Customer ID: " + order.getUserId()));
            document.add(new Paragraph("Order Date: " + order.getOrderDate()));
            document.add(new Paragraph("Status: " + order.getStatus()));
            document.add(new Paragraph("\n"));

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
                table.addCell(food.getFoodName());
                table.addCell(String.valueOf(item.getQuantity()));
                table.addCell(String.format("%,.0f", food.getPrice()));
                table.addCell(String.format("%,.0f", item.getPrice()));
            }
            document.add(table);

            // Total
            Font totalFont = new Font(Font.FontFamily.HELVETICA, 12, Font.BOLD);
            document.add(new Paragraph("\nTotal: " + String.format("%,.0f VND", order.getTotalPrice()), totalFont));

            document.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
