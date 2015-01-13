package fr.roignantclaire.indiana;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.InputStreamReader;
import java.util.List;
import java.util.Map;

import org.joda.time.DateTime;

import fr.roignantclaire.dto.Stopover;
import fr.roignantclaire.mapping.PathMapping;
import fr.roignantclaire.operations.Dijkstra;

public class App 
{

	private final static String RESOURCES_PATH = "src/main/resources/";
	private final static String DATA_PATHS_NAME = "trajets.csv";
	private final static String ENCODING_FILE = "UTF-8";

	public static void main( String[] args )
	{
		try {
			FileInputStream file = new FileInputStream(RESOURCES_PATH + DATA_PATHS_NAME);

			String res = computePath(file);

			System.out.println(res);
		}
		catch (Exception e){
			e.printStackTrace();
		}
	}

	protected static String computePath(FileInputStream file) throws Exception{
		BufferedReader reader = new BufferedReader(new InputStreamReader(file, ENCODING_FILE));
		String[] firstLineTab = null;
		Map<String, Stopover> stopovers = null;

		try {
			String firstLine = reader.readLine();
			if (firstLine != null){
				firstLineTab = firstLine.split(PathMapping.SEPARATOR);
			}
			reader.readLine();
			stopovers = PathMapping.getStopovers(reader);
		}
		finally {
			if (reader != null){
				reader.close();
			}
		}

		String departureTown = firstLineTab == null ? "" : firstLineTab[1];//"Paris"
		String destinationTown = firstLineTab == null ? "" : firstLineTab[2];//"Berlin"
		DateTime departureTime = firstLineTab == null ? null : PathMapping.getDate(firstLineTab[0]/*, destinationTown*/);//TrajetMapping.getDate("08:20")
		if("".equals(departureTown) || "".equals(destinationTown)){
			throw new Exception("Departure or destination required");
		}
		List<Stopover> roadmap = Dijkstra.computePath(stopovers, departureTown, departureTime, destinationTown);

		String res = "";
		for(Stopover stopover : roadmap){
			if (destinationTown.equals(stopover.getTown())){
				res = PathMapping.getFormattedDate(stopover.getArrivalTime());
			}
			System.out.println(stopover.getTown()+" à "+PathMapping.getFormattedDate(stopover.getArrivalTime()));
		}

		return res;
	}
}
