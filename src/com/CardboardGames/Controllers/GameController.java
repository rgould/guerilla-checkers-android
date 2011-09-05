package com.CardboardGames.Controllers;

import android.graphics.Point;

import com.CardboardGames.Models.BoardModel;
import com.CardboardGames.Views.BoardView;

public class GameController
{
	/// PUBLIC METHODS

	public GameController(BoardModel model, BoardView view) {
		m_model = model;
		m_view = view;
	}

	public void handleCoinInput(float viewx, float viewy) {
		Point board_coords = m_view.getCoinBoardCoords(viewx, viewy);
		BoardModel.Piece old_piece = m_model.getSelectedCoinPiece();
		if (m_model.hasSelectedCoinPiece()) {
			if (m_model.isValidCoinMove(old_piece, board_coords)) {
				m_model.moveSelectedCoinPiece(board_coords);
				m_model.deselectCoinPiece();
				m_view.invalidate();
				moveToNextState();
				return;
			}
		}

		m_model.selectCoinPieceAt(board_coords);
		if (old_piece != m_model.getSelectedCoinPiece())
			m_view.invalidate();
	}

	public void handleGuerillaInput(float viewx, float viewy) {
		Point board_coords = m_view.getGuerillaBoardCoords(viewx, viewy);
		if (!m_model.isValidGuerillaPlacement(board_coords))
			return;

		m_model.placeGuerillaPiece(board_coords);
		m_view.invalidate();
		moveToNextState();
	}

	public void moveToNextState() {
		if (m_model.isGameOver()) {
			m_state = GameState.END_GAME;
			return;
		}

		switch (m_state) {
		case GUERILLA_SETUP_FIRST:
			m_state = GameState.GUERILLA_SETUP_SECOND;
			return;
		case GUERILLA_SETUP_SECOND:
			m_state = GameState.COIN_MOVE;
			return;
		case COIN_MOVE:
			m_state = GameState.GUERILLA_MOVE_FIRST;
			return;
		case GUERILLA_MOVE_FIRST:
			m_state = GameState.GUERILLA_MOVE_SECOND;
			return;
		case GUERILLA_MOVE_SECOND:
			m_state = GameState.COIN_MOVE;
			return;
		case END_GAME:
			break;
		}
	}

	public void addTouch(float viewx, float viewy) {
		switch (m_state) {
		case GUERILLA_SETUP_FIRST:
		case GUERILLA_SETUP_SECOND:
		case GUERILLA_MOVE_FIRST:
		case GUERILLA_MOVE_SECOND:
			handleGuerillaInput(viewx, viewy);
			break;
		case COIN_MOVE:
			handleCoinInput(viewx, viewy);
			break;
		case END_GAME:
			break;
		}
	}

	/// PRIVATE TYPES

	enum GameState {
		GUERILLA_SETUP_FIRST,
		GUERILLA_SETUP_SECOND,
		COIN_MOVE,
		GUERILLA_MOVE_FIRST,
		GUERILLA_MOVE_SECOND,
		END_GAME
	}

	/// PRIVATE MEMBERS

	GameState m_state = GameState.GUERILLA_SETUP_FIRST;
	BoardModel m_model = null;
	BoardView m_view = null;
}
