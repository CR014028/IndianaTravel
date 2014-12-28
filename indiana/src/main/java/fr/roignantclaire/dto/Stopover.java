package fr.roignantclaire.dto;

import java.util.ArrayList;
import java.util.List;

import org.joda.time.DateTime;

public class Stopover {

	private String town;
	private List<Path> nextAvailablePaths;
	private Path previousPath;
	private DateTime arrivalTime;
	private Stopover previousStopover;
	
	public Stopover(String town){
		this.town = town;
	}
	
	public List<Path> getNextAvailablePaths() {
		if (this.nextAvailablePaths == null){
			this.nextAvailablePaths = new ArrayList<Path>();
		}
		
		return nextAvailablePaths;
	}

	public Stopover getPreviousStopover() {
		return previousStopover;
	}

	public void setPreviousStopover(Stopover previousStopover) {
		this.previousStopover = previousStopover;
	}

	public String getTown() {
		return town;
	}

	public void setTown(String town) {
		this.town = town;
	}

	public Path getPreviousPath() {
		return previousPath;
	}

	public DateTime getArrivalTime() {
		return arrivalTime;
	}

	public void setArrivalTime(DateTime arrivalTime) {
		this.arrivalTime = arrivalTime;
	}

	public void setPreviousPath(Path previousPath) {
		this.previousPath = previousPath;
	}

	@Override
	public String toString() {
		return "Escale à "+this.town +" départ possible pour : "+this.getNextAvailablePaths();
	}

	@Override
	public boolean equals(Object obj) {
		return obj instanceof Stopover && town.equals(((Stopover)obj).getTown());
	}

	@Override
	public int hashCode() {
		return town.hashCode();
	}


}
