import org.json.simple.JSONArray;
import org.json.simple.JSONObject;

import javax.swing.*;
import java.awt.*;
/**
 * @author Connor Stevens
 * @version 1.0
 * Revision Notes: Full Release
 *
 *
 * ISTE 121.01 CPS:ID2
 * Final Project
 *
 * Class description: The WageFrame class creates an interface for employee punches..
 *
 *                    The duplication of this code without written consent of the author is strictly prohibited.
 *
 */
public class WageFrame extends JFrame {
    //attributes
    private JSONArray punches;

    /**
     * Parameterized Constructor
     *      Displays punches
     * @param punches An employee's punches
     */
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
    }//end constructor
}//end class
