package com.CardboardGames.Activities;


import android.app.Application;

import com.CardboardGames.Controllers.GameController;
import com.CardboardGames.Models.BoardModel;
import com.CardboardGames.Views.BoardView;

public class GuerillaCheckersApplication extends Application
{
	private static GuerillaCheckersApplication instance;

	public GuerillaCheckersApplication() {
		instance = this;
	}

	@Override
	public void onCreate() {
		super.onCreate();
		m_model = new BoardModel();
		m_view = new BoardView(this, m_model);
		m_controller = new GameController(m_model, m_view);
	}

	public static GuerillaCheckersApplication getInstance() {
		return instance;
	}

	public BoardModel getModel() { return m_model; }
	public BoardView getView() { return m_view; }
	public GameController getController() { return m_controller; }

	private BoardModel m_model = null;
	private BoardView m_view = null;
	private GameController m_controller = null;
}
