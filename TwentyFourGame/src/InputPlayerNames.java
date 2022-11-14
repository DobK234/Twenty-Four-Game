import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Random;

public class InputPlayerNames extends DefaultPanel implements ActionListener {

    private int numberOfTeams;
    private int playersPerTeam;
    private ArrayList<String> playerNamesAndTeam = new ArrayList<>();
    private ArrayList<JLabel> teamBoxes = new ArrayList<>();
    private JButton nextJButton = new JButton();
    private String difficulty = TwentyFourWriter.MEDIUM_DIFF;
    private JLabel errorLabel;

    private ArrayList<JTextField> teamNames = new ArrayList<>();
    private ArrayList<JTextField> players = new ArrayList<>();

    private JLabel playersPerTeamReminderLabel = new JLabel();
    
    private ArrayList<Team> teams = new ArrayList<>();
    

    public InputPlayerNames(GameFrame frame) {
        super(frame,true);
        buildComponents();
        addLabelArray(teamBoxes);
        addTextFieldArray(teamNames);
        addTextFieldArray(players);
    }

    public void changeTeamAndPlayerSize(int numberOfTeams, int playersPerTeam) {
        this.numberOfTeams = numberOfTeams;
        this.playersPerTeam = playersPerTeam;
        updatePanel();
    }

    public void buildErrorLabel(String message) {
        errorLabel = new JLabel(message);
        errorLabel.setForeground(Color.BLACK);
        errorLabel.setBackground(Color.WHITE);
        errorLabel.setBorder(GameFrame.border);
        errorLabel.setBounds(50,200,2000,100);
        this.add(errorLabel);
        this.repaint();
    }

    public void setDifficulty(String difficulty) {
        this.difficulty = difficulty;
    }

    private void updatePanel() {
        playersPerTeamReminderLabel.setText("Players per team: " + playersPerTeam);

        for (int i = 0; i < 4; i++) {
            teamBoxes.get(i).setVisible(true);
            teamNames.get(i).setVisible(true);
            players.get(i).setVisible(true);
        }
        
        for (int i = numberOfTeams; i < 4; i++) {
            teamBoxes.get(i).setVisible(false);
            teamNames.get(i).setVisible(false);
            players.get(i).setVisible(false);
        }
    }

    private void buildComponents() {
        ImageIcon teamBoxIcon = new ImageIcon(this.getClass().getResource("TeamBox.png"));

        int[] xValuesLabels = {60, 520, 60, 520};
        int[] yValuesLabels = {105, 105, 290, 290};

        for (int i = 0; i < 4; i++) {
            JLabel teamLabel = new JLabel();
            teamLabel.setIcon(GameFrame.scaleImage(teamBoxIcon.getImage(),teamBoxIcon.getIconWidth()*1.1,teamBoxIcon.getIconHeight()*1.1));
            teamLabel.setHorizontalAlignment(JLabel.CENTER);
            teamLabel.setVerticalAlignment(JLabel.CENTER);
            teamLabel.setBounds(xValuesLabels[i],yValuesLabels[i],teamLabel.getIcon().getIconWidth(),teamLabel.getIcon().getIconHeight());
            teamBoxes.add(teamLabel);
        }
        int[] reminderBounds = {GameFrame.FRAME_WIDTH/2-200,GameFrame.FRAME_HEIGHT/2-70,400,50};
        GameFrame.setLabelDefaults(playersPerTeamReminderLabel,"Error",GameFrame.CARD_BLUE,reminderBounds);
        playersPerTeamReminderLabel.setBackground(null);
        playersPerTeamReminderLabel.setBorder(null);
        this.add(playersPerTeamReminderLabel);

        int[] boundsStart = {this.getWidth()/2-100,450,200,75};
        GameFrame.setButtonDefaults(nextJButton,"Next", GameFrame.CARD_BLUE,boundsStart);
        nextJButton.addActionListener(this);
        nextJButton.setName("Next");
        this.add(nextJButton);

        for (int i = 0; i < 4; i++) {
            teamNames.add(null);
        }

        for (int i = 0; i < 4; i++) {
            players.add(null);
        }

        int[] xValuesTeamAndPlayerNames = {290, 750, 290, 750};

        int[] yValuesTeamNames = {104, 104, 289, 289};

        for (int i = 0; i < 4; i++) {
            JTextField text = new JTextField();
            text.setBounds(xValuesTeamAndPlayerNames[i],yValuesTeamNames[i],200,44);
            text.setBorder(GameFrame.border);
            text.setFont(GameFrame.font);
            teamNames.set(i,text);
        }

        int[] yValuesPlayerNames = {149,149,334,334};

        for (int i = 0; i < 4; i++) {
            JTextField text = new JTextField();
            text.setBounds(xValuesTeamAndPlayerNames[i],yValuesPlayerNames[i],200,44);
            text.setBorder(GameFrame.border);
            text.setFont(GameFrame.font);
            players.set(i,text);
        }

        JLabel instructions = new JLabel();
        instructions.setFont(GameFrame.font);
        instructions.setText("<html>Please input team and player names. <br>For the players, separate names using \";\".</html>");
        instructions.setBounds(275,-1,1000,100);
        this.add(instructions);
    }

    private void addLabelArray(ArrayList<JLabel> arr) {
        for (JLabel jLabel : arr) {
            this.add(jLabel);
        }
    }

    private void addTextFieldArray(ArrayList<JTextField> arr) {
        for (JTextField jTextField : arr) {
            this.add(jTextField);
        }
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        JButton button = (JButton) e.getSource();
        String name = button.getName();
        if (name.contains("Back")) {
            frame.changePanel(this, frame.getPanel(GameFrame.GAME_MAKER_PANEL));
        } else {
            ArrayList<String> allPlayerNames = new ArrayList<>();
            int avoidDuplicates = 333;
            for (int i = 0; i < numberOfTeams; i++) {
                String teamName = teamNames.get(i).getText().trim();
                Team team;
                if (!teamName.equals("")) {
                    for (Team teamteam : teams) {
                        if (teamName.equals(teamteam.getName())) {
                            teamName = teamName + String.valueOf(avoidDuplicates);
                            avoidDuplicates+=1;
                        }
                    }
                    team = new Team(teamName);
                } else {
                    team = new Team("TeamName" + String.valueOf(i+1));
                }
                ArrayList<Player> playersList = new ArrayList<>();
                String listedPlayers = players.get(i).getText();
                String[] split = listedPlayers.split(";");
                for (int j = 0; j < playersPerTeam; j++) {
                    Player player;
                    if (j < split.length) {
                        String playerName = listedPlayers.split(";")[j].trim();
                        if (!playerName.equals("")) {
                            for (int k = 0; k < allPlayerNames.size(); k++) {
                                if (playerName.equals(allPlayerNames.get(k))) {
                                    playerName = playerName + avoidDuplicates;
                                    avoidDuplicates+=1;
                                }
                            }
                            player = new Player(playerName,team);
                        } else {
                            Random random = new Random();
                            String nameOfPlayer = "DefaultName" + String.valueOf(random.nextInt(101));
                            for (int k = 0; k < allPlayerNames.size(); k++) {
                                if (nameOfPlayer.equals(allPlayerNames.get(k))) {
                                    nameOfPlayer = nameOfPlayer + avoidDuplicates;
                                    avoidDuplicates+=1;
                                }
                            }
                            player = new Player(nameOfPlayer,team);
                        }
                    } else {
                        Random random = new Random();
                        String nameOfPlayer = "DefaultName" + String.valueOf(random.nextInt(101));
                        for (int k = 0; k < allPlayerNames.size(); k++) {
                            if (nameOfPlayer.equals(allPlayerNames.get(k))) {
                                nameOfPlayer = nameOfPlayer + avoidDuplicates;
                                avoidDuplicates+=1;
                            }
                        }
                        player = new Player(nameOfPlayer,team);
                    }
                    playersList.add(player);
                }
                team.addPlayers(playersList);
                teams.add(team);
            }
            TwentyFourGamePanel tfwpanel = (TwentyFourGamePanel) frame.getPanel(GameFrame.TWENTY_FOUR_GAME);
            try {
                tfwpanel.setUpGame(teams,numberOfTeams,playersPerTeam,difficulty);
            } catch (IOException ioException) {
                ioException.printStackTrace();
                this.buildErrorLabel(ioException.getMessage());
                return;
            }
            frame.changePanel(this,tfwpanel);
        }
    }
}
