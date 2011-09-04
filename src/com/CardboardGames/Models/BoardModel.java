package com.CardboardGames.Models;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import android.graphics.Point;

public class BoardModel {

	/// PUBLIC METHODS

	public BoardModel() {
		initPieces();
	}

	public boolean isBlack(final int row, final int col) {
		return row % 2 != col % 2;
	}

	public boolean hasSelectedCoinPiece() {
		return m_selectedCoinPiece != null;
	}

	public Piece getSelectedCoinPiece() {
		return m_selectedCoinPiece;
	}

	private boolean hasGuerillaPieceOrBoardEdge(int x, int y) {
		if (x < 0 || y < 0 || x >= COLS-1 || y >= ROWS-1)
			return true;
		return getGuerillaPieceAt(x, y) != null;
	}

	public boolean coinPieceWouldBeCapturedAt(int x, int y) {
		if (hasGuerillaPieceOrBoardEdge(x-1, y-1) &&
			hasGuerillaPieceOrBoardEdge(x-1, y) &&
			hasGuerillaPieceOrBoardEdge(x, y-1) &&
			hasGuerillaPieceOrBoardEdge(x, y)) {
			return true;
		}
		return false;
	}

	public boolean isValidCoinMove(Piece piece, int x, int y) {
		if (x < 0 || x >= COLS)
			return false;
		if (y < 0 || y >= ROWS)
			return false;

		Point piece_pos = piece.getPosition();
		int xdiff = Math.abs(x - piece_pos.x);
		int ydiff = Math.abs(y - piece_pos.y);
		if (xdiff != 1 || ydiff != 1)
			return false;

		if (getCoinPieceAt(x, y) != null)
			return false;

		return true;
	}

	public boolean isValidCoinMove(Piece piece, Point pos) {
		return isValidCoinMove(piece, pos.x, pos.y);
	}

	private Piece getPieceAt(final Point point, List<Piece> pieces) {
		for (Piece piece : pieces) {
			if (piece.getPosition().equals(point))
				return piece;
		}
		return null;
	}

	public Piece getCoinPieceAt(final Point point) {
		return getPieceAt(point, m_coinPieces);
	}

	public Piece getCoinPieceAt(int x, int y) {
		return getCoinPieceAt(new Point(x, y));
	}

	public Piece getGuerillaPieceAt(final Point point) {
		return getPieceAt(point, m_guerillaPieces);
	}

	public Piece getGuerillaPieceAt(int x, int y) {
		return getGuerillaPieceAt(new Point(x, y));
	}

	public void addCoinPiece(Piece piece) {
		m_coinPieces.add(piece);
	}

	public void addGuerillaPiece(Piece piece) {
		m_guerillaPieces.add(piece);
	}

	public List<Piece> getCoinPieces() {
		return Collections.unmodifiableList(m_coinPieces);
	}

	public List<Piece> getGuerillaPieces() {
		return Collections.unmodifiableList(m_guerillaPieces);
	}

	public int getNumCoinPieces() {
		return m_coinPieces.size();
	}

	public int getNumGuerillaPieces() {
		return m_guerillaPieces.size();
	}

	private boolean isValidGuerillaPlacement(Point point, boolean is_first) {
		if (point.x < 0 || point.x >= COLS - 1)
			return false;
		if (point.y < 0 || point.y >= ROWS - 1)
			return false;
		if (getGuerillaPieceAt(point) != null)
			return false;
		if (is_first)
			return true;
		if (getGuerillaPieceAt(point.x + 1, point.y) == null &&
			getGuerillaPieceAt(point.x - 1, point.y) == null &&
			getGuerillaPieceAt(point.x, point.y + 1) == null &&
			getGuerillaPieceAt(point.x, point.y - 1) == null) {
			return false;
		}
		return true;
	}

	public boolean isValidGuerillaPlacement(final Point point) {
		boolean is_first = getNumGuerillaPieces() == 0;
		return isValidGuerillaPlacement(point, is_first);
	}

	public void placeGuerillaPiece(final Point point) {
		Piece piece = new Piece(false, point);
		m_guerillaPieces.add(piece);
	}

	public boolean hasSelectedPiece() {
		return m_selectedCoinPiece != null;
	}

	public boolean selectCoinPieceAt(Point point) {
		m_selectedCoinPiece = getCoinPieceAt(point);
		return m_selectedCoinPiece != null;
	}

	public boolean captureGuerillaPiece(Point coin_from, Point coin_to) {
		int guerilla_x = coin_from.x - (coin_to.x < coin_from.x ? 1 : 0);
		int guerilla_y = coin_from.y - (coin_to.y < coin_from.y ? 1 : 0);
		Point capture_point = new Point(guerilla_x, guerilla_y);

		int num_pieces = m_guerillaPieces.size();
		for (int idx = 0; idx < num_pieces; ++idx) {
			if (m_guerillaPieces.get(idx).getPosition().equals(capture_point)) {
				m_guerillaPieces.remove(idx);
				return true;
			}
		}
		return false;
	}

	public boolean moveSelectedCoinPiece(Point point) {
		if (m_selectedCoinPiece == null)
			return false;
		if (!isValidCoinMove(m_selectedCoinPiece, point))
			return false;

		Point piece_pos = m_selectedCoinPiece.getPosition();
		captureGuerillaPiece(piece_pos, point);
		m_selectedCoinPiece.setPosition(point);
		return true;
	}

	public void deselectCoinPiece() {
		m_selectedCoinPiece = null;
	}

	/// PRIVATE METHODS

	private void initPieces() {
		m_coinPieces.add(new Piece(false, new Point(3,2)));
		m_coinPieces.add(new Piece(false, new Point(2,3)));
		m_coinPieces.add(new Piece(false, new Point(4,3)));
		m_coinPieces.add(new Piece(false, new Point(3,4)));
		m_coinPieces.add(new Piece(false, new Point(5,4)));
		m_coinPieces.add(new Piece(false, new Point(4,5)));
	}

	/// PRIVATE TYPES

	public class Piece {
		private boolean captured = false;
		private Point position = new Point(0,0);

		public Piece(boolean captured, Point position) {
			this.setCaptured(captured);
			this.setPosition(position);
		}

		public boolean isCaptured() { return captured; }
		public void setCaptured(boolean captured) { this.captured = captured; }
		public Point getPosition() { return position; }
		public void setPosition(Point position) { this.position = position; }
	}

	/// PRIVATE MEMBERS

	Piece m_selectedCoinPiece = null;

	/// @{
	/// Pieces
	private final ArrayList<Piece> m_coinPieces = new ArrayList<Piece>();
	private final ArrayList<Piece> m_guerillaPieces = new ArrayList<Piece>();
	/// @}

	/// @{
	/// Grid Dimensions
	public final int ROWS = 8;
	public final int COLS = 8;
	/// @}
}
