import javax.sound.sampled.*;
import javax.swing.*;
import javax.swing.border.Border;
import java.awt.*;
import java.io.File;
import java.io.IOException;
import java.net.URISyntaxException;
import java.util.ArrayList;

public class GameFrame extends JFrame {

    public static final int FRAME_WIDTH = 1024;
    public static final int FRAME_HEIGHT = 576;

    private FirstPanel mainPanel;
    private InstructionsPanel instructionsPanel;
    private GameMakerPanel gameMakerPanel;
    private InputPlayerNames playerNamesPanel;
    private TwentyFourGamePanel tfwPanel;
    private EndOfGamePanel endPanel;
    private LeaderboardsPanel leaderboardsPanel;
    private boolean leaderboards = true;
    private JLabel errorLabel;

    public static final int MAIN_PANEL = 0;
    public static final int INSTRUCTIONS_PANEL = 1;
    public static final int GAME_MAKER_PANEL = 2;
    public static final int INPUT_PLAYER_NAMES = 3;
    public static final int TWENTY_FOUR_GAME = 4;
    public static final int END_GAME_PANEL = 5;
    public static final int LEADERBOARDS_PANEL = 6;

    private ArrayList<Team> previousTeams = null;
    private int previousPlayersNum = -1;
    private int previousTeamsNum = -1;

    // All panels are here, with a static final int reference that
    //  I use to toggle visibility only rather than creating new ones each time.
    // (I believe this is better because there aren't that many panels I need to use)

    public static final Color CARD_YELLOW = Color.decode("#fcb827");
    public static final Color CARD_BLUE = Color.decode("#014fa2");
    public static final Color CARD_RED = Color.decode("#ec1161");
    public static final Color CARD_GREEN = Color.decode("#34eb5e");
    public static final Font font = new Font("Calibri", Font.PLAIN, 28);
    public static final Border border = BorderFactory.createLineBorder(Color.black, 2);

    private AudioInputStream ais;
    private Clip clip;

    public GameFrame() {
        //this.buildResources();
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.setVisible(false);
        ImageIcon image = new ImageIcon(getClass().getResource("TwentyFourGameIcon.png"));
        this.setIconImage(image.getImage());
        this.setTitle("Twenty Four Game");
        this.setResizable(false);
        this.setSize(FRAME_WIDTH, FRAME_HEIGHT);
        this.setLocationRelativeTo(null); // Just puts it in the middle of a screen
        this.setResizable(false);
        this.setLayout(null);

        this.buildDefaultPanels();
        //this.testTwentyFourGame();

        this.add(mainPanel);
        this.setVisible(true);
        mainPanel.setVisible(true);

        try {
            File file = new File("Leaderboards.txt");
            if (!file.exists()) {
                file.createNewFile();
            }
        } catch (IOException io) {
            io.printStackTrace();
            leaderboards = false;
            errorLabel = new JLabel();
            errorLabel = new JLabel("<html>Please run as admin,<br>or there will be no <br>leaderboards</html>");
            errorLabel.setFont(new Font("Calibri",Font.PLAIN,40));
            errorLabel.setForeground(Color.RED);
            errorLabel.setBackground(Color.WHITE);
            errorLabel.setBorder(GameFrame.border);
            errorLabel.setBounds(10,20,350,165);
            errorLabel.setVisible(true);
            errorLabel.repaint();
            mainPanel.add(errorLabel);
            mainPanel.repaint();
            this.repaint();
        }

        /*try {
            ais = AudioSystem.getAudioInputStream(new File("a"));
            TODO: Add music
            clip = AudioSystem.getClip();
            clip.loop(Clip.LOOP_CONTINUOUSLY);
            clip.open(ais);
        } catch (UnsupportedAudioFileException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (LineUnavailableException e) {
            e.printStackTrace();
        }*/
    }

    public boolean leaderboards() {
        return leaderboards;
    }

    private void testTwentyFourGame() {
        ArrayList<Team> teams = new ArrayList<>();
        int currPlayer = 1;
        for (int i = 1; i <= 3; i++) {
            Team team = new Team("Team " + (i));
            ArrayList<Player> players = new ArrayList<>();
            for (int j = 1; j <= 3; j++) {
                Player player = new Player("Player " + (currPlayer),team);
                currPlayer++;
                players.add(player);
            }
            team.addPlayers(players);
            teams.add(team);
        }
        try {
            tfwPanel.setUpGame(teams,3,3,TwentyFourWriter.MEDIUM_DIFF);
        } catch (IOException e) {
            e.printStackTrace();
        }
        tfwPanel.setVisible(true);
        this.add(tfwPanel);
        this.setVisible(true);
    }

    public void reset() {
        mainPanel = null;
        instructionsPanel = null;
        gameMakerPanel = null;
        playerNamesPanel = null;
        tfwPanel = null;
        endPanel = null;
        leaderboardsPanel = null;
        buildDefaultPanels();
    }

    private void buildDefaultPanels() {
        mainPanel = new FirstPanel(this);
        instructionsPanel = new InstructionsPanel(this);
        gameMakerPanel = new GameMakerPanel(this);
        playerNamesPanel = new InputPlayerNames(this);
        tfwPanel = new TwentyFourGamePanel(this);
        endPanel = new EndOfGamePanel(this);
        leaderboardsPanel = new LeaderboardsPanel(this);
    }

    public static String getNumbersFileName(String difficulty) {
        return switch (difficulty) {
            case TwentyFourWriter.EASY_DIFF -> "TwentyFourNumbersEasyFixed.txt";
            case TwentyFourWriter.MEDIUM_DIFF -> "TwentyFourNumbersMediumFixed.txt";
            case TwentyFourWriter.HARD_DIFF -> "TwentyFourNumbersHardFixed.txt";
            default -> throw new IllegalArgumentException("Only 'Easy', 'Medium', or 'Hard' allowed");
        };
    }

    public JPanel getPanel(int panelNumber) {
        return switch (panelNumber) {
            case MAIN_PANEL -> mainPanel;
            case INSTRUCTIONS_PANEL -> instructionsPanel;
            case GAME_MAKER_PANEL -> gameMakerPanel;
            case INPUT_PLAYER_NAMES -> playerNamesPanel;
            case TWENTY_FOUR_GAME -> tfwPanel;
            case END_GAME_PANEL -> endPanel;
            case LEADERBOARDS_PANEL -> leaderboardsPanel;
            default -> throw new IllegalStateException("Unexpected value: " + panelNumber);
        };
    }

    public static ImageIcon scaleImage(Image image, double width, double height) {
        return new ImageIcon(image.getScaledInstance((int)width,(int)height,Image.SCALE_SMOOTH));
    }

    public void changePanel(JPanel currPanel, JPanel newPanel) {
        currPanel.setVisible(false);
        newPanel.setVisible(true);
        this.setContentPane(newPanel);
        this.repaint();
    }

    // A lot of my buttons look the same for consistency, these are the general defaults I set
    public static void setButtonDefaults(JButton button, String text, Color bgColor, int[] bounds) {
        button.setVerticalAlignment(JButton.CENTER);
        button.setHorizontalAlignment(JButton.CENTER);
        button.setFont(GameFrame.font);
        button.setText(text);
        button.setName(text);
        button.setForeground(Color.WHITE);
        button.setBackground(bgColor);
        button.setBorder(GameFrame.border);
        button.setFocusable(false);
        button.setVerticalTextPosition(JButton.CENTER);
        button.setHorizontalTextPosition(JButton.CENTER);
        button.setBounds(bounds[0], bounds[1], bounds[2], bounds[3]);
        button.setVisible(true);
    }

    public static void setLabelDefaults(JLabel label, String text, Color bgColor, int[] bounds) {
        label.setVerticalAlignment(JLabel.CENTER);
        label.setHorizontalAlignment(JLabel.CENTER);
        label.setFont(GameFrame.font);
        label.setText(text);
        label.setForeground(Color.WHITE);
        label.setBackground(bgColor);
        label.setOpaque(true);
        label.setBorder(GameFrame.border);
        label.setFocusable(false);
        label.setVerticalTextPosition(JLabel.CENTER);
        label.setHorizontalTextPosition(JLabel.CENTER);
        label.setBounds(bounds[0], bounds[1], bounds[2], bounds[3]);
        label.setVisible(true);
    }

    public void setPreviousTeams(ArrayList<Team> teams, int previousPlayersNum, int previousTeamsNum) {
        previousTeams = teams;
        this.previousPlayersNum = previousPlayersNum;
        this.previousTeamsNum = previousTeamsNum;
    }

    public ArrayList<Team> getPreviousTeams() {
        return previousTeams;
    }

    public int getPreviousPlayersNum() {
        return previousPlayersNum;
    }

    public int getPreviousTeamsNum() {
        return previousTeamsNum;
    }
}
