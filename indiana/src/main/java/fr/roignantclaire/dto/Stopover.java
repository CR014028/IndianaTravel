package fr.roignantclaire.dto;

import java.util.HashSet;
import java.util.Set;

import org.joda.time.DateTime;

import fr.roignantclaire.mapping.PathMapping;

public class Stopover {

	private String town;
	private Set<Path> nextPaths;
	private DateTime arrivalTime;
	private Stopover previousStopover;
	
	public Stopover(String town){
		this.town = town;
	}
	
	public Set<Path> getNextPaths() {
		if (this.nextPaths == null){
			this.nextPaths = new HashSet<Path>();
		}
		
		return nextPaths;
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

	public DateTime getArrivalTime() {
		return arrivalTime;
	}

	public void setArrivalTime(DateTime arrivalTime) {
		this.arrivalTime = arrivalTime;
	}

	@Override
	public String toString() {
		StringBuilder res = new StringBuilder();		
		res.append("Escale à ").append(this.getTown() == null ? "": this.getTown())
		.append(" à ").append(this.getArrivalTime() == null ? "?" : PathMapping.getFormattedDate(this.getArrivalTime()))
		.append(" départ possible pour : ").append(this.getNextPaths());
		return res.toString();
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
