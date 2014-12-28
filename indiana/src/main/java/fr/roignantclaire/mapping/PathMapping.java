package fr.roignantclaire.mapping;

import java.io.BufferedReader;
import java.util.HashMap;
import java.util.Map;

import org.joda.time.DateTime;
import org.joda.time.DateTimeZone;
import org.joda.time.Period;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;

import fr.roignantclaire.dto.Path;
import fr.roignantclaire.dto.Stopover;

public final class PathMapping {

	private final static int IDX_DEPARTURE_TIME = 0;
	private final static int IDX_DEPARTURE_TOMWN = 1;
	private final static int IDX_ARRIVAL_TOMWN = 2;
	private final static int IDX_TRAVEL_TIME = 3;
	public final static String SEPARATOR = ";";

	private final static DateTimeFormatter DATE_FORMATTER = DateTimeFormat.forPattern("HH:mm");

	private static Path mapping(String[] data){

		String departureTown= data[IDX_DEPARTURE_TOMWN];
		String arrivalTown = data[IDX_ARRIVAL_TOMWN];

		DateTime departureTime = getDate(data[IDX_DEPARTURE_TIME], departureTown);

		DateTime arrivalTime = null;
		if (data[IDX_TRAVEL_TIME] != null  && data[IDX_TRAVEL_TIME].length() == 5){
			Period period = new Period(Integer.parseInt(data[IDX_TRAVEL_TIME].substring(0, 2)),
					Integer.parseInt(data[IDX_TRAVEL_TIME].substring(3, 5)), 0, 0);
			arrivalTime = departureTime.plus(period);
			arrivalTime = arrivalTime.withZone(getDateTimeZone(arrivalTown));
		}

		return new Path(departureTime, departureTown, arrivalTown, arrivalTime);
	}

	private static DateTimeZone getDateTimeZone(String town){
		DateTimeZone zone = null;
		try {
			zone = DateTimeZone.forID("Europe/"+town);
		}
		catch (IllegalArgumentException e){
			zone = DateTimeZone.forID(null);
		}
		return zone;
	}

	public static DateTime getDate(String time, String town){
		DateTimeFormatter formatterZone = DATE_FORMATTER.withZone(getDateTimeZone(town));
		return DateTime.parse(time, formatterZone);
	}

	public static String getFormattedDate(DateTime time){
		return time == null ? "" : time.toString(DATE_FORMATTER);
	}


	public static Map<String, Stopover> getStopovers(BufferedReader reader) throws Exception {

		Map<String, Stopover> stopovers = new HashMap<String, Stopover>();

		String nextLine = null;
		while ((nextLine = reader.readLine()) != null) {
			Path path = mapping(nextLine.split(SEPARATOR));
			String departureTown = path.getDepartureTown();

			Stopover stopover = stopovers.get(departureTown);
			if (stopover == null){
				stopover = new Stopover(departureTown);
				stopovers.put(departureTown, stopover);
			}
			stopover.getNextAvailablePaths().add(path);

			String arrival = path.getArrivalTown();
			if (arrival!=null && !stopovers.containsKey(arrival)){
				stopovers.put(arrival, new Stopover(arrival));
			}
		}

		return stopovers;
	}
}
