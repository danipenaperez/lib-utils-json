package org.dppware.lib.utils.json;

import org.junit.jupiter.api.Test;

import com.octomix.josson.Josson;

public class JSOSONTest {

	String input = """
						[{
			    "city": "London",
			    "team": "Chelsea",
			    "player": "Palmer"
			},
			{
			    "city": "London",
			    "team": "Chelsea",
			    "player": "Jackson"
			},
			{
			    "city": "London",
			    "team": "Arsenal",
			    "player": "Calafiori"
			},
			{
			    "city": "London",
			    "team": "Arsenal",
			    "player": "Saka"
			},
			{
			    "city": "Manchester",
			    "team": "City",
			    "player": "Rodri"
			},
			{
			    "city": "Manchester",
			    "team": "United",
			    "player": "Fernandes"
			}]

						""";
	
	
	@Test
	void testSomething() throws Exception {
		Josson josson = Josson.fromJsonString(input);
		String exp = "group(city).map(city,teams:elements.group(team).map(team,players:elements.map(name:player)))";
		var result = josson.getNode(exp);
		System.out.println(result);
	}
}
