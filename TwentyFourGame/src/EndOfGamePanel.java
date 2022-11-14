import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.util.ArrayList;

public class EndOfGamePanel extends DefaultPanel implements ActionListener {

    private JLabel popper1;
    private JLabel popper2;
    private JLabel winningTeam;
    private JLabel winningTeamScore;
    private JLabel player1Winner;
    private JLabel player1Score;
    private JLabel player2Winner;
    private JLabel player2Score;
    private JLabel player3Winner;
    private JLabel player3Score;
    private JButton returnToStart;

    private ArrayList<Player> players;

    private JButton end;

    public EndOfGamePanel(GameFrame frame) {
        super(frame,false);
        buildComponents();
    }

    private void buildComponents() {
        ImageIcon popperIconFull = new ImageIcon(this.getClass().getResource("PartyPopper.png"));
        double widthIcon = popperIconFull.getIconWidth()*0.25;
        double heightIcon = popperIconFull.getIconHeight()*0.25;
        ImageIcon popperIcon = GameFrame.scaleImage(popperIconFull.getImage(), widthIcon, heightIcon);
        ImageIcon popperIconFlippedFull = new ImageIcon(this.getClass().getResource("PartyPopperFlipped.png"));
        ImageIcon popperIconFlipped = GameFrame.scaleImage(popperIconFlippedFull.getImage(),widthIcon,heightIcon);

        popper1 = new JLabel();
        popper1.setBounds(30,50,(int)widthIcon,(int)heightIcon);
        popper1.setIcon(popperIcon);

        popper2 = new JLabel();
        popper2.setBounds(GameFrame.FRAME_WIDTH-31-(int)widthIcon, 50, (int)widthIcon, (int)heightIcon);
        popper2.setIcon(popperIconFlipped);
        
        int widthTeam = 400;
        int heightTeam = 100;
        int widthP = 350;
        int heightP = 75;
        int xBoundTeam = 315;
        int xBoundPlayer = 340;
        int[] yBounds = {25,150,250,350};

        winningTeam = new JLabel();
        int[] winningTeamBounds = {xBoundTeam, yBounds[0], widthTeam, heightTeam};
        GameFrame.setLabelDefaults(winningTeam,"Winning_Team",null,winningTeamBounds);
        winningTeam.setFont(new Font("Calibri",Font.PLAIN,40));
        winningTeam.setBackground(null);

        player1Winner = new JLabel();
        player2Winner = new JLabel();
        player3Winner = new JLabel();

        JLabel[] labels = {player1Winner, player2Winner, player3Winner};

        for (int i = 0; i < 3; i++) {
            int[] bounds = {xBoundPlayer,yBounds[i+1],widthP,heightP};
            GameFrame.setLabelDefaults(labels[i],"Player"+(i+1),null,bounds);
            labels[i].setFont(new Font("Calibri",Font.PLAIN,30));
            labels[i].setBackground(null);
            this.add(labels[i]);
        }

        returnToStart = new JButton();
        int[] returnBounds = {300,450,424,70};
        GameFrame.setButtonDefaults(returnToStart,"Return",GameFrame.CARD_BLUE,returnBounds);
        returnToStart.addActionListener(this);

        winningTeamScore = new JLabel();
        player1Score = new JLabel();
        player2Score = new JLabel();
        player3Score = new JLabel();
        // "00000"
        int scoreXBound = 490;
        int[] scoreYBounds = {yBounds[0]-32,yBounds[1]-32,yBounds[2]-32,yBounds[3]-32};
        JLabel[] scoreLabels = {winningTeamScore,player1Score,player2Score,player3Score};
        for (int i = 0; i < scoreLabels.length; i++) {
            JLabel scoreLabel = scoreLabels[i];
            scoreLabel.setText("00000");
            scoreLabel.setFont(new Font("Calibri",Font.PLAIN,20));
            scoreLabel.setBounds(scoreXBound,scoreYBounds[i],300,50);
            this.add(scoreLabel);
        }

        this.add(returnToStart);
        this.add(popper1);
        this.add(popper2);
        this.add(winningTeam);
    }

    public void addAllPlayers(ArrayList<Player> players) {
        this.players = players;
    }

    public void setup(Team winningTeam, Player player1, Player player2, Player player3, boolean leaderboards, Color[] colors, int[] scores) {
        player3Winner.setVisible(true);
        player3Score.setVisible(true);
        JLabel[] labels = {this.winningTeam,this.player1Winner,this.player2Winner,this.player3Winner};
        JLabel[] labelsScore = {winningTeamScore,player1Score,player2Score,player3Score};
        String[] names = {winningTeam.getName(),player1.getName(),player2.getName(),player3.getName()};
        String[] scoreStrings = {String.valueOf(scores[0]),String.valueOf(scores[1]),String.valueOf(scores[2]),String.valueOf(scores[3])};

        for (int i = 0; i < 4; i++) {
            labels[i].setText(names[i]);
            labels[i].setBackground(colors[i]);
            labelsScore[i].setText(scoreStrings[i]);
            labelsScore[i].setForeground(colors[i]);
        }
        if (leaderboards) {
            leaderboardsWrite();
        }
        this.repaint();
    }

    public void setup(Team winningTeam, Player player1, Player player2, boolean leaderboards, Color[] colors, int[] scores) {
        JLabel[] labels = {this.winningTeam,this.player1Winner,this.player2Winner};
        JLabel[] labelsScore = {winningTeamScore,player1Score,player2Score};
        String[] names = {winningTeam.getName(),player1.getName(),player2.getName()};
        String[] scoreStrings = {String.valueOf(scores[0]),String.valueOf(scores[1]),String.valueOf(scores[2])};

        for (int i = 0; i < 3; i++) {
            labels[i].setText(names[i]);
            labels[i].setBackground(colors[i]);
            labelsScore[i].setText(scoreStrings[i]);
            labelsScore[i].setForeground(colors[i]);
        }
        player3Winner.setVisible(false);
        player3Score.setVisible(false);
        if (leaderboards) {
            leaderboardsWrite();
        }
        this.repaint();
    }

    private void leaderboardsWrite() {
        try (RandomAccessFile raf = new RandomAccessFile("Leaderboards.txt","rws")) {
            ArrayList<String> fileAsString = new ArrayList<>();
            String line = raf.readLine();
            while (line != null) {
                fileAsString.add(line);
                line = raf.readLine();
            }
            raf.seek(0);
            for (Player player : players) {
                boolean contains = false;
                int pos = -1;
                for (int i = 0; i < fileAsString.size(); i++) {
                    if (fileAsString.get(i).contains(player.getName())) {
                        contains = true;
                        pos = i;
                        break;
                    }
                }
                if (contains) {
                    int score = Integer.parseInt(fileAsString.get(pos).split(",")[1]);
                    score = score + player.getScore();
                    String newLine = fileAsString.get(pos).split(",")[0] + "," + String.valueOf(score);
                    fileAsString.set(pos,newLine);
                } else {
                    fileAsString.add(player.getName() + "," + String.valueOf(player.getScore()));
                }
            }
            raf.setLength(0);
            raf.seek(0);
            for (int i = 0; i < fileAsString.size(); i++) {
                raf.write((fileAsString.get(i)+"\n").getBytes());
            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        frame.reset();
        frame.changePanel(this,frame.getPanel(GameFrame.MAIN_PANEL));
    }
}
