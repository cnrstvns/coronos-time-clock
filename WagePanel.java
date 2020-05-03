import javax.swing.*;
import java.awt.*;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

public class WagePanel extends JPanel {
    DateFormat format = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");

    private JLabel inLabel = new JLabel("Time In: ");
    private JLabel outLabel = new JLabel("Time Out: ");
    private JLabel payLabel = new JLabel("Pay for Period: ");

    private JLabel inTime;
    private JLabel outTime;
    private JLabel wageLabel;

    public WagePanel(long in, long out, double pay){
        Date inDate = new Date(in);
        Date outDate = new Date(out);
        String inFormatted = format.format(inDate);
        String outFormatted = format.format(outDate);

        inTime = new JLabel(inFormatted);
        outTime = new JLabel(outFormatted);
        wageLabel = new JLabel(String.format("$%f", pay));

        setLayout(new FlowLayout());
        add(inLabel);
        add(inTime);
        add(outLabel);
        add(outTime);
        add(payLabel);
        add(wageLabel);
    }
}
