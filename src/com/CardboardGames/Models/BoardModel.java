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

		if (m_coinMustCapture
			&& !wouldCaptureGuerillaPiece(piece, new Point(x, y)))
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

	public boolean isGameOver() {
		if (!m_gameStarted)
			return false;
		if (m_guerillaPieces.size() == 0)
			return true;
		if (m_coinPieces.size() == 0)
			return true;
		return false;
	}

	public void placeGuerillaPiece(final Point point) {
		Piece piece = new Piece(point);
		m_guerillaPieces.add(piece);
		int x = point.x;
		int y = point.y;
		if (coinPieceWouldBeCapturedAt(x, y))
			captureCoinPieceAt(x, y);
		if (coinPieceWouldBeCapturedAt(x, y+1))
			captureCoinPieceAt(x, y+1);
		if (coinPieceWouldBeCapturedAt(x+1, y))
			captureCoinPieceAt(x+1, y);
		if (coinPieceWouldBeCapturedAt(x+1, y+1))
			captureCoinPieceAt(x+1, y+1);

		m_gameStarted = true;
	}

	public boolean hasSelectedPiece() {
		return m_selectedCoinPiece != null;
	}

	public boolean selectCoinPieceAt(Point point) {
		if (m_coinMustCapture)
			return false;

		m_selectedCoinPiece = getCoinPieceAt(point);
		m_lastCoinMoveCaptured = false;
		return m_selectedCoinPiece != null;
	}

	public boolean captureCoinPieceAt(Point point) {
		int num_pieces = m_coinPieces.size();
		for (int idx = 0; idx < num_pieces; ++idx) {
			if (m_coinPieces.get(idx).getPosition().equals(point)) {
				m_coinPieces.remove(idx);
				return true;
			}
		}
		return false;
	}

	public boolean captureCoinPieceAt(int x, int y) {
		return captureCoinPieceAt(new Point(x, y));
	}

	private boolean doCaptureGuerillaPiece(
		Point coin_from,
		Point coin_to,
		boolean do_capture)
	{
		int guerilla_x = coin_from.x - (coin_to.x < coin_from.x ? 1 : 0);
		int guerilla_y = coin_from.y - (coin_to.y < coin_from.y ? 1 : 0);
		Point capture_point = new Point(guerilla_x, guerilla_y);

		int num_pieces = m_guerillaPieces.size();
		for (int idx = 0; idx < num_pieces; ++idx) {
			if (m_guerillaPieces.get(idx).getPosition().equals(capture_point)) {
				if (do_capture)
					m_guerillaPieces.remove(idx);
				return true;
			}
		}
		return false;
	}

	public boolean captureGuerillaPiece(Point coin_from, Point coin_to) {
		return doCaptureGuerillaPiece(coin_from, coin_to, true);
	}

	public boolean wouldCaptureGuerillaPiece(Piece piece, Point coin_to) {
		return doCaptureGuerillaPiece(piece.getPosition(), coin_to, false);
	}

	public boolean wouldCaptureGuerillaPiece(Piece piece, int x, int y) {
		return wouldCaptureGuerillaPiece(piece, new Point(x, y));
	}

	public boolean moveSelectedCoinPiece(Point point) {
		if (m_selectedCoinPiece == null)
			return false;
		if (!isValidCoinMove(m_selectedCoinPiece, point))
			return false;

		Point piece_pos = m_selectedCoinPiece.getPosition();
		m_lastCoinMoveCaptured = captureGuerillaPiece(piece_pos, point);
		m_selectedCoinPiece.setPosition(point);
		return true;
	}

	public void deselectCoinPiece() {
		m_selectedCoinPiece = null;
	}

	public boolean lastCoinMoveCaptured() {
		return m_lastCoinMoveCaptured;
	}

	public void setLastCoinMoveCaptured(boolean captured) {
		m_lastCoinMoveCaptured = captured;
	}

	public boolean selectedCoinPieceHasValidMoves() {
		if (m_selectedCoinPiece == null)
			return false;

		Point pos = m_selectedCoinPiece.getPosition();
		int x = pos.x;
		int y = pos.y;

		if (isValidCoinMove(m_selectedCoinPiece, x-1, y-1) ||
			isValidCoinMove(m_selectedCoinPiece, x-1, y+1) ||
			isValidCoinMove(m_selectedCoinPiece, x+1, y-1) ||
			isValidCoinMove(m_selectedCoinPiece, x+1, y+1)) {
			return true;
		}
		return false;
	}

	public void setCoinMustCapture(boolean must_capture) {
		m_coinMustCapture = must_capture;
	}

	/// PRIVATE METHODS

	private void initPieces() {
		m_coinPieces.add(new Piece(new Point(3,2)));
		m_coinPieces.add(new Piece(new Point(2,3)));
		m_coinPieces.add(new Piece(new Point(4,3)));
		m_coinPieces.add(new Piece(new Point(3,4)));
		m_coinPieces.add(new Piece(new Point(5,4)));
		m_coinPieces.add(new Piece(new Point(4,5)));
	}

	public void reset() {
		m_selectedCoinPiece = null;
		m_lastCoinMoveCaptured = false;
		m_coinMustCapture = false;
		m_gameStarted = false;
		m_guerillaPieces.clear();
		m_coinPieces.clear();
		initPieces();
	}

	/// PRIVATE TYPES

	public class Piece {
		private Point position = new Point(0,0);

		public Piece(Point position) {
			this.setPosition(position);
		}

		public Point getPosition() { return position; }
		public void setPosition(Point position) { this.position = position; }
	}

	/// PRIVATE MEMBERS

	private Piece m_selectedCoinPiece = null;
	private boolean m_lastCoinMoveCaptured = false;
	private boolean m_coinMustCapture = false;
	private boolean m_gameStarted = false;

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
