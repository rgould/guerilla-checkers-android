package com.CardboardGames.Activities;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnTouchListener;

import com.CardboardGames.R;
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

		showDialog(DIALOG_CHOOSE_TEAM);
	}

	public boolean onTouch(View view, MotionEvent event) {
		if (event.getActionMasked() != MotionEvent.ACTION_DOWN)
			return false;
		m_controller.addTouch(event.getX(), event.getY());
		return true;
	}

	private Dialog buildTeamChoiceDialog() {
		String team_names[] = getResources().getStringArray(R.array.team_names);
		AlertDialog.Builder builder = new AlertDialog.Builder(this);
		builder.setTitle(R.string.dialog_choose_team);
		builder.setItems(team_names, m_chooseTeamHandler);
		return builder.create();
	}

	@Override
	protected Dialog onCreateDialog(int id) {
	    switch(id) {
	    case DIALOG_CHOOSE_TEAM:
	    	return buildTeamChoiceDialog();
	    default:
	        return super.onCreateDialog(id);
	    }
	}

	/// EVENT HANDLERS

	private static final DialogInterface.OnClickListener m_chooseTeamHandler =
		new DialogInterface.OnClickListener() {
			public void onClick(DialogInterface dialog, int idx_team) {
				switch (idx_team) {
				case IDX_COIN: // TODO: implement
					break;
				case IDX_GUERILLA: // TODO: implement
					break;
				default:
					assert(false);
				}
			}
		};

	/// PRIVATE MEMBERS

	private static final int DIALOG_CHOOSE_TEAM = 0;
	private static final int IDX_COIN = 0;
	private static final int IDX_GUERILLA = 1;

	GameController m_controller = null;
	BoardModel m_model = null;
	BoardView m_view = null;
}
