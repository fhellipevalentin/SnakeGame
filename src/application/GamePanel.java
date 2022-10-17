package application;

import java.awt.*;
import java.awt.event.*;
import java.util.Random;
import javax.swing.*;

public class GamePanel extends JPanel implements ActionListener {
	private static final long serialVersionUID = 1L;
	
	static final int SCREEN_WIDTH = 600;
	static final int SCREEN_HEIGHT = 600;
	static final int UNIT_SIZE = 25;
	static final int GAME_UNITS = (SCREEN_WIDTH * SCREEN_HEIGHT) / UNIT_SIZE;
	static final int DELAY = 90;
	final int x[] = new int[GAME_UNITS];
	final int y[] = new int[GAME_UNITS];
	int bodyParts = 6;
	int fruitsEaten;
	int appleX;
	int appleY;
	char direction = 'R';
	boolean running = false;
	Timer timer;
	Random random;
	String status;
	
	public void hack() {
		bodyParts = 50;
		fruitsEaten = 56;
	}
	
	public void resetPoints() {
		bodyParts = 6;
		fruitsEaten = 0;
	}
	
	public void increaseDifficult() {
		timer = new Timer(DELAY, this);
		timer.start();
	}
	
	public GamePanel() {
		random = new Random();
		this.setPreferredSize(new Dimension(SCREEN_WIDTH, SCREEN_HEIGHT));
		this.setBackground(new Color(153, 76, 0));
		this.setFocusable(true);
		this.addKeyListener(new MyKeyAdapter());
		startGame();
	}

	public void startGame() {
		newFruit();
		running = true;
		timer = new Timer(DELAY, this);
		timer.start();
	}

	public void paintComponent(Graphics g) {
		super.paintComponent(g);
		draw(g);
	}

	public void draw(Graphics g) {
		if (running) {
			for (int i = 0; i < SCREEN_HEIGHT / UNIT_SIZE; i++) {
				g.drawLine(i * UNIT_SIZE, 0, i * UNIT_SIZE, SCREEN_HEIGHT);
				g.drawLine(0, i * UNIT_SIZE, SCREEN_WIDTH, i * UNIT_SIZE);
			}
			// formato da fruta
			g.setColor(Color.orange);
			g.fillRect(appleX, appleY, UNIT_SIZE, UNIT_SIZE);
			
			//corpo da cobra
			for (int i = 0; i < bodyParts; i++) {
				if (i == 0) {
					g.setColor(Color.green);
					g.fillRect(x[i], y[i], UNIT_SIZE, UNIT_SIZE);
				} else {
					g.setColor(new Color(45, 180, 0));
					g.fillRect(x[i], y[i], UNIT_SIZE, UNIT_SIZE);
				}
			}
		} else {
			gameOver(g);
		}
		if (fruitsEaten <= 20) {
			status = "Gafanhoto";
		} else if (fruitsEaten <= 40) {
			status = "Recruta";
		} else if (fruitsEaten <= 80) {
			status = "Comandante";
		} else if (fruitsEaten <= 160) {
			status = "Jedi";
		} else if (fruitsEaten > 320) {
			status = "Darth Vader";
		}
		// Texto do Score
		g.setColor(Color.white);
		g.setFont(new Font("Ink Free", Font.BOLD, 40));
		FontMetrics metrics = getFontMetrics(g.getFont());
		g.drawString("Score: " + fruitsEaten, (SCREEN_WIDTH - metrics.stringWidth("Score: " + fruitsEaten)) / 2,
				g.getFont().getSize());
		// Texto das instruções
		g.setColor(Color.white);
		g.setFont(new Font("Ink Free", Font.BOLD, 15));
	    FontMetrics metrics2 = getFontMetrics(g.getFont());
		g.drawString("W: Increase difficult", (580 - metrics2.stringWidth("W:increase difficult")),20);
		
		g.setColor(Color.white);
		g.setFont(new Font("Ink Free", Font.BOLD, 15));
	    FontMetrics metrics3 = getFontMetrics(g.getFont());
		g.drawString("C: Restart", (590 - metrics3.stringWidth("C: Restart")),40);
		// Texto dos status
		g.setColor(Color.white);
		g.setFont(new Font("Ink Free", Font.BOLD, 20));
		FontMetrics metrics4 = getFontMetrics(g.getFont());
		g.drawString(status, (SCREEN_WIDTH - metrics4.stringWidth(status)) / 2,
				60);
	}			

	public void newFruit() {
		appleX = random.nextInt((int) (SCREEN_WIDTH / UNIT_SIZE)) * UNIT_SIZE;
		appleY = random.nextInt((int) (SCREEN_HEIGHT / UNIT_SIZE)) * UNIT_SIZE;
	}

	public void move() {
		for (int i = bodyParts; i > 0; i--) {
			x[i] = x[i - 1];
			y[i] = y[i - 1];
		}
		switch (direction) {
		case 'U':
			y[0] = y[0] - UNIT_SIZE;
			break;
		case 'D':
			y[0] = y[0] + UNIT_SIZE;
			break;
		case 'L':
			x[0] = x[0] - UNIT_SIZE;
			break;
		case 'R':
			x[0] = x[0] + UNIT_SIZE;
			break;
		}
	}
	
	public void checkFruit() {
		if ((x[0] == appleX) && (y[0] == appleY)) {
			bodyParts++;
			fruitsEaten++;
			newFruit();
		}
	}

	public void checkCollisions() {
		// checando se a cabeça colide com o corpo
		for (int i = bodyParts; i > 0; i--) {
			if ((x[0] == x[i]) && (y[0] == y[i])) {
				running = false;
			}
		}
		// verifica se a cabeça toca a borda da esquerda
		if (x[0] < 0) {
			running = false;
		}
		// verifica se a cabeça toca a borda da direita
		if (x[0] > SCREEN_WIDTH) {
			running = false;
		}
		// verifica se a cabeça toca a borda de cima
		if (y[0] < 0) {
			running = false;
		}
		// verifica se a cabeça toda a borda de baixo
		if (y[0] > SCREEN_HEIGHT) {
			running = false;
		}
		if (!running) {
			timer.stop();
		}
	}

	public void gameOver(Graphics g) {
		// Texto do Score
		g.setColor(Color.white);
		g.setFont(new Font("Ink Free", Font.BOLD, 40));
		FontMetrics metrics1 = getFontMetrics(g.getFont());
		g.drawString("Score: " + fruitsEaten, (SCREEN_WIDTH - metrics1.stringWidth("Score: " + fruitsEaten)) / 2,
				g.getFont().getSize());
		// Texto do Game Over
		g.setColor(new Color(255,51,51));
		g.setFont(new Font("Ink Free", Font.BOLD, 75));
		FontMetrics metrics2 = getFontMetrics(g.getFont());
		g.drawString("Game Over", (SCREEN_WIDTH - metrics2.stringWidth("Game Over")) / 2, SCREEN_HEIGHT / 2);
		// Texto Reset Game
		g.setColor(Color.WHITE);
		g.setFont(new Font("Ink Free", Font.BOLD, 30));
		FontMetrics metrics3 = getFontMetrics(g.getFont());
		g.drawString("Reset Game = Press SPACE!", (490 - metrics3.stringWidth("Reset Game = Press SPACE!")), 580);
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		if (running) {
			move();
			checkFruit();
			checkCollisions();
		}
		repaint();

	}

	public class MyKeyAdapter extends KeyAdapter {
		@Override
		public void keyPressed(KeyEvent e) {

			switch (e.getKeyCode()) {
			case KeyEvent.VK_LEFT:
				if (direction != 'R') {
					direction = 'L';
				}
				break;
			case KeyEvent.VK_RIGHT:
				if (direction != 'L') {
					direction = 'R';
				}
				break;
			case KeyEvent.VK_UP:
				if (direction != 'D') {
					direction = 'U';
				}
				break;
			case KeyEvent.VK_DOWN:
				if (direction != 'U') {
					direction = 'D';
				}
				break;
			case KeyEvent.VK_SPACE:
				new GameFrame();
				break;
				
			case KeyEvent.VK_W:
				increaseDifficult();
				break;
				
			case KeyEvent.VK_X:
				hack();
				break;
			case KeyEvent.VK_C:
				resetPoints();
				break;
			case KeyEvent.VK_ESCAPE:
				System.exit(ABORT);
			}
		}
	}
}
