package com.CardboardGames.Views;

import java.util.List;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Point;
import android.graphics.Rect;
import android.view.View;

import com.CardboardGames.Models.BoardModel;

public class BoardView extends View {
	
	/// PUBLIC METHODS

	public BoardView(Context ctx) {
		super(ctx);
		m_paint.setFlags(Paint.ANTI_ALIAS_FLAG);
	}

	@Override
	public void onDraw(Canvas canvas) {
		drawBoard(canvas);
		drawPieces(canvas);
	}

	// PRIVATE METHODS

	private boolean isBlack(int row, int col) {
		return row % 2 != col % 2;
	}

	private Rect getRect(int row, int col, int board_length) {
		double rect_width = (double)board_length / COLS;
		double rect_height = (double)board_length / ROWS;

		double top = BORDER_PX + row * rect_height;
		double left = BORDER_PX + col * rect_width;
		double bottom = top + rect_height;
		double right = left + rect_width;

		return new Rect((int)top, (int)left, (int)bottom, (int)right);
	}
	
	private int getBoardSizeInclBorder() {
		return Math.min(super.getWidth(), super.getHeight());
	}
	
	private int getBoardSize() {
		return getBoardSizeInclBorder() - 2*BORDER_PX;
	}

	private void drawBoard(Canvas canvas) {
		int board_size_incl_border = getBoardSizeInclBorder();
		int board_px = getBoardSize();

		m_paint.setColor(BORDER_CLR);
		canvas.drawRect(0, 0, board_size_incl_border, board_size_incl_border, m_paint);

		m_paint.setColor(BLACK_CLR);
		canvas.drawRect(
			BORDER_PX,
			BORDER_PX, 
			BORDER_PX + board_px,
			BORDER_PX + board_px,
			m_paint);

		m_paint.setColor(WHITE_CLR);
		for (int idx_row = 0; idx_row < ROWS; ++idx_row) {
			for (int idx_col = 0; idx_col < ROWS; ++idx_col) {
				if (isBlack(idx_row, idx_col))
					continue;
				canvas.drawRect(getRect(idx_row, idx_col, board_px), m_paint);
			}
		}
	}	
	
	private void drawPiece(Canvas canvas, int cx, int cy, int radius) {
		canvas.drawCircle(cx, cy, radius, m_paint);
	}
	
	private int getCoinPieceRadius(int board_size) {
		return (int)(
			0.5 * getRect(0, 0, board_size).width() 
			* COIN_PIECE_TO_SQUARE_RATIO);
	}
	
	private void drawCoinPieces(
		Canvas canvas,
		List<BoardModel.Piece> pieces) 
	{
		m_paint.setColor(COIN_PIECE_CLR);
		int board_size = getBoardSize();
		int radius = getCoinPieceRadius(board_size);
		for (BoardModel.Piece piece : pieces) {
			Point pos = piece.getPosition();
			Rect rect = getRect(pos.x, pos.y, board_size);
			drawPiece(canvas, rect.centerX(), rect.centerY(), radius);
		}
	}
	
	private int getGuerillaPieceRadius(int board_size) {
		return (int)(
			0.5 * getRect(0, 0, board_size).width() 
			* GUERILLA_PIECE_TO_SQUARE_RATIO);
	}
	
	private void drawGuerillaPieces(
		Canvas canvas,
		List<BoardModel.Piece> pieces) 
	{
		m_paint.setColor(GUERILLA_PIECE_CLR);
		int board_size = getBoardSize();
		int radius = getGuerillaPieceRadius(board_size);
		for (BoardModel.Piece piece : pieces) {
			Point pos = piece.getPosition();
			Rect rect = getRect(pos.x, pos.y, board_size);
			drawPiece(canvas, rect.right, rect.bottom, radius);
		}
	}
	
	private void drawPieces(Canvas canvas) {
		drawCoinPieces(canvas, m_model.getCoinPieces());
		
		m_paint.setColor(GUERILLA_PIECE_CLR);
		drawGuerillaPieces(canvas, m_model.getGuerillaPieces());
	}

	/// PRIVATE MEMBERS

	private Paint m_paint = new Paint();
	
	/// Board Model
	private BoardModel m_model = new BoardModel();

	/// @{
	/// Board Properties
	private final int ROWS = 8;
	private final int COLS = 8;
	private final int BORDER_PX = 10;
	
	private final double COIN_PIECE_TO_SQUARE_RATIO = 0.9;
	private final double GUERILLA_PIECE_TO_SQUARE_RATIO = 0.4;
	
	private final int BORDER_CLR = Color.BLUE;
	private final int COIN_PIECE_CLR = Color.DKGRAY;
	private final int GUERILLA_PIECE_CLR = Color.GREEN;
	private final int WHITE_CLR = Color.RED;
	private final int BLACK_CLR = Color.WHITE;
	/// @}
}
