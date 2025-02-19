package org.dppware.lib.utils.json;

import java.util.Iterator;
import java.util.Map;
import java.util.function.BiFunction;

import org.json.JSONArray;
import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


public class FilterInclude implements BiFunction<JSONObject, String, JSONObject>{
	
	@SuppressWarnings("unused")
	private static final Logger LOGGER = LoggerFactory.getLogger(FilterInclude.class);
	
	
	@Override
	public JSONObject apply(JSONObject source, String filter) {
		var mapping = new TreeMappingAssembler().assembleMapping(filter);
		doFiltering(source, mapping);
		return source;
	}

	
	protected void doFiltering(Object src, Map<String, Map> acceptedFields) {
		if (src instanceof JSONArray) {
			((JSONArray) src).forEach(child -> {
				doFiltering(child, acceptedFields);
			});
		} else if (src instanceof JSONObject) {
			JSONObject _src = (JSONObject) src;
			Iterator<String> keyIterator = _src.keys();

			while (keyIterator.hasNext()) {
				String objectKey = keyIterator.next();
				
				if (acceptedFields.keySet().contains(objectKey+"*")) { //Remove any kind of node
					//Accept all childs No futher processing 
				} else if (acceptedFields.keySet().contains(objectKey)) {
					doFiltering(_src.get(objectKey), acceptedFields.get(objectKey));
				} else if (acceptedFields.keySet().contains("*")) {
					// Only process child nodes that are not final nodes
					if (_src.get(objectKey) instanceof JSONArray || _src.get(objectKey) instanceof JSONObject) {
						keyIterator.remove();
						_src.remove(objectKey);
					}
				} else {
					keyIterator.remove();
					_src.remove(objectKey);
				}
			}
		} else {
			//Accept all final nodes
		}

	}











	
	
}
