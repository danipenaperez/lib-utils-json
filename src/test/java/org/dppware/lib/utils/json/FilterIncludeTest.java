package org.dppware.lib.utils.json;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;

import org.json.JSONArray;
import org.json.JSONObject;
import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class FilterIncludeTest {
	@SuppressWarnings("unused")
	private static final Logger LOGGER = LoggerFactory.getLogger(FilterIncludeTest.class);
	String data = """
			
			{
			    "countries":[
			        {
			            "name": "Spain",
			            "habitants": 48619695,
			            "cities":[
			                {
			                    "name": "Madrid",
			                    "location": {
			                        "latitude": 40.4165,
			                        "longitude": -3.70256
			                    }
			                },
			                {
			                    "name": "Barcelona",
			                    "location": {
			                        "latitude": 41.38879,
			                        "longitude": 2.15899
			                    }
			                }
			            ] 
			        },
			        {
			            "name": "France",
			            "habitants": 68401997,
			            "cities":[
			                {
			                    "name": "Paris",
			                    "location": {
			                        "latitude": 48.85341,
			                        "longitude": 2.3488
			                    }
			                }
			            ] 
			        }
			
			    ],
			    "pagination":{
			        "requestedPage":1,
			        "pageSize": 2
			    }
			}
			
			
			""";
	
	@Test
	void testSomething() throws Exception {
		String includeNodes = "pagination_*,countries_name,countries_cities_*,countries_cities_location_latitude";
	
		JSONObject jsonObject =new FilterInclude().apply(new JSONObject(data), includeNodes);
		
		/** 
		 * includePattern:  pagination_* 
		 **/ 
		assertNotNull(jsonObject.getJSONObject("pagination").getInt("requestedPage"));
		assertNotNull(jsonObject.getJSONObject("pagination").getInt("pageSize"));
		
		/** 
		 * includePattern:  countries_name 
		 **/ 
		assertNotNull(jsonObject.getJSONArray("countries"));
		JSONArray countries = jsonObject.getJSONArray("countries");
		countries.forEach(_country-> {
			JSONObject country = (JSONObject)_country;
			assertNotNull(country.get("name")); //Must exists
			assertThrows(org.json.JSONException.class, ()-> country.get("habitants"));  //Null (is not included
			
			/** 
			 * includePattern:  countries_cities_* 
			 **/
			assertNotNull(country.getJSONArray("cities")); //Must exists
			JSONArray cities = country.getJSONArray("cities");
			cities.forEach(_city->{
				JSONObject city = (JSONObject)_city;
				assertNotNull(city.get("name")); //Must exists
				
				/** 
				 * includePattern:  countries_cities_location_latitude
				 **/
				assertNotNull(city.get("location")); //Must exists
				JSONObject location = city.getJSONObject("location");
				assertNotNull(location.get("latitude")); //Must exists
				assertThrows(org.json.JSONException.class, ()-> location.get("longitude"));  //Null (is not included
				
			});
		});
		

	}
	
	@Test
	void testIncludeWholeTree() throws Exception {
		
		JSONObject jsonObject = new FilterInclude().apply(new JSONObject(data), "pagination_*, countries_cities*");

		/** 
		 * includePattern:  pagination_* 
		 **/ 
		assertNotNull(jsonObject.getJSONObject("pagination").getInt("requestedPage"));
		assertNotNull(jsonObject.getJSONObject("pagination").getInt("pageSize"));
		
		/** 
		 * includePattern:  countries_name 
		 **/ 
		assertNotNull(jsonObject.getJSONArray("countries"));
		JSONArray countries = jsonObject.getJSONArray("countries");
		countries.forEach(_country-> {
			JSONObject country = (JSONObject)_country;
			assertThrows(org.json.JSONException.class, ()-> country.get("name"));
			assertThrows(org.json.JSONException.class, ()-> country.get("habitants"));  //Null (is not included
			
			/** 
			 * includePattern:  countries_cities_* 
			 **/
			assertNotNull(country.getJSONArray("cities")); //Must exists
			JSONArray cities = country.getJSONArray("cities");
			cities.forEach(_city->{
				JSONObject city = (JSONObject)_city;
				assertNotNull(city.get("name")); //Must exists
				
				/** 
				 * includePattern:  countries_cities_location_latitude
				 **/
				assertNotNull(city.get("location")); //Must exists
				JSONObject location = city.getJSONObject("location");
				assertNotNull(location.get("latitude")); //Must exists
				assertNotNull(location.get("longitude")); //Must exists
				
			});
		});
		

	}
	
}
