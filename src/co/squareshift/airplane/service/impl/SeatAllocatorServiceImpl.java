package co.squareshift.airplane.service.impl;

import java.security.InvalidParameterException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;

import co.squareshift.airplane.comparator.SortByGridRowAndColumn;
import co.squareshift.airplane.dto.Seat;
import co.squareshift.airplane.dto.SeatTypeEnum;
import co.squareshift.airplane.service.SeatAllocatorService;

public class SeatAllocatorServiceImpl implements SeatAllocatorService{
	
	private static final HashMap<SeatTypeEnum, List<String>> seatMapping = new HashMap<>();

	/**
	 * Initialize the Seat Map and book tickets as per the seating order
	 * @param seatLayout
	 * @param noOfPassengers
	 * @return Seat[][][]
	 */
	@Override
	public Seat[][][] bookSeats(int[][] seatLayout, int noOfPassengers) throws IllegalArgumentException {		
		Seat[][][] seats = initializeSeats(seatLayout, seatMapping);
		bookSeatsBySeatType(seats, noOfPassengers, seatMapping);
		return seats;	
	}

	/**
	 * Print the Seat Layout in Console
	 * @param seatLayout
	 * @param seats
	 */
	@Override
	public void printValues(int[][] seatLayout, Seat[][][] seats) {		
		List<List<String>> rowString = new ArrayList<>();
		int maxRows = 0;
		for(int i = 0; i < seatLayout.length; i++) {
			maxRows = createSeatLayoutStringAndGetMaxRow(seatLayout, seats, rowString, maxRows, i);
		}
		for(int i = 0; i < maxRows; i++) {
			printRowWiseSeatAllocation(rowString, i);
		}
	}
	/**
	 * Append all the layout in row wise string.
	 * if a particular seat lay out does not have seating in current row, we are filling with empty string
	 * @param rowString
	 * @param row
	 */
	private void printRowWiseSeatAllocation(List<List<String>> rowString, int row) {
		StringBuilder sb = new StringBuilder();
		for(List<String> rows : rowString) {
			try {
				sb.append(rows.get(row));
			} catch (IndexOutOfBoundsException e) {
				String[] str = rows.get(0).split("\\|");
				for(int j = 0; j < str.length; j++)
					sb.append("\t");
			}
		}
		System.out.println(sb.toString());
	}
	/**
	 * Create Seat Layout string by traversing all rows & columns in the grid
	 * @param seatLayout
	 * @param seats
	 * @param rowString
	 * @param maxRows
	 * @param gridNumber
	 * @return
	 */
	private int createSeatLayoutStringAndGetMaxRow(int[][] seatLayout, Seat[][][] seats, List<List<String>> rowString, int maxRows,
			int gridNumber) {
		List<String> currentGridRow = new ArrayList<String>();
		int rows = seatLayout[gridNumber][1];
		int columns = seatLayout[gridNumber][0];
		if(maxRows < rows) {
			maxRows = rows;
		}
		for(int row = 0; row < rows; row ++) {
			StringBuilder sb = new StringBuilder();
			for(int column = 0; column < columns; column++) {
				sb.append(seats[gridNumber][row][column]+"|\t");
			}
			currentGridRow.add(sb.toString()+"\t");
		}
		rowString.add(currentGridRow);
		return maxRows;
	}
	/**
	 * Initialize seat layout in array format with empty seat Object
	 * @param seatLayout
	 * @param seatMapping
	 * @return
	 * @throws InvalidParameterException
	 */
	private static Seat[][][] initializeSeats(int[][] seatLayout, HashMap<SeatTypeEnum, List<String>> seatMapping) throws InvalidParameterException{
		Seat[][][] seats = new Seat[seatLayout.length][][];
		for(int i = 0; i < seatLayout.length; i++) {			
			int rows = seatLayout[i][1];
			int columns = seatLayout[i][0];
			if(rows < 0 || columns < 0) {
				throw new IllegalArgumentException(" Invalid seat layout");
			}
			
			seats[i] = new Seat[rows][columns];			
			for(int row = 0; row < rows; row ++) {
				for(int column = 0; column < columns; column++) {
					assignEmptySeatWithSeatType(seatLayout, seatMapping, seats, i, columns, row, column);
				}
			}
		}
		return seats;
	}
	/**
	 * Assign Empty Seat Object in the seat array
	 * 
	 * @param seatLayout
	 * @param seatMapping
	 * @param seats
	 * @param gridNumber
	 * @param columns
	 * @param row
	 * @param column
	 */
	private static void assignEmptySeatWithSeatType(int[][] seatLayout, HashMap<SeatTypeEnum, List<String>> seatMapping,
			Seat[][][] seats, int gridNumber, int columns, int row, int column) {
		Seat seat = new Seat();
		seat.setGridNumber(gridNumber);
		seat.setRow(row);
		seat.setColumn(column);
		SeatTypeEnum type;
		if( (gridNumber ==0 && column == 0) || (gridNumber == seatLayout.length-1 && column == columns-1)) {
			type = SeatTypeEnum.Window;						
		} else if(column == 0 || column == columns-1) {
			type = SeatTypeEnum.Aisle;
		} else {
			type = SeatTypeEnum.Middle;
		}					
		seat.setSeatType(type);		
		seats[gridNumber][row][column] = seat;
		addSeatTypeInMapping(seatMapping, gridNumber, row, column, type);
	}
	/**
	 * Add Seat Type in HashMap, so that we can re-use the map for getting list of array position with respect to seat types ( Aisle, Window, Middle)
	 * 
	 * @param seatMapping
	 * @param gridNumber
	 * @param row
	 * @param column
	 * @param type
	 */
	private static void addSeatTypeInMapping(HashMap<SeatTypeEnum, List<String>> seatMapping, int gridNumber, int row,
			int column, SeatTypeEnum type) {
		List<String> mappingIndex = seatMapping.get(type);
		if(mappingIndex == null) {
			mappingIndex = new ArrayList<String>();
		}
		mappingIndex.add(gridNumber+"_"+row+"_"+column);
		seatMapping.put(type, mappingIndex);
	}
	/**
	 * Ticket Booking based on the Seat Order Aisle, Window & Middle.
	 * @param seats
	 * @param noOfPassengers
	 * @param seatMapping
	 */
	private static void bookSeatsBySeatType(Seat[][][] seats, int noOfPassengers, HashMap<SeatTypeEnum, List<String>> seatMapping) {
		int bookedSeatsCount = 0;
		bookedSeatsCount = bookSeats(seats, noOfPassengers, seatMapping.get(SeatTypeEnum.Aisle), bookedSeatsCount);
		if(noOfPassengers > bookedSeatsCount) {
			bookedSeatsCount = bookSeats(seats, noOfPassengers, seatMapping.get(SeatTypeEnum.Window), bookedSeatsCount);
			if(noOfPassengers > bookedSeatsCount) {
				bookedSeatsCount = bookSeats(seats, noOfPassengers, seatMapping.get(SeatTypeEnum.Middle), bookedSeatsCount);
			}
		}
	}

	/**
	 * Assign customer id for the given available seats
	 * @param seats
	 * @param noOfPassengers
	 * @param seatTypeIndexes
	 * @param bookedSeatsCount
	 * @return
	 */
	private static int bookSeats(Seat[][][] seats, int noOfPassengers, List<String> seatTypeIndexes, int bookedSeatsCount) {
		Collections.sort(seatTypeIndexes, new SortByGridRowAndColumn());
		for(String index : seatTypeIndexes) {
			if(bookedSeatsCount < noOfPassengers) {
				String[] indexes = index.split("_");
				Seat seat = seats[Integer.valueOf(indexes[0])][Integer.valueOf(indexes[1])][Integer.valueOf(indexes[2])];
				seat.setCustomerId(""+(1+bookedSeatsCount++));
			} else {
				return bookedSeatsCount;
			}
		}
		return bookedSeatsCount;
	}
}
