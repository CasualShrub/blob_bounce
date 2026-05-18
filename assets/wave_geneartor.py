import json
import random

# ============================================================
# ======================= EDIT HERE ==========================
# ============================================================
#
# Main settings section.
# This is the ONLY place you normally need to edit.
#

CONFIG = {

    # Number of waves to generate
    "NUM_WAVES": 25, 

    # Minimum and maximum wave duration
    "MIN_WAVE_DURATION": 6.5,
    "MAX_WAVE_DURATION": 9.5,

    # Minimum and maximum creature count per wave
    "MIN_CREATURES_PER_WAVE": 10,
    "MAX_CREATURES_PER_WAVE": 14,

    # Creature spawn percentages
    # Must add up to 100
    "CREATURE_PERCENTAGES": {
        "Crab": 49,
        "Mackerel": 45,
        "OceanSunfish": 5,
        "Jellyfish": 1,
        "Shark": 0,
        "Marlin": 0
    },

    # Output json filename
    "OUTPUT_FILE": "waves_easy_1.json"
}

# ============================================================
# ============================================================
# ============================================================


# ============================================================
# HELPER FUNCTIONS
# ============================================================

def random_speed(min_speed, max_speed):
    return round(random.uniform(min_speed, max_speed), 2)


# ============================================================
# CREATURE GENERATORS
# ============================================================

def create_crab(time_value):
    return {
        "type": "Crab",
        "time": round(time_value, 2),
        "x": random.choice(["LEFT_SPAWN_X", "RIGHT_SPAWN_X"]),
        "speedMultiplier": random_speed(0.9, 1.3)
    }


def create_ocean_sunfish(time_value):
    return {
        "type": "OceanSunfish",
        "time": round(time_value, 2),
        "x": random.choice(["LEFT_SPAWN_X", "RIGHT_SPAWN_X"]),
        "y": "SCREEN_HEIGHT",
        "yDivide": random.choice([2, 3]),
        "speedMultiplier": random_speed(0.8, 1.1)
    }


def create_jellyfish(time_value):
    return {
        "type": "Jellyfish",
        "time": round(time_value, 2),
        "x": random.choice(["LEFT_SPAWN_X", "RIGHT_SPAWN_X"]),
        "y": random.randint(250, 550),
        "speedMultiplier": random_speed(0.9, 1.1)
    }


def create_shark(time_value):
    moving_left = random.choice([True, False])

    return {
        "type": "Shark",
        "time": round(time_value, 2),
        "x": "LEFT_SPAWN_X" if moving_left else "RIGHT_SPAWN_X",
        "movingLeft": "true" if moving_left else "false",
        "speedMultiplier": random_speed(1.0, 1.4)
    }


def create_marlin(time_value):
    return {
        "type": "Marlin",
        "time": round(time_value, 2),
        "x": random.choice(["LEFT_SPAWN_X", "RIGHT_SPAWN_X"]),
        "y": float(random.randint(350, 550)),
        "speedMultiplier": random_speed(1.0, 1.3)
    }


# ============================================================
# IMPROVED MACKEREL GENERATION
# ============================================================
#
# MOST mackerels travel across the ENTIRE screen.
#
# Only a SMALL number travel halfway.
#
# Full-cross examples:
# LEFT_SPAWN_X -> GAME_WIDTH
# RIGHT_SPAWN_X -> 0
#
# Half-cross examples:
# LEFT_SPAWN_X -> GAME_WIDTH / 2
# RIGHT_SPAWN_X -> GAME_WIDTH / 2
#
# ============================================================

def create_mackerel(time_value):

    # 85% chance to cross the full screen
    full_cross = random.random() < 0.85

    start_from_left = random.choice([True, False])

    if start_from_left:

        start_x = random.choice(["LEFT_SPAWN_X", 0])

        if full_cross:
            target_divide = 1
            target_offset = random.randint(-80, 80)
        else:
            target_divide = random.choice([1.6, 1.8, 2])
            target_offset = random.randint(50, 150)

        return {
            "type": "Mackerel",
            "time": round(time_value, 2),

            "startX": start_x,
            "startXOffset": random.randint(20, 80),

            "targetX": "GAME_WIDTH",
            "targetXDivide": target_divide,
            "targetXOffset": target_offset,

            "speedMultiplier": random_speed(0.9, 1.3)
        }

    else:

        start_x = random.choice(["RIGHT_SPAWN_X", "GAME_WIDTH"])

        if full_cross:
            target_divide = 1
            target_offset = random.randint(-80, 80)
        else:
            target_divide = random.choice([1.6, 1.8, 2])
            target_offset = -random.randint(50, 150)

        return {
            "type": "Mackerel",
            "time": round(time_value, 2),

            "startX": start_x,
            "startXOffset": -random.randint(20, 80),

            "targetX": 0,
            "targetXDivide": target_divide,
            "targetXOffset": target_offset,

            "speedMultiplier": random_speed(0.9, 1.3)
        }


CREATURE_CREATORS = {
    "Crab": create_crab,
    "Mackerel": create_mackerel,
    "OceanSunfish": create_ocean_sunfish,
    "Jellyfish": create_jellyfish,
    "Shark": create_shark,
    "Marlin": create_marlin
}


# ============================================================
# RANDOM CREATURE PICKER
# ============================================================

creature_names = list(CONFIG["CREATURE_PERCENTAGES"].keys())
creature_weights = list(CONFIG["CREATURE_PERCENTAGES"].values())


def random_creature():
    return random.choices(
        creature_names,
        weights=creature_weights,
        k=1
    )[0]


# ============================================================
# SPAWN TIME GENERATION
# ============================================================
#
# Creatures are spread naturally across the wave.
# Intervals are random instead of fixed.
#
# ============================================================

def generate_spawn_times(duration, creature_count):

    usable_time = duration - 0.4

    weights = [
        random.uniform(0.5, 1.5)
        for _ in range(creature_count)
    ]

    total_weight = sum(weights)

    intervals = [
        (w / total_weight) * usable_time
        for w in weights
    ]

    current_time = 0.0

    times = []

    for interval in intervals:
        current_time += interval
        times.append(round(current_time, 2))

    shift = random.uniform(0.0, 0.8)

    times = [
        max(0.0, round(t - shift, 2))
        for t in times
    ]

    times = [
        min(duration - 0.1, t)
        for t in times
    ]

    return sorted(times)


# ============================================================
# WAVE GENERATION
# ============================================================

def generate_wave():

    duration = round(random.uniform(
        CONFIG["MIN_WAVE_DURATION"],
        CONFIG["MAX_WAVE_DURATION"]
    ), 1)

    creature_count = random.randint(
        CONFIG["MIN_CREATURES_PER_WAVE"],
        CONFIG["MAX_CREATURES_PER_WAVE"]
    )

    spawn_times = generate_spawn_times(
        duration,
        creature_count
    )

    spawns = []

    for time_value in spawn_times:

        creature_type = random_creature()

        creature = CREATURE_CREATORS[creature_type](
            time_value
        )

        spawns.append(creature)

    return {
        "duration": duration,
        "spawns": spawns
    }


# ============================================================
# MAIN
# ============================================================

def main():

    waves = []

    for _ in range(CONFIG["NUM_WAVES"]):
        waves.append(generate_wave())

    with open(CONFIG["OUTPUT_FILE"], "w") as file:
        json.dump(waves, file, indent=2)

    print(f"Generated {CONFIG['NUM_WAVES']} waves")
    print(f"Saved to {CONFIG['OUTPUT_FILE']}")


if __name__ == "__main__":
    main()

