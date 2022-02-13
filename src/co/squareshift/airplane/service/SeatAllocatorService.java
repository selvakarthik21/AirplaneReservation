package co.squareshift.airplane.service;

import co.squareshift.airplane.dto.Seat;

public interface SeatAllocatorService {
	/**
	 * Initialize the Seat Map and book tickets as per the seating order
	 * @param seatLayout
	 * @param noOfPassengers
	 * @return
	 */
	Seat[][][] bookSeats(int[][] seatLayout, int noOfPassengers) throws IllegalArgumentException;
	/**
	 * Print the Seat Layout in Console
	 * @param seatLayout
	 * @param seats
	 */
	void printValues(int[][] seatLayout, Seat[][][] seats);
}
