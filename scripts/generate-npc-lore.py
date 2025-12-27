#!/usr/bin/env python3
"""
Script to generate NPC lore documents for NECPGAME project.
Creates comprehensive NPC profiles following the established template.
"""

import os
import random
import yaml
from datetime import datetime
from pathlib import Path

# Configuration
BASE_DIR = Path("knowledge/canon/narrative/npc-lore")
TEMPLATE_FILE = BASE_DIR / "NPC-TEMPLATE.yaml"
OUTPUT_COUNT = 100  # Generate 100 more NPCs

# NPC Categories and their configurations
NPC_CATEGORIES = {
    "common/citizens": {
        "roles": ["Office Worker", "Factory Worker", "Shopkeeper", "Retiree", "Student"],
        "eras": ["2020s", "2030s", "2040s", "2050s", "2060s", "2070s"],
        "districts": ["Watson", "Westbrook", "City Center", "Heywood", "Santo Domingo"]
    },
    "common/service": {
        "roles": ["Waiter", "Cleaner", "Security Guard", "Delivery Driver", "Taxi Driver"],
        "eras": ["2020s", "2030s", "2040s", "2050s", "2060s", "2070s"],
        "districts": ["Watson", "Westbrook", "City Center", "Heywood", "Santo Domingo"]
    },
    "common/traders": {
        "roles": ["Shop Owner", "Market Vendor", "Importer", "Exporter", "Pawn Broker"],
        "eras": ["2020s", "2030s", "2040s", "2050s", "2060s", "2070s"],
        "districts": ["Watson", "Westbrook", "City Center", "Heywood", "Santo Domingo"]
    },
    "factions/corporations/common": {
        "roles": ["Security Officer", "Research Assistant", "PR Specialist", "HR Manager", "IT Support"],
        "corporations": ["Arasaka", "Militech", "Biotechnica", "Zetatech", "Petrochem"],
        "eras": ["2030s", "2040s", "2050s", "2060s", "2070s"],
        "districts": ["City Center", "Westbrook"]
    },
    "factions/gangs": {
        "roles": ["Gang Member", "Lieutenant", "Enforcer", "Recruiter", "Territory Guard"],
        "gangs": ["Mox", "Maelstrom", "Valentinos", "Trauma Team", "Tyger Claws"],
        "eras": ["2020s", "2030s", "2040s", "2050s", "2060s", "2070s"],
        "districts": ["Watson", "Heywood", "Santo Domingo"]
    }
}

# Name generators
FIRST_NAMES = {
    "male": ["John", "Mike", "David", "James", "Robert", "Michael", "William", "Richard", "Joseph", "Thomas"],
    "female": ["Mary", "Linda", "Patricia", "Susan", "Deborah", "Barbara", "Debra", "Karen", "Nancy", "Donna"],
    "non-binary": ["Alex", "Jordan", "Taylor", "Morgan", "Casey", "Riley", "Avery", "Quinn", "Skyler", "Jamie"]
}

LAST_NAMES = ["Smith", "Johnson", "Williams", "Brown", "Jones", "Garcia", "Miller", "Davis", "Rodriguez", "Martinez",
              "Hernandez", "Lopez", "Gonzalez", "Wilson", "Anderson", "Thomas", "Taylor", "Moore", "Jackson", "Martin"]

def generate_name(gender=None):
    """Generate a random name"""
    if not gender:
        gender = random.choice(["male", "female", "non-binary"])
    first = random.choice(FIRST_NAMES[gender])
    last = random.choice(LAST_NAMES)
    return f"{first} {last}", gender

def generate_personality():
    """Generate personality traits"""
    traits = ["loyal", "cunning", "brave", "cautious", "ambitious", "compassionate", "ruthless", "wise"]
    strengths = ["leadership", "technical skills", "combat training", "social connections", "problem-solving"]
    weaknesses = ["trust issues", "recklessness", "greed", "impulsiveness", "overconfidence"]

    return {
        "traits": random.sample(traits, 3),
        "strengths": random.sample(strengths, 2),
        "weaknesses": random.sample(weaknesses, 2),
        "temperament": random.choice(["calm", "aggressive", "nervous", "confident", "paranoid"]),
        "motivations": random.sample(["survival", "wealth", "power", "justice", "knowledge"], 2)
    }

def generate_background(name, era, role):
    """Generate background story"""
    birth_year = int(era[:4]) - random.randint(20, 60)
    return {
        "origin": f"Born in Night City, {random.choice(['working-class', 'middle-class', 'immigrant'])} background",
        "upbringing": f"Grew up in {random.choice(['tough streets', 'corporate housing', 'family business'])}",
        "career_history": f"Started as {random.choice(['apprentice', 'entry-level', 'intern'])}, became {role.lower()} through experience",
        "key_events": [f"Major life event in {birth_year + random.randint(10, 30)}"],
        "relationships": {
            "family": [f"{random.choice(['spouse', 'children', 'siblings'])} in {random.choice(['same district', 'another part of city'])}"],
            "allies": [f"Local {random.choice(['community', 'guild', 'network'])}"],
            "enemies": [f"{random.choice(['corporate interests', 'rival groups', 'personal vendettas'])}"]
        }
    }

def generate_skills(role):
    """Generate skills based on role"""
    if "security" in role.lower() or "guard" in role.lower():
        return {
            "combat": "trained in corporate security protocols",
            "technical": "basic surveillance and access control",
            "social": "authority and intimidation"
        }
    elif "trader" in role.lower() or "merchant" in role.lower():
        return {
            "combat": "basic self-defense",
            "technical": "inventory management and pricing",
            "social": "negotiation and customer service"
        }
    else:
        return {
            "combat": "basic self-defense training",
            "technical": f"skills related to {role.lower()} work",
            "social": "professional networking"
        }

def create_npc_document(category, role, era, district, index):
    """Create a complete NPC document"""
    name, gender = generate_name()
    nickname = random.choice(["'Fast'", "'Slow'", "'Lucky'", "'Tough'", "'Smart'", "'Crazy'"])
    full_name = f"{name.split()[0]} {nickname} {name.split()[1]}"

    # Determine faction based on category
    if "corporations" in category:
        faction = random.choice(NPC_CATEGORIES["factions/corporations/common"]["corporations"])
    elif "gangs" in category:
        faction = random.choice(NPC_CATEGORIES["factions/gangs"]["gangs"])
    else:
        faction = "Independent"

    doc = {
        "metadata": {
            "id": f"npc-{category.replace('/', '-')}-{era.lower()}-{district.lower().replace(' ', '-')}-{index:03d}",
            "name": full_name,
            "aliases": [nickname.strip("'")],
            "faction": faction,
            "role": role,
            "location": f"Night City, {district}",
            "era": era,
            "importance": "MINOR",
            "status": "ACTIVE",
            "version": "1.0.0",
            "last_updated": datetime.now().strftime("%Y-%m-%dT%H:%M:%S")
        },
        "appearance": {
            "age": random.randint(25, 65),
            "gender": gender,
            "ethnicity": random.choice(["Caucasian", "African-American", "Latino", "Asian-American", "Mixed"]),
            "height": f"{random.randint(160, 190)}cm",
            "build": random.choice(["slender", "athletic", "stocky", "average"]),
            "hair": random.choice(["short black", "long brown", "blond", "graying", "bald"]),
            "eyes": random.choice(["brown", "blue", "green", "hazel"]),
            "cyberware_visible": random.choice(["none", "neural ports", "enhanced optics", "subdermal armor"]),
            "clothing_style": f"practical {random.choice(['street wear', 'work clothes', 'business casual'])}",
            "distinctive_features": random.choice(["confident posture", "nervous habits", "professional demeanor", "street toughness"])
        }
    }

    # Add personality
    personality = generate_personality()
    doc["personality"] = personality

    # Add background
    doc["background"] = generate_background(full_name, era, role)

    # Add skills
    doc["skills_abilities"] = generate_skills(role)

    # Add role function
    doc["role_function"] = {
        "primary_role": f"{role} in {district} district",
        "services_offered": [f"professional {role.lower()} services"],
        "influence_level": "local",
        "reputation": random.choice(["trusted", "respected", "feared", "neutral"]),
        "business_relations": [f"local {random.choice(['suppliers', 'associates', 'networks'])}"],
        "territory": f"{district} district operations"
    }

    # Add story integration
    doc["story_integration"] = {
        "quest_involvement": [f"{role.lower()} side quests"],
        "plot_hooks": [f"personal {random.choice(['secrets', 'vendettas', 'ambitions'])}"],
        "faction_impact": f"supports {faction.lower() if faction != 'Independent' else 'local community'} interests",
        "player_interactions": [f"{role.lower()} services"],
        "dialogue_options": random.randint(5, 15)
    }

    # Add lifecycle
    birth_year = int(era[:4]) - random.randint(25, 60)
    doc["lifecycle"] = {
        "birth_date": f"{birth_year}-{random.randint(1,12):02d}-{random.randint(1,28):02d}",
        "active_period": f"{era[:4]}-2077",
        "retirement_age": "N/A",
        "death_circumstances": "N/A",
        "legacy": f"Known as reliable {role.lower()} in {district}"
    }

    # Add additional notes
    doc["additional_notes"] = {
        "trivia": [f"Has {random.randint(5,20)} years experience"],
        "inspirations": ["Real working professionals"],
        "voice_actor": "Unassigned",
        "design_notes": f"Represents {role.lower()} archetype in cyberpunk setting",
        "future_plans": [f"Add {random.choice(['backstory', 'relationships', 'quests'])}"]
    }

    # Add validation
    doc["validation"] = {
        "checksum": "auto-generated",
        "schema_version": "1.0",
        "last_validated": datetime.now().strftime("%Y-%m-%dT%H:%M:%S")
    }

    return doc

def main():
    """Main generation function"""
    print(f"Generating {OUTPUT_COUNT} NPC documents...")

    generated = 0
    categories = list(NPC_CATEGORIES.keys())

    while generated < OUTPUT_COUNT:
        # Pick random category
        category = random.choice(categories)
        config = NPC_CATEGORIES[category]

        # Pick random parameters
        role = random.choice(config["roles"])
        era = random.choice(config["eras"])
        district = random.choice(config["districts"])

        # Create document
        npc_doc = create_npc_document(category, role, era, district, generated + 1)

        # Create output directory
        output_dir = BASE_DIR / category
        output_dir.mkdir(parents=True, exist_ok=True)

        # Generate filename
        filename = f"npc-{category.replace('/', '-')}-{era.lower()}-{district.lower().replace(' ', '-')}-{generated + 1:03d}.yaml"
        filepath = output_dir / filename

        # Write file
        with open(filepath, 'w', encoding='utf-8') as f:
            yaml.dump(npc_doc, f, allow_unicode=True, default_flow_style=False, sort_keys=False)

        print(f"Created: {filepath}")
        generated += 1

    print(f"Successfully generated {OUTPUT_COUNT} NPC documents!")

if __name__ == "__main__":
    main()
