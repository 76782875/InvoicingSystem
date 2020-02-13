
package invoicingsystem;

import com.itextpdf.text.BadElementException;
import com.itextpdf.text.BaseColor;
import com.itextpdf.text.Document;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.Font;
import com.itextpdf.text.Image;
import com.itextpdf.text.PageSize;
import com.itextpdf.text.Paragraph;
import com.itextpdf.text.Phrase;
import com.itextpdf.text.Rectangle;
import com.itextpdf.text.Utilities;
import com.itextpdf.text.pdf.PdfPCell;
import com.itextpdf.text.pdf.PdfPTable;
import com.itextpdf.text.pdf.PdfWriter;
import java.awt.Color;
import java.awt.Desktop;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.sql.SQLException;
import java.text.NumberFormat;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Locale;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JFileChooser;
import javax.swing.table.DefaultTableModel;


public class Index extends javax.swing.JFrame {
    static int idget = 0;
     static String addr = "";
     static String phone = "";
      static String landlineno = "";
      static String companyname = "";
      static String com23 ="";
     //static int totalnetamnt = 0;
     static LocalDate today = LocalDate.now();
MyConnection con = new MyConnection();


    public Index() {
        try {
            for (javax.swing.UIManager.LookAndFeelInfo info : javax.swing.UIManager.getInstalledLookAndFeels()) {
                if ("Nimbus".equals(info.getName())) {
                    javax.swing.UIManager.setLookAndFeel(info.getClassName());
                    break;
                }
            }
        } catch (Exception ex) {}
        initComponents();
        populateInfo();
        setBackground();
      
      //  jPanel2.setBackground(null);
        
      // jLabel9.setText(getReferenceNo()+"");
    }
    
    public void setBackground(){
        int c = 0;
        try {
          con.rs =  con.st.executeQuery("SELECT * from inv_color");
          c =  con.rs.getInt("set_color");
          
          
    }catch(Exception e){} finally{ try { con.rs.close(); con.st.close();} catch (SQLException ex) { }}
        
        if (c==0){
        
        jPanel1.setBackground(null);
        jPanel2.setBackground(null);
        jPanel3.setBackground(null);
        jPanel4.setBackground(null);
        }
        else if(c!=0){
        
    jPanel1.setBackground(new Color(c));
    jPanel2.setBackground(new Color(c));
    jPanel3.setBackground(new Color(c));
   jPanel4.setBackground(new Color(c));
        }
    }
    
    
    
    public static void populateInfo(){
        
        jTextArea1.setText("");
        jComboBox1selectcompany.removeAllItems();
        jComboBox1selectcompany.addItem("Select Company");
       
          jTextField8.setText(today+ "");
          jTextField1Date.setText(today+ "");
      
           
   try {
          MyConnection.rs2 =  MyConnection.st2.executeQuery("SELECT com_name, com_id, com_type, com_owner from inv_company");
           
           while(MyConnection.rs2.next()){
           
          jComboBox1selectcompany.addItem(MyConnection.rs2.getString("com_name"));
         
          }
    }catch(Exception e){} finally{ try { MyConnection.rs2.close(); MyConnection.st2.close();} catch (SQLException ex) { }}
    
 //  System.out.print(jComboBox1.getItemCount()+", ");
    }
    
    
    public static void selectCompany(){
        
        ((DefaultTableModel)jTable2.getModel()).setNumRows(0);
        DefaultTableModel model = (DefaultTableModel) jTable1.getModel();
        //model.addRow(new Object[]{"hello", "hi", "hhh", "tyty"});
     model.setRowCount(0);
     TotalInvoiceAmnt();
   if(jComboBox1selectcompany.getItemCount()>0){  
   com23 = jComboBox1selectcompany.getSelectedItem().toString();
   }
   int comid=0;
    
    
    
    try {
          MyConnection.rs2 =  MyConnection.st2.executeQuery("SELECT * from inv_company, inv_items where com_name = '"+com23+"' and com_id = itm_com_id");
           
          
          comid = MyConnection.rs2.getInt("com_id");
          
          addr = MyConnection.rs2.getString("com_address");
          phone = MyConnection.rs2.getString("com_phone");
          landlineno = MyConnection.rs2.getString("com_landline");
          while(MyConnection.rs2.next()){
           
          int itmid = MyConnection.rs2.getInt("itm_id");
          int itmcode = MyConnection.rs2.getInt("itm_code");
          String itmname = MyConnection.rs2.getString("itm_name");
          int itmrate = MyConnection.rs2.getInt("itm_rate");
          int itmtax = MyConnection.rs2.getInt("itm_tax_perc");
          
         
          model.addRow(new Object[]{itmid, itmcode, itmname, itmrate,itmtax});
            
          }
          
    }catch(Exception e){} finally{ try { MyConnection.rs2.close(); MyConnection.st2.close();} catch (SQLException ex) { }}
    
    
    }
    
    
     void deleteSelectedItemFromCart(int itemToDelete){
    
        
        ((DefaultTableModel)jTable2.getModel()).removeRow(itemToDelete);
        TotalInvoiceAmnt();
 
    
    }
    
    void deleteAllfromCart(){
        jTextField2cashreceived.setText("");
    
    ((DefaultTableModel)jTable2.getModel()).setNumRows(0);
      TotalInvoiceAmnt();
    
    }
    
    
    public static void AddToCartTable(String code, String name, String unit, String rate, String tax){
    
           DefaultTableModel model2 = (DefaultTableModel) jTable2.getModel();

           
           double tax2 = Integer.parseInt(tax);
           int tax3 = (int) tax2;
           int amount = Integer.parseInt(unit)*Integer.parseInt(rate);
          
           int taxamnt = amount*tax3/100;
           
           int netamnt = amount+taxamnt;
        model2.addRow(new Object[]{code, name, unit, rate, amount, tax2, taxamnt, netamnt});
           TotalInvoiceAmnt();
      
    
    
    }
    
    
    static void TotalInvoiceAmnt(){
        NumberFormat numberFormat = NumberFormat.getNumberInstance(Locale.US);
    //-----------------------------------------------------------   
        int totalunit = 0;
        for(int i = 0; i < jTable2.getRowCount(); i++){
     
            String tt = jTable2.getValueAt(i, 2).toString();
        int Amount = Integer.parseInt(tt); 
        totalunit = Amount+totalunit;
    } jTextField12.setText(totalunit+"");
   //-----------------------------------------------------------  
      double totalamount = 0;
     for(int i = 0; i < jTable2.getRowCount(); i++){
         String tt = jTable2.getValueAt(i, 4).toString();
        int Amount = Integer.parseInt(tt);
        totalamount = Amount+totalamount;
        
    } jTextField9.setText(numberFormat.format(totalamount));
    //-----------------------------------------------------------  
    double totaltaxamnt = 0;
     for(int i = 0; i < jTable2.getRowCount(); i++){
     
            String tt = jTable2.getValueAt(i, 6).toString();
        double Amount = Double.parseDouble(tt);
        totaltaxamnt = Amount+totaltaxamnt;
        
    } jTextField10.setText(numberFormat.format(totaltaxamnt));
    //-----------------------------------------------------------  
     int totalnetamnt = 0;
     for(int i = 0; i < jTable2.getRowCount(); i++){
     
            String tt = jTable2.getValueAt(i, 7).toString();
            int Amount = Integer.parseInt(tt);
        totalnetamnt = Amount+totalnetamnt;
        
    } jTextField11.setText(totalnetamnt+""); 
    //----------------------------------------------------------- 
    
    
    
    
    }
    
    
    
   ArrayList<String> creatingDataforInvoice(){
   
   NumberFormat numberFormat = NumberFormat.getNumberInstance(Locale.US);
   ArrayList<String> data = new ArrayList<String>();
   for(int count = 0; count < ((DefaultTableModel)jTable2.getModel()).getRowCount(); count++){
      data.add(((DefaultTableModel)jTable2.getModel()).getValueAt(count, 1).toString());
      data.add(((DefaultTableModel)jTable2.getModel()).getValueAt(count, 2).toString());
      data.add(numberFormat.format(Integer.parseInt(((DefaultTableModel)jTable2.getModel()).getValueAt(count, 4).toString())));
  }
  
   
   return data;
   
   }
   
   
   
   int getReferenceNo(){
   int x = 0;
   
   try {
          con.rs =  con.st.executeQuery("SELECT * from inv_ref");
          x =  con.rs.getInt("ref_no");
          
          
    }catch(Exception e){} finally{ try { con.rs.close(); con.st.close();} catch (SQLException ex) { }}
   
   
   return x;
   }
   
   
   void updateReferenceNo(){
       int x = getReferenceNo();
       int x2 = x+1;
       String query = "UPDATE inv_ref SET ref_no= "+x2;
             
       try {
         con.st.executeUpdate(query);
          
    }catch(Exception e){} finally{ try { con.rs.close(); con.st.close();} catch (SQLException ex) { }}
   
   
   }
   
    void generateInvoice(){
        
         ArrayList<String> dataForInvocie = creatingDataforInvoice();
     NumberFormat numberFormat2 = NumberFormat.getNumberInstance(Locale.US);
    int pageWidth = 80;
        int pageDefaultHeight = 175;
        int itemCount = jTable2.getRowCount();
        int margin = 12;
        int footer = 0;
        int bodyTextSize = 9;
        
       // jLabel2.setText(jTextArea1.toString().length()+"");
        if(jTextArea1.getText().toString().trim().length()>80){
            
        footer = 20;
        
        }
        int setPageHeight = pageDefaultHeight+10*itemCount+footer;
        String cutLine = "-----------------------------------------";
        Paragraph cutLinePara = new Paragraph(cutLine,new Font(new Font(Font.FontFamily.COURIER, 8, Font.NORMAL)));
        
    LocalDate date = LocalDate.now();
        File theDir = new File("Invoices History/"+date+"/");
        if(!theDir.exists()){
        theDir.mkdirs();
                }
        
        String filename = "Invoices History/"+date+"/Invoice"+getReferenceNo()+"_no_"+jTextField2Ref.getText()+".pdf";
        
    Document doc = new Document();
    doc.setMargins(margin,margin,margin,0);
    Rectangle rec =  new Rectangle(Utilities.millimetersToPoints(pageWidth),Utilities.millimetersToPoints(setPageHeight));
   
    doc.setPageSize(rec);
        try {
        try {
            Image image = Image.getInstance("logo.jpeg");
            PdfWriter.getInstance(doc, new FileOutputStream(filename));
            doc.open();
            image.scaleToFit(180, 120);
           doc.add(image);
    Paragraph pp = new Paragraph(jComboBox1selectcompany.getSelectedItem().toString(),new Font(new Font(Font.FontFamily.HELVETICA, 15, Font.BOLD)));
    //pp.setAlignment(pp.ALIGN_CENTER);
    doc.add(pp);
    doc.add(new Paragraph("\n"));
    doc.add(new Paragraph("Address: "+addr,new Font(new Font(Font.FontFamily.COURIER, bodyTextSize, Font.BOLD))));
   // doc.add(new Paragraph("Phone: "+phone,new Font(new Font(Font.FontFamily.COURIER, bodyTextSize, Font.BOLD))));
    doc.add(new Paragraph("LandLine: "+landlineno,new Font(new Font(Font.FontFamily.COURIER, bodyTextSize, Font.BOLD))));
    doc.add(new Paragraph("Date: "+jTextField1Date.getText()+" "+jTextField2Time.getText(),new Font(new Font(Font.FontFamily.COURIER, bodyTextSize, Font.BOLD))));
    doc.add(new Paragraph("Receipt No: "+jTextField2Ref.getText(),new Font(new Font(Font.FontFamily.COURIER, bodyTextSize, Font.BOLD))));
    doc.add(new Paragraph("\n"));
    doc.add(cutLinePara);
    
    
    
    // ------------------------------------------------------------------------------------
    float[] columnWidths = new float[]{33f, 10f, 17f}; 
    PdfPTable title = new PdfPTable(3);
    title.setWidthPercentage(100);
    title.setWidths(columnWidths);
    Font titlefont = new Font(new Font(Font.FontFamily.COURIER, 10, Font.BOLD));
   ArrayList<String> titleelement = new ArrayList<String>();
   titleelement.add("Description");
   titleelement.add("Qty");
   titleelement.add("Amount");
   
   
    for(int aw = 0; aw < titleelement.size(); aw++){
    PdfPCell cell2 = new PdfPCell(new Phrase(titleelement.get(aw),titlefont));
    cell2.setBorder(Rectangle.NO_BORDER);
    title.addCell(cell2);
    }
    doc.add(title);
    doc.add(cutLinePara);
     // ------------------------------------------------------------------------------------
    
    PdfPTable table = new PdfPTable(3);
    table.setWidthPercentage(100);
    table.setWidths(columnWidths);
    Font tabtotalfont = new Font(new Font(Font.FontFamily.COURIER, bodyTextSize, Font.BOLD));
   
   
    for(int aw = 0; aw < dataForInvocie.size(); aw++){
    PdfPCell cell = new PdfPCell(new Phrase(dataForInvocie.get(aw),tabtotalfont));
    
    cell.setBorder(Rectangle.NO_BORDER);
    table.addCell(cell);
    }
    doc.add(table);
    
    
    
   // ------------------------------------------------------------------------------------ Total
    doc.add(cutLinePara);
    PdfPTable tabtotal2 = new PdfPTable(3);
    tabtotal2.setWidthPercentage(100);
    tabtotal2.setWidths(columnWidths);
    Font tabtotal2font = new Font(new Font(Font.FontFamily.COURIER, 10, Font.BOLD));
   ArrayList<String> tabtotalelement2 = new ArrayList<String>();
   tabtotalelement2.add("Total ");
   tabtotalelement2.add(""+jTextField12.getText().toString());
   tabtotalelement2.add(""+jTextField9.getText().toString());
   
   
    for(int aw = 0; aw < tabtotalelement2.size(); aw++){
    PdfPCell cell2 = new PdfPCell(new Phrase(tabtotalelement2.get(aw),tabtotal2font));
    cell2.setBorder(Rectangle.NO_BORDER);
    tabtotal2.addCell(cell2);
    }
    doc.add(tabtotal2);
    // ------------------------------------------------------------------------------------ Tax
    
    
    
       // totalnetamnt = Amount+totalnetamnt;
    
    PdfPTable tabtotal3 = new PdfPTable(3);
    tabtotal3.setWidthPercentage(100);
    tabtotal3.setWidths(columnWidths);
    Font tabtotal3font = new Font(new Font(Font.FontFamily.COURIER, 10, Font.BOLD));
   ArrayList<String> tabtotalelement3 = new ArrayList<String>();
   tabtotalelement3.add("Amnt after Tax: ");
   tabtotalelement3.add("");
   tabtotalelement3.add(numberFormat2.format(Integer.parseInt(jTextField11.getText())));
   
   
    for(int aw = 0; aw < tabtotalelement2.size(); aw++){
    PdfPCell cell2 = new PdfPCell(new Phrase(tabtotalelement3.get(aw),tabtotal3font));
    cell2.setBorder(Rectangle.NO_BORDER);
    tabtotal3.addCell(cell2);
    }
    
    doc.add(tabtotal3);
    doc.add(new Paragraph("\n")); 
    // ------------------------------------------------------------------------------------ cash
  
    PdfPTable tabtotal4 = new PdfPTable(3);
    tabtotal4.setWidthPercentage(100);
    tabtotal4.setWidths(columnWidths);
    Font tabtotal4font = new Font(new Font(Font.FontFamily.COURIER, 10, Font.BOLD));
   ArrayList<String> tabtotalelement4 = new ArrayList<String>();
   tabtotalelement4.add("Cash Received: ");
   tabtotalelement4.add("");
   tabtotalelement4.add(numberFormat2.format(Integer.parseInt(jTextField2cashreceived.getText())));
   
   ;
    for(int aw = 0; aw < tabtotalelement2.size(); aw++){
    PdfPCell cell2 = new PdfPCell(new Phrase(tabtotalelement4.get(aw),tabtotal4font));
    cell2.setBorder(Rectangle.NO_BORDER);
    tabtotal4.addCell(cell2);
    }
    doc.add(tabtotal4);
    
    
    // ------------------------------------------------------------------------------------ return
   int cashreceive = Integer.parseInt(jTextField2cashreceived.getText());
   int net = Integer.parseInt(jTextField11.getText());
    
    PdfPTable tabtotal5 = new PdfPTable(3);
    tabtotal5.setWidthPercentage(100);
    tabtotal5.setWidths(columnWidths);
    Font tabtotal5font = new Font(new Font(Font.FontFamily.COURIER, 10, Font.BOLD));
   ArrayList<String> tabtotalelement5 = new ArrayList<String>();
   tabtotalelement5.add("Return Cash: ");
   tabtotalelement5.add("");
   tabtotalelement5.add(numberFormat2.format(cashreceive-net));
   
   
    for(int aw = 0; aw < tabtotalelement2.size(); aw++){
    PdfPCell cell2 = new PdfPCell(new Phrase(tabtotalelement5.get(aw),tabtotal5font));
    cell2.setBorder(Rectangle.NO_BORDER);
    tabtotal5.addCell(cell2);
    }
    doc.add(tabtotal5);
    
   // ------------------------------------------------------------------------------------Footer 
     
     
    doc.add(cutLinePara);
    doc.add(new Paragraph("\n")); 
    doc.add(new Paragraph(jTextArea1.getText()
            ,new Font(new Font(Font.FontFamily.COURIER, bodyTextSize, Font.BOLD))));
    //doc.add(new Paragraph(jTextArea1.getText(),new Font(new Font(Font.FontFamily.COURIER, bodyTextSize, Font.ITALIC))));
    

   
    
     
    doc.close();
        } catch (FileNotFoundException ex) {
            Logger.getLogger(Index.class.getName()).log(Level.SEVERE, null, ex);
        }   catch (BadElementException ex) {
                Logger.getLogger(Index.class.getName()).log(Level.SEVERE, null, ex);
            } catch (IOException ex) {
                Logger.getLogger(Index.class.getName()).log(Level.SEVERE, null, ex);
            }
        } catch (DocumentException ex) {
            Logger.getLogger(Index.class.getName()).log(Level.SEVERE, null, ex);
        }
    
    
    
    
    if (Desktop.isDesktopSupported()) {
    try {
        File myFile = new File(filename);
        Desktop.getDesktop().open(myFile);
    } catch (IOException ex) {
        Logger.getLogger(Index.class.getName()).log(Level.SEVERE, null, ex);
    }
}
    updateReferenceNo();
    }
   
   void generateInvoice2(){
   
   ArrayList<String> dataForInvocie = creatingDataforInvoice();
        String filename = "Invoice_"+getReferenceNo()+".pdf";
        
        String path = "";
      File file  =  new File(filename);
    JFileChooser chos = new JFileChooser();
    chos.setSelectedFile(file);
    int userSelection = chos.showSaveDialog(null);
   
    if (userSelection == JFileChooser.APPROVE_OPTION) {
  
     path = chos.getSelectedFile().getPath();
   //Font font = new Font(new Font(Font.FontFamily.COURIER, 25, Font.BOLD, new BaseColor(12,12,12)));
    Document doc = new Document(PageSize.LETTER,0,0,Utilities.millimetersToPoints(100),Utilities.millimetersToPoints(100));
    //Rectangle rec =  new Rectangle(100,200);
    Rectangle rec2 =  new Rectangle(Utilities.millimetersToPoints(100),Utilities.millimetersToPoints(100));
    //doc.setPageSize(PageSize.A4);
  //  doc.setPageSize(rec);
        try {
        try {
            PdfWriter.getInstance(doc, new FileOutputStream(path));
            doc.open();
           
    doc.add(new Paragraph(jComboBox1selectcompany.getSelectedItem().toString(),new Font(new Font(Font.FontFamily.TIMES_ROMAN, 22, Font.BOLD))));
    doc.add(new Paragraph("\n")); 
    doc.add(new Paragraph("Address: "+addr,new Font(new Font(Font.FontFamily.COURIER, 10, Font.BOLD, new BaseColor(105,105,105)))));
    doc.add(new Paragraph("Phone # "+phone,new Font(new Font(Font.FontFamily.COURIER, 10, Font.BOLD, new BaseColor(105,105,105)))));
    doc.add(new Paragraph("LandLine # "+landlineno,new Font(new Font(Font.FontFamily.COURIER, 10, Font.BOLD, new BaseColor(105,105,105)))));
    doc.add(new Paragraph("Date: "+jTextField1Date.getText()+"   "+jTextField2Time.getText(),new Font(new Font(Font.FontFamily.COURIER, 10, Font.BOLD, new BaseColor(105,105,105)))));
    doc.add(new Paragraph("Reference# "+getReferenceNo(),new Font(new Font(Font.FontFamily.COURIER, 11, Font.BOLD, new BaseColor(105,105,105)))));

    doc.add(new Paragraph("-----------------------------------------------------------------------------------",new Font(new Font(Font.FontFamily.COURIER, 10, Font.BOLD))));
    doc.add(new Paragraph("Invoice\n\n",new Font(new Font(Font.FontFamily.TIMES_ROMAN, 20, Font.BOLD)))); 
    
    
    float[] columnWidths = new float[]{30f, 10f, 15f, 15f, 15f,15f}; 

    //-------------------------------------------------------------------------------- Heading of Invoice
    PdfPTable tabheader = new PdfPTable(6);
    tabheader.setWidthPercentage(95);
    tabheader.setWidths(columnWidths);
    Font headingfont = new Font(new Font(Font.FontFamily.TIMES_ROMAN, 11, Font.BOLD));
   ArrayList<String> heading = new ArrayList<String>();
   heading.add("Description");
   heading.add("Quantity");
   heading.add("Price");
   heading.add("Amount");
   heading.add("Tax Amount");
   heading.add("Net Amount");
   
    for(int aw = 0; aw < 6; aw++){
    PdfPCell cell = new PdfPCell(new Phrase(heading.get(aw),headingfont));
    cell.setBorder(Rectangle.NO_BORDER);
    cell.setBackgroundColor(new BaseColor(220,220,220));
    tabheader.addCell(cell);
    }
    doc.add(tabheader);

   //-------------------------------------------------------------------------------- Body of Invoice
   
    PdfPTable table = new PdfPTable(6);
    table.setWidthPercentage(95);
    table.setWidths(columnWidths);
    Font font = new Font(new Font(Font.FontFamily.COURIER, 10, Font.NORMAL));
   
   
    for(int aw = 0; aw < dataForInvocie.size(); aw++){
    PdfPCell cell = new PdfPCell(new Phrase(dataForInvocie.get(aw),font));
    
    cell.setBorder(Rectangle.NO_BORDER);
    table.addCell(cell);
    }
    doc.add(table);
    
    //-------------------------------------------------------------------------------- Total of Invoice
    
    doc.add(new Paragraph("\n")); 
    PdfPTable tabtotal = new PdfPTable(6);
    tabtotal.setWidthPercentage(95);
    tabtotal.setWidths(columnWidths);
    Font tabtotalfont = new Font(new Font(Font.FontFamily.COURIER, 10, Font.NORMAL));
   ArrayList<String> tabtotalelement = new ArrayList<String>();
   tabtotalelement.add("Total Invoice Amount");
   tabtotalelement.add("");
   tabtotalelement.add("");
   tabtotalelement.add("= "+jTextField9.getText().toString());
   tabtotalelement.add("");
   tabtotalelement.add("= "+jTextField11.getText().toString());
   
    for(int aw = 0; aw < 6; aw++){
    PdfPCell cell = new PdfPCell(new Phrase(tabtotalelement.get(aw),tabtotalfont));
    cell.setBorder(Rectangle.NO_BORDER);
    cell.setBackgroundColor(new BaseColor(242, 242, 242));
    tabtotal.addCell(cell);
    }
    doc.add(tabtotal);
    
    
    
     
    doc.close();
        } catch (FileNotFoundException ex) {
            Logger.getLogger(Index.class.getName()).log(Level.SEVERE, null, ex);
        }
        } catch (DocumentException ex) {
            Logger.getLogger(Index.class.getName()).log(Level.SEVERE, null, ex);
        }
    
    
    }
    
    if (Desktop.isDesktopSupported()) {
    try {
        File myFile = new File(path);
        Desktop.getDesktop().open(myFile);
    } catch (IOException ex) {
        Logger.getLogger(Index.class.getName()).log(Level.SEVERE, null, ex);
    }
}
   updateReferenceNo();
   
   }
   
   
  
    

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jMenuItem6 = new javax.swing.JMenuItem();
        jMenuItem1 = new javax.swing.JMenuItem();
        jMenuItem10 = new javax.swing.JMenuItem();
        jMenuItem11 = new javax.swing.JMenuItem();
        jPanel1 = new javax.swing.JPanel();
        jPanel2 = new javax.swing.JPanel();
        jComboBox1selectcompany = new javax.swing.JComboBox();
        jLabel1 = new javax.swing.JLabel();
        jLabel2 = new javax.swing.JLabel();
        jLabel4 = new javax.swing.JLabel();
        jScrollPane1 = new javax.swing.JScrollPane();
        jTable1 = new javax.swing.JTable();
        jLabel5 = new javax.swing.JLabel();
        jButton1 = new javax.swing.JButton();
        jTextField8 = new javax.swing.JTextField();
        jScrollPane3 = new javax.swing.JScrollPane();
        jTextArea1 = new javax.swing.JTextArea();
        jPanel3 = new javax.swing.JPanel();
        jScrollPane2 = new javax.swing.JScrollPane();
        jTable2 = new javax.swing.JTable();
        jButton5 = new javax.swing.JButton();
        jButton6 = new javax.swing.JButton();
        jButton9 = new javax.swing.JButton();
        jPanel4 = new javax.swing.JPanel();
        jLabel6 = new javax.swing.JLabel();
        jTextField9 = new javax.swing.JTextField();
        jTextField10 = new javax.swing.JTextField();
        jTextField11 = new javax.swing.JTextField();
        jTextField12 = new javax.swing.JTextField();
        jTextField1Date = new javax.swing.JTextField();
        jLabel8 = new javax.swing.JLabel();
        jTextField2Time = new javax.swing.JTextField();
        jLabel7 = new javax.swing.JLabel();
        jLabel9 = new javax.swing.JLabel();
        jTextField2cashreceived = new javax.swing.JTextField();
        jLabel10 = new javax.swing.JLabel();
        jTextField2Ref = new javax.swing.JTextField();
        jMenuBar1 = new javax.swing.JMenuBar();
        jMenu1 = new javax.swing.JMenu();
        jMenuItem3 = new javax.swing.JMenuItem();
        jMenuItem5 = new javax.swing.JMenuItem();
        jMenuItem4 = new javax.swing.JMenuItem();
        jMenuItem2 = new javax.swing.JMenuItem();
        jMenu2 = new javax.swing.JMenu();
        jMenuItem7 = new javax.swing.JMenuItem();
        jMenuItem13 = new javax.swing.JMenuItem();
        jMenuItem9 = new javax.swing.JMenuItem();
        jMenuItem14 = new javax.swing.JMenuItem();
        jMenuItem12 = new javax.swing.JMenuItem();
        jMenu3 = new javax.swing.JMenu();
        jMenuItem8 = new javax.swing.JMenuItem();

        jMenuItem6.setText("jMenuItem6");

        jMenuItem1.setText("jMenuItem1");

        jMenuItem10.setText("jMenuItem10");

        jMenuItem11.setText("jMenuItem11");

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        setTitle("Billing and Invoice System - Powered by sanaulhassan@outlook.com");
        setResizable(false);

        jPanel1.setSize(new java.awt.Dimension(1100, 700));

        jPanel2.setBorder(javax.swing.BorderFactory.createTitledBorder("Company Detail"));

        jComboBox1selectcompany.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "Select Company" }));
        jComboBox1selectcompany.addItemListener(new java.awt.event.ItemListener() {
            public void itemStateChanged(java.awt.event.ItemEvent evt) {
                jComboBox1selectcompanyItemStateChanged(evt);
            }
        });
        jComboBox1selectcompany.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusGained(java.awt.event.FocusEvent evt) {
                jComboBox1selectcompanyFocusGained(evt);
            }
        });
        jComboBox1selectcompany.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jComboBox1selectcompanyActionPerformed(evt);
            }
        });

        jLabel1.setText("Company Name");

        jLabel2.setText("Remarks");

        jLabel4.setText("Today");

        jTable1.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {
                "id", "Item Code", "Item Name", "Rate", "Tax%"
            }
        ) {
            boolean[] canEdit = new boolean [] {
                false, false, false, false, false
            };

            public boolean isCellEditable(int rowIndex, int columnIndex) {
                return canEdit [columnIndex];
            }
        });
        jScrollPane1.setViewportView(jTable1);
        if (jTable1.getColumnModel().getColumnCount() > 0) {
            jTable1.getColumnModel().getColumn(0).setMinWidth(0);
            jTable1.getColumnModel().getColumn(0).setPreferredWidth(0);
            jTable1.getColumnModel().getColumn(0).setMaxWidth(0);
            jTable1.getColumnModel().getColumn(1).setPreferredWidth(90);
            jTable1.getColumnModel().getColumn(1).setMaxWidth(90);
            jTable1.getColumnModel().getColumn(2).setMinWidth(300);
            jTable1.getColumnModel().getColumn(2).setPreferredWidth(300);
            jTable1.getColumnModel().getColumn(4).setPreferredWidth(90);
            jTable1.getColumnModel().getColumn(4).setMaxWidth(90);
        }

        jLabel5.setText("Items in Our Company");

        jButton1.setText("Add to Cart");
        jButton1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton1ActionPerformed(evt);
            }
        });

        jTextField8.setEditable(false);
        jTextField8.setBackground(new java.awt.Color(204, 204, 204));

        jTextArea1.setBackground(new java.awt.Color(236, 236, 236));
        jTextArea1.setColumns(20);
        jTextArea1.setLineWrap(true);
        jTextArea1.setRows(5);
        jTextArea1.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyTyped(java.awt.event.KeyEvent evt) {
                jTextArea1KeyTyped(evt);
            }
        });
        jScrollPane3.setViewportView(jTextArea1);

        javax.swing.GroupLayout jPanel2Layout = new javax.swing.GroupLayout(jPanel2);
        jPanel2.setLayout(jPanel2Layout);
        jPanel2Layout.setHorizontalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel2Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addGroup(jPanel2Layout.createSequentialGroup()
                        .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(jPanel2Layout.createSequentialGroup()
                                .addGap(54, 54, 54)
                                .addComponent(jLabel4)
                                .addGap(18, 18, 18)
                                .addComponent(jTextField8, javax.swing.GroupLayout.PREFERRED_SIZE, 285, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addGroup(jPanel2Layout.createSequentialGroup()
                                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                                    .addComponent(jLabel1)
                                    .addComponent(jLabel2))
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                                    .addGroup(jPanel2Layout.createSequentialGroup()
                                        .addGap(6, 6, 6)
                                        .addComponent(jScrollPane3))
                                    .addComponent(jComboBox1selectcompany, javax.swing.GroupLayout.PREFERRED_SIZE, 285, javax.swing.GroupLayout.PREFERRED_SIZE))))
                        .addGap(35, 35, 35)
                        .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(jPanel2Layout.createSequentialGroup()
                                .addComponent(jLabel5)
                                .addGap(0, 484, Short.MAX_VALUE))
                            .addComponent(jScrollPane1)))
                    .addGroup(jPanel2Layout.createSequentialGroup()
                        .addGap(0, 0, Short.MAX_VALUE)
                        .addComponent(jButton1, javax.swing.GroupLayout.PREFERRED_SIZE, 149, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addContainerGap())
        );
        jPanel2Layout.setVerticalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addComponent(jLabel5)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel2Layout.createSequentialGroup()
                        .addGap(3, 3, 3)
                        .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jLabel1)
                            .addComponent(jComboBox1selectcompany, javax.swing.GroupLayout.PREFERRED_SIZE, 33, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jScrollPane3, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jLabel2))
                        .addGap(18, 18, 18)
                        .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jTextField8, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jLabel4)))
                    .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 181, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jButton1)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        jPanel3.setBorder(javax.swing.BorderFactory.createTitledBorder("Cart (Items to be add)"));

        jTable2.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {
                "Code", "Item Name", "Unit", "Rate", "Amount", "Tax%", "Tax Amnt", "Net Amount"
            }
        ) {
            boolean[] canEdit = new boolean [] {
                false, false, false, false, false, false, false, false
            };

            public boolean isCellEditable(int rowIndex, int columnIndex) {
                return canEdit [columnIndex];
            }
        });
        jScrollPane2.setViewportView(jTable2);
        if (jTable2.getColumnModel().getColumnCount() > 0) {
            jTable2.getColumnModel().getColumn(0).setPreferredWidth(3);
            jTable2.getColumnModel().getColumn(1).setMinWidth(160);
            jTable2.getColumnModel().getColumn(1).setPreferredWidth(160);
        }

        jButton5.setText("Delete Item");
        jButton5.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton5ActionPerformed(evt);
            }
        });

        jButton6.setText("Clear All");
        jButton6.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton6ActionPerformed(evt);
            }
        });

        jButton9.setText("Generate Invoice");
        jButton9.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton9ActionPerformed(evt);
            }
        });

        jPanel4.setBorder(javax.swing.BorderFactory.createEtchedBorder());

        jLabel6.setText("Total Invoice Amount");

        jTextField9.setEditable(false);
        jTextField9.setBackground(new java.awt.Color(204, 204, 204));

        jTextField10.setEditable(false);
        jTextField10.setBackground(new java.awt.Color(204, 204, 204));

        jTextField11.setEditable(false);
        jTextField11.setBackground(new java.awt.Color(204, 204, 204));

        jTextField12.setEditable(false);
        jTextField12.setBackground(new java.awt.Color(204, 204, 204));

        javax.swing.GroupLayout jPanel4Layout = new javax.swing.GroupLayout(jPanel4);
        jPanel4.setLayout(jPanel4Layout);
        jPanel4Layout.setHorizontalGroup(
            jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel4Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabel6)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 141, Short.MAX_VALUE)
                .addComponent(jTextField12, javax.swing.GroupLayout.PREFERRED_SIZE, 122, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(127, 127, 127)
                .addComponent(jTextField9, javax.swing.GroupLayout.PREFERRED_SIZE, 127, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(126, 126, 126)
                .addComponent(jTextField10, javax.swing.GroupLayout.PREFERRED_SIZE, 110, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jTextField11, javax.swing.GroupLayout.PREFERRED_SIZE, 127, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap())
        );
        jPanel4Layout.setVerticalGroup(
            jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel4Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel6)
                    .addComponent(jTextField9, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jTextField10, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jTextField11, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jTextField12, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        jTextField1Date.setBackground(new java.awt.Color(237, 237, 237));

        jLabel8.setText("Time:");

        jTextField2Time.setBackground(new java.awt.Color(238, 238, 238));
        jTextField2Time.setText("00:00 AM");

        jLabel7.setText("Date:");

        jLabel9.setText("Cash:");

        jTextField2cashreceived.setBackground(new java.awt.Color(238, 238, 238));
        jTextField2cashreceived.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyTyped(java.awt.event.KeyEvent evt) {
                jTextField2cashreceivedKeyTyped(evt);
            }
        });

        jLabel10.setText("Ref No:");

        jTextField2Ref.setBackground(new java.awt.Color(238, 238, 238));

        javax.swing.GroupLayout jPanel3Layout = new javax.swing.GroupLayout(jPanel3);
        jPanel3.setLayout(jPanel3Layout);
        jPanel3Layout.setHorizontalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel3Layout.createSequentialGroup()
                .addGap(25, 25, 25)
                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jScrollPane2)
                    .addGroup(jPanel3Layout.createSequentialGroup()
                        .addComponent(jButton5, javax.swing.GroupLayout.PREFERRED_SIZE, 99, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jButton6)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(jLabel7)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jTextField1Date, javax.swing.GroupLayout.PREFERRED_SIZE, 111, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(18, 18, 18)
                        .addComponent(jLabel8)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jTextField2Time, javax.swing.GroupLayout.PREFERRED_SIZE, 89, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jLabel10)
                        .addGap(18, 18, 18)
                        .addComponent(jTextField2Ref)
                        .addGap(18, 18, 18)
                        .addComponent(jLabel9)
                        .addGap(18, 18, 18)
                        .addComponent(jTextField2cashreceived, javax.swing.GroupLayout.PREFERRED_SIZE, 123, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(18, 18, 18)
                        .addComponent(jButton9))
                    .addComponent(jPanel4, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addContainerGap())
        );
        jPanel3Layout.setVerticalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel3Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                        .addComponent(jLabel9)
                        .addComponent(jTextField2cashreceived, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                        .addComponent(jButton5)
                        .addComponent(jButton6)
                        .addComponent(jButton9)
                        .addComponent(jLabel7)
                        .addComponent(jTextField1Date, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addComponent(jLabel8)
                        .addComponent(jTextField2Time, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addComponent(jLabel10)
                        .addComponent(jTextField2Ref, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jScrollPane2, javax.swing.GroupLayout.DEFAULT_SIZE, 187, Short.MAX_VALUE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jPanel4, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(16, 16, 16))
        );

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(jPanel2, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jPanel3, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addContainerGap(14, Short.MAX_VALUE))
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jPanel2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jPanel3, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        jMenu1.setText("File");

        jMenuItem3.setAccelerator(javax.swing.KeyStroke.getKeyStroke(java.awt.event.KeyEvent.VK_C, java.awt.event.InputEvent.CTRL_MASK));
        jMenuItem3.setText("Manage Companies");
        jMenuItem3.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jMenuItem3ActionPerformed(evt);
            }
        });
        jMenu1.add(jMenuItem3);

        jMenuItem5.setAccelerator(javax.swing.KeyStroke.getKeyStroke(java.awt.event.KeyEvent.VK_N, java.awt.event.InputEvent.CTRL_MASK));
        jMenuItem5.setText("Add Company");
        jMenuItem5.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jMenuItem5ActionPerformed(evt);
            }
        });
        jMenu1.add(jMenuItem5);

        jMenuItem4.setAccelerator(javax.swing.KeyStroke.getKeyStroke(java.awt.event.KeyEvent.VK_I, java.awt.event.InputEvent.CTRL_MASK));
        jMenuItem4.setText("Manage Items");
        jMenuItem4.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jMenuItem4ActionPerformed(evt);
            }
        });
        jMenu1.add(jMenuItem4);

        jMenuItem2.setAccelerator(javax.swing.KeyStroke.getKeyStroke(java.awt.event.KeyEvent.VK_X, java.awt.event.InputEvent.CTRL_MASK));
        jMenuItem2.setText("Exit");
        jMenuItem2.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jMenuItem2ActionPerformed(evt);
            }
        });
        jMenu1.add(jMenuItem2);

        jMenuBar1.add(jMenu1);

        jMenu2.setText("Edit");

        jMenuItem7.setAccelerator(javax.swing.KeyStroke.getKeyStroke(java.awt.event.KeyEvent.VK_G, java.awt.event.InputEvent.CTRL_MASK));
        jMenuItem7.setText("Generate Invoice");
        jMenuItem7.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jMenuItem7ActionPerformed(evt);
            }
        });
        jMenu2.add(jMenuItem7);

        jMenuItem13.setAccelerator(javax.swing.KeyStroke.getKeyStroke(java.awt.event.KeyEvent.VK_EQUALS, 0));
        jMenuItem13.setText("Add to Cart");
        jMenuItem13.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jMenuItem13ActionPerformed(evt);
            }
        });
        jMenu2.add(jMenuItem13);

        jMenuItem9.setAccelerator(javax.swing.KeyStroke.getKeyStroke(java.awt.event.KeyEvent.VK_D, java.awt.event.InputEvent.SHIFT_MASK | java.awt.event.InputEvent.CTRL_MASK));
        jMenuItem9.setText("Clear All");
        jMenuItem9.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jMenuItem9ActionPerformed(evt);
            }
        });
        jMenu2.add(jMenuItem9);

        jMenuItem14.setText("Background");
        jMenuItem14.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jMenuItem14ActionPerformed(evt);
            }
        });
        jMenu2.add(jMenuItem14);

        jMenuItem12.setAccelerator(javax.swing.KeyStroke.getKeyStroke(java.awt.event.KeyEvent.VK_D, java.awt.event.InputEvent.CTRL_MASK));
        jMenuItem12.setText("Delete Item");
        jMenuItem12.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jMenuItem12ActionPerformed(evt);
            }
        });
        jMenu2.add(jMenuItem12);

        jMenuBar1.add(jMenu2);

        jMenu3.setText("Help");

        jMenuItem8.setAccelerator(javax.swing.KeyStroke.getKeyStroke(java.awt.event.KeyEvent.VK_H, java.awt.event.InputEvent.CTRL_MASK));
        jMenuItem8.setText("About");
        jMenuItem8.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jMenuItem8ActionPerformed(evt);
            }
        });
        jMenu3.add(jMenuItem8);

        jMenuBar1.add(jMenu3);

        setJMenuBar(jMenuBar1);

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addComponent(jPanel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addContainerGap())
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(jPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap())
        );

        pack();
        setLocationRelativeTo(null);
    }// </editor-fold>//GEN-END:initComponents

    private void jComboBox1selectcompanyActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jComboBox1selectcompanyActionPerformed
        
        
    }//GEN-LAST:event_jComboBox1selectcompanyActionPerformed

    private void jComboBox1selectcompanyItemStateChanged(java.awt.event.ItemEvent evt) {//GEN-FIRST:event_jComboBox1selectcompanyItemStateChanged
       selectCompany();
    }//GEN-LAST:event_jComboBox1selectcompanyItemStateChanged

    private void jButton1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton1ActionPerformed
       
        int row = jTable1.getSelectedRow();
      String value_id = (jTable1.getModel().getValueAt(row, 0).toString());
      int x = Integer.parseInt(value_id);
       idget = x;
        AddingToCart addcart = new AddingToCart();
       addcart.setVisible(true);
        
        
    }//GEN-LAST:event_jButton1ActionPerformed

    private void jButton6ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton6ActionPerformed
        deleteAllfromCart();
    }//GEN-LAST:event_jButton6ActionPerformed

    private void jButton5ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton5ActionPerformed
      
        
        int row = jTable2.getSelectedRow();
      
        
        deleteSelectedItemFromCart(row);
    }//GEN-LAST:event_jButton5ActionPerformed

    private void jButton9ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton9ActionPerformed
     if(jTable2.getRowCount()>0){
        generateInvoice();}
    }//GEN-LAST:event_jButton9ActionPerformed

    private void jComboBox1selectcompanyFocusGained(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_jComboBox1selectcompanyFocusGained
        
    }//GEN-LAST:event_jComboBox1selectcompanyFocusGained

    private void jMenuItem3ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jMenuItem3ActionPerformed
       ManageCompany managec = new ManageCompany();
       managec.setVisible(true);
    }//GEN-LAST:event_jMenuItem3ActionPerformed

    private void jMenuItem4ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jMenuItem4ActionPerformed
       new ManageItems().setVisible(true);
    }//GEN-LAST:event_jMenuItem4ActionPerformed

    private void jMenuItem2ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jMenuItem2ActionPerformed
       dispose();
    }//GEN-LAST:event_jMenuItem2ActionPerformed

    private void jMenuItem5ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jMenuItem5ActionPerformed
        new AddCompany().setVisible(true);
    }//GEN-LAST:event_jMenuItem5ActionPerformed

    private void jMenuItem7ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jMenuItem7ActionPerformed
         if(jTable2.getRowCount()>0){
        generateInvoice();}
    }//GEN-LAST:event_jMenuItem7ActionPerformed

    private void jMenuItem8ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jMenuItem8ActionPerformed
        new About().setVisible(true);
    }//GEN-LAST:event_jMenuItem8ActionPerformed

    private void jMenuItem13ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jMenuItem13ActionPerformed
        int row = jTable1.getSelectedRow();
      String value_id = (jTable1.getModel().getValueAt(row, 0).toString());
      int x = Integer.parseInt(value_id);
       idget = x;
        AddingToCart addcart = new AddingToCart();
       addcart.setVisible(true);
    }//GEN-LAST:event_jMenuItem13ActionPerformed

    private void jMenuItem9ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jMenuItem9ActionPerformed
       deleteAllfromCart();
    }//GEN-LAST:event_jMenuItem9ActionPerformed

    private void jMenuItem12ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jMenuItem12ActionPerformed
        int row = jTable2.getSelectedRow();
        deleteSelectedItemFromCart(row);
    }//GEN-LAST:event_jMenuItem12ActionPerformed

    private void jMenuItem14ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jMenuItem14ActionPerformed
        new Setting().setVisible(true);
    }//GEN-LAST:event_jMenuItem14ActionPerformed

    private void jTextArea1KeyTyped(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_jTextArea1KeyTyped
       String s=jTextArea1.getText();
   int l=s.length();
   try{
   if(l>=250){evt.consume();
   jTextArea1.setText(s.subSequence(0, 250)+"");
   }
   }
   catch(Exception w){}
    }//GEN-LAST:event_jTextArea1KeyTyped

    private void jTextField2cashreceivedKeyTyped(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_jTextField2cashreceivedKeyTyped
       char c = evt.getKeyChar();
      if (!((c >= '0') && (c <= '9') ||
         (c == evt.VK_BACK_SPACE) ||
         (c == evt.VK_DELETE))) {
        getToolkit().beep();
        evt.consume();
      }
    }//GEN-LAST:event_jTextField2cashreceivedKeyTyped

    /**
     * @param args the command line arguments
     */
    public static void main(String args[]) {
        /* Set the Nimbus look and feel */
        //<editor-fold defaultstate="collapsed" desc=" Look and feel setting code (optional) ">
        /* If Nimbus (introduced in Java SE 6) is not available, stay with the default look and feel.
         * For details see http://download.oracle.com/javase/tutorial/uiswing/lookandfeel/plaf.html 
         */
        try {
            for (javax.swing.UIManager.LookAndFeelInfo info : javax.swing.UIManager.getInstalledLookAndFeels()) {
                if ("Nimbus".equals(info.getName())) {
                    javax.swing.UIManager.setLookAndFeel(info.getClassName());
                    break;
                }
            }
        } catch (ClassNotFoundException ex) {
            java.util.logging.Logger.getLogger(Index.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(Index.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(Index.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(Index.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>

        /* Create and display the form */
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                new Index().setVisible(true);
            }
        });
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton jButton1;
    private javax.swing.JButton jButton5;
    private javax.swing.JButton jButton6;
    private javax.swing.JButton jButton9;
    private static javax.swing.JComboBox jComboBox1selectcompany;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel10;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JLabel jLabel6;
    private javax.swing.JLabel jLabel7;
    private javax.swing.JLabel jLabel8;
    private javax.swing.JLabel jLabel9;
    private javax.swing.JMenu jMenu1;
    private javax.swing.JMenu jMenu2;
    private javax.swing.JMenu jMenu3;
    private javax.swing.JMenuBar jMenuBar1;
    private javax.swing.JMenuItem jMenuItem1;
    private javax.swing.JMenuItem jMenuItem10;
    private javax.swing.JMenuItem jMenuItem11;
    private javax.swing.JMenuItem jMenuItem12;
    private javax.swing.JMenuItem jMenuItem13;
    private javax.swing.JMenuItem jMenuItem14;
    private javax.swing.JMenuItem jMenuItem2;
    private javax.swing.JMenuItem jMenuItem3;
    private javax.swing.JMenuItem jMenuItem4;
    private javax.swing.JMenuItem jMenuItem5;
    private javax.swing.JMenuItem jMenuItem6;
    private javax.swing.JMenuItem jMenuItem7;
    private javax.swing.JMenuItem jMenuItem8;
    private javax.swing.JMenuItem jMenuItem9;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JPanel jPanel3;
    private javax.swing.JPanel jPanel4;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JScrollPane jScrollPane2;
    private javax.swing.JScrollPane jScrollPane3;
    private static javax.swing.JTable jTable1;
    public static javax.swing.JTable jTable2;
    private static javax.swing.JTextArea jTextArea1;
    private static javax.swing.JTextField jTextField10;
    private static javax.swing.JTextField jTextField11;
    private static javax.swing.JTextField jTextField12;
    private static javax.swing.JTextField jTextField1Date;
    private javax.swing.JTextField jTextField2Ref;
    private javax.swing.JTextField jTextField2Time;
    private javax.swing.JTextField jTextField2cashreceived;
    private static javax.swing.JTextField jTextField8;
    private static javax.swing.JTextField jTextField9;
    // End of variables declaration//GEN-END:variables
}
