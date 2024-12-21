package game;

import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import javax.swing.*;

public class Grid extends JPanel {

	private static final long serialVersionUID = 1L;

	private static final int TILE_RADIUS = 15;
	private static final int WIN_MARGIN = 20;
	private static final int TILE_MARGIN = 15;
	private static final String FONT = "Tahoma";

	public Grid() {
		super(true); // turn on double buffering
		addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				handleMouseClick(e);
			}
		});
	}

	@Override
	public void paintComponent(Graphics g2) {
		super.paintComponent(g2);

		Graphics2D g = (Graphics2D) g2; // cast to get context for drawing



		// Enable antialiasing for smooth edges
		g.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
		g.setRenderingHint(RenderingHints.KEY_RENDERING, RenderingHints.VALUE_RENDER_QUALITY);

		drawBackground(g);
		drawTitle(g);
		drawScoreBoard(g);
		drawBoard(g);
		drawCountdown(g);
		drawCountdown_swap(g);

		g.dispose(); // release memory
	}

	private static void drawTitle(Graphics g) {
		g.setFont(new Font(FONT, Font.BOLD, 38));
		g.setColor(ColorScheme.BRIGHT);
		g.drawString("2048", WIN_MARGIN, 50);
	}

	private void drawScoreBoard(Graphics2D g) {
		int width = 80;
		int height = 40;
		int xOffset = getWidth() - WIN_MARGIN - width;
		int yOffset = 20;
		g.fillRoundRect(xOffset, yOffset, width, height, TILE_RADIUS, TILE_RADIUS);
		g.setFont(new Font(FONT, Font.BOLD, 10));
		g.setColor(new Color(0XFFFFFF));
		g.drawString("SCORE", xOffset + 22, yOffset + 15);
		g.setFont(new Font(FONT, Font.BOLD, 12));
		g.drawString(String.valueOf(Game.BOARD.getScore()), xOffset + 35, yOffset + 30);
	}

	private static void drawBackground(Graphics g) {
		g.setColor(ColorScheme.WINBG);
		g.fillRect(0, 0, Game.WINDOW.getWidth(), Game.WINDOW.getHeight());
	}

	private void drawBoard(Graphics g) {
		int boardWidth = Math.min(getWidth() - 2 * WIN_MARGIN, getHeight() - 100);
		int tileSize = (boardWidth - (TILE_MARGIN * 5)) / 4; // Calculate tile size based on the board width

		g.translate(WIN_MARGIN, 80);
		g.setColor(ColorScheme.GRIDBG);
		g.fillRoundRect(0, 0, boardWidth, boardWidth, TILE_RADIUS, TILE_RADIUS);

		for (int row = 0; row < 4; row++) {
			for (int col = 0; col < 4; col++) {
				drawTile(g, Game.BOARD.getTileAt(row, col), col, row, tileSize);
			}
		}
	}
	private void drawCountdown(Graphics2D g) {
		int width = 120;
		int height = 40;
		int smallWidth = 60; // Chiều rộng mới cho nền
		int smallHeight = 40; // Chiều cao không thay đổi
		int xOffset = Game.WINDOW.getWidth() - WIN_MARGIN -  (2*width) - TILE_MARGIN +50;
		int yOffset = TILE_MARGIN-74;
		// Kiểm tra countdown
		if (Game.BOARD.getCountdown() == 0) {
			// Vẽ hình chữ nhật hoặc hình tròn để nổi bật
			g.setColor(new Color(252, 143, 84)); // Màu sắc nổi bật
			g.fillRoundRect(xOffset, yOffset, smallWidth, smallHeight, TILE_RADIUS, TILE_RADIUS);
			g.setColor(new Color(225, 1, 60)); // Màu sắc nổi bật
			g.drawRoundRect(xOffset, yOffset, smallWidth, smallHeight, TILE_RADIUS, TILE_RADIUS); // Viền cho ô
			Image undoIcon = new ImageIcon("D:\\Java learing\\2048-dsa24-25 - Copy\\src\\game\\undo.png").getImage(); // Đường dẫn tới biểu tượng Undo
			g.drawImage(undoIcon, xOffset + 19, yOffset + 10, 22, 22, null); // Vẽ biểu tượng
		} else {
			// Vẽ biểu tượng Undo nếu countdown > 0
			// Vẽ nền nút
			g.setColor(new Color(253, 231, 187)); // Màu nền
			g.fillRoundRect(xOffset, yOffset, smallWidth, smallHeight, TILE_RADIUS, TILE_RADIUS);
			Image undoIcon = new ImageIcon("D:\\Java learing\\2048-dsa24-25 - Copy\\src\\game\\undo.png").getImage(); // Đường dẫn tới biểu tượng Undo
			g.drawImage(undoIcon, xOffset + 19, yOffset + 10, 22, 22, null); // Vẽ biểu tượng
		}

		int size = 25; // Kích thước hình tròn

		// Vẽ hình tròn
		g.setColor(new Color(0xDA9F64)); // Màu nền
		g.fillOval(xOffset+47, yOffset-12, size, size); // Vẽ hình tròn
		// Vẽ số 20
		g.setFont(new Font(FONT, Font.BOLD, 12));
		g.setColor(new Color(0XFFFFFF));
		g.drawString(String.valueOf(Game.BOARD.getCountdown()), xOffset + 53, yOffset +5);
	}

	private void drawCountdown_swap(Graphics2D g) {
		int width = 120; // Đặt lại width để đồng bộ
		int height = 40; // Giữ nguyên height
		int smallWidth = 60; // Chiều rộng mới cho nền
		int smallHeight = 40; // Chiều cao không thay đổi
		int xOffset = Game.WINDOW.getWidth() - WIN_MARGIN - (2*width) - TILE_MARGIN - 40; // Đặt xOffset sao cho ở gần drawCountdown
		int yOffset = TILE_MARGIN-74; // Điều chỉnh vị trí y để tách biệt với drawCountdown
		if (Game.BOARD.getCountdownSwap() == 0) {
			// Vẽ hình chữ nhật hoặc hình tròn để nổi bật
			g.setColor(new Color(252, 143, 84)); // Màu sắc nổi bật
			g.fillRoundRect(xOffset, yOffset, smallWidth, smallHeight, TILE_RADIUS, TILE_RADIUS);
			g.setColor(new Color(225, 1, 60)); // Màu sắc nổi bật
			g.drawRoundRect(xOffset, yOffset, smallWidth, smallHeight, TILE_RADIUS, TILE_RADIUS); // Viền cho ô
			Image undoIcon = new ImageIcon("D:\\Java learing\\2048-dsa24-25 - Copy\\src\\game\\swap.jpg").getImage(); // Đường dẫn tới biểu tượng Undo
			g.drawImage(undoIcon, xOffset + 13, yOffset +3, 35, 35, null); // Vẽ biểu tượng, điều chỉnh vị trí nếu cần;
		} else {
			// Vẽ biểu tượng Undo nếu countdown > 0
			// Vẽ nền nút
			g.setColor(new Color(253, 231, 187)); // Màu nền
			g.fillRoundRect(xOffset, yOffset, smallWidth, smallHeight, TILE_RADIUS, TILE_RADIUS);
			Image undoIcon = new ImageIcon("D:\\Java learing\\2048-dsa24-25 - Copy\\src\\game\\swap.jpg").getImage(); // Đường dẫn tới biểu tượng Undo
			g.drawImage(undoIcon, xOffset + 17, yOffset + 8, 25, 25, null); // Vẽ biểu tượng
		}
		int size = 25; // Kích thước hình tròn

		// Vẽ hình tròn
		g.setColor(new Color(0xDA9F64)); // Màu nền
		g.fillOval(xOffset+47, yOffset-12, size, size); // Vẽ hình tròn
		// Vẽ số 20
		g.setFont(new Font(FONT, Font.BOLD, 12));
		g.setColor(new Color(0XFFFFFF));
		g.drawString(String.valueOf(Game.BOARD.getCountdownSwap()), xOffset + 53, yOffset +5);
	}

	private static void drawTile(Graphics g, Tile tile, int x, int y, int tileSize) {
		int value = tile.getValue();
		int xOffset = x * (TILE_MARGIN + tileSize) + TILE_MARGIN;
		int yOffset = y * (TILE_MARGIN + tileSize) + TILE_MARGIN;


		// Vẽ nền cho ô
		g.setColor(Game.COLORS.getTileBackground(value));
		g.fillRoundRect(xOffset, yOffset, tileSize, tileSize, TILE_RADIUS, TILE_RADIUS);

		// Kiểm tra xem ô có phải là ô đã chọn không
		boolean isFirstSelected = tile == Game.CONTROLS.getFirstSelectedTile();
		boolean isSecondSelected = tile == Game.CONTROLS.getSecondSelectedTile();

		// Vẽ highlight cho ô đã chọn
		if (isFirstSelected || isSecondSelected) {
			g.setColor(Color.YELLOW); // Màu vàng nổi bật
			g.fillRoundRect(xOffset, yOffset, tileSize, tileSize, TILE_RADIUS, TILE_RADIUS);
		}

		// Vẽ chữ số trên ô
		g.setColor(Game.COLORS.getTileColor(value));
		final Font font = new Font(FONT, Font.BOLD, value < 100 ? 36 : value < 1000 ? 32 : 24);
		g.setFont(font);
		String s = String.valueOf(value);
		final FontMetrics fm = g.getFontMetrics(font);
		final int w = fm.stringWidth(s);
		final int h = -(int) fm.getLineMetrics(s, g).getBaselineOffsets()[2];

		if (value != 0) {
			g.drawString(s, xOffset + (tileSize - w) / 2, yOffset + tileSize - (tileSize - h) / 2 - 2);
		}



		// Handle win/loss messages
		if (Game.BOARD.getWonOrLost() != null && !Game.BOARD.getWonOrLost().isEmpty()) {
			g.setColor(new Color(255, 255, 255, 40));
			g.fillRect(0, 0, Game.WINDOW.getWidth(), Game.WINDOW.getHeight());
			g.setColor(ColorScheme.BRIGHT);
			g.setFont(new Font(FONT, Font.BOLD, 30));
			g.drawString("You " + Game.BOARD.getWonOrLost() + "!", 68, 150);
			Game.CONTROLS.unbind();
		}
	}
	private void handleMouseClick(MouseEvent e) {
		int x = e.getX();
		int y = e.getY();

		// Tính kích thước ô dựa trên chiều rộng của bảng
		int boardWidth = Math.min(getWidth() - 2 * WIN_MARGIN, getHeight() - 100);
		int tileSize = (boardWidth - (TILE_MARGIN * 5)) / 4; // Kích thước ô

		// Tính toán chỉ số hàng và cột dựa trên vị trí nhấp chuột
		int col = (x - WIN_MARGIN) / (tileSize + TILE_MARGIN);
		int row = (y - 80) / (tileSize + TILE_MARGIN);

		// Kiểm tra giới hạn
		if (row >= 0 && row < 4 && col >= 0 && col < 4) {
			Tile clickedTile = Game.BOARD.getTileAt(row, col);

			// Chọn ô nếu chưa chọn đủ hai ô
			if (Game.CONTROLS.getFirstSelectedTile() == null) {
				Game.CONTROLS.selectTile(clickedTile);
				System.out.println("Chọn ô 1");
			} else if (Game.CONTROLS.getSecondSelectedTile() == null) {
				Game.CONTROLS.selectTile(clickedTile);
				System.out.println("Chọn ô 2");
			}

			// Vẽ lại giao diện để cập nhật highlight
			Game.WINDOW.repaint();
		}
	}
}