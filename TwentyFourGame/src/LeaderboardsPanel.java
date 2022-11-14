import javax.swing.*;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.util.ArrayList;
import java.util.Random;

public class LeaderboardsPanel extends DefaultPanel {

    private ArrayList<JLabel> topScores = new ArrayList<>();
    private JLabel title;

    public LeaderboardsPanel(GameFrame frame) {
        super(frame, true);
        //testQuickSort();
        buildLabels();
    }

    private void testQuickSort() {
        ArrayList<String> words = new ArrayList<>();
        Random random = new Random();
        for (int i = 0; i < 10; i++) {
            String line = "a,";
            line = line + String.valueOf((random.nextInt(1000)+1));
            words.add(line);
        }
        quickSort(words);
        for (int i = 0; i < words.size(); i++) {
            System.out.println(words.get(i));
        }
    }

    private void buildLabels() {
        ImageIcon leaderboardsTitleFull = new ImageIcon(this.getClass().getResource("LeaderboardsTitle.png"));
        double widthIcon = leaderboardsTitleFull.getIconWidth()*0.7;
        double heightIcon = leaderboardsTitleFull.getIconHeight()*0.7;
        ImageIcon img = GameFrame.scaleImage(leaderboardsTitleFull.getImage(),widthIcon,heightIcon);

        title = new JLabel();
        title.setIcon(img);
        title.setBounds(285,-150,(int)(widthIcon+1),(int)(heightIcon+1));
        this.add(title);

        int width = 450;
        int height = 60;
        int xBound = 305;
        int currY = 80;
        int yDiff = height + 20;

        for (int i = 0; i < 5; i++) {
            JLabel label = new JLabel();
            int[] bounds = {xBound,currY,width,height};
            GameFrame.setLabelDefaults(label,"No scores currently saved",GameFrame.CARD_BLUE,bounds);
            currY = currY + yDiff;
            topScores.add(label);
            this.add(label);
        }
    }

    public void updateLeaderboards() {
        try (RandomAccessFile raf = new RandomAccessFile("Leaderboards.txt","rws")) {
            raf.seek(0);
            ArrayList<String> file = new ArrayList<>();
            String line = raf.readLine();
            while (line != null) {
                file.add(line);
                line = raf.readLine();
            }
            quickSort(file);
            int currTopScore = 0;
            for (int i = file.size()-1; i > file.size()-6 && i >= 0; i--) {
                String playerLine = file.get(i).split(",")[0] + " - " + file.get(i).split(",")[1];
                topScores.get(currTopScore).setText(playerLine);
                currTopScore+=1;
            }
            repaint();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private static void quickSort(ArrayList<String> arr) {
        quickSort(arr,0,arr.size()-1);
    }

    private static void quickSort(ArrayList<String> arr, int start, int end) {
        if (start >= end) {
            return;
        }

        int pivot = getScore(arr.get(end));
        int leftpointer = start;
        int rightpointer = end;
        while (leftpointer < rightpointer) {
            while (getScore(arr.get(leftpointer)) <= pivot && leftpointer < rightpointer) {
                leftpointer++;
            }
            while (getScore(arr.get(rightpointer)) >= pivot && leftpointer < rightpointer) {
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

    private static void swap(ArrayList<String> arr, int index1, int index2) {
        String temp = arr.get(index1);
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
