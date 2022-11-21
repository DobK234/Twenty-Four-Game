import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class InstructionsPanel extends DefaultPanel{

    private JLabel instructionImage;

    public InstructionsPanel(GameFrame frame) {
        super(frame,true);

        ImageIcon image = new ImageIcon(getClass().getResource("Instructions.png"));
        ImageIcon scaledImage = GameFrame.scaleImage(image.getImage(), image.getIconWidth()*0.8, image.getIconHeight()*0.8);
        instructionImage = new JLabel();
        instructionImage.setIcon(scaledImage);
        instructionImage.setBounds(0,0,GameFrame.FRAME_WIDTH,GameFrame.FRAME_HEIGHT);
        instructionImage.setVerticalAlignment(JLabel.CENTER);
        instructionImage.setHorizontalAlignment(JLabel.CENTER);
        this.add(instructionImage);
    }
}
