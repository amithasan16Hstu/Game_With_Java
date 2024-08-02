import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;

public class BrickBreakerGame extends JFrame {

    public BrickBreakerGame() {
        add(new GameBoard());
        setResizable(false);
        pack();

        setTitle("Brick Breaker Game");
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    }

    public static void main(String[] args) {
        JFrame frame = new BrickBreakerGame();
        frame.setVisible(true);
    }
}

class GameBoard extends JPanel implements ActionListener {

    private final int B_WIDTH = 400;
    private final int B_HEIGHT = 300;
    private final int PADDLE_WIDTH = 60;
    private final int PADDLE_HEIGHT = 10;
    private final int BALL_SIZE = 10;
    private final int BRICK_WIDTH = 40;
    private final int BRICK_HEIGHT = 10;
    private final int ROWS = 5;
    private final int COLS = 10;
    private final int DELAY = 10;

    private int paddleX = B_WIDTH / 2 - PADDLE_WIDTH / 2;
    private int ballX = B_WIDTH / 2 - BALL_SIZE / 2;
    private int ballY = B_HEIGHT / 2 - BALL_SIZE / 2;
    private int ballXDir = 1;
    private int ballYDir = 1;
    private int score = 0;
    private boolean[][] bricks = new boolean[ROWS][COLS];

    private Timer timer;

    public GameBoard() {
        initBoard();
    }

    private void initBoard() {
        addKeyListener(new TAdapter());
        setBackground(Color.black);
        setFocusable(true);

        setPreferredSize(new Dimension(B_WIDTH, B_HEIGHT));

        for (int i = 0; i < ROWS; i++) {
            for (int j = 0; j < COLS; j++) {
                bricks[i][j] = true;
            }
        }

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
        g.fillRect(paddleX, B_HEIGHT - PADDLE_HEIGHT - 30, PADDLE_WIDTH, PADDLE_HEIGHT);
        g.fillRect(ballX, ballY, BALL_SIZE, BALL_SIZE);

        for (int i = 0; i < ROWS; i++) {
            for (int j = 0; j < COLS; j++) {
                if (bricks[i][j]) {
                    g.fillRect(j * BRICK_WIDTH, i * BRICK_HEIGHT, BRICK_WIDTH, BRICK_HEIGHT);
                }
            }
        }

        drawScore(g);

        Toolkit.getDefaultToolkit().sync();
    }

    private void drawScore(Graphics g) {
        String scoreMsg = "Score: " + score;
        Font small = new Font("Helvetica", Font.BOLD, 14);
        g.setColor(Color.white);
        g.setFont(small);
        g.drawString(scoreMsg, 10, B_HEIGHT - 10);
    }

    private void movePaddle(int dir) {
        paddleX += dir;
        if (paddleX < 0) {
            paddleX = 0;
        }
        if (paddleX > B_WIDTH - PADDLE_WIDTH) {
            paddleX = B_WIDTH - PADDLE_WIDTH;
        }
    }

    private void moveBall() {
        ballX += ballXDir;
        ballY += ballYDir;

        if (ballX < 0 || ballX > B_WIDTH - BALL_SIZE) {
            ballXDir *= -1;
        }
        if (ballY < 0) {
            ballYDir *= -1;
        }
        if (ballY > B_HEIGHT) {
            timer.stop();
        }

        if (ballY > B_HEIGHT - PADDLE_HEIGHT - 40 && ballX > paddleX && ballX < paddleX + PADDLE_WIDTH) {
            ballYDir *= -1;
        }

        for (int i = 0; i < ROWS; i++) {
            for (int j = 0; j < COLS; j++) {
                if (bricks[i][j] && ballX > j * BRICK_WIDTH && ballX < j * BRICK_WIDTH + BRICK_WIDTH &&
                    ballY > i * BRICK_HEIGHT && ballY < i * BRICK_HEIGHT + BRICK_HEIGHT) {
                    bricks[i][j] = false;
                    ballYDir *= -1;
                    score++;
                }
            }
        }
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        moveBall();
        repaint();
    }

    private class TAdapter extends KeyAdapter {
        @Override
        public void keyPressed(KeyEvent e) {
            int key = e.getKeyCode();
            if (key == KeyEvent.VK_LEFT) {
                movePaddle(-5);
            }
            if (key == KeyEvent.VK_RIGHT) {
                movePaddle(5);
            }
        }
    }
}
