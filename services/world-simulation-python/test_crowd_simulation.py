#!/usr/bin/env python3
"""
Test script for Crowd Simulation
Demonstrates Mesa-based crowd simulation with signal aggregation
"""

import logging
from crowd_simulation import WorldModel

# Configure logging
logging.basicConfig(level=logging.INFO)

def test_crowd_simulation():
    """Test the crowd simulation for a few steps"""

    print("Testing Crowd Simulation with Mesa")
    print("=" * 50)

    # Initialize model
    model = WorldModel(width=20, height=20, num_agents=50)

    print(f"Initialized model with {model.num_agents} agents")
    print(f"Grid size: {model.width}x{model.height}")
    print(f"Food vendors: {len(model.get_food_vendor_locations())}")
    print()

    # Run simulation for 10 steps
    for step in range(10):
        print(f"Step {step + 1}")
        print("-" * 20)

        # Execute step
        model.step()

        # Show statistics
        behaviors = model._count_behaviors()
        print(f"Agent behaviors: {behaviors}")
        print(f"Total signals generated: {len(model.signals)}")

        # Show aggregated signals
        aggregated = model.get_aggregated_signals()
        if aggregated:
            print(f"Aggregated signals: {len(aggregated)}")
            for signal in aggregated:
                print(f"  • {signal['signal_type']}: {signal['description']}")
        else:
            print("No aggregated signals this step")

        print()

    print("Crowd simulation test completed!")
    print(f"Total simulation steps: {model.current_step}")
    print(f"Total agents: {len(model.agents)}")

    # Show final data
    try:
        final_data = model.datacollector.get_model_vars_dataframe()
        print("\nFinal Statistics:")
        print(final_data.tail())
    except Exception as e:
        print(f"Could not get final statistics: {e}")

if __name__ == "__main__":
    test_crowd_simulation()