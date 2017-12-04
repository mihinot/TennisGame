package com.rizen.katas.tennisgame.beans;

import java.util.ArrayList;
import java.util.List;

/**
 * A player take part of Tennis party game
 * 
 * @author Mihinot
 *
 */
public class Player {

	private List<Integer> pointsList;

	public Player() {
		pointsList = new ArrayList<>();
	}

	/**
	 * @return the pointsList
	 */
	public List<Integer> getPointsList() {
		return pointsList;
	}

	/**
	 * @param pointsList
	 *            the pointsList to set
	 */
	public void setPointsList(List<Integer> pointsList) {
		this.pointsList = pointsList;
	}

	/**
	 * add point for new set turn
	 * 
	 * @param point
	 */
	public void addPoint(int point) {
		this.pointsList.add(point);
	}

	@Override
	public String toString() {
		return pointsList.toString();
	}
}
