#!/usr/bin/env python3
"""
Convert Quest Graph from YAML to JSON
Version: 1.0.0
Date: 2025-11-07 00:29
"""

import yaml
import json
import sys
from pathlib import Path
from typing import Dict, List, Any

def load_yaml_file(filepath: str) -> Dict:
    """Load YAML file"""
    with open(filepath, 'r', encoding='utf-8') as f:
        return yaml.safe_load(f)

def convert_quest_dependencies(yaml_data: Dict) -> Dict:
    """Convert quest dependencies to JSON format"""
    
    graph = {
        "metadata": yaml_data.get("metadata", {}),
        "nodes": [],
        "edges": []
    }
    
    # Convert quests to nodes
    quests = yaml_data.get("quests", {})
    for quest_id, quest_data in quests.items():
        node = {
            "id": quest_id,
            "name": quest_data.get("name", ""),
            "era": quest_data.get("era", ""),
            "type": quest_data.get("type", ""),
            "class_focus": quest_data.get("class_focus"),
            "faction_focus": quest_data.get("faction_focus")
        }
        graph["nodes"].append(node)
        
        # Convert influences to edges
        influences = quest_data.get("influences", {})
        
        # Unlocks
        if "unlocks" in influences:
            for unlocked_quest in influences["unlocks"].get("immediate", []):
                graph["edges"].append({
                    "from": quest_id,
                    "to": unlocked_quest,
                    "type": "unlocks",
                    "timing": "immediate"
                })
            
            for unlocked_quest in influences["unlocks"].get("next_era", []):
                graph["edges"].append({
                    "from": quest_id,
                    "to": unlocked_quest,
                    "type": "unlocks",
                    "timing": "next_era"
                })
        
        # Blocks
        if "blocks" in influences:
            for blocked_quest in influences["blocks"].get("permanent", []):
                graph["edges"].append({
                    "from": quest_id,
                    "to": blocked_quest,
                    "type": "blocks",
                    "permanent": True
                })
        
        # Influenced_by (prerequisites)
        if "influenced_by" in quest_data:
            for prereq in quest_data["influenced_by"]:
                if isinstance(prereq, dict) and "quest" in prereq:
                    graph["edges"].append({
                        "from": prereq["quest"],
                        "to": quest_id,
                        "type": "requires",
                        "condition": prereq.get("condition")
                    })
    
    # Add critical chains
    if "critical_chains" in yaml_data:
        graph["critical_chains"] = yaml_data["critical_chains"]
    
    # Add statistics
    graph["statistics"] = {
        "total_nodes": len(graph["nodes"]),
        "total_edges": len(graph["edges"]),
        "quests_by_type": {},
        "quests_by_era": {}
    }
    
    # Calculate statistics
    for node in graph["nodes"]:
        quest_type = node.get("type", "unknown")
        era = node.get("era", "unknown")
        
        graph["statistics"]["quests_by_type"][quest_type] = \
            graph["statistics"]["quests_by_type"].get(quest_type, 0) + 1
        
        graph["statistics"]["quests_by_era"][era] = \
            graph["statistics"]["quests_by_era"].get(era, 0) + 1
    
    return graph

def convert_triggers(yaml_data: Dict) -> Dict:
    """Convert quest triggers to JSON"""
    
    result = {
        "metadata": yaml_data.get("metadata", {}),
        "triggers": [],
        "trigger_chains": yaml_data.get("trigger_chains", {}),
        "finale_triggers": yaml_data.get("finale_triggers", {})
    }
    
    triggers = yaml_data.get("triggers", {})
    for trigger_id, trigger_data in triggers.items():
        result["triggers"].append({
            "id": trigger_id,
            "name": trigger_data.get("name", ""),
            "unlocks": trigger_data.get("unlocks", {}),
            "timing": trigger_data.get("timing", ""),
            "note": trigger_data.get("note", "")
        })
    
    return result

def convert_blockers(yaml_data: Dict) -> Dict:
    """Convert quest blockers to JSON"""
    
    result = {
        "metadata": yaml_data.get("metadata", {}),
        "blockers": [],
        "mutual_exclusions": yaml_data.get("mutual_exclusions", {}),
        "block_cascades": yaml_data.get("block_cascades", {})
    }
    
    blockers = yaml_data.get("blockers", {})
    for blocker_id, blocker_data in blockers.items():
        result["blockers"].append({
            "id": blocker_id,
            "name": blocker_data.get("name", ""),
            "blocks": blocker_data.get("blocks", {}),
            "reason": blocker_data.get("reason", ""),
            "permanent": blocker_data.get("permanent", False),
            "recovery": blocker_data.get("recovery", {})
        })
    
    return result

def main():
    """Main conversion process"""
    
    print("=" * 50)
    print("YAML → JSON Converter for Quest System")
    print("=" * 50)
    
    # Paths
    base_path = Path(__file__).parent.parent
    
    files_to_convert = [
        {
            "input": base_path / "phase2-narrative/connections/side-quests-matrix.yaml",
            "output": base_path / "export/side-quests-matrix.json",
            "converter": convert_quest_dependencies
        },
        {
            "input": base_path / "phase2-narrative/connections/quest-triggers.yaml",
            "output": base_path / "export/quest-triggers.json",
            "converter": convert_triggers
        },
        {
            "input": base_path / "phase2-narrative/connections/quest-blockers.yaml",
            "output": base_path / "export/quest-blockers.json",
            "converter": convert_blockers
        },
        {
            "input": base_path / "phase3-event-matrix/graph/quest-dependencies.yaml",
            "output": base_path / "export/quest-dependencies-full.json",
            "converter": lambda x: x  # Simple copy for full graph
        }
    ]
    
    # Create export directory
    export_dir = base_path / "export"
    export_dir.mkdir(exist_ok=True)
    
    # Convert each file
    for file_info in files_to_convert:
        input_file = file_info["input"]
        output_file = file_info["output"]
        converter = file_info["converter"]
        
        print(f"\nConverting: {input_file.name}")
        
        if not input_file.exists():
            print(f"  ⚠️  File not found: {input_file}")
            continue
        
        # Load YAML
        yaml_data = load_yaml_file(str(input_file))
        
        # Convert
        json_data = converter(yaml_data)
        
        # Save JSON
        with open(output_file, 'w', encoding='utf-8') as f:
            json.dump(json_data, f, indent=2, ensure_ascii=False)
        
        print(f"  ✅ Saved: {output_file.name}")
        print(f"     Size: {output_file.stat().st_size} bytes")
    
    print("\n" + "=" * 50)
    print("✅ ALL FILES CONVERTED SUCCESSFULLY!")
    print("=" * 50)
    print(f"\nOutput directory: {export_dir}")
    print("\nGenerated files:")
    for file_info in files_to_convert:
        print(f"  - {file_info['output'].name}")

if __name__ == "__main__":
    main()

