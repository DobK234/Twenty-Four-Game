import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class Timer extends JButton implements ActionListener {
    
    private TimerThread currThread;
    private boolean paused = false;
    
    public Timer (int x, int y, int w, int h) {
        this.setBounds(x,y,w,h);
        this.setForeground(Color.black);
        this.setBackground(null);
        this.setBorder(null);
        this.setFocusable(false);
        this.addActionListener(this);
        this.setVerticalTextPosition(JLabel.CENTER);
        this.setHorizontalTextPosition(JLabel.CENTER);
        this.setFont(GameFrame.font);
        this.setText("00:00");
    }

    public boolean isPaused() {
        return paused;
    }
    
    public void reset() {
        paused = false;
        if (currThread != null) currThread.setRun(false);
        currThread = null;
        TimerThread timer = new TimerThread(this);
        this.currThread = timer;
        currThread.setRun(true);
    }

    public int getSeconds() {
        return currThread.getSeconds();
    }

    public String getTime() {
        return currThread.getTime();
    }

    private static String secondsToString(int seconds) {
        if (seconds < 60) {
            return ("00:" + numAsString(seconds));
        } else {
            int minutes = seconds/60;
            int newSeconds = seconds-minutes*60;
            return (numAsString(minutes) + ":" + numAsString(newSeconds));
        }
    }

    private static String numAsString(int num) {
        if (num >= 10) {
            return String.valueOf(num);
        } else {
            return ("0" + String.valueOf(num));
        }
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        pause();
    }

    public void pause() {
        if (!paused) {
            paused = true;
            int seconds = currThread.getSeconds();
            if (currThread != null) currThread.setRun(false);
            currThread = null;
            TimerThread timer = new TimerThread(this);
            this.currThread = timer;
            currThread.setSeconds(seconds);
        } else {
            paused = false;
            int seconds = currThread.getSeconds();
            if (currThread != null) currThread.setRun(false);
            currThread = null;
            TimerThread timer = new TimerThread(this);
            this.currThread = timer;
            currThread.setSeconds(seconds);
            currThread.setRun(true);
        }
    }

    private class TimerThread extends Thread {

        private boolean run = false;
        private String time = "00:00";
        private int seconds = 0;
        private JButton button;

        private TimerThread (JButton button) {
            this.button = button;
        }

        @Override
        public void run() {
            while (run) {
                try {
                    Thread.sleep(1000);
                    if (run) {
                        seconds++;
                        changeTime();
                    }
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }

            }
        }

        private void reset() {
            setRun(false);
            seconds = 0;
            changeTime();
        }

        private String getTime() {
            return time;
        }

        private void changeTime() {
            time = secondsToString(seconds);
            button.setText(time);
        }

        private void setRun(boolean run) {
            this.run = run;
            changeTime();
            if (run) {
                start();
            }
        }

        protected int getSeconds() {
            return seconds;
        }

        protected void setSeconds(int seconds) {
            this.seconds = seconds;
            changeTime();
        }
    }
}
