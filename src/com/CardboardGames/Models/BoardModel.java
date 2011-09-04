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

	public boolean isValidMove(Piece piece, int x, int y) {
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

	public boolean isValidMove(Piece piece, Point pos) {
		return isValidMove(piece, pos.x, pos.y);
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

	public boolean isValidGuerillaPlacement(final Point point) {
		if (point.x < 0 || point.x >= COLS - 1)
			return false;
		if (point.y < 0 || point.y >= ROWS - 1)
			return false;
		if (getGuerillaPieceAt(point) != null)
			return false;
		return true;
	}

	public boolean isValidGuerillaSetupPlacement(final Point point) {
		if (!isValidGuerillaPlacement(point))
			return false;
		if (getNumGuerillaPieces() == 0)
			return true;
		if (getNumGuerillaPieces() != 1)
			return false;

		List<Piece> pieces = getGuerillaPieces();
		assert(pieces.size() == 1);
		Piece piece = pieces.get(0);
		Point piece_pos = piece.getPosition();

		int xdiff = Math.abs(point.x - piece_pos.x);
		int ydiff = Math.abs(point.y - piece_pos.y);
		if (xdiff == 0 && ydiff == 1 || xdiff == 1 && ydiff == 0)
			return true;
		return false;
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

	public boolean moveSelectedCoinPiece(Point point) {
		if (m_selectedCoinPiece == null)
			return false;
		if (!isValidMove(m_selectedCoinPiece, point))
			return false;
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
