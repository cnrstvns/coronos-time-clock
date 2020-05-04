import javax.swing.*;
import java.awt.*;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
/**
 * @author Connor Stevens
 * @version 1.0
 * Revision Notes: Full Release
 *
 *
 * ISTE 121.01 CPS:ID2
 * Final Project
 *
 * Class description: The WagePanel class creates a panel to add to the WageFrame for the Client.
 *
 *                    The duplication of this code without written consent of the author is strictly prohibited.
 *
 */
public class WagePanel extends JPanel {
    //attributes
    DateFormat format = new SimpleDateFormat("MM/dd/yyyy HH:mm:ss");

    private JLabel inLabel = new JLabel("Time In: ");
    private JLabel outLabel = new JLabel("Time Out: ");
    private JLabel payLabel = new JLabel("Pay for Period: ");

    private JLabel inTime;
    private JLabel outTime;
    private JLabel wageLabel;

    /**
     * Parameterized Constructor
     * @param in Clock In Time
     * @param out Clock Out Time
     * @param pay Pay rate
     */
    public WagePanel(long in, long out, double pay){
        Date inDate = new Date(in);
        Date outDate = new Date(out);
        String inFormatted = format.format(inDate);
        String outFormatted = format.format(outDate);

        inTime = new JLabel(inFormatted);
        outTime = new JLabel(outFormatted);
        wageLabel = new JLabel(String.format("$%.2f", pay));

        setLayout(new FlowLayout());
        add(inLabel);
        add(inTime);
        add(outLabel);
        add(outTime);
        add(payLabel);
        add(wageLabel);
    }//end constructor
}//end class
