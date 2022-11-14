import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;

public class Card extends JLabel {

    private ArrayList<Double> nums = new ArrayList<>();
    private ArrayList<JLabel> labels = new ArrayList<>();
    private double width;
    private double height;

    public Card (double a, double b, double c, double d) {
        this.setBasics();
        this.setNums(a,b,c,d);
        this.repaint();
    }

    private void setBasics() {
        ImageIcon cardFull = new ImageIcon(this.getClass().getResource("Empty24Card.png"));
        width = cardFull.getIconWidth()*0.8;
        height = cardFull.getIconHeight()*0.8;
        ImageIcon card = GameFrame.scaleImage(cardFull.getImage(), width, height);
        this.setIcon(card);
    }

    public void setBounds(int x, int y) {
        this.setBounds(x, y, (int)width, (int)height);
    }

    public double getWidthCard() {
        return width;
    }

    public double getHeightCard() {
        return height;
    }

    private void setNums (double a, double b, double c, double d) {
        nums.add(a);
        nums.add(b);
        nums.add(c);
        nums.add(d);

        this.addNumsToCard();
    }

    public void changeNums (double a, double b, double c, double d) {
        nums.set(0,a);
        nums.set(1,b);
        nums.set(2,c);
        nums.set(3,d);

        for (int i = 0; i < labels.size(); i++) {
            String numAsText = String.valueOf(nums.get(i));
            if (numAsText.charAt(numAsText.length()-1)=='0') {
                String newNumText = String.valueOf(Math.floor(nums.get(i)));
                labels.get(i).setText(newNumText.split("\\.")[0]);
            } else {
                labels.get(i).setText(String.valueOf(nums.get(i)));
            }
            // If the number ends in .0 (has no decimals), the String will show it as just the number.
            // This was a request from the client.
        }
    }

    private void addNumsToCard() {
        int[] xCoors = {(int)width/2-50,10,170,(int)width/2-50};
        int[] yCoors = {15,95,95,180};

        int i = 0;
        while (i < 4) {
            JLabel label = new JLabel();
            label.setText(String.valueOf(nums.get(i)));
            label.setFont(new Font("Calibri", Font.PLAIN, 38));
            label.setForeground(Color.BLACK);
            label.setHorizontalAlignment(JLabel.CENTER);
            label.setVerticalAlignment(JLabel.CENTER);
            label.setHorizontalTextPosition(JLabel.CENTER);
            label.setVerticalTextPosition(JLabel.CENTER);
            label.setBounds(xCoors[i],yCoors[i],100,100);
            labels.add(label);
            this.add(label);
            i++;
        }
    }

    public ArrayList<Double> getCurrentNums() {
        return nums;
    }
}
