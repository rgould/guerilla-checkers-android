package com.CardboardGames;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.view.View;

public class BoardView extends View {
	private Paint paint = new Paint();
	
	private final int rows = 8;
	private final int cols = 8;
	private final int border_size_px = 10;
	
	private final int border_clr = Color.BLUE;
	private final int white_clr = Color.RED;
	private final int black_clr = Color.WHITE;

	public BoardView(Context ctx) {
		super(ctx);
		paint.setColor(Color.BLACK);
	}
	
	private boolean isBlack(int row, int col) {
		return row % 2 != col % 2;
	}
	
	private Rect getRect(int row, int col, int board_length) {
		double rect_width = (double)board_length / cols;
		double rect_height = (double)board_length / rows;
		
		double top = border_size_px + row * rect_height;
		double left = border_size_px + col * rect_width;
		double bottom = top + rect_height;
		double right = left + rect_width;
		
		return new Rect((int)top, (int)left, (int)bottom, (int)right);
	}
	
	@Override
	public void onDraw(Canvas canvas) {
		int board_size_incl_border = Math.min(super.getWidth(), super.getHeight());
		int board_size = board_size_incl_border - 2*border_size_px;
				
		paint.setColor(border_clr);
		canvas.drawRect(0, 0, board_size_incl_border, board_size_incl_border, paint);
		
		paint.setColor(black_clr);
		canvas.drawRect(
			border_size_px,
			border_size_px,
			border_size_px + board_size,
			border_size_px + board_size,
			paint);
		
		paint.setColor(white_clr);
		for (int idx_row = 0; idx_row < rows; ++idx_row) {
			for (int idx_col = 0; idx_col < rows; ++idx_col) {
				if (isBlack(idx_row, idx_col))
					continue;
				canvas.drawRect(getRect(idx_row, idx_col, board_size), paint);
			}
		}
	}
}
