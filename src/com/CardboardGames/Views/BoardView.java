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

	public BoardView(Context ctx, BoardModel model) {
		super(ctx);
		m_model = model;
		m_paint.setFlags(Paint.ANTI_ALIAS_FLAG);
	}

	@Override
	public void onDraw(Canvas canvas) {
		drawBoard(canvas);
		drawPieces(canvas);
	}
	
	// PRIVATE METHODS

	private Rect getRect(int row, int col, int board_length) {
		double rect_width = (double)board_length / m_model.COLS;
		double rect_height = (double)board_length / m_model.ROWS;

		double top = BORDER_SIZE_PX + row * rect_height;
		double left = BORDER_SIZE_PX + col * rect_width;
		double bottom = top + rect_height;
		double right = left + rect_width;

		return new Rect((int)top, (int)left, (int)bottom, (int)right);
	}
	
	public Point getBoardCoords(final float screenx, final float screeny) {
		int screen_pos[] = new int[2];
		getLocationOnScreen(screen_pos);
		float square_size = getBoardSize() / m_model.ROWS;
		int xoffset = BORDER_SIZE_PX + screen_pos[0];
		int yoffset = BORDER_SIZE_PX + screen_pos[1];
		int xcoord = (int)((screenx - xoffset) / square_size);
		int ycoord = (int)((screeny - yoffset) / square_size);
		return new Point(xcoord, ycoord);
	}
	
	private int getBoardSizeInclBorder() {
		return Math.min(super.getWidth(), super.getHeight());
	}
	
	private int getBoardSize() {
		return getBoardSizeInclBorder() - 2*BORDER_SIZE_PX;
	}

	private void drawBoard(Canvas canvas) {
		int board_size_incl_border = getBoardSizeInclBorder();
		int board_size_px = getBoardSize();

		m_paint.setColor(BORDER_CLR);
		canvas.drawRect(0, 0, board_size_incl_border, board_size_incl_border, m_paint);

		m_paint.setColor(BLACK_CLR);
		canvas.drawRect(
			BORDER_SIZE_PX,
			BORDER_SIZE_PX, 
			BORDER_SIZE_PX + board_size_px,
			BORDER_SIZE_PX + board_size_px,
			m_paint);

		m_paint.setColor(WHITE_CLR);
		for (int idx_row = 0; idx_row < m_model.ROWS; ++idx_row) {
			for (int idx_col = 0; idx_col < m_model.COLS; ++idx_col) {
				if (m_model.isBlack(idx_row, idx_col))
					continue;
				canvas.drawRect(getRect(idx_row, idx_col, board_size_px), m_paint);
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
	
	private int getCoinPieceColor(BoardModel.Piece piece) {
		return piece == m_model.getSelectedPiece() ?
			SELECTED_COIN_PIECE_CLR :
			COIN_PIECE_CLR;
	}
	
	private void drawCoinPieces(
		Canvas canvas,
		List<BoardModel.Piece> pieces) 
	{
		int board_size = getBoardSize();
		int radius = getCoinPieceRadius(board_size);
		for (BoardModel.Piece piece : pieces) {
			m_paint.setColor(getCoinPieceColor(piece));
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
	private final int BORDER_SIZE_PX = 10;
	
	private final double COIN_PIECE_TO_SQUARE_RATIO = 0.9;
	private final double GUERILLA_PIECE_TO_SQUARE_RATIO = 0.4;
	
	private final int BORDER_CLR = Color.BLUE;
	private final int COIN_PIECE_CLR = Color.DKGRAY;
	private final int SELECTED_COIN_PIECE_CLR = Color.MAGENTA;
	private final int GUERILLA_PIECE_CLR = Color.GREEN;
	private final int WHITE_CLR = Color.RED;
	private final int BLACK_CLR = Color.WHITE;
	/// @}
}
