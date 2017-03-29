/*
 * Prajakta Surve
 * Period 4
 * March 7, 2015
 * 
 * This lab took me like 4-5 hours, not counting class time. Whenever I open
 * eclipse and first run this program, it takes a minute or so for the JFrame to
 * actually pop up and run. But after that, all the other times I hit run, it
 * starts up much quicker. I'm not sure if it is a problem with my computer or
 * my program. But other than that, I don't think it has any glitches. I had
 * trouble with the recursive code originally. It ended up being a problem with
 * what I was checking in the if statement that prevented the program from
 * actually going inside and running the method. I only had to modify the method
 * a little to get it to work. The timer and the size drop menu and mines
 * counter weren't centering perfectly in their JPanels so I had to add smaller
 * empty JPanels to position them. The JPanel that they are on have Flow Layouts
 * because I know that it will automatically keep things in the center. After a
 * game is lost, I made it so the flagged squares with mines underneath are
 * outlined in black so the user know which squares they got right. I think the
 * main reason this lab took so long was because of the formatting and keeping
 * track of the numerous things that are going on.
 */
import java.awt.Color;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Random;
import java.util.Scanner;

import javax.swing.BoxLayout;
import javax.swing.JEditorPane;
import javax.swing.JOptionPane;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.Timer;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.SwingUtilities;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

import java.awt.event.MouseAdapter;

public class MineSweeperPSurvePeriod4 {
	public static void main(String[] args) {

		MyGUI gui = new MyGUI();
	}

}

class MyGUI extends MouseAdapter implements ActionListener, ChangeListener {

	// Attributes
	int cellNumber = 20;
	MyDrawingPanel drawingPanel;
	JFrame window;
	int[][] content;
	int[][] cover;
	Color color1 = new Color(216, 191, 216);
	Color accent1 = new Color(255, 228, 196);
	Color accent2 = new Color(0, 206, 209);
	Color accent3 = new Color(220, 20, 60);
	Color gray = new Color(240, 240, 240);
	Color flag = new Color(106, 90, 205);
	Color mineColor = new Color(199, 21, 133);
	JComboBox<Integer> sizeMenu;
	JTextField mineInput;
	boolean gameStart = false;
	JFrame mines;
	JLabel time;
	Timer timer;
	int seconds = 0;
	JButton button;
	boolean gameEnded;
	JLabel minesLeft;
	int mineNumber = 10;
	int mineCount = mineNumber;
	int mineActualCount = mineNumber;
	boolean start = true;
	JPanel toolPanel;
	int[][] square = new int[][] { { -1, -1 }, { -1, 0 }, { -1, 1 }, { 0, -1 },
			{ 0, 1 }, { 1, -1 }, { 1, 0 }, { 1, 1 } };

	MyGUI() {

		// Create Java Window
		window = new JFrame("MineSweeper");
		window.setBounds(100, 100, 445, 600);
		window.setResizable(false);
		window.setBackground(Color.white);
		window.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

		// Create GUI elements

		gameEnded = false;

		// JPanel to draw in
		drawingPanel = new MyDrawingPanel();
		drawingPanel.setBounds(20, 45, 400, 400);
		drawingPanel.setBackground(Color.white);
		drawingPanel.addMouseMotionListener(this);
		drawingPanel.addMouseListener(this);
		drawingPanel.setVisible(true);

		cellNumber = 20;
		content = new int[cellNumber][cellNumber];
		cover = new int[cellNumber][cellNumber];
		for (int i = 0; i < content.length; i++) {
			for (int j = 0; j < content[i].length; j++) {
				content[i][j] = 0;
				cover[i][j] = 0;
			}
		}

		addMines(content);

		// JMenu
		JMenuBar bar = new JMenuBar();
		JMenu game = new JMenu("Game");
		JMenu options = new JMenu("Options");
		JMenu help = new JMenu("Help");
		JMenuItem newgame = new JMenuItem("New Game");
		JMenuItem exit = new JMenuItem("Exit");
		JMenuItem total = new JMenuItem("Total Mines");
		JMenuItem how = new JMenuItem("How To Play");
		JMenuItem about = new JMenuItem("About");
		newgame.addActionListener(this);
		exit.addActionListener(this);
		total.addActionListener(this);
		how.addActionListener(this);
		about.addActionListener(this);

		// Choices Panel
		toolPanel = new JPanel();
		toolPanel.setLayout(new BoxLayout(toolPanel, BoxLayout.Y_AXIS));
		toolPanel.setBounds(20, 450, 400, 80);
		toolPanel.setBackground(null);

		// Time Panel
		JPanel timerPanel = new JPanel();
		timerPanel.setBounds(95, 0, 50, 30);
		timerPanel.setBackground(new Color(61, 61, 61));
		time = new JLabel();
		time.setForeground(accent1);
		time.setFont(new Font("Hiragino Kaku Gothic Pro", Font.PLAIN, 35));
		time.setPreferredSize(new Dimension(58, 30));
		time.setText("000");
		timerPanel.add(time);

		// Drop Panel
		int[] description = { 4, 10, 20, 30, 50 };
		// final JTextField t = new JTextField(15);
		sizeMenu = new JComboBox<Integer>();
		sizeMenu.setPreferredSize(new Dimension(40, 16));
		sizeMenu.setFont(new Font("Hiragino Kaku Gothic Pro", Font.PLAIN, 10));
		sizeMenu.setForeground(new Color(181, 38, 38));
		sizeMenu.setBackground(Color.white);
		for (int i = 0; i < 5; i++) {
			sizeMenu.addItem(description[i]);
		}
		sizeMenu.setSelectedIndex(2);
		// t.setEditable(false);
		sizeMenu.addActionListener(this);
		sizeMenu.setActionCommand("Size");

		JLabel size = new JLabel("Size:");
		size.setBounds(140, 530, 70, 30);
		size.setFont(new Font("Courier New", Font.PLAIN, 14));

		minesLeft = new JLabel();
		minesLeft.setText("10 mines left");
		minesLeft.setFont(new Font("Courier New", Font.PLAIN, 14));

		JPanel blank = new JPanel();
		blank.setPreferredSize(new Dimension(30, 20));

		JPanel blank1 = new JPanel();
		blank1.setPreferredSize(new Dimension(15, 20));

		JPanel sizePanel = new JPanel();
		sizePanel.setLayout(new FlowLayout());
		sizePanel.add(blank1);
		sizePanel.add(size);
		sizePanel.add(sizeMenu);
		sizePanel.add(blank);
		sizePanel.add(minesLeft);
		// TIMER
		timer = new Timer(1000, new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				seconds++;
				if (seconds < 10) {
					time.setText("00" + seconds);
				} else if (seconds >= 10 && seconds < 100) {
					time.setText("0" + seconds);
				} else if (seconds >= 100 && seconds < 999) {
					time.setText("" + seconds);
				} else if (seconds == 999) {
					time.setText("" + seconds);
					timer.stop();
					reveal(drawingPanel.getGraphics());
					lose();
				}
				if (mineActualCount == 0) {
					timer.stop();
					win();
				}
			}
		});

		// Add GUI elements to the Java window's ContentPane
		JPanel mainPanel = new JPanel();
		mainPanel.setBackground(new Color(245, 245, 245));
		mainPanel.setLayout(null);

		game.add(newgame);
		game.add(exit);
		options.add(total);
		help.add(how);
		help.add(about);

		bar.add(game);
		bar.add(options);
		bar.add(help);

		toolPanel.add(timerPanel);
		toolPanel.add(sizePanel);

		mainPanel.add(drawingPanel);
		mainPanel.add(toolPanel);

		window.setJMenuBar(bar);
		window.getContentPane().add(mainPanel);

		// Let there be light
		window.setVisible(true);

	}

	public void actionPerformed(ActionEvent e) {

		System.out.println("Action -> " + e.getActionCommand());

		if (e.getActionCommand() != null) {

			if (e.getActionCommand().equals("Size")) {
				JComboBox c = (JComboBox) e.getSource();
				cellNumber = (Integer) c.getSelectedItem();
				newGame();
			} else if (e.getActionCommand().equals("New Game")) {
				newGame();
			} else if (e.getActionCommand().equals("Exit")) {
				System.exit(0);
			} else if (e.getActionCommand().equals("Total Mines")) {
				mines = new JFrame();
				mines.setBounds(200, 200, 210, 150);
				mines.setResizable(false);
				mines.setBackground(Color.white);
				mines.getContentPane()
						.setLayout(
								new BoxLayout(mines.getContentPane(),
										BoxLayout.Y_AXIS));
				mines.setDefaultCloseOperation(mines.DISPOSE_ON_CLOSE);
				JTextArea text = new JTextArea();
				text.setText("The current total number of mines \nis "
						+ mineNumber + ". "
						+ "Type a new value below & \npress 'Okay'.");
				text.setPreferredSize(new Dimension(185, 60));
				text.setBackground(null);
				mineInput = new JTextField();
				button = new JButton("Okay");
				button.setActionCommand("Okay");
				button.addActionListener(this);
				JPanel panel1 = new JPanel();
				panel1.setLayout(new FlowLayout());
				panel1.add(text);
				JPanel buttonPanel = new JPanel();
				buttonPanel.setLayout(new FlowLayout());
				buttonPanel.add(button);
				mines.add(panel1);
				mines.add(mineInput);
				mines.add(buttonPanel);
				// Last thing
				mines.setVisible(true);

			} else if (e.getActionCommand().equals("How To Play")) {
				try {
					JEditorPane helpContent = new JEditorPane(new URL(
							"file:howtoplay.html"));
					helpContent.setEditable(false);
					JScrollPane helpPane = new JScrollPane(helpContent);
					helpPane.setPreferredSize(new Dimension(400, 300));
					JOptionPane.showMessageDialog(null, helpPane,
							"How To Play", JOptionPane.PLAIN_MESSAGE, null);
				} catch (MalformedURLException e1) {
					e1.printStackTrace();
				} catch (IOException e1) {
					e1.printStackTrace();
				}

			} else if (e.getActionCommand().equals("About")) {
				try {
					JEditorPane helpContent = new JEditorPane(new URL(
							"file:copyright.html"));
					helpContent.setEditable(false);
					JScrollPane helpPane = new JScrollPane(helpContent);
					helpPane.setPreferredSize(new Dimension(400, 300));
					JOptionPane.showMessageDialog(null, helpPane,
							"How To Play", JOptionPane.PLAIN_MESSAGE, null);
				} catch (MalformedURLException e1) {
					e1.printStackTrace();
				} catch (IOException e1) {
					e1.printStackTrace();
				}
			} else if (e.getActionCommand().equals("Okay")) {
				if (!mineInput.getText().equals("")
						&& Integer.parseInt(mineInput.getText()) > 0) {
					mineNumber = Integer.parseInt(mineInput.getText());
					mines.dispose();
					newGame();
				}
			}

		}

	}

	public void newGame() {
		gameStart = false;
		gameEnded = false;
		seconds = 0;
		time.setText("000");
		timer.stop();

		if (cellNumber <= 20) {
			content = new int[cellNumber][cellNumber];
			cover = new int[cellNumber][cellNumber];
			for (int i = 0; i < content.length; i++) {
				for (int j = 0; j < content.length; j++) {
					content[i][j] = 0;
					cover[i][j] = 0;
				}
			}
		} else {
			content = new int[cellNumber][20];
			cover = new int[cellNumber][20];
			for (int i = 0; i < 20; i++) {
				for (int j = 0; j < content[1].length; j++) {
					content[i][j] = 0;
					cover[i][j] = 0;
				}
			}
		}
		if (mineNumber > content.length * content[1].length) {
			mineNumber = content.length * content[1].length;
		}
		addMines(content);
		if (cellNumber > 20) {
			window.setBounds(100, 100, 20 * cellNumber + 45, 600);
			drawingPanel.setBounds(20, 45, 20 * cellNumber, 400);
			toolPanel.setBounds(20, 450, 20 * cellNumber, 80);
		} else {
			drawingPanel.setBounds(20 + (200 - 10 * cellNumber),
					45 + (200 - 10 * cellNumber), 20 * cellNumber,
					20 * cellNumber);
			window.setBounds(100, 100, 445, 600);
			toolPanel.setBounds(20, 450, 400, 80);
		}
		mineCount = mineNumber;
		mineActualCount = mineNumber;
		minesLeft.setText(mineNumber + " mines left");
		drawingPanel.paintComponent(drawingPanel.getGraphics());
	}

	public void win() {
		JFrame win = new JFrame();
		win.setBounds(200, 200, 150, 120);
		win.setResizable(false);
		win.getContentPane().setLayout(null);
		win.setBackground(Color.white);
		JLabel youWin = new JLabel("You Win!");
		youWin.setFont(new Font("Arial", Font.PLAIN, 20));
		youWin.setBounds(20, 30, 100, 40);
		win.add(youWin);
		win.setVisible(true);
		gameEnded = true;
	}

	public void lose() {
		JFrame win = new JFrame();
		win.setBounds(200, 200, 150, 120);
		win.setResizable(false);
		win.getContentPane().setLayout(null);
		win.setBackground(Color.white);
		JLabel youWin = new JLabel("You Lose :(");
		youWin.setFont(new Font("Arial", Font.PLAIN, 20));
		youWin.setBounds(20, 30, 120, 40);
		win.add(youWin);
		win.setVisible(true);
		gameEnded = true;
	}

	public void reveal(Graphics g) {
		for (int i = 0; i < content.length; i++) {
			for (int j = 0; j < content[1].length; j++) {
				if (content[i][j] == 9 && cover[i][j] == 0 || cover[i][j] == 3) {
					cover[i][j] = 1;
					drawMine(g, i, j);
				} else if (content[i][j] == 9 && cover[i][j] == 2) {
					cover[i][j] = 4;
					g.setColor(Color.black);
					g.fillRect((int) (i * 20 + 1), (int) (j * 20 + 1),
							(int) (20 - 1), (int) (20 - 1));
					g.setColor(flag);
					g.fillRect((int) (i * 20 + 4), (int) (j * 20 + 4),
							(int) (13), (int) (13));
				}

			}
		}
	}

	public void drawMine(Graphics g, int i, int j) {
		g.setColor(Color.black);
		g.fillRect((int) (i * 20 + 1), (int) (j * 20 + 1), (int) (20 - 1),
				(int) (20 - 1));
		g.setColor(mineColor);
		g.fillRect((int) (i * 20 + 6), (int) (j * 20 + 6), (int) (9), (int) (9));
	}

	private class MyDrawingPanel extends JPanel {

		// Not required, but gets rid of the serialVersionUID warning. Google
		// it, if desired.
		static final long serialVersionUID = 1234567890L;

		MyDrawingPanel() {

		}

		public void paintComponent(Graphics g) {
			super.paintComponent(g);
			g.setColor(Color.white);
			g.fillRect(0, 0, 20 * cellNumber, 20 * cellNumber);

			for (int x = 0; x < 20 * cellNumber; x += 20)
				g.drawLine(x, 0, x, 20 * cellNumber);

			for (int y = 0; y < 20 * cellNumber; y += 20)
				g.drawLine(0, y, 20 * cellNumber, y);

			for (int i = 0; i < content.length; i++) {
				for (int j = 0; j < content[1].length; j++) {
					if (cover[i][j] == 0) {
						g.setColor(color1);
						g.fillRect((int) (i * 20 + 1), (int) (j * 20 + 1),
								(int) (20 - 1), (int) (20 - 1));
					} else if (cover[i][j] == 1) {
						if (content[i][j] == 0) {
							g.setColor(Color.white);
							g.fillRect((int) (i * 20 + 1), (int) (j * 20 + 1),
									(int) (20 - 1), (int) (20 - 1));
						} else if (content[i][j] == 9) {
							drawMine(g, i, j);
						} else {
							paintNeighborNumber(g, i, j);
						}
					} else if (cover[i][j] == 2) {
						g.setColor(flag);
						g.fillRect((int) (i * 20 + 1), (int) (j * 20 + 1),
								(int) (20 - 1), (int) (20 - 1));
					} else if (cover[i][j] == 4) {
						g.setColor(Color.black);
						g.fillRect((int) (i * 20 + 1), (int) (j * 20 + 1),
								(int) (20 - 1), (int) (20 - 1));
						g.setColor(flag);
						g.fillRect((int) (i * 20 + 4), (int) (j * 20 + 4),
								(int) (13), (int) (13));
					} else {
						g.setColor(color1);
						g.fillRect((int) (i * 20 + 1), (int) (j * 20 + 1),
								(int) (20 - 1), (int) (20 - 1));
						g.setColor(Color.black);
						g.drawString("?", i * 20 + 7, j * 20 + 15);
					}

				}

			}
		}

	}

	public void paintNeighborNumber(Graphics g, int x, int y) {
		switch (content[x][y]) {
		case 1:
			g.setColor(gray);
			g.fillRect((int) (x * 20 + 1), (int) (y * 20 + 1), (int) (20 - 1),
					(int) (20 - 1));
			g.setColor(accent2);
			g.drawString("1", x * 20 + 7, y * 20 + 15);
			break;
		case 2:
			g.setColor(gray);
			g.fillRect((int) (x * 20 + 1), (int) (y * 20 + 1), (int) (20 - 1),
					(int) (20 - 1));
			g.setColor(accent3);
			g.drawString("2", x * 20 + 7, y * 20 + 15);
			break;
		case 3:
			g.setColor(gray);
			g.fillRect((int) (x * 20 + 1), (int) (y * 20 + 1), (int) (20 - 1),
					(int) (20 - 1));
			g.setColor(new Color(0, 250, 154));
			g.drawString("3", x * 20 + 7, y * 20 + 15);
			break;
		case 4:
			g.setColor(gray);
			g.fillRect((int) (x * 20 + 1), (int) (y * 20 + 1), (int) (20 - 1),
					(int) (20 - 1));
			g.setColor(new Color(0, 0, 205));
			g.drawString("4", x * 20 + 7, y * 20 + 15);
			break;
		case 5:
			g.setColor(gray);
			g.fillRect((int) (x * 20 + 1), (int) (y * 20 + 1), (int) (20 - 1),
					(int) (20 - 1));
			g.setColor(new Color(210, 105, 30));
			g.drawString("5", x * 20 + 7, y * 20 + 15);
			break;
		case 6:
			g.setColor(gray);
			g.fillRect((int) (x * 20 + 1), (int) (y * 20 + 1), (int) (20 - 1),
					(int) (20 - 1));
			g.setColor(new Color(119, 136, 153));
			g.drawString("6", x * 20 + 7, y * 20 + 15);
			break;
		case 7:
			g.setColor(gray);
			g.fillRect((int) (x * 20 + 1), (int) (y * 20 + 1), (int) (20 - 1),
					(int) (20 - 1));
			g.setColor(new Color(34, 139, 34));
			g.drawString("7", x * 20 + 7, y * 20 + 15);
			break;
		case 8:
			g.setColor(gray);
			g.fillRect((int) (x * 20 + 1), (int) (y * 20 + 1), (int) (20 - 1),
					(int) (20 - 1));
			g.setColor(new Color(220, 20, 60));
			g.drawString("8", x * 20 + 7, y * 20 + 15);
			break;
		default:
			break;
		}
	}

	public void mousePressed(MouseEvent e) {
		Graphics2D g = (Graphics2D) drawingPanel.getGraphics();

		if (!gameEnded) {
			if (!gameStart) {
				timer.start();
				gameStart = false;
			}
			int x = (int) (e.getX() % 20);
			int y = (int) (e.getY() % 20);
			int X = (int) (e.getX() / 20);
			int Y = (int) (e.getY() / 20);
			if (SwingUtilities.isLeftMouseButton(e)) {
				if (cover[X][Y] != 2) {

					if (content[X][Y] == 0) {
						g.setColor(Color.white);
						g.fillRect((int) (X * 20 + 1), (int) (Y * 20 + 1),
								(int) (20 - 1), (int) (20 - 1));
						erase(g, X, Y);
					} else if (content[X][Y] == 9) {
						cover[X][Y] = 1;
						drawMine(g, X, Y);
						timer.stop();
						reveal(g);
						lose();
					} else {
						paintNeighborNumber(g, X, Y);
						cover[X][Y] = 1;
					}
				}

			}
			if (SwingUtilities.isRightMouseButton(e)) {
				if (cover[X][Y] == 0) {
					g.setColor(flag);
					if (content[X][Y] == 9) {
						if (mineCount > 0) {
							mineCount--;
							mineActualCount--;
							minesLeft.setText(mineCount + " mines left");
							g.fillRect((int) (e.getX() - x + 1),
									(int) (e.getY() - y + 1), (int) (20 - 1),
									(int) (20 - 1));
							cover[X][Y] = 2;
						}

					} else {
						if (mineCount > 0) {
							mineCount--;
							minesLeft.setText(mineCount + " mines left");
							g.fillRect((int) (e.getX() - x + 1),
									(int) (e.getY() - y + 1), (int) (20 - 1),
									(int) (20 - 1));
							cover[X][Y] = 2;
						}

					}
				} else if (cover[X][Y] == 2) {
					g.setColor(color1);
					if (content[X][Y] == 9) {
						g.fillRect((int) (e.getX() - x + 1), (int) (e.getY()
								- y + 1), (int) (20 - 1), (int) (20 - 1));
						g.setColor(Color.black);
						g.drawString("?", X * 20 + 7, Y * 20 + 15);
						cover[X][Y] = 3;
						mineCount++;
						mineActualCount++;
						minesLeft.setText(mineCount + " mines left");

					} else {
						g.fillRect((int) (e.getX() - x + 1), (int) (e.getY()
								- y + 1), (int) (20 - 1), (int) (20 - 1));
						g.setColor(Color.black);
						g.drawString("?", X * 20 + 7, Y * 20 + 15);
						cover[X][Y] = 3;
						mineCount++;
						minesLeft.setText(mineCount + " mines left");

					}

				} else if (cover[X][Y] == 3) {
					g.setColor(color1);
					g.fillRect((int) (e.getX() - x + 1),
							(int) (e.getY() - y + 1), (int) (20 - 1),
							(int) (20 - 1));
					cover[X][Y] = 0;

				}
			}
		}

	}

	public void stateChanged(ChangeEvent e) {

	}

	public void printArray(int[][] arr) {
		for (int i = 0; i < arr.length; i++) {
			for (int j = 0; j < arr[1].length; j++) {
				System.out.print(arr[i][j]);
			}
			System.out.println();
		}

	}

	public void addMines(int[][] arr) {
		Random rand = new Random();
		for (int i = 0; i < mineNumber; i++) {
			int x = rand.nextInt(arr.length);
			int y = rand.nextInt(arr[1].length);
			if (content[x][y] != 9) {
				content[x][y] = 9;
				for (int j = 0; j < square.length; j++) {
					int a = x + square[j][0];
					int b = y + square[j][1];
					if (a >= 0 && a < content.length && b >= 0
							&& b < content[1].length) {
						if (arr[a][b] != 9) {
							arr[a][b]++;
						}
					}
				}
			} else {
				i--;
			}
		}

	}

	public void erase(Graphics2D g, int x, int y) {
		if (x >= 0 && x < content.length && y >= 0 && y < content[1].length) {
			if (cover[x][y] == 0) {

				if (content[x][y] == 0) {
					g.setColor(Color.white);
					g.fillRect((int) (x * 20 + 1), (int) (y * 20 + 1),
							(int) (20 - 1), (int) (20 - 1));
					cover[x][y] = 1;
					for (int j = 0; j < square.length; j++) {
						int a = x + square[j][0];
						int b = y + square[j][1];
						erase(g, a, b);
					}
				} else if (content[x][y] > 0) {
					cover[x][y] = 1;
					paintNeighborNumber(g, x, y);
				}

			} else {
				;
			}
		}
	}
}
