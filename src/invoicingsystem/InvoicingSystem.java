
package invoicingsystem;


public class InvoicingSystem {

    
    public static void main(String[] args) {
        try {
            for (javax.swing.UIManager.LookAndFeelInfo info : javax.swing.UIManager.getInstalledLookAndFeels()) {
                if ("Nimbus".equals(info.getName())) {
                    javax.swing.UIManager.setLookAndFeel(info.getClassName());
                    break;
                }
            }
        } catch (Exception ex) {}
        
        Index x = new Index();
      //  MyConnection con = new MyConnection();
        
       // ManageCompany x = new ManageCompany();
      //  Setting x = new Setting();
        
         x.setVisible(true);
    }
    
}
