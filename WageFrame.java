import org.json.simple.JSONArray;
import org.json.simple.JSONObject;

import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;

public class WageFrame extends JFrame {

    private JSONArray punches;

    public WageFrame(JSONArray punches){
        this.punches = punches;
        JPanel containerPanel = new JPanel(new GridLayout(punches.size(), 1));
        JScrollPane scrollPane = new JScrollPane(containerPanel);
        setTitle("Punches for Employee");
        add(scrollPane);
        for (Object punch : punches) {
            try{
                JSONObject line = (JSONObject) punch;
                long in = (long) line.get("in");
                long out = (long) line.get("out");
                double pay = (double) line.get("pay");
                containerPanel.add(new WagePanel(in, out, pay));
            }
            catch(NullPointerException npe){
                break;
            }
        }
        setVisible(true);
        setSize(400, 400);
        pack();
    }
}
