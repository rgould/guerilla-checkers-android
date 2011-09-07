package com.CardboardGames.Activities;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
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

		showDialog(DIALOG_CHOOSE_TEAM_ID);
	}

	public boolean onTouch(View view, MotionEvent event) {
		if (event.getActionMasked() != MotionEvent.ACTION_DOWN)
			return false;
		m_controller.addTouch(event.getX(), event.getY());
		return true;
	}

	@Override
	protected Dialog onCreateDialog(int id) {
	    Dialog dialog;
	    switch(id) {
	    case DIALOG_CHOOSE_TEAM_ID:
	    	final CharSequence[] items = {"Counterinsurgents", "Guerrillas"};

			AlertDialog.Builder builder = new AlertDialog.Builder(this);
			builder.setTitle("Choose your preferred team:");
			builder.setItems(items, new DialogInterface.OnClickListener() {
			    public void onClick(DialogInterface dialog, int item) {
			    	if (items[item] == items[0]) {
			    		//counterinsurgents
			    	} else if (items[item] == items[1]) {
			    		//guerrillas
			    	} else {
			    		// WTF
			    	}
			    }
			});
			AlertDialog alert = builder.create();
	    	dialog = alert;
	        break;
	    default:
	        dialog = super.onCreateDialog(id);
	    }
	    return dialog;

	}


	/// PRIVATE MEMBERS

	private static final int DIALOG_CHOOSE_TEAM_ID = 0;

	GameController m_controller = null;
	BoardModel m_model = null;
	BoardView m_view = null;
}
