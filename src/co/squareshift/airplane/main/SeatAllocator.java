package co.squareshift.airplane.main;

import co.squareshift.airplane.dto.Seat;
import co.squareshift.airplane.service.SeatAllocatorService;
import co.squareshift.airplane.service.impl.SeatAllocatorServiceImpl;

public class SeatAllocator {
	
	public static void  main(String[] args) {
		int[][] seatLayout = {	
				{3,4},	
				{4,5},	
				{2,3},	
				{3,4}
				};
		int noOfPassengers = 30;
		SeatAllocatorService service = new SeatAllocatorServiceImpl();
		Seat[][][] seats = service.bookSeats(seatLayout, noOfPassengers);
		service.printValues(seatLayout, seats );
	}
	
}
