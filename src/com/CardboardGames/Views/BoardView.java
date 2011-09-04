package com.CardboardGames.Views;

import java.util.List;

import android.content.Context;
import android.graphics.Canvas;
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

	private Rect getRect(int row, int col, Point board_pos, int board_size) {
		double rect_width = (double)board_size / m_model.COLS;
		double rect_height = (double)board_size / m_model.ROWS;

		double left = board_pos.x + BORDER_SIZE_PX + col * rect_width;
		double top = board_pos.y + BORDER_SIZE_PX + row * rect_height;
		double right = left + rect_width;
		double bottom = top + rect_height;

		return new Rect((int)left, (int)top, (int)right, (int)bottom);
	}

	private int getRectSize() {
		return (int)((double)getBoardSize() / m_model.COLS);
	}

	public Point getBoardCoords(final float screenx, final float screeny) {
		float rect_size = getRectSize();
		Point board_pos = getBoardScreenPosition();
		int xoffset = board_pos.x + BORDER_SIZE_PX;
		int yoffset = board_pos.y + BORDER_SIZE_PX;
		int xcoord = (int)((screenx - xoffset) / rect_size);
		int ycoord = (int)((screeny - yoffset) / rect_size);
		return new Point(xcoord, ycoord);
	}

	private int getBoardSizeInclBorder() {
		return Math.min(getWidth(), getHeight());
	}

	private int getBoardSize() {
		return getBoardSizeInclBorder() - 2*BORDER_SIZE_PX;
	}

	private Point getBoardScreenPosition() {
		int screen_pos[] = new int[2];
		getLocationOnScreen(screen_pos);
		Point board_pos = getBoardPosition();
		board_pos.x += screen_pos[0];
		board_pos.y += screen_pos[1];
		return board_pos;
	}

	private Point getBoardPosition() {
		int board_size = getBoardSizeInclBorder();
		int offsetx = (getWidth() - board_size) / 2;
		int offsety = (getHeight() - board_size) / 2;
		return new Point(offsetx, offsety);
	}

	private void drawBoard(Canvas canvas) {
		Point board_pos = getBoardPosition();
		int board_size_incl_border = getBoardSizeInclBorder();
		int board_size_px = getBoardSize();

		m_paint.setColor(BORDER_CLR);
		canvas.drawRect(
			board_pos.x,
			board_pos.y,
			board_pos.x + board_size_incl_border,
			board_pos.y + board_size_incl_border,
			m_paint);

		m_paint.setColor(BLACK_CLR);
		canvas.drawRect(
			board_pos.x + BORDER_SIZE_PX,
			board_pos.y + BORDER_SIZE_PX, 
			board_pos.x + BORDER_SIZE_PX + board_size_px,
			board_pos.y + BORDER_SIZE_PX + board_size_px,
			m_paint);

		m_paint.setColor(WHITE_CLR);
		for (int idx_row = 0; idx_row < m_model.ROWS; ++idx_row) {
			for (int idx_col = 0; idx_col < m_model.COLS; ++idx_col) {
				if (m_model.isBlack(idx_row, idx_col))
					continue;

				Rect rect = getRect(idx_row, idx_col, board_pos, board_size_px);
				canvas.drawRect(rect, m_paint);
			}
		}
	}	

	private void drawPiece(Canvas canvas, int cx, int cy, int radius) {
		canvas.drawCircle(cx, cy, radius, m_paint);
	}

	private int getCoinPieceRadius(int board_size) {
		return (int)(0.5 * getRectSize() * COIN_PIECE_TO_SQUARE_RATIO);
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
		Point board_pos = getBoardPosition();
		int radius = getCoinPieceRadius(board_size);

		for (BoardModel.Piece piece : pieces) {
			m_paint.setColor(getCoinPieceColor(piece));
			Point pos = piece.getPosition();
			Rect r = getRect(pos.y, pos.x, board_pos, board_size);
			drawPiece(canvas, r.centerX(), r.centerY(), radius);
		}
	}

	private void drawPotentialMoves(Canvas canvas) {
		BoardModel.Piece piece = m_model.getSelectedPiece();
		if (piece == null)
			return;

		Point board_pos = getBoardPosition();
		int board_size = getBoardSize();
		int radius = getCoinPieceRadius(board_size);
		m_paint.setColor(POTENTIAL_COIN_MOVE_CLR);
		for (int idx_col = 0; idx_col < m_model.COLS; ++idx_col) {
			for (int idx_row = 0; idx_row < m_model.ROWS; ++idx_row) {
				if (!m_model.isValidMove(piece, idx_col, idx_row))
					continue;
				Rect r = getRect(idx_row, idx_col, board_pos, board_size);
				drawPiece(canvas, r.centerX(), r.centerY(), radius);
			}
		}
	}

	private int getGuerillaPieceRadius(int board_size) {
		return (int)(0.5 * getRectSize() * GUERILLA_PIECE_TO_SQUARE_RATIO);
	}

	private void drawGuerillaPieces(
		Canvas canvas,
		List<BoardModel.Piece> pieces) 
	{
		m_paint.setColor(GUERILLA_PIECE_CLR);
		int board_size = getBoardSize();
		Point board_pos = getBoardPosition();
		int radius = getGuerillaPieceRadius(board_size);
		for (BoardModel.Piece piece : pieces) {
			Point pos = piece.getPosition();
			Rect rect = getRect(pos.y, pos.x, board_pos, board_size);
			drawPiece(canvas, rect.right, rect.bottom, radius);
		}
	}

	private void drawPieces(Canvas canvas) {
		drawCoinPieces(canvas, m_model.getCoinPieces());
		drawGuerillaPieces(canvas, m_model.getGuerillaPieces());
		if (m_model.getSelectedPiece() != null)
			drawPotentialMoves(canvas);
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
	/// @}

	/// @{
	/// Board Colours (32 bit ARGB format)
	private final int BORDER_CLR              = 0xFFA66000;
	private final int COIN_PIECE_CLR          = 0xFF7B9E00;
	private final int SELECTED_COIN_PIECE_CLR = 0xFF00AA72;
	private final int POTENTIAL_COIN_MOVE_CLR = // set alpha directly
		(SELECTED_COIN_PIECE_CLR & 0x00FFFFFF) | 0x66000000;
	private final int GUERILLA_PIECE_CLR      = 0xFF222222;
	private final int WHITE_CLR               = 0xFF9B7D27;
	private final int BLACK_CLR               = 0xFFC1A657;
	/// @}
}
