package co.squareshift.airplane.dto;

public class Seat {
	private int gridNumber;
	private int row;
	private int column;
	private SeatTypeEnum seatType;
	private String customerId;	
	public int getGridNumber() {
		return gridNumber;
	}
	public void setGridNumber(int gridNumber) {
		this.gridNumber = gridNumber;
	}
	public int getRow() {
		return row;
	}
	public void setRow(int row) {
		this.row = row;
	}
	public int getColumn() {
		return column;
	}
	public void setColumn(int column) {
		this.column = column;
	}
	public SeatTypeEnum getSeatType() {
		return seatType;
	}
	public void setSeatType(SeatTypeEnum seatType) {
		this.seatType = seatType;
	}
	public String getCustomerId() {
		return customerId;
	}
	public void setCustomerId(String customerId) {
		this.customerId = customerId;
	}
	
	@Override
	public String toString() {
		if(null != customerId) {
			return customerId+"_"+seatType.toString().charAt(0);
		}
		return "  _"+seatType.toString().charAt(0);
	}
	
}
