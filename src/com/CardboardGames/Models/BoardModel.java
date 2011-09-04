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
	
	public Piece getSelectedPiece() {
		return m_selectedPiece;
	}
	
	public Piece getCoinPieceAt(int x, int y) {
		return getCoinPieceAt(new Point(x, y));
	}
	
	public Piece getCoinPieceAt(final Point point) {
		for (Piece piece : m_coinPieces) {
			if (piece.getPosition().equals(point))
				return piece;
		}
		return null;
	}
	
	public List<Piece> getCoinPieces() {
		return Collections.unmodifiableList(m_coinPieces);
	}
	
	public List<Piece> getGuerillaPieces() {
		return Collections.unmodifiableList(m_coinPieces);
	}
	
	public void addTouch(Point point) {
		m_selectedPiece = getCoinPieceAt(point);
	}
	
	/// PRIVATE METHODS
	
	private void initPieces() {
		m_coinPieces.add(new Piece(false, new Point(3,2)));
		m_coinPieces.add(new Piece(false, new Point(2,3)));
		m_coinPieces.add(new Piece(false, new Point(4,3)));
		m_coinPieces.add(new Piece(false, new Point(3,4)));
		m_coinPieces.add(new Piece(false, new Point(5,4)));
		m_coinPieces.add(new Piece(false, new Point(4,5)));
		
		// DEBUG DEBUG DEBUG
		m_guerillaPieces.add(new Piece(false, new Point(3,2)));
		m_guerillaPieces.add(new Piece(false, new Point(2,3)));
		m_guerillaPieces.add(new Piece(false, new Point(4,3)));
		m_guerillaPieces.add(new Piece(false, new Point(3,4)));
		m_guerillaPieces.add(new Piece(false, new Point(5,4)));
		m_guerillaPieces.add(new Piece(false, new Point(4,5)));
		// DEBUG DEBUG DEBUG
	}
	
	/// PRIVATE TYPES
	
	public class Piece {
		private boolean captured = false;
		private Point position = new Point(0,0);
		
		public Piece(boolean captured, Point position) {
			this.setCaptured(captured);
			this.setPosition(position);
		}
		
		public boolean isCaptured() {
			return captured;
		}
		public void setCaptured(boolean captured) {
			this.captured = captured;
		}
		public Point getPosition() {
			return position;
		}
		public void setPosition(Point position) {
			this.position = position;
		}
	}
	
	/// PRIVATE MEMBERS
	
	Piece m_selectedPiece = null;
	
	/// @{
	/// Pieces
	private ArrayList<Piece> m_coinPieces = new ArrayList<Piece>();
	private ArrayList<Piece> m_guerillaPieces = new ArrayList<Piece>();
	/// @}

	/// @{
	/// Grid Dimensions
	public final int ROWS = 8;
	public final int COLS = 8;
	/// @}
}
