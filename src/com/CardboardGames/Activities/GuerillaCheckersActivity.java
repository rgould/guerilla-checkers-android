package com.CardboardGames.Activities;

import android.app.Activity;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnTouchListener;
import android.widget.TextView;

import com.CardboardGames.Controllers.GameController;
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
		m_controller = new GameController(m_model, m_view);
	}

	public boolean onTouch(View view, MotionEvent event) {
		if (event.getActionMasked() != MotionEvent.ACTION_DOWN)
			return false;

		float viewx = event.getX();
		float viewy = event.getY();
		m_controller.addTouch(viewx, viewy);

		if (m_model.isGameOver()) {
			TextView tv = new TextView(this);
			tv.setText("GAME OVER!");
			setContentView(tv);
		}
		return true;
	}

	/// PRIVATE MEMBERS

	GameController m_controller = null;
	BoardModel m_model = null;
	BoardView m_view = null;
}
