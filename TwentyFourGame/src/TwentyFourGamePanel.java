import javax.swing.*;
import javax.swing.border.Border;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Random;

public class TwentyFourGamePanel extends DefaultPanel implements ActionListener {

    private ArrayList<Team> teams = new ArrayList<>();
    private ArrayList<JLabel> teamNames = new ArrayList<>();
    private ArrayList<JButton> playersTeam1 = new ArrayList<>();
    private ArrayList<JLabel> playersTeam1Scores = new ArrayList<>();
    private ArrayList<JButton> playersTeam2 = new ArrayList<>();
    private ArrayList<JLabel> playersTeam2Scores = new ArrayList<>();
    private ArrayList<JButton> playersTeam3 = new ArrayList<>();
    private ArrayList<JLabel> playersTeam3Scores = new ArrayList<>();
    private ArrayList<JButton> playersTeam4 = new ArrayList<>();
    private ArrayList<JLabel> playersTeam4Scores = new ArrayList<>();
    private ArrayList<JLabel> teamScores = new ArrayList<>();
    private Card card;
    private JButton endGame = new JButton();
    private JButton nextCard = new JButton();
    private JButton showSolution = new JButton();
    private JButton skipCard = new JButton();
    private JLabel solution;
    private int numberOfTeams;
    private int playersPerTeam;
    private String difficulty;
    private Timer timer;

    public TwentyFourGamePanel(GameFrame frame) {
        super(frame, false);
        buildComponents();
    }

    private void buildComponents() {
        int[] xBounds = {GameFrame.FRAME_WIDTH/10-50, GameFrame.FRAME_WIDTH - (GameFrame.FRAME_WIDTH/10+200) + 50, GameFrame.FRAME_WIDTH/10 - 50, GameFrame.FRAME_WIDTH - (GameFrame.FRAME_WIDTH/10+200) + 50};
        int[] yBounds = {25, 25, 270, 270};
        int width = 200;
        int height = 45;
        Color[] teamColors = {Color.red,Color.decode("#A865C9"),Color.decode("#41873d"),Color.decode("#FF5733")};


        for (int i = 0; i < 4; i++) {
            JLabel teamLabel = new JLabel();
            int[] bounds = {xBounds[i],yBounds[i],width,height};
            GameFrame.setLabelDefaults(teamLabel,"teamName",teamColors[i],bounds);
            teamLabel.setBorder(BorderFactory.createLineBorder(Color.BLACK, 5));
            //GameFrame.setLabelDefaults(teamLabel,"teamName",Color.black,bounds);
            teamNames.add(teamLabel);
            this.add(teamNames.get(i));

            JLabel scoreLabel = new JLabel(numToScoreString(0));
            scoreLabel.setFont(GameFrame.font);
            scoreLabel.setForeground(teamColors[i]);
            if (i == 0 || i == 2) {
                scoreLabel.setBounds(xBounds[i]+width+10,yBounds[i],200,45);
            } else {
                scoreLabel.setBounds(xBounds[i]-80,yBounds[i],200,45);
            }
            scoreLabel.setVerticalTextPosition(JLabel.CENTER);
            scoreLabel.setHorizontalTextPosition(JLabel.CENTER);
            teamScores.add(scoreLabel);
            scoreLabel.setVisible(true);
            scoreLabel.setBackground(Color.RED);
            this.add(scoreLabel);
        }

        for (int i = 0; i < 4; i++) {
            for (int j = 0; j < 4; j++) {
                JButton button = new JButton();
                int[] bounds = {teamNames.get(i).getX(),teamNames.get(i).getY()+(height*(j+1)),width,height};
                GameFrame.setButtonDefaults(button,"player",teamColors[i],bounds);
                button.addActionListener(this);
                getTeam(i).add(button);
                this.add(button);

                JLabel scoreLabel = new JLabel(numToScoreString(0));
                scoreLabel.setFont(new Font("Calibri", Font.PLAIN, 14));
                scoreLabel.setForeground(teamColors[i]);
                if (i == 0 || i == 2) {
                    scoreLabel.setBounds(bounds[0]+width+10,bounds[1],200,45);
                } else {
                    scoreLabel.setBounds(bounds[0]-45,bounds[1],200,45);
                }
                scoreLabel.setVerticalTextPosition(JLabel.CENTER);
                scoreLabel.setHorizontalTextPosition(JLabel.CENTER);
                getTeamScores(i).add(scoreLabel);
                scoreLabel.setVisible(true);
                scoreLabel.setBackground(Color.RED);
                this.add(scoreLabel);
            }
        }

        card = new Card(1,1,1,1);
        card.setBounds((int)(GameFrame.FRAME_WIDTH/2-card.getWidthCard()/2),60);
        this.add(card);

        timer = new Timer(card.getX()+39,15,200,40);
        this.add(timer);

        int[] endGameBounds = {card.getX()-25,470,330,60};
        GameFrame.setButtonDefaults(endGame,"End Game", Color.decode("#8b0000"), endGameBounds);
        endGame.addActionListener(this);
        this.add(endGame);

        GameFrame.setButtonDefaults(nextCard,"Next Card", Color.decode("#4273a1"), endGameBounds);
        nextCard.addActionListener(this);
        nextCard.setVisible(false);
        this.add(nextCard);

        int[] showSolutionBounds = {card.getX(), 350, 280, 50};
        GameFrame.setButtonDefaults(showSolution,"Show solution",Color.MAGENTA,showSolutionBounds);
        showSolution.addActionListener(this);
        this.add(showSolution);

        int[] skipCardBounds = {card.getX(), 410, 280, 50};
        GameFrame.setButtonDefaults(skipCard, "Skip", Color.MAGENTA, skipCardBounds);
        skipCard.addActionListener(this);
        this.add(skipCard);

        solution = new JLabel();
        solution.setVisible(false);
        solution.setBounds(card.getX()-35,25,(int)((card.getWidthCard()+1)+80),40);
        solution.setVerticalTextPosition(JLabel.CENTER);
        solution.setHorizontalTextPosition(JLabel.CENTER);
        solution.setHorizontalAlignment(JLabel.CENTER);
        solution.setVerticalAlignment(JLabel.CENTER);
        solution.setFont(GameFrame.font);
        solution.setForeground(Color.BLACK);
        this.add(solution);
    }

    public void setUpGame(ArrayList<Team> teams, int numberOfTeams, int playersPerTeam, String difficulty) throws IOException {
        this.teams = teams;
        this.numberOfTeams = numberOfTeams;
        this.playersPerTeam = playersPerTeam;
        this.difficulty = difficulty;
        setVisibilitiesAndNames();
        updateCard();
        this.repaint();
    }

    private ArrayList<JButton> getTeam(int team) {
        return switch(team) {
            case 0 -> playersTeam1;
            case 1 -> playersTeam2;
            case 2 -> playersTeam3;
            case 3 -> playersTeam4;
            default -> null;
        };
    }

    private ArrayList<JLabel> getTeamScores(int team) {
        return switch(team) {
            case 0 -> playersTeam1Scores;
            case 1 -> playersTeam2Scores;
            case 2 -> playersTeam3Scores;
            case 3 -> playersTeam4Scores;
            default -> null;
        };
    }

    private void showSolution(boolean show) {
        if (!show) {
            solution.setVisible(false);
        } else {
            ArrayList<Double> currNums = card.getCurrentNums();
            solution.setText(TwentyFourWriter.getSolution(currNums.get(0),currNums.get(1),currNums.get(2),currNums.get(3),difficulty));
            solution.setVisible(true);
        }
    }

    private void setVisibilitiesAndNames() {
        for (int i = 0; i < 4; i++) {
            teamNames.get(i).setVisible(false);
            playersTeam1.get(i).setVisible(false);
            playersTeam2.get(i).setVisible(false);
            playersTeam3.get(i).setVisible(false);
            playersTeam4.get(i).setVisible(false);
            teamScores.get(i).setText("00000");
            teamScores.get(i).setVisible(false);
            for (int j = 0; j < 4; j++) {
                getTeamScores(i).get(j).setVisible(false);
            }
        }
        ArrayList<ArrayList<JLabel>> scores = new ArrayList<>();
        scores.add(playersTeam1Scores);
        scores.add(playersTeam2Scores);
        scores.add(playersTeam3Scores);
        scores.add(playersTeam4Scores);

        for (ArrayList<JLabel> score : scores) {
            for (JLabel jLabel : score) {
                jLabel.setText("00000");
                jLabel.setVisible(false);
            }
        }

        for (int i = 0; i < numberOfTeams; i++) {
            teamNames.get(i).setVisible(true);
            teamNames.get(i).setText(teams.get(i).getName());
            teamNames.get(i).setName(teams.get(i).getName());
            teamScores.get(i).setVisible(true);
            for (int j = 0; j < playersPerTeam; j++) {
                getTeam(i).get(j).setVisible(true);
                getTeamScores(i).get(j).setVisible(true);
                Player player = teams.get(i).getPlayer(j);
                player.setColor(getTeam(i).get(j).getBackground());
                getTeam(i).get(j).setText(player.getName());
                getTeam(i).get(j).setName(player.getName());
                scores.get(i).get(j).setVisible(true);
            }
        }
    }

    private void updateCard() throws IOException {
        showSolution(false);
        Random random = new Random();
        double[] nums = new double[4];
        // Range for random.nextInt(x) is 0 to (x-1)
        String twentyFourEquation = TwentyFourWriter.getLine(random.nextInt(getBound()),GameFrame.getNumbersFileName(difficulty));
        String[] split = twentyFourEquation.split(",");
        for (int i = 0; i < 4; i++) {
            nums[i] = Double.parseDouble(split[i]);
        }
        card.changeNums(nums[0],nums[1],nums[2],nums[3]);

        timer.reset();
        timer.setVisible(true);
    }

    private int getBound() {
        return switch (difficulty) {
            case TwentyFourWriter.EASY_DIFF -> 57;
            case TwentyFourWriter.MEDIUM_DIFF -> 1714;
            case TwentyFourWriter.HARD_DIFF -> 2471;
            default -> -1;
        };
    }

    private void updateScoreTracker(int teamNum, int playerNum) {
        Integer currScore = Integer.parseInt(teamScores.get(teamNum).getText());
        double seconds = timer.getSeconds();
        double one = 1;
        Double multiplier = one - (seconds/240);
        Double scoreToAdd;
        if (multiplier * 10000 > 5000) {
            scoreToAdd = multiplier * 10000;
        } else {
            scoreToAdd = (double) 5000;
        }
        currScore = currScore + scoreToAdd.intValue();

        Player player = teams.get(teamNum).getPlayer(playerNum);

        player.updateScore(player.getScore() + scoreToAdd.intValue());
        teamScores.get(teamNum).setText(numToScoreString(currScore));
        getTeamScores(teamNum).get(playerNum).setText(numToScoreString(player.getScore()));
    }

    private String numToScoreString(int score) {
        String scoreString = String.valueOf(score);
        while (scoreString.length()<5) {
            scoreString = "0" + scoreString;
        }
        return scoreString;
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        JButton button = (JButton)e.getSource();
        String text = button.getText();

        if (text.contains("End Game")) {
            EndOfGamePanel eogp = (EndOfGamePanel) frame.getPanel(GameFrame.END_GAME_PANEL);
            ArrayList<Player> playersArrayList = new ArrayList<>();
            for (int i = 0; i < teams.size(); i++) {
                ArrayList<Player> playersInCurrentTeam = teams.get(i).getAllPlayers();
                for (int j = 0; j < playersInCurrentTeam.size(); j++) {
                    playersArrayList.add(playersInCurrentTeam.get(j));
                }
            }
            eogp.addAllPlayers(playersArrayList);
            int currHighestScoreTeam = 0;
            int currHighestScoreTeamPos = 0;

            ArrayList<Integer> allTeamScores = new ArrayList<>();

            for (int i = 0; i < teamScores.size(); i++) {
                int currScore = Integer.parseInt(teamScores.get(i).getText());
                allTeamScores.add(currScore);
            }

            quickSort(allTeamScores);
            currHighestScoreTeam = allTeamScores.get(allTeamScores.size()-1);
            for (int i = 0; i < teamScores.size(); i++) {
                int currScore = Integer.parseInt(teamScores.get(i).getText());
                if (currScore == currHighestScoreTeam) {
                    currHighestScoreTeamPos = i;
                }
            }

            if (playersArrayList.size()>=3) {

                ArrayList<Integer> allPlayerScores = new ArrayList<>();

                for (int i = 0; i < playersArrayList.size(); i++) {
                    allPlayerScores.add(playersArrayList.get(i).getScore());
                }

                quickSort(allPlayerScores);
                ArrayList<Integer> topThreeScores = new ArrayList<>();
                topThreeScores.add(allPlayerScores.get(allPlayerScores.size()-1));
                topThreeScores.add(allPlayerScores.get(allPlayerScores.size()-2));
                topThreeScores.add(allPlayerScores.get(allPlayerScores.size()-3));

                ArrayList<Integer> topThreePositions = new ArrayList<>();
                topThreePositions.add(-1);
                topThreePositions.add(-1);
                topThreePositions.add(-1);
                for (int i = 0; i < playersArrayList.size(); i++) {
                    int currScore = playersArrayList.get(i).getScore();
                    for (int j = 0; j < topThreeScores.size(); j++) {
                        if (currScore == topThreeScores.get(j)) {
                            if (topThreePositions.get(j) != i) {
                                topThreePositions.set(j,i);
                            }
                        }
                    }
                }
                System.out.println("currHighestScoreTeamPos: " + currHighestScoreTeamPos);
                System.out.println("topThreePos1: " + topThreePositions.get(0));
                System.out.println("topThreePos2: " + topThreePositions.get(1));
                System.out.println("topThreePos3: " + topThreePositions.get(2));

                Color[] colors = {teamNames.get(currHighestScoreTeamPos).getBackground(),playersArrayList.get(topThreePositions.get(0)).getColor(),playersArrayList.get(topThreePositions.get(1)).getColor(),playersArrayList.get(topThreePositions.get(2)).getColor()};
                int[] allScores = {currHighestScoreTeam,topThreeScores.get(0),topThreeScores.get(1),topThreeScores.get(2)};
                eogp.setup(teams.get(currHighestScoreTeamPos),playersArrayList.get(topThreePositions.get(0)),playersArrayList.get(topThreePositions.get(1)),playersArrayList.get(topThreePositions.get(2)),frame.leaderboards(),colors,allScores);

            } else {
                Player p1 = teams.get(0).getPlayer(0);
                Player p2 = teams.get(1).getPlayer(0);
                if (p1.getScore() > p2.getScore()) {
                    Color[] colors = {p1.getColor(),p1.getColor(),p2.getColor()};
                    int[] allScores = {p1.getScore(),p1.getScore(),p2.getScore()};
                    eogp.setup(teams.get(0),p1,p2,frame.leaderboards(),colors,allScores);
                } else {
                    Color[] colors = {p2.getColor(),p2.getColor(),p1.getColor()};
                    int[] allScores = {p2.getScore(),p2.getScore(),p1.getScore()};
                    eogp.setup(teams.get(1),p2,p1,frame.leaderboards(),colors,allScores);
                }
            }

            frame.changePanel(this,frame.getPanel(GameFrame.END_GAME_PANEL));
            return;
        }

        if (text.contains("Skip")) {
            try {
                updateCard();
            } catch (IOException ioException) {
                ioException.printStackTrace();
            }
            return;
        }

        if (text.contains("Show solution")) {
            timer.setVisible(false);
            showSolution(true);
            return;
        }

        if (text.contains("Next Card")) {
            try {
                updateCard();
                nextCard.setVisible(false);
                Thread.sleep(250);
                endGame.setVisible(true);
            } catch (IOException | InterruptedException ioException) {
                ioException.printStackTrace();
            }
            return;
        }

        if (!timer.isPaused()) {
            timer.pause();
        }
        int teamOfPress = -1;
        int playerNumberOfPress = -1;
        for (int i = 0; i < numberOfTeams; i++) {
            if (teams.get(i).getPlayer(text) != null) {
                teamOfPress = i;
                playerNumberOfPress = teams.get(i).getPositionOfPlayer(text);
            }
        }

        updateScoreTracker(teamOfPress,playerNumberOfPress);
        endGame.setVisible(false);
        nextCard.setVisible(true);
        repaint();
    }

    public static void testQuicksort(ArrayList<Integer> arr) {
        quickSort(arr);
        for (int i = 0; i < arr.size(); i++) {
            System.out.println(arr.get(i));
        }
    }

    private static void quickSort(ArrayList<Integer> arr) {
        quickSort(arr,0,arr.size()-1);
    }

    private static void quickSort(ArrayList<Integer> arr, int start, int end) {
        if (start >= end) {
            return;
        }

        int pivot = arr.get(end);
        int leftpointer = start;
        int rightpointer = end;
        while (leftpointer < rightpointer) {
            while (arr.get(leftpointer) <= pivot && leftpointer < rightpointer) {
                leftpointer++;
            }
            while (arr.get(rightpointer) >= pivot && leftpointer < rightpointer) {
                rightpointer--;
            }
            swap(arr,leftpointer,rightpointer);
        }
        swap(arr,leftpointer,end);
        quickSort(arr,start,leftpointer-1);
        quickSort(arr,leftpointer+1,end);
    }

    private static Integer getScore(String line) {
        return Integer.parseInt(line.split(",")[1]);
    }

    private static void swap(ArrayList<Integer> arr, int index1, int index2) {
        Integer temp = arr.get(index1);
        arr.set(index1,arr.get(index2));
        arr.set(index2,temp);
    }

    private static int getPivotPos (int[] arr, int pivot) {
        for (int i = 0; i < arr.length; i++) {
            if (arr[i]==pivot) {
                return i;
            }
        }
        return -1;
    }
}
