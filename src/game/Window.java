package game;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.util.List;

import javax.swing.*;
import javax.swing.border.Border;
import javax.swing.border.CompoundBorder;
import javax.swing.border.EmptyBorder;
import javax.swing.border.LineBorder;

public class Window extends JFrame {
	private static final long serialVersionUID = -8804446439773037674L;
	private int width =555;
	private int height = 420;
	private static LeaderBoard leaderboard;
	private static Window instance;
	private static final String FONT = "Tahoma";
	public static void loadLeaderBoard() {
		leaderboard.load();
	}

	public Window(String title) {

		super(title); // Window title
		instance = this;

		this.setLayout(new BorderLayout());
		this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE); // close window to exit
		this.setSize(this.width, this.height);
		this.setLocationRelativeTo(null); // centering

		this.setResizable(true); // Cho phép thay đổi kích thước cửa sổ
		this.setFocusable(true); // set focus on window so KeyListener works

		// Main panel (JPanel) with dynamic size
		JPanel mainPanel = new JPanel();
		mainPanel.setLayout(new BorderLayout());
		// change another color
		mainPanel.setBackground(ColorScheme.BRIGHT);

		// Add Grid or other components
		mainPanel.add(new Grid(), BorderLayout.CENTER);

		// Bottom panel for buttons
		JPanel buttonPanel = new JPanel();
		buttonPanel.setBackground(ColorScheme.BRIGHT);
		buttonPanel.add(this.createSimpleButton("TUTORIAL"));
		buttonPanel.add(createSimpleButton("NEW GAME"));
		buttonPanel.add(this.createSimpleButton("LEADERBOARD"));

//		buttonPanel.add(createSimpleButton("UNDO")); // Thêm nút UNDO
		mainPanel.add(buttonPanel, BorderLayout.SOUTH);

		// Add main panel to JFrame
		this.add(mainPanel, BorderLayout.CENTER);

		this.setVisible(true); // show window
		leaderboard = new LeaderBoard();
	}
	public static Window getInstance() {
		return instance;
	}

	private JButton createSimpleButton(String text) {
		JButton button = new RoundedButton(text);
		button.setForeground(Color.BLACK);
		button.setBackground(ColorScheme.WINBG);
		Border line = new LineBorder(Color.WHITE);
		Border margin = new EmptyBorder(5, 15, 5, 15);
		Border compound = new CompoundBorder(line, margin);
		button.setBorder(compound);
		button.setFont(new java.awt.Font(FONT, java.awt.Font.BOLD, 12));

		// Thêm sự kiện khi nhấn nút
		if (text.equalsIgnoreCase("NEW GAME")) {
			button.addActionListener(e -> {
				System.out.println("New Game button pressed"); // Debug log
				restartGame();
			});
		}else if (text.equalsIgnoreCase("LEADERBOARD")) {
			button.addActionListener((e) -> {
				System.out.println("Leaderboard button pressed");
				displayLeaderBoard();

				// Add leaderboard functionality here
			});
		}
		else if (text.equalsIgnoreCase("TUTORIAL")) {
			button.addActionListener(e -> {
				System.out.println("Tutorial button pressed");
				displayTutorial();

				// Add tutorial functionality here
			});
		}

//		else if (text.equalsIgnoreCase("UNDO")) {
//			button.addActionListener(e -> {
//				System.out.println("Undo button pressed");
//				Game.BOARD.undo(); // Gọi phương thức undo
//				Game.BOARD.isGameOver(); // Kiểm tra trạng thái game sau khi undo
//				Game.WINDOW.repaint(); // Cập nhật giao diện
//			});
//		}
		return button;
	}

	public static void displayLeaderBoard() {
		// Create a new JFrame to display the leaderboard
		JFrame leaderboardFrame = new JFrame("Leaderboard");
		leaderboardFrame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		leaderboardFrame.setSize(300, 200);
		leaderboardFrame.setLocationRelativeTo(null);
		leaderboardFrame.setBackground(ColorScheme.BRIGHT);

		// Get the top 10 players
		List<Player> topPlayers = leaderboard.getTopPlayers(10);

		// Create a 2D array to hold the player data
		String[][] playerData = new String[topPlayers.size()][2];
		for (int i = 0; i < topPlayers.size(); i++) {
			playerData[i][0] = topPlayers.get(i).getName();
			playerData[i][1] = String.valueOf(topPlayers.get(i).getScore());
		}

		// Create a table to display the player data
		String[] columnNames = {"Player Name", "Score"};
		JTable table = new JTable(playerData, columnNames);
		JScrollPane scrollPane = new JScrollPane(table);
		leaderboardFrame.add(scrollPane, BorderLayout.CENTER);
		leaderboardFrame.addWindowListener(new java.awt.event.WindowAdapter() {
			@Override
			public void windowClosed(java.awt.event.WindowEvent windowEvent) {
				Window.getInstance().requestFocus();
			}
		});

		// Display the leaderboard
		leaderboardFrame.setVisible(true);
	}
	public static void addPlayerToLeaderboard(Player player) {
		leaderboard.addPlayer(player);
		displayLeaderBoard();
	}
	public static void displayTutorial() {
		// Create a new JFrame to display the tutorial
		JFrame tutorialFrame = new JFrame("Tutorial");
		tutorialFrame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		tutorialFrame.setSize(580, 220);
		tutorialFrame.setBackground(ColorScheme.BRIGHT);
		tutorialFrame.setLocationRelativeTo(null);

		// Create a text area to display the tutorial
		JTextArea textArea = new JTextArea("Welcome to the 2048 game!\n\n" +
				"Use the arrow keys to move the tiles in the desired direction.\n" +
				"Combine tiles with the same number to create a new tile with double the value.\n" +
				"Try to reach the 2048 tile to win the game!\n\n" +
				"YOU CAN UNDO YOUR MOVE BY PRESS Z BUTTON BUT YOU NEED COMPLETE 20 MOVES.\n\n" +
				"YOU CAN SWAP THE TILES BY PRESS S BUTTON BUT YOU NEED COMPLETE 30 MOVES \n\n" +
				"Good luck and have fun!");
		textArea.setEditable(false);
		tutorialFrame.add(textArea, BorderLayout.CENTER);
		// Add a WindowListener to request focus back to the main window when closed
		tutorialFrame.addWindowListener(new java.awt.event.WindowAdapter() {
			@Override
			public void windowClosed(java.awt.event.WindowEvent windowEvent) {
				Window.getInstance().requestFocus();
			}
		});

		// Display the tutorial
		tutorialFrame.setVisible(true);
	}




	//	private JPanel createGamePanel() {
//		JPanel gamePanel = new JPanel();
//		gamePanel.setLayout(new BorderLayout());
//
//		// Thêm bảng trò chơi
//		gamePanel.add(new Grid(), BorderLayout.CENTER);
//
//		// Nút Undo
//		JButton undoButton = new JButton("Undo");
//		undoButton.addActionListener(e -> Game.BOARD.undo()); // Gọi phương thức undo từ BOARD
//		gamePanel.add(undoButton, BorderLayout.SOUTH); // Thêm nút vào phía dưới
//
//		return gamePanel;
//	}




	private void restartGame() {
		Game.BOARD.reset();// Reset trạng thái bảng trò chơi
		Game.CONTROLS.unbind(); // Hủy bỏ các listener trước đó
		Game.CONTROLS.bind(); // Gắn lại listener
		this.repaint(); // Vẽ lại giao diện
		this.requestFocus(); // Đảm bảo JFrame lấy focus
	}
	public static void main(String[] args) {
		new Window("Resizable Game Window");

	}
}
