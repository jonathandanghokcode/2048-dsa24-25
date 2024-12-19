package game;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.util.List;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.border.Border;
import javax.swing.border.CompoundBorder;
import javax.swing.border.EmptyBorder;
import javax.swing.border.LineBorder;

public class Window extends JFrame {
	private static final long serialVersionUID = -8804446439773037674L;
	private int width = 375;
	private int height = 450;

	public Window(String title) {
		super(title); // Window title

		this.setLayout(new BorderLayout());
		this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE); // close window to exit
		this.setSize(this.width, this.height);
		this.setLocationRelativeTo(null); // centering

		this.setResizable(true); // Cho phép thay đổi kích thước cửa sổ
		this.setFocusable(true); // set focus on window so KeyListener works

		// Main panel (JPanel) with dynamic size
		JPanel mainPanel = new JPanel();
		mainPanel.setLayout(new BorderLayout());
		mainPanel.setBackground(Color.LIGHT_GRAY);

		// Add Grid or other components
		mainPanel.add(new Grid(), BorderLayout.CENTER);

		// Bottom panel for buttons
		JPanel buttonPanel = new JPanel();
		buttonPanel.add(createSimpleButton("NEW GAME"));
//		buttonPanel.add(createSimpleButton("UNDO")); // Thêm nút UNDO
		mainPanel.add(buttonPanel, BorderLayout.SOUTH);

		// Add main panel to JFrame
		this.add(mainPanel, BorderLayout.CENTER);

		this.setVisible(true); // show window
	}

	private JButton createSimpleButton(String text) {
		JButton button = new JButton(text);
		button.setForeground(Color.BLACK);
		button.setBackground(Color.WHITE);
		Border line = new LineBorder(Color.BLACK);
		Border margin = new EmptyBorder(5, 15, 5, 15);
		Border compound = new CompoundBorder(line, margin);
		button.setBorder(compound);

		// Thêm sự kiện khi nhấn nút
		if (text.equalsIgnoreCase("NEW GAME")) {
			button.addActionListener(e -> {
				System.out.println("New Game button pressed"); // Debug log
				restartGame();
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
