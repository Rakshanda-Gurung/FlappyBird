import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import javax.imageio.ImageIO;

public class FlappyBird extends JFrame implements ActionListener, KeyListener {
    private final int WIDTH = 800;
    private final int HEIGHT = 600;
    private final int PIPE_WIDTH = 50;
    private final int PIPE_HEIGHT = 300;
    private final int PIPE_GAP = 200;
    private final int BIRD_SIZE = 20;
    private final int GRAVITY = 2;
    private final int JUMP_STRENGTH = 30;  // Increased jump strength
    private final int PIPE_MOVEMENT_SPEED = 5;

    private List<Rectangle> pipes;
    private Rectangle bird;
    private Timer timer;
    private boolean gameOver;
    private int score;
    private BufferedImage birdImage;

    public FlappyBird() {
        setTitle("Flappy Bird");
        setSize(WIDTH, HEIGHT);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setResizable(false);

        bird = new Rectangle(100, HEIGHT / 2, BIRD_SIZE, BIRD_SIZE);
        pipes = new ArrayList<>();
        timer = new Timer(20, this);

        try {
            birdImage = ImageIO.read(new File("C:\\Users\\Shivam\\Desktop\\FILES\\bird.jpg")); // Corrected path
        } catch (IOException e) {
            e.printStackTrace();
        }

        addKeyListener(this);
        setFocusable(true);
    }

    public void startGame() {
        timer.start();
        pipes.clear();
        bird.setLocation(100, HEIGHT / 2);
        gameOver = false;
        score = 0;
    }

    @Override
    public void paint(Graphics g) {
        super.paint(g);

        g.setColor(Color.GREEN);
        for (Rectangle pipe : pipes) {
            g.fillRect(pipe.x, pipe.y, PIPE_WIDTH, PIPE_HEIGHT);
            g.fillRect(pipe.x, pipe.y + PIPE_HEIGHT + PIPE_GAP, PIPE_WIDTH, HEIGHT - PIPE_HEIGHT - PIPE_GAP - pipe.y);
        }

        g.setColor(Color.BLACK);
        g.setFont(new Font("Arial", Font.BOLD, 20));
        g.drawString("Score", 20, 20);
        g.drawString("Score: " + score, 20, 50);

        if (birdImage != null) {
            g.drawImage(birdImage, bird.x, bird.y, BIRD_SIZE, BIRD_SIZE, null);
        } else {
            g.setColor(Color.RED);
            g.fillRect(bird.x, bird.y, bird.width, bird.height);
        }

        if (gameOver) {
            g.setColor(Color.BLACK);
            g.setFont(new Font("Arial", Font.BOLD, 36));
            g.drawString("Game Over", WIDTH / 2 - 100, HEIGHT / 2 - 50);
            g.setFont(new Font("Arial", Font.PLAIN, 24));
            g.drawString("Final Score: " + score, WIDTH / 2 - 60, HEIGHT / 2);
            g.drawString("Press Space Bar to Continue", WIDTH / 2 - 120, HEIGHT / 2 + 50);
        }
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if (!gameOver) {
            moveBird();
            movePipes();
            checkCollisions();
            repaint();
        }
    }

    private void moveBird() {
        bird.y += GRAVITY;
    }

    private void movePipes() {
        List<Rectangle> newPipes = new ArrayList<>();

        for (Rectangle pipe : pipes) {
            pipe.x -= PIPE_MOVEMENT_SPEED;

            // Check if the bird has passed the pipe and increase the score
            if (pipe.x + PIPE_WIDTH == bird.x) {
                score++;
            }

            if (pipe.x + PIPE_WIDTH > 0) {
                newPipes.add(pipe);
            }
        }

        pipes = newPipes;

        if (pipes.isEmpty() || pipes.get(pipes.size() - 2).x < WIDTH - WIDTH / 3) {
            int pipeY = (int) (Math.random() * (HEIGHT - PIPE_HEIGHT - PIPE_GAP));
            pipes.add(new Rectangle(WIDTH, pipeY, PIPE_WIDTH, PIPE_HEIGHT));
            pipes.add(new Rectangle(WIDTH, pipeY + PIPE_HEIGHT + PIPE_GAP, PIPE_WIDTH, HEIGHT - pipeY - PIPE_HEIGHT - PIPE_GAP));
        }
    }

    private void checkCollisions() {
        for (Rectangle pipe : pipes) {
            if (pipe.intersects(bird)) {
                gameOver = true;
                timer.stop();
            }
        }
        if (bird.y <= 0 || bird.y >= HEIGHT - bird.height) {
            gameOver = true;
            timer.stop();
        }
    }

    @Override
    public void keyTyped(KeyEvent e) {
    }

    @Override
    public void keyPressed(KeyEvent e) {
        if (e.getKeyCode() == KeyEvent.VK_SPACE) {
            if (!gameOver) {
                bird.y -= JUMP_STRENGTH;
            } else {
                startGame();
            }
        }
        if (e.getKeyCode() == KeyEvent.VK_ENTER && gameOver) {
            startGame();
        }
    }

    @Override
    public void keyReleased(KeyEvent e) {
    }

    public static void main(String[] args) {
        FlappyBird game = new FlappyBird();
        game.setVisible(true);
        game.startGame();
    }
}

