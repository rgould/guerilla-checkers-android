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

	public void handleCoinInput(float screenx, float screeny) {
		Point board_coords = m_view.getCoinBoardCoords(screenx, screeny);
		if (m_model.hasSelectedCoinPiece()) {
			BoardModel.Piece piece = m_model.getSelectedCoinPiece();
			if (m_model.isValidMove(piece, board_coords)) {
				m_model.moveSelectedCoinPiece(board_coords);
				m_model.deselectCoinPiece();
				m_view.invalidate();
				m_state = GameState.GUERILLA_TURN;
				return;
			}
		}

		if (m_model.selectCoinPieceAt(board_coords))
			m_view.invalidate();
	}

	public void handleGuerillaSetupInput(float screenx, float screeny) {
		Point board_coords = m_view.getGuerillaBoardCoords(screenx, screeny);
		if (!m_model.isValidGuerillaSetupPlacement(board_coords))
			return;

		m_model.placeGuerillaPiece(board_coords);
		m_view.invalidate();
		if (m_model.getNumGuerillaPieces() > 1)
			m_state = GameState.COIN_TURN;
	}

	public void handleGuerillaInput(float screenx, float screeny) {
		Point board_coords = m_view.getGuerillaBoardCoords(screenx, screeny);
		if (!m_model.isValidGuerillaPlacement(board_coords))
			return;

		m_model.placeGuerillaPiece(board_coords);
		m_view.invalidate();

		m_state = GameState.COIN_TURN;
	}

	public void addTouch(float screenx, float screeny) {
		switch (m_state) {
		case GUERILLA_SETUP:
			handleGuerillaSetupInput(screenx, screeny);
			break;
		case GUERILLA_TURN:
			handleGuerillaInput(screenx, screeny);
			break;
		case COIN_TURN:
			handleCoinInput(screenx, screeny);
			break;
		case END_GAME:
			break;
		default:
			assert(false);
			break;
		}

	}

	/// PRIVATE TYPES

	enum GameState {
		GUERILLA_SETUP,
		COIN_TURN,
		GUERILLA_TURN,
		END_GAME
	}

	/// PRIVATE MEMBERS

	GameState m_state = GameState.GUERILLA_SETUP;
	BoardModel m_model = null;
	BoardView m_view = null;
}
