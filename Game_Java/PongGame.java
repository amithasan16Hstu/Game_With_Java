import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;

public class PongGame extends JFrame {

    public PongGame() {
        add(new GameBoard());
        setResizable(false);
        pack();

        setTitle("Pong Game");
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    }

    public static void main(String[] args) {
        JFrame frame = new PongGame();
        frame.setVisible(true);
    }
}

class GameBoard extends JPanel implements ActionListener {

    private final int B_WIDTH = 400;
    private final int B_HEIGHT = 300;
    private final int PADDLE_WIDTH = 10;
    private final int PADDLE_HEIGHT = 60;
    private final int BALL_SIZE = 10;
    private final int DELAY = 10;

    private int paddleY = B_HEIGHT / 2 - PADDLE_HEIGHT / 2;
    private int ballX = B_WIDTH / 2 - BALL_SIZE / 2;
    private int ballY = B_HEIGHT / 2 - BALL_SIZE / 2;
    private int ballXDir = 1;
    private int ballYDir = 1;
    private int paddleDir = 0;
    private int score = 0;

    private Timer timer;

    public GameBoard() {
        initBoard();
    }

    private void initBoard() {
        addKeyListener(new TAdapter());
        setBackground(Color.black);
        setFocusable(true);

        setPreferredSize(new Dimension(B_WIDTH, B_HEIGHT));
        timer = new Timer(DELAY, this);
        timer.start();
    }

    @Override
    public void paintComponent(Graphics g) {
        super.paintComponent(g);
        doDrawing(g);
    }

    private void doDrawing(Graphics g) {
        g.setColor(Color.white);
        g.fillRect(0, paddleY, PADDLE_WIDTH, PADDLE_HEIGHT);
        g.fillRect(ballX, ballY, BALL_SIZE, BALL_SIZE);
        drawScore(g);
        Toolkit.getDefaultToolkit().sync();
    }

    private void drawScore(Graphics g) {
        String scoreMsg = "Score: " + score;
        Font small = new Font("Helvetica", Font.BOLD, 14);
        g.setColor(Color.white);
        g.setFont(small);
        g.drawString(scoreMsg, B_WIDTH - 100, 30);
    }

    private void movePaddle() {
        paddleY += paddleDir;
        if (paddleY < 0) {
            paddleY = 0;
        }
        if (paddleY > B_HEIGHT - PADDLE_HEIGHT) {
            paddleY = B_HEIGHT - PADDLE_HEIGHT;
        }
    }

    private void moveBall() {
        ballX += ballXDir;
        ballY += ballYDir;

        if (ballY < 0 || ballY > B_HEIGHT - BALL_SIZE) {
            ballYDir *= -1;
        }
        if (ballX < PADDLE_WIDTH) {
            if (ballY >= paddleY && ballY <= paddleY + PADDLE_HEIGHT) {
                ballXDir *= -1;
                score++;
            } else {
                timer.stop();
            }
        }
        if (ballX > B_WIDTH - BALL_SIZE) {
            ballXDir *= -1;
        }
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        movePaddle();
        moveBall();
        repaint();
    }

    private class TAdapter extends KeyAdapter {
        @Override
        public void keyPressed(KeyEvent e) {
            int key = e.getKeyCode();
            if (key == KeyEvent.VK_UP) {
                paddleDir = -1;
            }
            if (key == KeyEvent.VK_DOWN) {
                paddleDir = 1;
            }
        }

        @Override
        public void keyReleased(KeyEvent e) {
            int key = e.getKeyCode();
            if (key == KeyEvent.VK_UP || key == KeyEvent.VK_DOWN) {
                paddleDir = 0;
            }
        }
    }
}
