package io.github.danipenaperez.lib.utils.json;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class TreeMappingAssembler {

	
	public Map<String, Map> assembleMapping(String targetFields) {

		List<String> nodes = Arrays.asList(targetFields.split(","));
		Map<String, Map> structure = new HashMap<>();
		nodes.forEach(_nodeEntry -> {
			String nodeEntry = _nodeEntry.trim();
			List<String> levelName = Arrays.asList(nodeEntry.split("_"));
			Map<String, Map> current = structure;
			for (String level : levelName) {
				if (current.get(level) == null)
					current.put(level, new HashMap<String, Map>());
				current = (Map<String, Map>) current.get(level);
			}
		});

		return structure;
	}
}
