package co.squareshift.airplane.service;



import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.Test;

import co.squareshift.airplane.dto.Seat;
import co.squareshift.airplane.service.impl.SeatAllocatorServiceImpl;


class SeatAllocatorServiceTest {

	SeatAllocatorService service = new SeatAllocatorServiceImpl();
	@Test
	void bookSeatsTest() {
		int[][] seatLayout = {	
				{3,4},	
				{4,5},	
				{2,3},	
				{3,4}
				};
		int noOfPassengers = 30;
		Seat[][][] seats = service.bookSeats(seatLayout, noOfPassengers);
		assertTrue("25_W".equalsIgnoreCase(seats[0][0][0].toString()));
		assertTrue("1_A".equalsIgnoreCase(seats[0][0][2].toString()));
	}
	@Test
	void bookSeatsTestException() {
		int[][] seatLayout = {	
				{3,-1},	
				{4,3},	
				{2,3},	
				{3,4}
				};
		int noOfPassengers = 30;
		boolean isExceptionThrown = false;
		try{
			service.bookSeats(seatLayout, noOfPassengers);
		}catch (Exception e) {
			isExceptionThrown = true;
		}
		assertTrue(isExceptionThrown);
	}
}
