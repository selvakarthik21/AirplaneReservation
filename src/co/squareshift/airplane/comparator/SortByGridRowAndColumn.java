package co.squareshift.airplane.comparator;

import java.util.Comparator;

public class SortByGridRowAndColumn implements Comparator<String> {

	@Override
	public int compare(String s1, String s2) {
		//Sample s1 = 0_0_0 refers gridNumber_row_column
		String[] s1IndexMapping = s1.split("_");
		String[] s2IndexMapping = s2.split("_");
		if(Integer.valueOf(s1IndexMapping[1]) == Integer.valueOf(s2IndexMapping[1])) { //if row number equals then sort by grid & column
			if(Integer.valueOf(s1IndexMapping[0]) == Integer.valueOf(s2IndexMapping[0])) { // check grid number
				return Integer.valueOf(s1IndexMapping[2]) - Integer.valueOf(s2IndexMapping[2]);
			}
			return Integer.valueOf(s1IndexMapping[0]) - Integer.valueOf(s2IndexMapping[0]);
		}
		return Integer.valueOf(s1IndexMapping[1]) - Integer.valueOf(s2IndexMapping[1]); //Sort by Row Number
	}

}
