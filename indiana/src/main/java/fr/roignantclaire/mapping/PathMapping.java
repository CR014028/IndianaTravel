package fr.roignantclaire.mapping;

import java.io.BufferedReader;
import java.util.HashMap;
import java.util.Map;

import org.joda.time.DateTime;
import org.joda.time.Period;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;

import fr.roignantclaire.dto.Path;
import fr.roignantclaire.dto.Stopover;

public final class PathMapping {

	private final static int IDX_DEPARTURE_TIME = 0;
	private final static int IDX_DEPARTURE_TOWN = 1;
	private final static int IDX_ARRIVAL_TOWN = 2;
	private final static int IDX_TRAVEL_TIME = 3;
	public final static String SEPARATOR = ";";

	private final static DateTimeFormatter DATE_FORMATTER = DateTimeFormat.forPattern("HH:mm");

	private final static Path mapping(final String[] data){

		DateTime departureTime = getDate(data[IDX_DEPARTURE_TIME]);
		DateTime arrivalTime = null;
		if (data[IDX_TRAVEL_TIME] != null  && data[IDX_TRAVEL_TIME].length() == 5){
			Period period = new Period(Integer.parseInt(data[IDX_TRAVEL_TIME].substring(0, 2)),
					Integer.parseInt(data[IDX_TRAVEL_TIME].substring(3, 5)), 0, 0);
			arrivalTime = departureTime.plus(period);
		}

		return new Path(departureTime, data[IDX_DEPARTURE_TOWN], data[IDX_ARRIVAL_TOWN], arrivalTime);
	}

	public final static DateTime getDate(final String time){
		return DateTime.parse(time, DATE_FORMATTER);
	}

	public final static String getFormattedDate(final DateTime time){
		return time == null ? "" : time.toString(DATE_FORMATTER);
	}


	public static Map<String, Stopover> getStopovers(BufferedReader reader) throws Exception {

		Map<String, Stopover> stopovers = new HashMap<String, Stopover>();

		String nextLine = "";
		String arrival = "";
		Stopover stopover = null;
		Path path = null;
		String departureTown = "";
		
		while ((nextLine = reader.readLine()) != null) {
			path = mapping(nextLine.split(SEPARATOR));
			departureTown = path.getDepartureTown();

			stopover = stopovers.get(departureTown);
			if (stopover == null){
				stopover = new Stopover(departureTown);
				stopovers.put(departureTown, stopover);
			}
			stopover.getNextPaths().add(path);

			arrival = path.getArrivalTown();
			if (arrival!=null && !stopovers.containsKey(arrival)){
				stopovers.put(arrival, new Stopover(arrival));
			}
		}

		return stopovers;
	}
}
