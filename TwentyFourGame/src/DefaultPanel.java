import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class DefaultPanel extends JPanel implements ActionListener {

    protected GameFrame frame;
    protected JButton backButton = new JButton();

    protected DefaultPanel(GameFrame frame, boolean button) {
        this.frame = frame;
        this.setVisible(false);
        this.setLayout(null);
        this.setBounds(0,0, GameFrame.FRAME_WIDTH,GameFrame.FRAME_HEIGHT);
        this.setBackground(GameFrame.CARD_YELLOW);

        int[] boundsBack = {20,445,240,80};
        GameFrame.setButtonDefaults(backButton,"Back",GameFrame.CARD_BLUE, boundsBack);
        backButton.addActionListener(this);
        backButton.setName("Back");

        if (button) {
            this.add(backButton);
        }
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        frame.changePanel(this, frame.getPanel(GameFrame.MAIN_PANEL));
    }
}
