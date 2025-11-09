#!/usr/bin/env node
/**
 * Convert Quest Graph from YAML to JSON (Node.js version)
 * Version: 1.0.0
 * Date: 2025-11-07 00:30
 */

const fs = require('fs');
const path = require('path');
const yaml = require('js-yaml');

/**
 * Load YAML file
 */
function loadYaml(filepath) {
    const content = fs.readFileSync(filepath, 'utf8');
    return yaml.load(content);
}

/**
 * Convert quest dependencies to JSON format
 */
function convertQuestDependencies(yamlData) {
    const graph = {
        metadata: yamlData.metadata || {},
        nodes: [],
        edges: []
    };
    
    const quests = yamlData.quests || {};
    
    for (const [questId, questData] of Object.entries(quests)) {
        // Add node
        graph.nodes.push({
            id: questId,
            name: questData.name || '',
            era: questData.era || '',
            type: questData.type || '',
            class_focus: questData.class_focus || null,
            faction_focus: questData.faction_focus || null
        });
        
        // Add edges from influences
        const influences = questData.influences || {};
        
        // Unlocks
        if (influences.unlocks) {
            (influences.unlocks.immediate || []).forEach(unlocked => {
                graph.edges.push({
                    from: questId,
                    to: unlocked,
                    type: 'unlocks',
                    timing: 'immediate'
                });
            });
            
            (influences.unlocks.next_era || []).forEach(unlocked => {
                graph.edges.push({
                    from: questId,
                    to: unlocked,
                    type: 'unlocks',
                    timing: 'next_era'
                });
            });
        }
        
        // Blocks
        if (influences.blocks) {
            (influences.blocks.permanent || []).forEach(blocked => {
                graph.edges.push({
                    from: questId,
                    to: blocked,
                    type: 'blocks',
                    permanent: true
                });
            });
        }
        
        // Prerequisites
        if (questData.influenced_by) {
            questData.influenced_by.forEach(prereq => {
                if (prereq.quest) {
                    graph.edges.push({
                        from: prereq.quest,
                        to: questId,
                        type: 'requires',
                        condition: prereq.condition || null
                    });
                }
            });
        }
    }
    
    // Add critical chains
    if (yamlData.critical_chains) {
        graph.critical_chains = yamlData.critical_chains;
    }
    
    // Calculate statistics
    graph.statistics = {
        total_nodes: graph.nodes.length,
        total_edges: graph.edges.length,
        quests_by_type: {},
        quests_by_era: {}
    };
    
    graph.nodes.forEach(node => {
        const type = node.type || 'unknown';
        const era = node.era || 'unknown';
        
        graph.statistics.quests_by_type[type] = (graph.statistics.quests_by_type[type] || 0) + 1;
        graph.statistics.quests_by_era[era] = (graph.statistics.quests_by_era[era] || 0) + 1;
    });
    
    return graph;
}

/**
 * Main conversion process
 */
function main() {
    console.log('='.repeat(50));
    console.log('YAML → JSON Converter for Quest System (Node.js)');
    console.log('='.repeat(50));
    
    const basePath = path.join(__dirname, '..');
    
    const filesToConvert = [
        {
            input: path.join(basePath, 'phase2-narrative/connections/side-quests-matrix.yaml'),
            output: path.join(basePath, 'export/side-quests-matrix.json'),
            converter: convertQuestDependencies
        },
        {
            input: path.join(basePath, 'phase2-narrative/connections/quest-triggers.yaml'),
            output: path.join(basePath, 'export/quest-triggers.json'),
            converter: (data) => data  // Simple copy
        },
        {
            input: path.join(basePath, 'phase2-narrative/connections/quest-blockers.yaml'),
            output: path.join(basePath, 'export/quest-blockers.json'),
            converter: (data) => data  // Simple copy
        },
        {
            input: path.join(basePath, 'phase3-event-matrix/graph/quest-dependencies.yaml'),
            output: path.join(basePath, 'export/quest-dependencies-full.json'),
            converter: (data) => data  // Simple copy
        }
    ];
    
    // Create export directory
    const exportDir = path.join(basePath, 'export');
    if (!fs.existsSync(exportDir)) {
        fs.mkdirSync(exportDir, { recursive: true });
    }
    
    // Convert each file
    filesToConvert.forEach(fileInfo => {
        console.log(`\nConverting: ${path.basename(fileInfo.input)}`);
        
        if (!fs.existsSync(fileInfo.input)) {
            console.log(`  ⚠️  File not found: ${fileInfo.input}`);
            return;
        }
        
        try {
            // Load YAML
            const yamlData = loadYaml(fileInfo.input);
            
            // Convert
            const jsonData = fileInfo.converter(yamlData);
            
            // Save JSON
            fs.writeFileSync(
                fileInfo.output,
                JSON.stringify(jsonData, null, 2),
                'utf8'
            );
            
            const stats = fs.statSync(fileInfo.output);
            console.log(`  ✅ Saved: ${path.basename(fileInfo.output)}`);
            console.log(`     Size: ${stats.size} bytes`);
        } catch (error) {
            console.log(`  ❌ Error: ${error.message}`);
        }
    });
    
    console.log('\n' + '='.repeat(50));
    console.log('✅ CONVERSION COMPLETED!');
    console.log('='.repeat(50));
    console.log(`\nOutput directory: ${exportDir}`);
    console.log('\nGenerated files:');
    filesToConvert.forEach(f => {
        console.log(`  - ${path.basename(f.output)}`);
    });
}

// Run if called directly
if (require.main === module) {
    main();
}

module.exports = { convertQuestDependencies, convertTriggers, convertBlockers: (d) => d };

