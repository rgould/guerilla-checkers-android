package com.CardboardGames.GuerillaCheckers.Core.Actions;

import java.util.ArrayList;

import com.CardboardGames.GuerillaCheckers.Core.BoardState;

public abstract class Action {

	private ArrayList<Action> m_sideEffects;
	
	protected Action() {
		m_sideEffects = new ArrayList<Action>();
	}
	
	public Action(ArrayList<Action> sideEffects) {
		m_sideEffects = sideEffects;
	}
	
	public ArrayList<Action> getSideEffects() {
		return m_sideEffects;
	}
	
	protected abstract void doExecute(BoardState boardState);
	protected abstract void doUndo(BoardState boardState);
	
	public void execute(BoardState boardState) {
		doExecute(boardState);
		for (Action action : m_sideEffects) {
			action.execute(boardState);
		}
	}
	
	public void undo(BoardState boardState) {
		for (int idx = m_sideEffects.size()-1; idx >= 0; --idx) {
			m_sideEffects.get(idx).undo(boardState);
		}
		doUndo(boardState);
	}
}
