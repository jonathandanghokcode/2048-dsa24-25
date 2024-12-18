package game;

import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

public class Controls implements KeyListener {
	private Tile firstSelectedTile = null;
	private Tile secondSelectedTile = null;

	@Override
	public void keyTyped(KeyEvent e) {}
	public void keyReleased(KeyEvent e) {}

	private boolean isMoving = false; // Biến để theo dõi trạng thái di chuyển
	public void keyPressed(KeyEvent e) {

		if (isMoving) return; // Nếu đang di chuyển, không làm gì cả

		isMoving = true; // Đặt trạng thái di chuyển là true
		int keyCode = e.getKeyCode();

		switch (keyCode) {
			case KeyEvent.VK_UP:
				Game.BOARD.moveUp();
				break;
			case KeyEvent.VK_DOWN:
				Game.BOARD.moveDown();
				break;
			case KeyEvent.VK_LEFT:
				Game.BOARD.moveLeft();
				break;
			case KeyEvent.VK_RIGHT:
				Game.BOARD.moveRight();
				break;
			case KeyEvent.VK_ESCAPE:
				Game.WINDOW.dispose();
				break;
			case KeyEvent.VK_Z: // Nút Undo
				Game.BOARD.undo();
				break;
			case KeyEvent.VK_S: // Giả sử 'S' để swap
				if (firstSelectedTile != null && secondSelectedTile != null) {
					Game.BOARD.swapTiles(firstSelectedTile, secondSelectedTile);
					firstSelectedTile = null; // Reset lựa chọn
					secondSelectedTile = null; // Reset lựa chọn
					Game.WINDOW.repaint(); // Cập nhật giao diện
				}
				break;
			default:
				break;
		}

		Game.BOARD.isGameOver();
//		Game.BOARD.show();
		Game.WINDOW.repaint();
//		Game.BOARD.decrementCountdown(); // Giảm giá trị countdown
		isMoving = false; // Đặt lại trạng thái di chuyển sau khi hoàn thành
	}


	public void bind() {
		Game.WINDOW.addKeyListener(this);
	}

	public void unbind() {
		Game.WINDOW.removeKeyListener(this);
	}

}
