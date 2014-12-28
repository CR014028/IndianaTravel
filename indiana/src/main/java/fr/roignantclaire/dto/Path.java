package fr.roignantclaire.dto;

import org.joda.time.DateTime;

import fr.roignantclaire.mapping.PathMapping;

public class Path {

	private DateTime departureTime;
	private String departureTown;
	private String arrivalTown;
	private DateTime arrivalTime;
	
	public Path(DateTime departureTime, String departureTown, String arrivalTown, DateTime arrivalTime){
		this.departureTime = departureTime;
		this.departureTown = departureTown;
		this.arrivalTown = arrivalTown;
		this.arrivalTime = arrivalTime;
	}

	public DateTime getDepartureTime() {
		return departureTime;
	}

	public void setDepartureTime(DateTime departureTime) {
		this.departureTime = departureTime;
	}

	public String getDepartureTown() {
		return departureTown;
	}

	public void setDepartureTown(String departureTown) {
		this.departureTown = departureTown;
	}

	public String getArrivalTown() {
		return arrivalTown;
	}

	public void setArrivalTown(String arrivalTown) {
		this.arrivalTown = arrivalTown;
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
		res.append("Prendre le ").append(this.getDepartureTown() == null ? "": this.getDepartureTown())
		.append("-").append(this.getArrivalTown() == null ? "" : this.getArrivalTown())
		.append(" de ").append(PathMapping.getFormattedDate(this.getDepartureTime()))
		.append(", arrivée à ").append(PathMapping.getFormattedDate(this.getArrivalTime()));
		return res.toString();
	}
	
}
