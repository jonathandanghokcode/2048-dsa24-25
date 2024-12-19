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
			case KeyEvent.VK_S: // Nút S để swap
				if (firstSelectedTile != null && secondSelectedTile != null && Game.BOARD.getCountdownSwap() == 0) {
					Game.BOARD.swapTiles(firstSelectedTile, secondSelectedTile);
					// Đặt lại các ô đã chọn
					firstSelectedTile = null;
					secondSelectedTile = null;
					Game.BOARD.resetCountdownSwap(); // Đặt lại đếm ngược
				} else {
					System.out.println("Cannot be swapped, wait until the countdown is complete.");
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
	public void selectTile(Tile tile) {
		if (firstSelectedTile == null) {
			// Chọn ô đầu tiên
			firstSelectedTile = tile;
		} else if (secondSelectedTile == null) {
			// Nếu ô thứ hai được chọn là ô đầu tiên, hủy chọn ô đầu tiên
			if (tile == firstSelectedTile) {
				firstSelectedTile = null; // Hủy chọn ô đầu tiên
			} else {
				// Chọn ô thứ hai
				secondSelectedTile = tile;
			}
		} else {
			// Nếu đã chọn 2 ô, đặt lại và chọn ô mới
			firstSelectedTile = tile; // Hủy chọn ô thứ nhất và chọn ô mới
			secondSelectedTile = null;
		}
	}
	public Tile getFirstSelectedTile() {
		return firstSelectedTile;
	}

	public Tile getSecondSelectedTile() {
		return secondSelectedTile;
	}


}
