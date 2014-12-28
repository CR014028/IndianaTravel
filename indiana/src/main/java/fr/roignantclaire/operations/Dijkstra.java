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

public class Dijkstra {

	/**
	 * 
	 * @param allStopovers
	 * @param departureTown
	 * @param departureTime
	 * @param destination
	 * @return
	 * @throws Exception
	 */
	public static List<Path> computePath(final Map<String, Stopover> allStopovers, final String departureTown, final DateTime departureTime, final String destination
			) throws Exception{
		Set<Path> nextAvailablePaths = new HashSet<Path>();

		//First path, bed to station
		Path shortestPath = new Path(null, null, departureTown, departureTime);
		Stopover lastStopover = null;

		while(shortestPath != null && lastStopover == null){
			String town = shortestPath.getArrivalTown();
			Stopover stopover = allStopovers.get(town);
			nextAvailablePaths.remove(shortestPath);
			if (stopover != null){
				nextAvailablePaths.addAll(stopover.getNextAvailablePaths());
				if(!town.equals(destination)){
					for (Path newAvailablePath : stopover.getNextAvailablePaths()){
						Stopover nextStopover = allStopovers.get(newAvailablePath.getArrivalTown());
						if(isShortest(nextStopover.getArrivalTime(), newAvailablePath.getArrivalTime())){
							nextStopover.setArrivalTime(newAvailablePath.getArrivalTime());
							nextStopover.setPreviousPath(newAvailablePath);
							nextStopover.setPreviousStopover(stopover);
						}
					}
					shortestPath = getShortestPath(shortestPath.getArrivalTime(), nextAvailablePaths);
				}
				else {
					lastStopover = stopover;
				}
			}
		}

		return computeRoadmap(lastStopover);
	}

	private static boolean isShortest(DateTime oldDate, DateTime newDateTime){
		return oldDate == null || oldDate.isAfter(newDateTime);
	}

	private static List<Path> computeRoadmap(Stopover lastStopover) throws Exception{
		if (lastStopover == null){
			throw new Exception("Destination not found");
		}

		List<Path> roadmap = new ArrayList<Path>();
		Stopover stopover = lastStopover;
		while(stopover != null && stopover.getPreviousPath()!=null){
			roadmap.add(stopover.getPreviousPath());
			stopover = stopover.getPreviousStopover();
		}
		Collections.reverse(roadmap);
		return roadmap;
	}

	/**
	 * 
	 * @param departureTime date de départ
	 * @param paths trajets possibles
	 * @return
	 */
	private static Path getShortestPath(DateTime departureTime, Set<Path> paths){
		Path shorterPath = null;
		for(Path path : paths){
			if(shorterPath == null || (!departureTime.isAfter(path.getDepartureTime()) && path.getArrivalTime().isBefore(shorterPath.getArrivalTime()))){
				shorterPath = path;
			}
		}
		return shorterPath;
	}

}
