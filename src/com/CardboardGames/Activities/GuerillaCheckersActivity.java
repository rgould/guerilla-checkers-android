package com.CardboardGames.Activities;

import android.app.Activity;
import android.graphics.Point;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnTouchListener;

import com.CardboardGames.Models.BoardModel;
import com.CardboardGames.Views.BoardView;

public class GuerillaCheckersActivity 
	extends Activity 
	implements OnTouchListener
{
	
	/// PUBLIC METHODS
	
	public GuerillaCheckersActivity() {
		m_model = new BoardModel();
	}
	
	/** Called when the activity is first created. */
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		m_view = new BoardView(this, m_model);
		m_view.setOnTouchListener(this);
		setContentView(m_view);
	}
	
	public boolean onTouch(View view, MotionEvent event) {
		if (!(view instanceof BoardView))
			return false;
		
		if (event.getActionMasked() != MotionEvent.ACTION_DOWN)
			return false;
		
		BoardView board_view = (BoardView)view;
		float screenx = event.getRawX();
		float screeny = event.getRawY();
		Point board_coords = board_view.getBoardCoords(screenx, screeny);
		
		BoardModel.Piece old_selected_piece = m_model.getSelectedPiece();
		m_model.addTouch(board_coords);
		if (m_model.getSelectedPiece() != old_selected_piece)
			view.invalidate();
		
		setTitle(board_coords.toString());
		return true;
	}
	
	/// PRIVATE MEMBERS
	
	BoardModel m_model = null;
	BoardView m_view = null;
}
