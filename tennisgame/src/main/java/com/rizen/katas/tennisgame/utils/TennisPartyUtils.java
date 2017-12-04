package com.rizen.katas.tennisgame.utils;

/**
 * Contains all constants needed for a 2 players Tennis party.
 * 
 * @author Mihinot
 *
 */
public class TennisPartyUtils {
	// the number of players
	public static final byte NB_PLAYERS = 2;

	// possible points for each Game (1:GAME ADVANTAGE | 2:DEUCE)
	public static final int[] GAME_POSSIBLE_POINTS = { 0, 15, 30, 40, 1, 2 };

	// Params for processing each Game (1:ADVANTAGE | 2:DEUCE | 3:GAME WIN)
	public static final int ADV = 1, ADV_DEUCE = 2, END = 3;

	// Maximum point that a player can score during a party
	public static final int MAX_POINT = 40;

	// minimum difference of points before winning a party
	public static final int MIN_SET_WINNING_POINTS = 2;

	// number of points for both player before announcing a tie break
	public static final int TIE_BREAK = 6;

}
