package com.CardboardGames.Activities;

import android.app.Activity;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnTouchListener;

import com.CardboardGames.Controllers.GameController;
import com.CardboardGames.Models.BoardModel;
import com.CardboardGames.Views.BoardView;

public class GuerillaCheckersActivity extends Activity
	implements OnTouchListener
{
	/// PUBLIC METHODS

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		GuerillaCheckersApplication app =
			GuerillaCheckersApplication.getInstance();

		m_model = app.getModel();
		m_view = new BoardView(this, m_model);
		m_view.setOnTouchListener(this);
		m_controller = app.getController();
		m_controller.setView(m_view);
		setContentView(m_view);
	}

	public boolean onTouch(View view, MotionEvent event) {
		if (event.getActionMasked() != MotionEvent.ACTION_DOWN)
			return false;
		m_controller.addTouch(event.getX(), event.getY());
		return true;
	}


	/// PRIVATE MEMBERS

	GameController m_controller = null;
	BoardModel m_model = null;
	BoardView m_view = null;
}
