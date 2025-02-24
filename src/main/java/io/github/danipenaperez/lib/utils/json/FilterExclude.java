package io.github.danipenaperez.lib.utils.json;

import java.util.Collections;
import java.util.Iterator;
import java.util.Map;
import java.util.function.BiFunction;

import org.json.JSONArray;
import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class FilterExclude implements BiFunction<JSONObject, String, JSONObject>{

	@SuppressWarnings("unused")
	private static final Logger LOGGER = LoggerFactory.getLogger(FilterExclude.class);
	
	@Override
	public JSONObject apply(JSONObject source, String filter) {
		var mapping = new TreeMappingAssembler().assembleMapping(filter);
		doFiltering(source, mapping);
		return source;
	}
	
	protected void doFiltering(Object src, Map<String, Map> excludedFields) {
		if (src instanceof JSONArray) {
			((JSONArray) src).forEach(child -> {
				doFiltering(child, excludedFields);
			});
		} else if (src instanceof JSONObject) {
			JSONObject _src = (JSONObject) src;
			Iterator<String> keyIterator = _src.keys();

			while (keyIterator.hasNext()) {
				String objectKey = keyIterator.next();
				
				if (excludedFields.keySet().contains(objectKey+"*")) { //Remove any kind of node
						keyIterator.remove();
						_src.remove(objectKey);
				} else if (excludedFields.keySet().contains(objectKey)) {
					if (_src.get(objectKey) instanceof JSONArray || _src.get(objectKey) instanceof JSONObject) {
						doFiltering(_src.get(objectKey), excludedFields.get(objectKey));
					}else {
						keyIterator.remove();
						_src.remove(objectKey);
					}
					
				} else if (excludedFields.keySet().contains("*")) {
					// Only process child nodes that are not final nodes
					if (_src.get(objectKey) instanceof JSONArray || _src.get(objectKey) instanceof JSONObject) {
						doFiltering(_src.get(objectKey), excludedFields.get(objectKey)!=null?excludedFields.get(objectKey): Map.of("", Collections.EMPTY_MAP));
					}else {
						keyIterator.remove();
						_src.remove(objectKey);
					}
				} else {
					if (_src.get(objectKey) instanceof JSONArray || _src.get(objectKey) instanceof JSONObject) {
						doFiltering(_src.get(objectKey), excludedFields.get(objectKey)!=null?excludedFields.get(objectKey): Map.of("", Collections.EMPTY_MAP));
					}else {
						//Accepted Node
					}
				}
			}
		} else {
			//Accept all final nodes
		}

	}


}
