package fr.roignantclaire.indiana;

import static org.junit.Assert.assertEquals;

import java.io.FileInputStream;
import java.io.FileNotFoundException;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;

public class AppTest 
{
	@Rule
	public ExpectedException exception = ExpectedException.none();

	private final static String RESOURCES_PATH = "src/test/resources/";
	private String dataPathsName;
	private FileInputStream file;

	@Before
	public void setUp() throws Exception {
	}

	@Test
	public void basic() throws Exception
	{
		dataPathsName = "trajets.csv";
		file = new FileInputStream(RESOURCES_PATH + dataPathsName);

		String res = App.computePath(file);
		assertEquals("18:40", res);
	}

	@Test
	public void testTwoChoices() throws Exception
	{
		dataPathsName = "trajetsDouble.csv";
		file = new FileInputStream(RESOURCES_PATH + dataPathsName);

		String res = App.computePath(file);
		assertEquals("17:50", res);
	}

	@Test
	public void testDestinationUnknown() throws Exception
	{
		dataPathsName = "destinationUnknown.csv";
		file = new FileInputStream(RESOURCES_PATH + dataPathsName);
		exception.expect(Exception.class);
		exception.expectMessage("Destination not found");

		App.computePath(file);
	}

	@Test(expected = FileNotFoundException.class)
	public void testFileNotFoundException() throws Exception
	{
		dataPathsName = null;
		file = new FileInputStream(RESOURCES_PATH + dataPathsName);

		App.computePath(file);
	}

	@Test
	public void testIncorrectInput() throws Exception
	{
		exception.expect(Exception.class);
		exception.expectMessage("Departure or destination required");
		dataPathsName = "incorrectInput.csv";
		file = new FileInputStream(RESOURCES_PATH + dataPathsName);
		App.computePath(file);

		dataPathsName = "incorrectInput.csv";
	}
}
