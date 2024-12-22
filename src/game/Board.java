package game;

import javax.swing.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Stack;

public class Board {

	private int size; 						// size of the grid
	private int score; 						// game score
	private int emptyTiles;					// number of tiles with zero value
	private int initTiles = 2; 				// number of tiles board starts with (usually two tiles)
	private boolean gameover = false; 		// game is over when 2048 tile is found
	private String wonOrLost;				// won or lost
	private boolean genNewTile = false;		// generate new tile when any tile moved
	private int moveCount = 0; // Biến đếm lượt di chuyển
	private int countdown = 20; // Biến đếm từ 20 xuống 0
	private LeaderBoard leaderBoard = new LeaderBoard(); // Đối tượng LeaderBoard
	private List<List<Tile>> tiles;			// board

	private Stack<List<List<Tile>>> historyStack; // Stack lưu trạng thái trước đó
	public Board(int size) {
		super();
		this.size = size;
		this.emptyTiles = this.size * this.size;
		this.tiles = new ArrayList<>();
		this.historyStack = new Stack<>(); // Khởi tạo Stack

		start();
	}

	private void initialize() {
		for (int row = 0; row < this.size; row++) {
			tiles.add(new ArrayList<Tile>());
			for (int col = 0; col < this.size; col++) {
				tiles.get(row).add(new Tile());
			}
		}
	}

	private void start() {
		Game.CONTROLS.bind();
		initialize();
		genInitTiles();
		//show();
	}

	public int getSize() {
		return size;
	}

	public void setSize(int size) {
		this.size = size;
	}

	public List<List<Tile>> getTiles() {
		return tiles;
	}

	public void setTiles(List<List<Tile>> tiles) {
		this.tiles = tiles;
	}

	public Tile getTileAt(int row, int col) {
		return tiles.get(row).get(col);
	}

	public void setTileAt(int row, int col, Tile t) {
		tiles.get(row).set(col, t);
	}

	public void remTileAt(int row, int col) {
		tiles.get(row).remove(col);
	}
	public int getScore() {
		return score;
	}
	private List<Tile> mergeTiles(List<Tile> sequence) {
		for (int l = 0; l < sequence.size() - 1; l++) {
			if (sequence.get(l).getValue() == sequence.get(l + 1).getValue()) {
				int value;
				if ((value = sequence.get(l).merging()) == 2048) {
					gameover = true;
				}
				score += value;
				sequence.remove(l + 1);
				genNewTile = true; // board has changed its state
				emptyTiles++;
				SoundPlayer.playSound("sounds/merge.wav");
			}
		}
		return sequence;
	}
	private List<Tile> addEmptyTilesFirst(List<Tile> merged) {
		for (int k = merged.size(); k < size; k++) {
			merged.add(0, new Tile());
		}
		return merged;
	}
	private List<Tile> addEmptyTilesLast(List<Tile> merged) { // boolean last/first
		for (int k = merged.size(); k < size; k++) {
			merged.add(k, new Tile());
		}
		return merged;
	}

	private List<Tile> removeEmptyTilesRows(int row) {

		List<Tile> moved = new ArrayList<>();

		for (int col = 0; col < size; col++) {
			if (!getTileAt(row, col).isEmpty()) { // NOT empty
				moved.add(getTileAt(row, col));
			}
		}

		return moved;
	}

	private List<Tile> removeEmptyTilesCols(int row) {

		List<Tile> moved = new ArrayList<>();

		for (int col = 0; col < size; col++) {
			if (!getTileAt(col, row).isEmpty()) { // NOT empty
				moved.add(getTileAt(col, row));
			}
		}

		return moved;
	}

	private List<Tile> setRowToBoard(List<Tile> moved, int row) {
		for (int col = 0; col < tiles.size(); col++) {
			if (moved.get(col).hasMoved(row, col)) {
				genNewTile = true;
				SoundPlayer.playSound("sounds/move.wav");
			}
			setTileAt(row, col, moved.get(col));
		}

		return moved;
	}

	private List<Tile> setColToBoard(List<Tile> moved, int row) {
		for (int col = 0; col < tiles.size(); col++) {
			if (moved.get(col).hasMoved(col, row)) {
				genNewTile = true;
				SoundPlayer.playSound("sounds/move.wav");
			}
			setTileAt(col, row, moved.get(col));
		}

		return moved;
	}

	public void moveUp() {
		saveState(); // Lưu trạng thái trước
		List<Tile> moved;

		for (int row = 0; row < size; row++) {

			moved = removeEmptyTilesCols(row);
			moved = mergeTiles(moved);
			moved = addEmptyTilesLast(moved);
			moved = setColToBoard(moved, row);

		}
		moveCount++;
		countdown--;
		decrementCountdownSwap();

	}

	public void moveDown() {
		saveState(); // Lưu trạng thái trước
		List<Tile> moved;

		for (int row = 0; row < size; row++) {

			moved = removeEmptyTilesCols(row);
			moved = mergeTiles(moved);
			moved = addEmptyTilesFirst(moved);
			moved = setColToBoard(moved, row);

		}
		moveCount++;
		countdown--;
		decrementCountdownSwap();
	}

	public void moveLeft() {
		saveState(); // Lưu trạng thái trước
		List<Tile> moved;

		for (int row = 0; row < size; row++) {

			moved = removeEmptyTilesRows(row);
			moved = mergeTiles(moved);
			moved = addEmptyTilesLast(moved);
			moved = setRowToBoard(moved, row);

		}
		moveCount++;
		countdown--;
		decrementCountdownSwap();
	}

	public void moveRight() {
		saveState(); // Lưu trạng thái trước
		List<Tile> moved;

		for (int row = 0; row < size; row++) {

			moved = removeEmptyTilesRows(row);
			moved = mergeTiles(moved);
			moved = addEmptyTilesFirst(moved);
			moved = setRowToBoard(moved, row);

		}
		moveCount++;
		countdown--;
		decrementCountdownSwap();
	}
	private void showNameDialogAndAddToLeaderboard() {
		String playerName = JOptionPane.showInputDialog(null, "Enter your name:", "Game Over", JOptionPane.PLAIN_MESSAGE);
		if (playerName != null && !playerName.trim().isEmpty()) {
			Player player = new Player(playerName, score);
			leaderBoard.addPlayer(player);
			Window.addPlayerToLeaderboard(player);

		}
	}


	public void isGameOver() {
		if (gameover) {
			setWonOrLost("WON");
			show();
			showNameDialogAndAddToLeaderboard();
		} else {
			if (isFull()) {
				if (!isMovePossible()) {
					setWonOrLost("LOST");
					show();
					showNameDialogAndAddToLeaderboard();
				}
			} else {
				newRandomTile(); // game continues
			}
		}
	}

	// Cai nay cho undo
	public void resetCountdown() {
		countdown = 20; // Đặt lại countdown về 20
	}

	private void saveState() {
		List<List<Tile>> currentState = new ArrayList<>();
		for (List<Tile> row : tiles) {
			List<Tile> rowCopy = new ArrayList<>();
			for (Tile tile : row) {
				rowCopy.add(new Tile(tile.getValue(), tile.getRow(), tile.getCol()));
			}
			currentState.add(rowCopy);
		}
		historyStack.push(currentState); // Đẩy trạng thái hiện tại vào Stack
	}

	public void undo() {
		if (countdown > 0) {
			System.out.println("Not enough turns to undo.");
			return;
		}
		if (historyStack.isEmpty()) {
			System.out.println("There is no state to undo.");
			return;
		}
		tiles = historyStack.pop(); // Lấy trạng thái gần nhất từ Stack
		Game.WINDOW.repaint(); // Cập nhật giao diện
		countdown = 20; // Đặt lại countdown
	}

	// Phương thức để lấy giá trị countdown
	public int getCountdown() {
		return Math.max(0, countdown);
	}

	// Cai nay den swap
	private int swapMoveCount = 30; // Biến để đếm số lượt cho swap
	public void resetCountdownSwap() {
		swapMoveCount = 30; // Đặt lại countdown về 20
	}
	public void decrementCountdownSwap() {
		if (swapMoveCount > 0) {
			swapMoveCount--;
		}
	}
	public int getCountdownSwap() {
		return swapMoveCount;
	}


	private boolean isFull() {
		return emptyTiles == 0;
	}


	private boolean isMovePossible() {
		for (int row = 0; row < size; row++) {
			for (int col = 0; col < size - 1; col++) {
				if (getTileAt(row, col).getValue() == getTileAt(row, col + 1).getValue()) {
					return true;
				}
			}
		}

		for (int row = 0; row < size - 1; row++) {
			for (int col = 0; col < size; col++) {
				if (getTileAt(col, row).getValue() == getTileAt(col, row + 1).getValue()) {
					return true;
				}
			}
		}
		return false;
	}

	private void genInitTiles() {
		for (int i = 0; i < initTiles; i++) {
			genNewTile = true;
			newRandomTile();
		}
	}

	private void newRandomTile() {
		if (genNewTile) {
			int row;
			int col;
			int value = Math.random() < 0.9 ? 2 : 4;
			do {
				row = (int) (Math.random () * 4);
				col = (int) (Math.random () * 4);
			} while (getTileAt(row, col).getValue() != 0);
			setTileAt(row, col, new Tile(value, row, col));
			emptyTiles--;
			genNewTile = false;
		}
	}

	// Phương thức khởi động lại trò chơi
	public void reset() {
		this.tiles.clear(); // Xóa toàn bộ trạng thái bảng
		this.score = 0; // Đặt lại điểm số
		this.emptyTiles = this.size * this.size; // Reset số ô trống
		this.gameover = false; // Reset trạng thái gameover
		this.wonOrLost = null; // Reset trạng thái thắng/thua
		this.genNewTile = false; // Reset trạng thái tạo ô mới
		initialize(); // Tạo lại bảng
		genInitTiles(); // Tạo lại các ô ban đầu
		resetCountdown(); // Đặt lại countdown về 20
		resetCountdownSwap(); // Đặt lại countdownSwap về 30
	}

	protected void show() {
		for (int i = 0; i < 2; ++i) System.out.println();
		System.out.println("SCORE: " + score);
		for (int i = 0; i < tiles.size(); i++) {
			for (int j = 0; j < tiles.get(i).size(); j++) {
				System.out.format("%-5d", getTileAt(i, j).getValue());
			}
			System.out.println();
		}
	}

	public String getWonOrLost() {
		return wonOrLost;
	}

	public void setWonOrLost(String wonOrLost) {
		this.wonOrLost = wonOrLost;
	}

	public void swapTiles(Tile tile1, Tile tile2) {
		if (tile1 == null || tile2 == null) {
			System.out.println("Một trong hai ô là null.");
			return;
		}

		// Hoán đổi giá trị giữa hai ô
		int tempValue = tile1.getValue();
		tile1.setValue(tile2.getValue());
		tile2.setValue(tempValue);

		// Cập nhật vị trí của các ô
		int tempRow = tile1.getRow();
		int tempCol = tile1.getCol();
		tile1.setPosition(tile2.getRow(), tile2.getCol());
		tile2.setPosition(tempRow, tempCol);
	}

}