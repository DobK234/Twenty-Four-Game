import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.Objects;

public class GameMakerPanel extends DefaultPanel implements ActionListener {

    private final ArrayList<JButton> teamSizes = new ArrayList<>();
    private final ArrayList<JButton> teamsQuantities = new ArrayList<>();
    private final ArrayList<JButton> difficultyChoices = new ArrayList<>();
    private final ArrayList<JLabel> titles = new ArrayList<>();
    private final JButton nextJButton = new JButton();

    private int teamSizeInt = 2;
    private int teamsQuantityInt = 3;
    private String difficultyChoiceString = TwentyFourWriter.MEDIUM_DIFF;

    public GameMakerPanel(GameFrame frame) {
        super(frame,true);

        buildComponents();
        this.add(nextJButton);
        addComponentListLabel(titles);
        addComponentListButton(teamsQuantities);
        addComponentListButton(difficultyChoices);
        addComponentListButton(teamSizes);
    }

    private void buildComponents() {
        String[] teamSizeChoices = {"One","Two","Four"};
        for (int i = 0; i < teamSizeChoices.length; i++) {
            JButton button = new JButton();
            int[] bounds = new int[4];
            bounds[0] = 150+108*i+200*i;
            bounds[1] = 315;
            bounds[2] = 108;
            bounds[3] = 50;
            GameFrame.setButtonDefaults(button,teamSizeChoices[i],GameFrame.CARD_BLUE,bounds);
            button.addActionListener(this);
            button.setName("Size: " + teamSizeChoices[i]);
            teamSizes.add(button);
        }

        String[] teamsQuantityChoices = {"Two","Three","Four"};
        for (int i = 0; i < teamsQuantityChoices.length; i++) {
            JButton button = new JButton();
            int[] bounds = new int[4];
            bounds[0] = 150+108*i+200*i;
            bounds[1] = 190;
            bounds[2] = 108;
            bounds[3] = 50;
            GameFrame.setButtonDefaults(button,teamsQuantityChoices[i],GameFrame.CARD_BLUE,bounds);
            button.addActionListener(this);
            button.setName("Quantity: " + teamsQuantityChoices[i]);
            teamsQuantities.add(button);
        }

        String[] difficultyChoicesList = {"Easy","Medium","Hard"};
        for (int i = 0; i < difficultyChoicesList.length; i++) {
            JButton button = new JButton();
            int[] bounds = new int[4];
            bounds[0] = 150+108*i+200*i;
            bounds[1] = 70;
            bounds[2] = 108;
            bounds[3] = 50;
            GameFrame.setButtonDefaults(button,difficultyChoicesList[i],GameFrame.CARD_BLUE,bounds);
            button.addActionListener(this);
            button.setName("Difficulty: " + difficultyChoicesList[i]);
            difficultyChoices.add(button);
        }

        Color green = GameFrame.CARD_GREEN;
        teamSizes.get(1).setBackground(green);
        teamsQuantities.get(1).setBackground(green);
        difficultyChoices.get(1).setBackground(green);

        for (int j = 0; j < 3; j++) {
            JLabel reference = new JLabel();
            ImageIcon icon = new ImageIcon(Objects.requireNonNull(getClass().getResource(getImageString(j))));
            reference.setIcon(GameFrame.scaleImage(icon.getImage(),icon.getIconWidth()*1.5,icon.getIconHeight()*1.5));
            reference.setBounds(this.getWidth()/2-(reference.getIcon().getIconWidth()/2),j*125+5,reference.getIcon().getIconWidth(),reference.getIcon().getIconHeight());
            reference.setHorizontalAlignment(JLabel.CENTER);
            reference.setVerticalAlignment(JLabel.CENTER);
            reference.setVisible(true);
            titles.add(reference);
        }

        int[] boundsStart = {this.getWidth()/2-100,450,200,75};
        GameFrame.setButtonDefaults(nextJButton,"Next", GameFrame.CARD_BLUE,boundsStart);
        nextJButton.addActionListener(this);

        ImageIcon blackLine = GameFrame.scaleImage(new ImageIcon(Objects.requireNonNull(getClass().getResource("BlackPixel.png"))).getImage(),2000,5);
        JLabel line1 = new JLabel(blackLine);
        line1.setBounds(0,130,line1.getIcon().getIconWidth(),line1.getIcon().getIconHeight());
        this.add(line1);
        JLabel line2 = new JLabel(blackLine);
        line2.setBounds(0,260,line2.getIcon().getIconWidth(),line2.getIcon().getIconHeight());
        this.add(line2);
        JLabel line3 = new JLabel(blackLine);
        line3.setBounds(0,390,line3.getIcon().getIconWidth(),line3.getIcon().getIconHeight());
        this.add(line3);

    }

    private void addComponentListLabel(ArrayList<JLabel> comp) {
        for (JLabel jLabel : comp) {
            this.add(jLabel);
        }
    }

    private void addComponentListButton(ArrayList<JButton> comp) {
        for (JButton jButton : comp) {
            this.add(jButton);
        }
    }

    private String getImageString(int reference) {
        return switch (reference) {
            case 0 -> "DifficultyLabel.png";
            case 1 -> "NumberOfTeamsLabel.png";
            default -> "PlayersPerTeamLabel.png";
        };
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        JButton button = (JButton) e.getSource();
        String name = button.getName();
        if (name.contains("Back")) {
            frame.changePanel(this, frame.getPanel(GameFrame.MAIN_PANEL));
            return;
        }
        if (name.contains("Difficulty")) {
            difficultyChoiceString = name.split(" ")[1];
            changeButtonsColors(difficultyChoices,button);
            return;
        } else if (name.contains("Quantity")) {
            teamsQuantityInt = StringToInt(name.split(" ")[1]);
            changeButtonsColors(teamsQuantities,button);
            return;
        } else if (name.contains("Size")) {
            teamSizeInt = StringToInt(name.split(" ")[1]);
            changeButtonsColors(teamSizes,button);
            return;
        }
        if (name.contains("Next")) {
            InputPlayerNames panel = (InputPlayerNames) frame.getPanel(GameFrame.INPUT_PLAYER_NAMES);
            panel.changeTeamAndPlayerSize(this.teamsQuantityInt,this.teamSizeInt);
            panel.setDifficulty(difficultyChoiceString);
            frame.changePanel(this, frame.getPanel(GameFrame.INPUT_PLAYER_NAMES));
        }
    }

    private static void changeButtonsColors(ArrayList<JButton> buttons, JButton greenButton) {
        for (JButton button : buttons) {
            button.setBackground(GameFrame.CARD_BLUE);
        }
        greenButton.setBackground(GameFrame.CARD_GREEN);
    }

    private static int StringToInt(String number) {
        return switch (number) {
            case "One" -> 1;
            case "Two" -> 2;
            case "Three" -> 3;
            case "Four" -> 4;
            default -> -1;
        };
    }
}
