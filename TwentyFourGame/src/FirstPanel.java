import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class FirstPanel extends DefaultPanel implements ActionListener {

    private JButton start = new JButton();
    private JButton instructions = new JButton();
    private JButton leaderboards = new JButton();
    private JLabel title = new JLabel();
    private final String startText = "Start Game";
    private final String instructionsText = "Instructions";
    private final String leaderboardsText = "Leaderboards";


    public FirstPanel(GameFrame frame) {
        super(frame,false);
        buildComponents();
        addComponents();
    }

    private void buildComponents() {
        int standardHeight = 80;
        int standardWidth = 240;
        int difference = 30;
        int[] startBounds = {this.getWidth()-this.getWidth()/2-(standardWidth/2),200,standardWidth,standardHeight};
        GameFrame.setButtonDefaults(start,startText, GameFrame.CARD_BLUE,startBounds);
        start.addActionListener(this);

        int[] instructionsBounds = {start.getX(), start.getY()+start.getHeight()+difference,start.getWidth(),start.getHeight()};
        GameFrame.setButtonDefaults(instructions,instructionsText,GameFrame.CARD_BLUE,instructionsBounds);
        instructions.addActionListener(this);

        int[] leaderboardsBounds = {start.getX(), instructions.getY()+instructions.getHeight()+difference,instructions.getWidth(),instructions.getHeight()};
        GameFrame.setButtonDefaults(leaderboards,leaderboardsText,GameFrame.CARD_BLUE,leaderboardsBounds);
        leaderboards.addActionListener(this);

        if (!frame.leaderboards()) {
            leaderboards.setBackground(GameFrame.CARD_RED);
            leaderboards.setText("<html>Leaderboards are currently<br>unavailable.</html>");
        }

        ImageIcon image = new ImageIcon(getClass().getResource("24Card.png"));
        ImageIcon scaledImage = GameFrame.scaleImage(image.getImage(),image.getIconWidth()*0.8,image.getIconHeight()*0.8);
        title.setIcon(scaledImage);
        title.setBounds(this.getWidth()-this.getWidth()/2-(scaledImage.getIconWidth()/2),15, scaledImage.getIconWidth(), scaledImage.getIconHeight());
        title.setHorizontalTextPosition(JLabel.CENTER);
        title.setVerticalTextPosition(JLabel.NORTH);
        title.setVerticalAlignment(JLabel.CENTER);
        title.setHorizontalAlignment(JLabel.CENTER);
    }

    private void addComponents() {
        this.add(start);
        this.add(instructions);
        this.add(leaderboards);
        this.add(title);
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        JButton buttonPressed = (JButton) e.getSource();
        switch (buttonPressed.getText()) {
            case (instructionsText) : {
                frame.changePanel(this, frame.getPanel(GameFrame.INSTRUCTIONS_PANEL));
                break;
            }
            case (startText) : {
                frame.changePanel(this,frame.getPanel(GameFrame.GAME_MAKER_PANEL));
                break;
            }
            case (leaderboardsText) : {
                LeaderboardsPanel lp = (LeaderboardsPanel) frame.getPanel(GameFrame.LEADERBOARDS_PANEL);
                lp.updateLeaderboards();
                frame.changePanel(this,frame.getPanel(GameFrame.LEADERBOARDS_PANEL));
                break;
            }
        }
    }
}
