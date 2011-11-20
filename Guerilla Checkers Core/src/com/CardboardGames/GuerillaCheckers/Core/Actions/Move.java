package com.CardboardGames.GuerillaCheckers.Core.Actions;

import java.util.ArrayList;

import com.CardboardGames.Core.Math.Point2I;
import com.CardboardGames.GuerillaCheckers.Core.BoardState;

public class Move extends Action {
	
	private Point2I m_from;
	private Point2I m_to;
	
	void init(Point2I from, Point2I to) {
		assert(from != null);
		assert(to != null);
		m_from = from;
		m_to = to;
	}
	
	public Move(Point2I from, Point2I to, ArrayList<Action> sideEffects) {
		super(sideEffects);
		init(from, to);
	}
	
	public Move(Point2I from, Point2I to) {
		init(from, to);
	}
	
	public Point2I getFromPosition() { return m_from; }
	public Point2I getToPosition() { return m_to; }
	
	private void move(BoardState boardState, Point2I from, Point2I to) {
		assert(boardState.coinPieceExistsAt(from));
		assert(!boardState.coinPieceExistsAt(to));
		
		boardState.removeCoinPieceAt(from);
		boardState.addCoinPieceAt(to);
		
		assert(boardState.coinPieceExistsAt(to));
		assert(!boardState.coinPieceExistsAt(from));
	}
	
	@Override
	public void doExecute(BoardState boardState) {
		move(boardState, m_from, m_to);
	}
	
	@Override
	public void doUndo(BoardState boardState) {
		move(boardState, m_to, m_from);
	}
}


//go to dungeon
//  reveal hand
//  choose monster
//  choose card
//    select target
//  choose card?
//    select target...
//  ....
//  kill monster
//  spoils