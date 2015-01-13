package fr.roignantclaire.operations;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.joda.time.DateTime;

import fr.roignantclaire.dto.Path;
import fr.roignantclaire.dto.Stopover;

public final class Dijkstra {

	/**
	 * @param allStopovers
	 * @param departureTown
	 * @param departureTime
	 * @param destination
	 * @return
	 * @throws Exception
	 */
	public final static List<Stopover> computePath(final Map<String, Stopover> allStopovers, final String departureTown, final DateTime departureTime, final String destination
			) throws Exception{
		Set<Stopover> nextAvailableStopovers = new HashSet<Stopover>();

		Stopover nextStopover = null;
		String town = departureTown;
		Stopover stopover = allStopovers.get(departureTown);
		stopover.setArrivalTime(departureTime);

		while(!destination.equals(town)){
			nextAvailableStopovers.remove(stopover);
			allStopovers.remove(town);
			for (Path newAvailablePath : stopover.getNextPaths()){
				if (isDepartureTimePossible(stopover.getArrivalTime(), newAvailablePath.getDepartureTime())){
					nextStopover = allStopovers.get(newAvailablePath.getArrivalTown());
					if(nextStopover!=null){
						nextAvailableStopovers.add(nextStopover);
						if(isShortest(nextStopover.getArrivalTime(), newAvailablePath.getArrivalTime())){
							nextStopover.setArrivalTime(newAvailablePath.getArrivalTime());
							nextStopover.setPreviousStopover(stopover);
						}
					}
				}
			}
			stopover = getShortestPath(nextAvailableStopovers);
			if (stopover == null){
				break;
			}
			else {
				town = stopover.getTown();
			}
		}
		return computeRoadmap(stopover);
	}

	private final static boolean isShortest(DateTime oldDate, DateTime newDate){
		return oldDate == null || oldDate.isAfter(newDate);
	}

	private final static boolean isDepartureTimePossible(DateTime stopoverTime, DateTime departureTime){
		return stopoverTime == null || !stopoverTime.isAfter(departureTime);
	}

	private static List<Stopover> computeRoadmap(Stopover lastStopover) throws Exception{
		if (lastStopover == null){
			throw new Exception("Destination not found");
		}

		List<Stopover> roadmap = new ArrayList<Stopover>();
		Stopover stopover = lastStopover;
		while(stopover != null){
			roadmap.add(stopover);
			stopover = stopover.getPreviousStopover();
		}
		Collections.reverse(roadmap);
		return roadmap;
	}

	private final static Stopover getShortestPath(Set<Stopover> stopovers){
		Stopover shorterPath = null;

		for(Stopover stopover : stopovers){
			if(shorterPath == null || stopover.getArrivalTime().isBefore(shorterPath.getArrivalTime())){
				shorterPath = stopover;
			}
		}
		return shorterPath;
	}
}
