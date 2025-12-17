package com.fujitsu.labs.virtualhome

/**
 * Object containing resource data as constant strings.
 */
object ResourceData {
    const val OBJECT_STATES = """{
  "razor": [
    "grabbed",
    "dirty",
    "clean"
  ],
  "needle": [
    "grabbed"
  ],
  "console": [
    "on",
    "grabbed",
    "plugged",
    "broken",
    "closed",
    "off",
    "open",
    "unplugged"
  ],
  "broom": [
    "grabbed"
  ],
  "hands_left": [
    "dirty",
    "clean",
    "hurt"
  ],
  "creditcard": [
    "grabbed"
  ],
  "cup": [
    "full",
    "grabbed",
    "hot",
    "dirty",
    "clean",
    "cold",
    "empty"
  ],
  "newspaper": [
    "open",
    "grabbed"
  ],
  "toy": [
    "grabbed",
    "dirty",
    "clean"
  ],
  "wall": [
    "dirty",
    "clean"
  ],
  "food_oatmeal": [
    "cooked",
    "uncooked",
    "grabbed",
    "cold",
    "hot"
  ],
  "mat": [
    "folded",
    "unfolded",
    "grabbed",
    "dirty",
    "clean"
  ],
  "document": [
    "cut",
    "grabbed"
  ],
  "soap": [
    "dry",
    "grabbed",
    "wet"
  ],
  "foundation": [
    "grabbed"
  ],
  "ceiling": [
    "dirty",
    "clean"
  ],
  "bathtub": [
    "dry",
    "free",
    "dirty",
    "clean",
    "wet",
    "occupied"
  ],
  "shower": [
    "on",
    "off",
    "free",
    "hot",
    "dirty",
    "clean",
    "cold",
    "occupied"
  ],
  "food_kiwi": [
    "cut",
    "grabbed",
    "peeled",
    "rotten",
    "full",
    "cold"
  ],
  "dirt": [
    "grabbed"
  ],
  "blanket": [
    "folded",
    "unfolded",
    "grabbed",
    "dirty",
    "clean"
  ],
  "mop": [
    "dry",
    "grabbed",
    "dirty",
    "clean",
    "wet"
  ],
  "instrument_piano": [
    "broken",
    "occupied",
    "free"
  ],
  "hands_right": [
    "dirty",
    "clean",
    "hurt"
  ],
  "mouthwash": [
    "grabbed"
  ],
  "light_bulb": [
    "on",
    "grabbed",
    "broken",
    "hot",
    "off",
    "cold"
  ],
  "glass": [
    "full",
    "grabbed",
    "broken",
    "dirty",
    "clean",
    "empty"
  ],
  "sofa": [
    "clean",
    "occupied",
    "dirty",
    "free"
  ],
  "diary": [
    "open",
    "grabbed"
  ],
  "sink": [
    "full",
    "free",
    "dirty",
    "clean",
    "occupied",
    "empty"
  ],
  "sheets": [
    "folded",
    "unfolded",
    "grabbed",
    "dirty",
    "clean"
  ],
  "piano_bench": [
    "free",
    "occupied",
    "grabbed",
    "dirty",
    "clean"
  ],
  "paper_towel": [
    "dry",
    "grabbed",
    "dirty",
    "clean",
    "wet",
    "empty"
  ],
  "band-aids": [
    "grabbed"
  ],
  "hairdryer": [
    "on",
    "grabbed",
    "plugged",
    "broken",
    "off",
    "unplugged"
  ],
  "scrabble": [
    "grabbed"
  ],
  "chessboard": [
    "grabbed"
  ],
  "food_egg": [
    "grabbed",
    "cooked",
    "hot",
    "broken",
    "rotten",
    "cold",
    "uncooked"
  ],
  "food_ice_cream": [
    "frozen",
    "melted",
    "grabbed"
  ],
  "wine_glass": [
    "full",
    "grabbed",
    "hot",
    "dirty",
    "clean",
    "cold",
    "empty"
  ],
  "food_carrot": [
    "cooked",
    "uncooked",
    "grabbed",
    "dirty",
    "clean"
  ],
  "stereo": [
    "on",
    "grabbed",
    "plugged",
    "off",
    "open",
    "unplugged"
  ],
  "coffee_cup": [
    "dry",
    "full",
    "grabbed",
    "hot",
    "dirty",
    "clean",
    "cold",
    "empty"
  ],
  "towel": [
    "dry",
    "grabbed",
    "folded",
    "unfolded",
    "dirty",
    "clean",
    "wet"
  ],
  "cd_player": [
    "on",
    "grabbed",
    "closed",
    "plugged",
    "broken",
    "dirty",
    "clean",
    "off",
    "open",
    "unplugged"
  ],
  "toothbrush": [
    "grabbed",
    "dirty",
    "clean"
  ],
  "notes": [
    "grabbed"
  ],
  "shampoo": [
    "open",
    "full",
    "grabbed",
    "closed",
    "empty"
  ],
  "placemat": [
    "grabbed",
    "dirty",
    "clean"
  ],
  "cloth_napkin": [
    "dry",
    "grabbed",
    "free",
    "dirty",
    "clean",
    "wet",
    "occupied"
  ],
  "cat": [
    "grabbed",
    "pleased"
  ],
  "bag": [
    "full",
    "grabbed",
    "closed",
    "dirty",
    "clean",
    "open",
    "empty"
  ],
  "magazine": [
    "open",
    "grabbed"
  ],
  "plate": [
    "dry",
    "grabbed",
    "hot",
    "dirty",
    "clean",
    "wet",
    "cold"
  ],
  "cards": [
    "grabbed"
  ],
  "coin": [
    "grabbed"
  ],
  "bills": [
    "grabbed"
  ],
  "filing_cabinet": [
    "full",
    "closed",
    "dirty",
    "clean",
    "open",
    "empty"
  ],
  "pillow": [
    "grabbed",
    "dirty",
    "clean"
  ],
  "food_food": [
    "grabbed",
    "sliced",
    "cooked",
    "hot",
    "dirty",
    "clean",
    "uncooked",
    "cold"
  ],
  "wine": [
    "hot",
    "full",
    "grabbed",
    "cold",
    "empty"
  ],
  "rag": [
    "folded",
    "unfolded",
    "grabbed",
    "dirty",
    "clean"
  ],
  "coffee_pot": [
    "grabbed",
    "burning",
    "hot",
    "dirty",
    "clean",
    "cold",
    "open"
  ],
  "fax_machine": [
    "on",
    "off",
    "unplugged",
    "plugged"
  ],
  "pantry": [
    "full",
    "open",
    "closed",
    "empty"
  ],
  "laptop": [
    "on",
    "grabbed",
    "off",
    "broken"
  ],
  "purse": [
    "open",
    "full",
    "grabbed",
    "closed",
    "empty"
  ],
  "telephone": [
    "on",
    "grabbed",
    "off"
  ],
  "food_bacon": [
    "frozen",
    "cooked",
    "uncooked",
    "grabbed",
    "hot"
  ],
  "food_banana": [
    "cut",
    "grabbed",
    "peeled",
    "hot",
    "rotten",
    "full",
    "cold"
  ],
  "juice": [
    "cold",
    "grabbed",
    "hot",
    "rotten"
  ],
  "washing_machine": [
    "on",
    "full",
    "off",
    "broken",
    "closed",
    "open",
    "empty"
  ],
  "keyboard": [
    "occupied",
    "grabbed",
    "unplugged",
    "free",
    "plugged"
  ],
  "trashcan": [
    "full",
    "open",
    "closed",
    "empty"
  ],
  "man": [
    "happy",
    "dirty",
    "pleased",
    "clean",
    "sad"
  ],
  "mouse": [
    "occupied",
    "grabbed",
    "unplugged",
    "free",
    "plugged"
  ],
  "hairbrush": [
    "grabbed"
  ],
  "measuring_cup": [
    "empty",
    "full",
    "grabbed",
    "dirty",
    "clean"
  ],
  "headset": [
    "broken",
    "grabbed"
  ],
  "food_butter": [
    "cold",
    "grabbed",
    "hot"
  ],
  "video_game_console": [
    "on",
    "off",
    "open",
    "free",
    "plugged",
    "closed",
    "occupied",
    "unplugged"
  ],
  "food_fish": [
    "grabbed",
    "burning",
    "frozen",
    "peeled",
    "rotten",
    "cooked",
    "hot",
    "cold",
    "uncooked"
  ],
  "bench": [
    "occupied",
    "empty",
    "free"
  ],
  "arms_left": [
    "dirty",
    "clean"
  ],
  "music_stand": [
    "occupied",
    "unfolded",
    "open",
    "free",
    "folded"
  ],
  "sauce_pan": [
    "cold",
    "grabbed",
    "dirty",
    "clean",
    "hot"
  ],
  "clothes_hat": [
    "occupied",
    "grabbed",
    "free"
  ],
  "freezer": [
    "on",
    "off",
    "clean",
    "dirty",
    "closed",
    "open"
  ],
  "clothes_dress": [
    "grabbed",
    "folded",
    "free",
    "unfolded",
    "dirty",
    "clean",
    "occupied"
  ],
  "shoe-shine_kit": [
    "open",
    "grabbed",
    "empty"
  ],
  "bookshelf": [
    "clean",
    "full",
    "open",
    "dirty",
    "empty"
  ],
  "mug": [
    "clean",
    "full",
    "grabbed",
    "dirty",
    "empty"
  ],
  "tape": [
    "broken",
    "cut",
    "grabbed",
    "full"
  ],
  "legs_both": [
    "dirty",
    "clean"
  ],
  "curtain": [
    "open",
    "dirty",
    "clean",
    "closed"
  ],
  "dog": [
    "grabbed",
    "pleased",
    "hurt"
  ],
  "food_chicken": [
    "full",
    "grabbed",
    "burning",
    "cooked",
    "hot",
    "cut",
    "uncooked",
    "cold"
  ],
  "homework": [
    "grabbed"
  ],
  "dresser": [
    "full",
    "closed",
    "dirty",
    "clean",
    "open",
    "empty"
  ],
  "fork": [
    "grabbed",
    "dirty",
    "clean"
  ],
  "pencil": [
    "grabbed"
  ],
  "coffee": [
    "hot",
    "grabbed",
    "cold"
  ],
  "printer": [
    "on",
    "open",
    "off"
  ],
  "door": [
    "open",
    "closed"
  ],
  "form": [
    "occupied",
    "grabbed",
    "free"
  ],
  "cookingpot": [
    "grabbed",
    "closed",
    "hot",
    "dirty",
    "clean",
    "cold",
    "open"
  ],
  "dvd_player": [
    "on",
    "grabbed",
    "plugged",
    "broken",
    "closed",
    "off",
    "open",
    "unplugged"
  ],
  "duster": [
    "grabbed",
    "dirty",
    "clean"
  ],
  "blow_dryer": [
    "on",
    "grabbed",
    "off",
    "broken"
  ],
  "dustpan": [
    "occupied",
    "clean",
    "grabbed",
    "dirty",
    "free"
  ],
  "bird": [
    "pleased",
    "hurt"
  ],
  "water": [
    "frozen",
    "cold",
    "hot",
    "burning"
  ],
  "controller": [
    "grabbed"
  ],
  "ironing_board": [
    "folded",
    "unfolded",
    "occupied",
    "free"
  ],
  "eyes_both": [
    "open",
    "closed"
  ],
  "knife": [
    "dry",
    "grabbed",
    "unsharpened",
    "free",
    "dirty",
    "clean",
    "wet",
    "sharpened",
    "occupied"
  ],
  "ground_coffee": [
    "open",
    "grabbed",
    "closed"
  ],
  "water_glass": [
    "full",
    "grabbed",
    "hot",
    "dirty",
    "clean",
    "cold",
    "empty"
  ],
  "box": [
    "open",
    "full",
    "grabbed",
    "empty",
    "closed"
  ],
  "cellphone": [
    "on",
    "grabbed",
    "off",
    "broken"
  ],
  "oven": [
    "on",
    "off",
    "hot",
    "closed",
    "cold",
    "open"
  ],
  "food_salt": [
    "full",
    "grabbed",
    "empty"
  ],
  "tooth_paste": [
    "open",
    "full",
    "grabbed",
    "closed",
    "empty"
  ],
  "light": [
    "on",
    "off",
    "broken"
  ],
  "hands_both": [
    "dirty",
    "clean"
  ],
  "microwave": [
    "on",
    "off",
    "closed",
    "occupied",
    "free",
    "dirty",
    "clean",
    "open"
  ],
  "laundry_detergent": [
    "full",
    "grabbed",
    "empty"
  ],
  "colander": [
    "dry",
    "grabbed",
    "dirty",
    "clean",
    "wet"
  ],
  "food_vegetable": [
    "full",
    "grabbed",
    "burning",
    "frozen",
    "peeled",
    "rotten",
    "cooked",
    "hot",
    "cut",
    "uncooked",
    "cold",
    "eatable"
  ],
  "woman": [
    "sad",
    "hot",
    "dirty",
    "clean",
    "cold",
    "pleased",
    "happy"
  ],
  "conditioner": [
    "full",
    "grabbed",
    "empty"
  ],
  "drawing": [
    "unhanged",
    "grabbed",
    "hanged"
  ],
  "slippers": [
    "occupied",
    "clean",
    "grabbed",
    "dirty",
    "free"
  ],
  "food_cheese": [
    "cut",
    "grabbed",
    "hot",
    "cooked",
    "rotten",
    "full",
    "uncooked",
    "cold"
  ],
  "drying_rack": [
    "clean",
    "full",
    "dirty",
    "empty"
  ],
  "instrument_violin": [
    "grabbed"
  ],
  "laser_pointer": [
    "on",
    "grabbed",
    "off"
  ],
  "hanger": [
    "occupied",
    "full",
    "grabbed",
    "empty",
    "free"
  ],
  "eye_right": [
    "open",
    "pleased",
    "closed"
  ],
  "food_cake": [
    "sliced",
    "cold",
    "full",
    "grabbed"
  ],
  "bathroom_cabinet": [
    "full",
    "closed",
    "dirty",
    "clean",
    "open",
    "empty"
  ],
  "pajamas": [
    "grabbed",
    "folded",
    "free",
    "unfolded",
    "dirty",
    "clean",
    "occupied"
  ],
  "electric_shaver": [
    "on",
    "grabbed",
    "plugged",
    "dirty",
    "clean",
    "off",
    "unplugged"
  ],
  "basket_for_clothes": [
    "open",
    "full",
    "grabbed",
    "empty"
  ],
  "face_soap": [
    "grabbed"
  ],
  "check": [
    "broken",
    "full",
    "grabbed"
  ],
  "hair": [
    "combed",
    "grabbed",
    "uncombed",
    "clean",
    "dirty"
  ],
  "paper": [
    "grabbed"
  ],
  "nightstand": [
    "broken",
    "open",
    "closed"
  ],
  "brush": [
    "dry",
    "grabbed",
    "dirty",
    "clean",
    "wet"
  ],
  "food_orange": [
    "peeled ",
    "cut",
    "grabbed",
    "full",
    "dirty",
    "clean",
    "squeezed"
  ],
  "mirror": [
    "dirty",
    "clean",
    "hanged"
  ],
  "comforter": [
    "occupied",
    "full",
    "grabbed",
    "empty",
    "free"
  ],
  "candle": [
    "on",
    "grabbed",
    "off",
    "burning"
  ],
  "chair": [
    "occupied",
    "grabbed",
    "free"
  ],
  "carpet": [
    "unfolded",
    "folded",
    "dirty",
    "clean"
  ],
  "mousepad": [
    "dirty",
    "clean"
  ],
  "groceries": [
    "grabbed"
  ],
  "lighting": [
    "on",
    "off"
  ],
  "lightswitch": [
    "on",
    "off"
  ],
  "microphone": [
    "on",
    "grabbed",
    "off"
  ],
  "clothes_underwear": [
    "grabbed",
    "folded",
    "free",
    "unfolded",
    "dirty",
    "clean",
    "occupied",
    "empty"
  ],
  "closet": [
    "full",
    "open",
    "empty",
    "closed"
  ],
  "clothes_jacket": [
    "grabbed",
    "folded",
    "free",
    "unfolded",
    "dirty",
    "clean",
    "occupied"
  ],
  "stamp": [
    "grabbed"
  ],
  "kettle": [
    "on",
    "full",
    "grabbed",
    "hot",
    "dirty",
    "clean",
    "off",
    "cold",
    "empty"
  ],
  "bowl": [
    "dry",
    "grabbed",
    "hot",
    "dirty",
    "clean",
    "wet",
    "cold"
  ],
  "dishrack": [
    "clean",
    "full",
    "grabbed",
    "dirty",
    "empty"
  ],
  "pasta": [
    "cooked",
    "uncooked",
    "grabbed",
    "cold",
    "hot"
  ],
  "lamp": [
    "on",
    "off"
  ],
  "book": [
    "open",
    "grabbed"
  ],
  "clothes_skirt": [
    "grabbed",
    "folded",
    "free",
    "unfolded",
    "dirty",
    "clean",
    "occupied"
  ],
  "bathroom_counter": [
    "full",
    "closed",
    "open",
    "free",
    "dirty",
    "clean",
    "occupied",
    "empty"
  ],
  "pen": [
    "grabbed"
  ],
  "food_snack": [
    "cooked",
    "uncooked",
    "grabbed"
  ],
  "cabinet": [
    "full",
    "closed",
    "dirty",
    "clean",
    "open",
    "empty"
  ],
  "sauce": [
    "hot",
    "full",
    "grabbed",
    "cold",
    "empty"
  ],
  "kitchen_cabinet": [
    "open",
    "dirty",
    "clean",
    "closed"
  ],
  "alcohol": [
    "full",
    "grabbed",
    "burning",
    "frozen",
    "hot",
    "cold",
    "empty"
  ],
  "food_cereal": [
    "full",
    "grabbed",
    "closed",
    "cold",
    "open",
    "empty"
  ],
  "centerpiece": [
    "grabbed",
    "dirty",
    "clean"
  ],
  "cupboard": [
    "full",
    "closed",
    "dirty",
    "clean",
    "open",
    "empty"
  ],
  "tea_bag": [
    "dry",
    "cooked",
    "uncooked",
    "grabbed",
    "wet"
  ],
  "keys": [
    "grabbed"
  ],
  "shoes": [
    "occupied",
    "clean",
    "grabbed",
    "dirty",
    "free"
  ],
  "coffee_table": [
    "clean",
    "occupied",
    "dirty",
    "free"
  ],
  "arms_both": [
    "dirty",
    "clean",
    "hurt"
  ],
  "phone": [
    "on",
    "grabbed",
    "plugged",
    "broken",
    "off",
    "unplugged"
  ],
  "spoon": [
    "dry",
    "grabbed",
    "dirty",
    "clean",
    "wet"
  ],
  "notebook": [
    "open",
    "grabbed"
  ],
  "kitchen_counter": [
    "closed",
    "open",
    "free",
    "dirty",
    "clean",
    "occupied"
  ],
  "lighter": [
    "on",
    "grabbed",
    "off"
  ],
  "cleaning_bottle": [
    "full",
    "grabbed",
    "broken",
    "closed",
    "open",
    "empty"
  ],
  "food_onion": [
    "cut",
    "grabbed",
    "peeled",
    "hot",
    "cooked",
    "rotten",
    "uncooked",
    "cold"
  ],
  "comb": [
    "grabbed"
  ],
  "shredder": [
    "on",
    "occupied",
    "off",
    "free"
  ],
  "fly": [
    "pleased",
    "hurt"
  ],
  "faucet": [
    "on",
    "off"
  ],
  "alarm_clock": [
    "on",
    "grabbed",
    "off",
    "broken"
  ],
  "toilet": [
    "closed",
    "open",
    "free",
    "dirty",
    "clean",
    "flushed",
    "occupied"
  ],
  "clothes_socks": [
    "grabbed",
    "folded",
    "free",
    "unfolded",
    "dirty",
    "clean",
    "occupied"
  ],
  "bed": [
    "done",
    "undone",
    "occupied",
    "free"
  ],
  "cd": [
    "broken",
    "grabbed",
    "dirty",
    "clean"
  ],
  "dish_soap": [
    "full",
    "grabbed",
    "empty"
  ],
  "facial_cleanser": [
    "grabbed",
    "empty"
  ],
  "shaving_cream": [
    "full",
    "grabbed",
    "empty"
  ],
  "legs_left": [
    "dirty",
    "clean"
  ],
  "love_seat": [
    "occupied",
    "empty",
    "free"
  ],
  "painting": [
    "unhanged",
    "grabbed",
    "hanged"
  ],
  "fridge": [
    "on",
    "off",
    "closed",
    "dirty",
    "clean",
    "open"
  ],
  "board_game": [
    "occupied",
    "grabbed",
    "free"
  ],
  "video_game_controller": [
    "on",
    "grabbed",
    "plugged",
    "broken",
    "off",
    "unplugged"
  ],
  "cutting_board": [
    "occupied",
    "clean",
    "grabbed",
    "dirty",
    "free"
  ],
  "nail_polish": [
    "open",
    "grabbed",
    "dirty",
    "empty",
    "closed"
  ],
  "coffee_filter": [
    "grabbed",
    "dirty",
    "clean"
  ],
  "arms_right": [
    "dirty",
    "clean"
  ],
  "food_steak": [
    "cut",
    "grabbed",
    "frozen",
    "hot",
    "cooked",
    "rotten",
    "full",
    "uncooked"
  ],
  "receipt": [
    "grabbed"
  ],
  "beer": [
    "open",
    "cold",
    "grabbed",
    "closed"
  ],
  "food_potato": [
    "full",
    "grabbed",
    "rotten",
    "cooked",
    "hot",
    "cut",
    "uncooked",
    "cold"
  ],
  "food_donut": [
    "cut",
    "grabbed",
    "full"
  ],
  "crayon": [
    "grabbed"
  ],
  "food_lemon": [
    "cooked",
    "uncooked",
    "cut",
    "grabbed",
    "full"
  ],
  "table_cloth": [
    "folded",
    "unfolded",
    "grabbed",
    "dirty",
    "clean"
  ],
  "floor_lamp": [
    "on",
    "off",
    "broken"
  ],
  "scissors": [
    "grabbed"
  ],
  "glue": [
    "full",
    "grabbed",
    "empty"
  ],
  "address_book": [
    "open",
    "grabbed"
  ],
  "stove": [
    "on",
    "off",
    "hot",
    "closed",
    "cold",
    "open"
  ],
  "eye_left": [
    "open",
    "closed"
  ],
  "spectacles": [
    "grabbed",
    "dirty",
    "clean"
  ],
  "vacuum_cleaner": [
    "on",
    "full",
    "grabbed",
    "plugged",
    "broken",
    "unplugged",
    "off",
    "empty"
  ],
  "drinking_glass": [
    "full",
    "grabbed",
    "hot",
    "dirty",
    "clean",
    "cold",
    "empty"
  ],
  "highlighter": [
    "grabbed"
  ],
  "floor": [
    "dirty",
    "clean"
  ],
  "bookmark": [
    "occupied",
    "grabbed"
  ],
  "food_pizza": [
    "cooked",
    "uncooked",
    "grabbed",
    "cold",
    "hot"
  ],
  "toaster": [
    "on",
    "off",
    "free",
    "dirty",
    "clean",
    "occupied"
  ],
  "food_noodles": [
    "cooked",
    "uncooked",
    "grabbed",
    "cold",
    "hot"
  ],
  "ice": [
    "melted",
    "grabbed"
  ],
  "couch": [
    "occupied",
    "free",
    "clean",
    "dirty"
  ],
  "detergent": [
    "full",
    "grabbed",
    "empty"
  ],
  "window": [
    "open",
    "clean",
    "closed",
    "dirty"
  ],
  "food_peanut_butter": [
    "hot",
    "full",
    "grabbed",
    "cold",
    "empty"
  ],
  "fryingpan": [
    "cold",
    "grabbed",
    "hot",
    "clean",
    "dirty"
  ],
  "clothes_scarf": [
    "grabbed",
    "folded",
    "free",
    "unfolded",
    "dirty",
    "clean",
    "occupied"
  ],
  "instrument_guitar": [
    "on",
    "grabbed",
    "off",
    "broken"
  ],
  "mail": [
    "open",
    "grabbed",
    "closed"
  ],
  "folder": [
    "open",
    "grabbed",
    "closed"
  ],
  "food_apple": [
    "cut",
    "grabbed",
    "peeled",
    "cooked",
    "rotten",
    "full",
    "dirty",
    "clean",
    "uncooked",
    "cold"
  ],
  "toilet_paper": [
    "dry",
    "cut",
    "grabbed",
    "full",
    "dirty",
    "clean",
    "wet"
  ],
  "teeth": [
    "grabbed",
    "dirty",
    "clean"
  ],
  "food_bread": [
    "full",
    "grabbed",
    "cooked",
    "hot",
    "cut",
    "uncooked",
    "cold"
  ],
  "wooden_spoon": [
    "dry",
    "grabbed",
    "dirty",
    "clean",
    "wet"
  ],
  "wall_clock": [
    "on",
    "grabbed",
    "broken",
    "hanged",
    "off",
    "unhanged"
  ],
  "oil": [
    "cold",
    "full",
    "grabbed",
    "hot",
    "empty"
  ],
  "thread": [
    "grabbed"
  ],
  "toothbrush_holder": [
    "occupied",
    "grabbed",
    "free"
  ],
  "coffe_maker": [
    "on",
    "occupied",
    "open",
    "off",
    "free"
  ],
  "printing_paper": [
    "grabbed"
  ],
  "mop_bucket": [
    "clean",
    "full",
    "grabbed",
    "dirty",
    "empty"
  ],
  "envelope": [
    "open",
    "grabbed",
    "closed"
  ],
  "sponge": [
    "dry",
    "grabbed",
    "dirty",
    "clean",
    "wet"
  ],
  "computer": [
    "on",
    "plugged",
    "off",
    "unplugged",
    "broken"
  ],
  "food_jam": [
    "open",
    "cold",
    "warm",
    "grabbed",
    "closed"
  ],
  "desk": [
    "closed",
    "open",
    "free",
    "dirty",
    "clean",
    "occupied"
  ],
  "oven_mitts": [
    "occupied",
    "clean",
    "grabbed",
    "dirty",
    "free"
  ],
  "radio": [
    "on",
    "grabbed",
    "plugged",
    "broken",
    "off",
    "unplugged"
  ],
  "shoe_rack": [
    "occupied",
    "clean",
    "grabbed",
    "dirty",
    "free"
  ],
  "food_hamburger": [
    "cut",
    "grabbed",
    "frozen",
    "hot",
    "cooked",
    "rotten",
    "cold",
    "uncooked"
  ],
  "food_fruit": [
    "cut",
    "grabbed",
    "peeled",
    "cooked",
    "hot",
    "full",
    "dirty",
    "clean",
    "squeezed",
    "uncooked",
    "cold"
  ],
  "television": [
    "on",
    "off",
    "broken"
  ],
  "napkin": [
    "dry",
    "grabbed",
    "folded",
    "unfolded",
    "dirty",
    "clean",
    "wet"
  ],
  "picture": [
    "grabbed",
    "hanged"
  ],
  "electrical_outlet": [
    "on",
    "broken"
  ],
  "feet_both": [
    "hot",
    "dirty",
    "cold",
    "clean",
    "hurt"
  ],
  "button": [
    "grabbed"
  ],
  "tea": [
    "cold",
    "grabbed",
    "hot",
    "burning"
  ],
  "jelly": [
    "open",
    "cold",
    "grabbed",
    "hot",
    "closed"
  ],
  "mechanical_pencil": [
    "grabbed"
  ],
  "chef_knife": [
    "dry",
    "grabbed",
    "unsharpened",
    "free",
    "dirty",
    "clean",
    "wet",
    "sharpened",
    "occupied"
  ],
  "novel": [
    "open",
    "grabbed"
  ],
  "food_dessert": [
    "cooked",
    "uncooked",
    "grabbed",
    "cold",
    "hot"
  ],
  "mini-fridge": [
    "on",
    "open",
    "off",
    "closed",
    "broken"
  ],
  "feet_left": [
    "dirty",
    "clean"
  ],
  "iron": [
    "on",
    "cold",
    "grabbed",
    "off",
    "hot"
  ],
  "crackers": [
    "open",
    "grabbed",
    "closed"
  ],
  "face": [
    "unshaved",
    "dirty",
    "clean",
    "shaved"
  ],
  "tray": [
    "free",
    "grabbed",
    "dirty",
    "clean"
  ],
  "food_turkey": [
    "grabbed",
    "burning",
    "frozen",
    "cooked",
    "hot",
    "uncooked",
    "cold"
  ],
  "food_rice": [
    "cooked",
    "uncooked",
    "grabbed",
    "cold",
    "hot"
  ],
  "dishwasher": [
    "on",
    "full",
    "off",
    "broken",
    "open",
    "empty"
  ],
  "clothes_gloves": [
    "occupied",
    "clean",
    "grabbed",
    "dirty",
    "free"
  ],
  "deck_of_cards": [
    "occupied",
    "grabbed",
    "free"
  ],
  "after_shave": [
    "open",
    "full",
    "grabbed",
    "empty"
  ],
  "clothes_pants": [
    "grabbed",
    "folded",
    "free",
    "unfolded",
    "dirty",
    "clean",
    "occupied"
  ],
  "feet_right": [
    "dirty",
    "clean"
  ],
  "food_sugar": [
    "grabbed"
  ],
  "pot": [
    "grabbed",
    "closed",
    "hot",
    "dirty",
    "clean",
    "cold",
    "open"
  ],
  "cleaning_solution": [
    "full",
    "grabbed",
    "empty"
  ],
  "child": [
    "happy",
    "dirty",
    "pleased",
    "clean",
    "sad"
  ],
  "textbook": [
    "open",
    "grabbed"
  ],
  "towel_rack": [
    "occupied",
    "full",
    "grabbed",
    "empty",
    "free"
  ],
  "note_pad": [
    "open",
    "grabbed",
    "broken"
  ],
  "dough": [
    "cooked",
    "uncooked",
    "grabbed",
    "cold",
    "hot"
  ],
  "garbage_can": [
    "full",
    "closed",
    "dirty",
    "clean",
    "open",
    "empty"
  ],
  "table": [
    "clean",
    "occupied",
    "dirty",
    "free"
  ],
  "remote_control": [
    "on",
    "grabbed"
  ],
  "blender": [
    "on",
    "grabbed",
    "clean",
    "dirty",
    "closed",
    "off",
    "open"
  ],
  "clothes_shirt": [
    "grabbed",
    "folded",
    "free",
    "unfolded",
    "dirty",
    "clean",
    "occupied"
  ],
  "dry_pasta": [
    "cooked",
    "uncooked",
    "grabbed",
    "cold",
    "hot"
  ],
  "legs_right": [
    "dirty",
    "clean"
  ],
  "milk": [
    "full",
    "grabbed",
    "hot",
    "rotten",
    "closed",
    "cold",
    "open",
    "empty"
  ]
}"""

    const val PROPERTIES_DATA = """{
  "addressbook": [
    "GRABBABLE",
    "CUTTABLE",
    "CAN_OPEN",
    "READABLE",
    "HAS_PAPER",
    "MOVABLE"
  ],
  "notes": [
    "GRABBABLE",
    "READABLE",
    "HAS_PAPER",
    "MOVABLE"
  ],
  "aftershave": [
    "GRABBABLE",
    "POURABLE",
    "CAN_OPEN",
    "MOVABLE",
    "CREAM"
  ],
  "facecream": [
    "GRABBABLE",
    "POURABLE",
    "MOVABLE",
    "CREAM"
  ],
  "alarmclock": [
    "GRABBABLE",
    "HAS_SWITCH",
    "LOOKABLE",
    "HAS_PLUG",
    "MOVABLE"
  ],
  "clock": [
    "GRABBABLE",
    "HAS_SWITCH",
    "LOOKABLE",
    "HAS_PLUG",
    "MOVABLE"
  ],
  "alcohol": [
    "GRABBABLE",
    "DRINKABLE",
    "POURABLE",
    "MOVABLE"
  ],
  "armsboth": [
    "BODY_PART"
  ],
  "armsleft": [
    "BODY_PART"
  ],
  "armsright": [
    "BODY_PART"
  ],
  "bag": [
    "GRABBABLE",
    "RECIPIENT",
    "CAN_OPEN",
    "CONTAINERS",
    "COVER_OBJECT",
    "MOVABLE"
  ],
  "band-aids": [
    "GRABBABLE",
    "CUTTABLE",
    "MOVABLE"
  ],
  "basketforclothes": [
    "GRABBABLE",
    "CAN_OPEN",
    "CONTAINERS",
    "MOVABLE"
  ],
  "clothespile": [
    "GRABBABLE",
    "CAN_OPEN",
    "CONTAINERS",
    "MOVABLE"
  ],
  "bathroomcabinet": [
    "SURFACES",
    "CAN_OPEN",
    "CONTAINERS"
  ],
  "bathroomcounter": [
    "SURFACES"
  ],
  "bathtub": [
    "SITTABLE",
    "LIEABLE"
  ],
  "bed": [
    "SURFACES",
    "SITTABLE",
    "LIEABLE"
  ],
  "beer": [
    "GRABBABLE",
    "DRINKABLE",
    "POURABLE",
    "CAN_OPEN",
    "MOVABLE"
  ],
  "bench": [
    "SURFACES",
    "SITTABLE",
    "LIEABLE",
    "MOVABLE"
  ],
  "bills": [
    "GRABBABLE",
    "CUTTABLE",
    "READABLE",
    "HAS_PAPER",
    "MOVABLE"
  ],
  "bird": [],
  "blanket": [
    "GRABBABLE",
    "COVER_OBJECT",
    "MOVABLE"
  ],
  "blender": [
    "GRABBABLE",
    "RECIPIENT",
    "POURABLE",
    "CAN_OPEN",
    "HAS_SWITCH",
    "HAS_PLUG",
    "MOVABLE"
  ],
  "blowdryer": [
    "GRABBABLE",
    "HAS_SWITCH",
    "HAS_PLUG",
    "MOVABLE"
  ],
  "boardgame": [
    "SURFACES",
    "GRABBABLE",
    "LOOKABLE",
    "MOVABLE"
  ],
  "book": [
    "GRABBABLE",
    "CUTTABLE",
    "CAN_OPEN",
    "READABLE",
    "HAS_PAPER",
    "MOVABLE"
  ],
  "bookmark": [
    "GRABBABLE",
    "HAS_PAPER",
    "MOVABLE"
  ],
  "bookshelf": [
    "SURFACES",
    "CONTAINERS"
  ],
  "bowl": [
    "GRABBABLE",
    "RECIPIENT",
    "MOVABLE"
  ],
  "dishbowl": [
    "GRABBABLE",
    "RECIPIENT",
    "MOVABLE"
  ],
  "box": [
    "GRABBABLE",
    "RECIPIENT",
    "CAN_OPEN",
    "CONTAINERS",
    "COVER_OBJECT",
    "MOVABLE"
  ],
  "broom": [
    "GRABBABLE",
    "MOVABLE"
  ],
  "brush": [
    "GRABBABLE",
    "MOVABLE"
  ],
  "button": [
    "GRABBABLE",
    "MOVABLE"
  ],
  "cabinet": [
    "SURFACES",
    "CAN_OPEN",
    "CONTAINERS"
  ],
  "candle": [
    "GRABBABLE",
    "HAS_SWITCH",
    "MOVABLE"
  ],
  "cards": [
    "GRABBABLE",
    "CUTTABLE",
    "HAS_PAPER",
    "MOVABLE"
  ],
  "carpet": [
    "SURFACES",
    "MOVABLE"
  ],
  "rug": [
    "SURFACES",
    "GRABBABLE",
    "SITTABLE",
    "LIEABLE",
    "MOVABLE"
  ],
  "cat": [
    "GRABBABLE",
    "MOVABLE"
  ],
  "cd": [
    "GRABBABLE",
    "MOVABLE"
  ],
  "cdplayer": [
    "SURFACES",
    "GRABBABLE",
    "CAN_OPEN",
    "HAS_SWITCH",
    "HAS_PLUG",
    "MOVABLE"
  ],
  "radio": [
    "SURFACES",
    "GRABBABLE",
    "CAN_OPEN",
    "HAS_SWITCH",
    "HAS_PLUG",
    "MOVABLE"
  ],
  "ceiling": [],
  "cellphone": [
    "GRABBABLE",
    "HAS_SWITCH",
    "HAS_PLUG",
    "MOVABLE"
  ],
  "centerpiece": [
    "GRABBABLE",
    "LOOKABLE",
    "COVER_OBJECT",
    "MOVABLE"
  ],
  "chair": [
    "SURFACES",
    "GRABBABLE",
    "SITTABLE",
    "MOVABLE"
  ],
  "check": [
    "GRABBABLE",
    "READABLE",
    "HAS_PAPER",
    "MOVABLE"
  ],
  "paper": [
    "GRABBABLE",
    "READABLE",
    "HAS_PAPER",
    "MOVABLE"
  ],
  "chefknife": [
    "GRABBABLE",
    "MOVABLE"
  ],
  "chessboard": [
    "SURFACES",
    "GRABBABLE",
    "MOVABLE"
  ],
  "child": [
    "PERSON"
  ],
  "cleaningbottle": [
    "GRABBABLE",
    "POURABLE",
    "CAN_OPEN",
    "MOVABLE"
  ],
  "dishwashingliquid": [
    "GRABBABLE",
    "POURABLE",
    "MOVABLE",
    "CREAM"
  ],
  "cleaningsolution": [
    "GRABBABLE",
    "POURABLE",
    "MOVABLE"
  ],
  "closet": [
    "CAN_OPEN",
    "CONTAINERS"
  ],
  "clothnapkin": [
    "GRABBABLE",
    "CUTTABLE",
    "COVER_OBJECT",
    "HAS_PAPER",
    "MOVABLE"
  ],
  "napkin": [
    "GRABBABLE",
    "COVER_OBJECT",
    "HAS_PAPER",
    "MOVABLE"
  ],
  "papertowel": [
    "GRABBABLE",
    "HANGABLE",
    "CUTTABLE",
    "COVER_OBJECT",
    "HAS_PAPER",
    "MOVABLE"
  ],
  "clothesdress": [
    "GRABBABLE",
    "HANGABLE",
    "CLOTHES",
    "MOVABLE"
  ],
  "clothesgloves": [
    "GRABBABLE",
    "HANGABLE",
    "CLOTHES",
    "MOVABLE"
  ],
  "clotheshat": [
    "GRABBABLE",
    "HANGABLE",
    "CLOTHES",
    "MOVABLE"
  ],
  "clothesjacket": [
    "GRABBABLE",
    "HANGABLE",
    "CLOTHES",
    "MOVABLE"
  ],
  "clothespants": [
    "GRABBABLE",
    "HANGABLE",
    "CLOTHES",
    "MOVABLE"
  ],
  "pants": [
    "GRABBABLE",
    "HANGABLE",
    "CLOTHES",
    "MOVABLE"
  ],
  "clothesscarf": [
    "GRABBABLE",
    "HANGABLE",
    "CLOTHES",
    "MOVABLE"
  ],
  "clothesshirt": [
    "GRABBABLE",
    "HANGABLE",
    "CLOTHES",
    "MOVABLE"
  ],
  "shirt": [
    "GRABBABLE",
    "HANGABLE",
    "CLOTHES",
    "MOVABLE"
  ],
  "clothesskirt": [
    "GRABBABLE",
    "HANGABLE",
    "CLOTHES",
    "MOVABLE"
  ],
  "clothessocks": [
    "GRABBABLE",
    "HANGABLE",
    "CLOTHES",
    "MOVABLE"
  ],
  "clothesunderwear": [
    "GRABBABLE",
    "HANGABLE",
    "CLOTHES",
    "MOVABLE"
  ],
  "coffemaker": [
    "RECIPIENT",
    "CAN_OPEN",
    "HAS_SWITCH",
    "CONTAINERS",
    "HAS_PLUG",
    "MOVABLE"
  ],
  "coffeemaker": [
    "RECIPIENT",
    "CAN_OPEN",
    "HAS_SWITCH",
    "CONTAINERS",
    "HAS_PLUG",
    "MOVABLE"
  ],
  "coffee": [
    "GRABBABLE",
    "DRINKABLE",
    "POURABLE",
    "MOVABLE"
  ],
  "coffeecup": [
    "GRABBABLE",
    "RECIPIENT",
    "POURABLE",
    "MOVABLE"
  ],
  "mug": [
    "GRABBABLE",
    "RECIPIENT",
    "POURABLE",
    "MOVABLE"
  ],
  "coffeefilter": [
    "GRABBABLE",
    "HAS_PAPER",
    "MOVABLE"
  ],
  "coffeepot": [
    "GRABBABLE",
    "RECIPIENT",
    "CAN_OPEN",
    "MOVABLE"
  ],
  "coffeetable": [
    "SURFACES",
    "MOVABLE"
  ],
  "coin": [
    "GRABBABLE",
    "MOVABLE"
  ],
  "colander": [
    "GRABBABLE",
    "RECIPIENT",
    "MOVABLE"
  ],
  "comb": [
    "GRABBABLE",
    "MOVABLE"
  ],
  "comforter": [
    "GRABBABLE",
    "COVER_OBJECT",
    "MOVABLE"
  ],
  "computer": [
    "HAS_SWITCH",
    "LOOKABLE"
  ],
  "cpucase": [
    "HAS_SWITCH",
    "LOOKABLE"
  ],
  "pc": [
    "HAS_SWITCH",
    "LOOKABLE"
  ],
  "conditioner": [
    "GRABBABLE",
    "POURABLE",
    "MOVABLE",
    "CREAM"
  ],
  "console": [
    "GRABBABLE",
    "CAN_OPEN",
    "HAS_SWITCH",
    "HAS_PLUG",
    "MOVABLE"
  ],
  "controller": [
    "GRABBABLE",
    "HAS_PLUG",
    "MOVABLE"
  ],
  "remotecontrol": [
    "GRABBABLE",
    "HAS_SWITCH",
    "MOVABLE"
  ],
  "cookingpot": [
    "GRABBABLE",
    "RECIPIENT",
    "CAN_OPEN",
    "MOVABLE"
  ],
  "couch": [
    "SURFACES",
    "SITTABLE",
    "LIEABLE",
    "MOVABLE"
  ],
  "sofa": [
    "SURFACES",
    "SITTABLE",
    "LIEABLE",
    "MOVABLE"
  ],
  "crackers": [
    "GRABBABLE",
    "MOVABLE"
  ],
  "crayon": [
    "GRABBABLE",
    "MOVABLE"
  ],
  "crayons": [
    "GRABBABLE",
    "MOVABLE"
  ],
  "creditcard": [
    "GRABBABLE",
    "MOVABLE"
  ],
  "cup": [
    "GRABBABLE",
    "RECIPIENT",
    "POURABLE",
    "MOVABLE"
  ],
  "waterglass": [
    "GRABBABLE",
    "RECIPIENT",
    "POURABLE",
    "MOVABLE"
  ],
  "wineglass": [
    "GRABBABLE",
    "RECIPIENT",
    "POURABLE",
    "MOVABLE"
  ],
  "cupboard": [
    "CAN_OPEN",
    "CONTAINERS"
  ],
  "kitchencabinets": [
    "SURFACES",
    "CAN_OPEN",
    "CONTAINERS"
  ],
  "curtain": [
    "CAN_OPEN",
    "COVER_OBJECT",
    "MOVABLE"
  ],
  "curtains": [
    "CAN_OPEN",
    "COVER_OBJECT",
    "MOVABLE"
  ],
  "cuttingboard": [
    "SURFACES",
    "GRABBABLE",
    "MOVABLE"
  ],
  "deckofcards": [
    "GRABBABLE",
    "HAS_PAPER",
    "MOVABLE"
  ],
  "desk": [
    "SURFACES",
    "CAN_OPEN",
    "MOVABLE"
  ],
  "cputable": [
    "SURFACES",
    "CAN_OPEN",
    "MOVABLE"
  ],
  "detergent": [
    "GRABBABLE",
    "POURABLE",
    "MOVABLE"
  ],
  "diary": [
    "GRABBABLE",
    "CAN_OPEN",
    "READABLE",
    "HAS_PAPER",
    "MOVABLE"
  ],
  "journal": [
    "GRABBABLE",
    "CAN_OPEN",
    "READABLE",
    "HAS_PAPER",
    "MOVABLE"
  ],
  "dirt": [
    "GRABBABLE",
    "POURABLE",
    "MOVABLE"
  ],
  "dishsoap": [
    "GRABBABLE",
    "POURABLE",
    "MOVABLE",
    "CREAM"
  ],
  "dishrack": [
    "SURFACES",
    "GRABBABLE",
    "MOVABLE"
  ],
  "dishwasher": [
    "CAN_OPEN",
    "HAS_SWITCH",
    "CONTAINERS"
  ],
  "document": [
    "GRABBABLE",
    "READABLE",
    "HAS_PAPER",
    "MOVABLE"
  ],
  "dog": [
    "GRABBABLE",
    "MOVABLE"
  ],
  "door": [
    "CAN_OPEN"
  ],
  "dough": [
    "GRABBABLE",
    "MOVABLE",
    "CREAM"
  ],
  "drawing": [
    "GRABBABLE",
    "CUTTABLE",
    "LOOKABLE",
    "HAS_PAPER",
    "MOVABLE"
  ],
  "wallpictureframe": [
    "GRABBABLE",
    "HANGABLE",
    "HAS_PAPER",
    "MOVABLE"
  ],
  "dresser": [
    "CAN_OPEN",
    "CONTAINERS"
  ],
  "drinkingglass": [
    "GRABBABLE",
    "RECIPIENT",
    "POURABLE",
    "MOVABLE"
  ],
  "drypasta": [
    "GRABBABLE",
    "MOVABLE"
  ],
  "dryingrack": [
    "SURFACES"
  ],
  "duster": [
    "GRABBABLE",
    "MOVABLE"
  ],
  "dustpan": [
    "GRABBABLE",
    "HANGABLE",
    "MOVABLE"
  ],
  "dvdplayer": [
    "SURFACES",
    "GRABBABLE",
    "CAN_OPEN",
    "HAS_SWITCH",
    "HAS_PLUG",
    "MOVABLE"
  ],
  "electricshaver": [
    "GRABBABLE",
    "HAS_SWITCH",
    "HAS_PLUG",
    "MOVABLE"
  ],
  "electricaloutlet": [
    "HAS_SWITCH"
  ],
  "envelope": [
    "GRABBABLE",
    "CUTTABLE",
    "CAN_OPEN",
    "COVER_OBJECT",
    "HAS_PAPER",
    "MOVABLE"
  ],
  "eyeleft": [
    "CAN_OPEN",
    "BODY_PART"
  ],
  "eyeright": [
    "CAN_OPEN",
    "BODY_PART"
  ],
  "eyesboth": [
    "CAN_OPEN",
    "BODY_PART"
  ],
  "face": [
    "BODY_PART"
  ],
  "facesoap": [
    "GRABBABLE",
    "POURABLE",
    "MOVABLE",
    "CREAM"
  ],
  "barsoap": [
    "GRABBABLE",
    "MOVABLE",
    "CREAM"
  ],
  "facialcleanser": [
    "GRABBABLE",
    "POURABLE",
    "MOVABLE",
    "CREAM"
  ],
  "faucet": [
    "HAS_SWITCH"
  ],
  "faxmachine": [
    "HAS_SWITCH",
    "HAS_PLUG"
  ],
  "printer": [
    "CAN_OPEN",
    "HAS_SWITCH",
    "CONTAINERS",
    "HAS_PLUG"
  ],
  "feetboth": [
    "BODY_PART"
  ],
  "feetleft": [
    "BODY_PART"
  ],
  "feetright": [
    "BODY_PART"
  ],
  "filingcabinet": [
    "SURFACES",
    "CAN_OPEN",
    "CONTAINERS"
  ],
  "floor": [
    "SURFACES"
  ],
  "floorlamp": [
    "HAS_SWITCH",
    "MOVABLE"
  ],
  "lamp": [
    "HAS_SWITCH",
    "HAS_PLUG"
  ],
  "fly": [],
  "folder": [
    "GRABBABLE",
    "CAN_OPEN",
    "READABLE",
    "CONTAINERS",
    "HAS_PAPER",
    "MOVABLE"
  ],
  "foodapple": [
    "GRABBABLE",
    "EATABLE",
    "CUTTABLE",
    "MOVABLE"
  ],
  "apple": [
    "GRABBABLE",
    "MOVABLE"
  ],
  "foodbacon": [
    "GRABBABLE",
    "EATABLE",
    "CUTTABLE",
    "MOVABLE"
  ],
  "foodbanana": [
    "GRABBABLE",
    "EATABLE",
    "CUTTABLE",
    "MOVABLE"
  ],
  "banana": [
    "GRABBABLE",
    "MOVABLE"
  ],
  "foodbread": [
    "GRABBABLE",
    "EATABLE",
    "CUTTABLE",
    "MOVABLE"
  ],
  "bread": [
    "GRABBABLE",
    "EATABLE",
    "CUTTABLE",
    "MOVABLE"
  ],
  "breadslice": [
    "GRABBABLE",
    "EATABLE",
    "CUTTABLE",
    "MOVABLE"
  ],
  "foodbutter": [
    "GRABBABLE",
    "MOVABLE",
    "CREAM"
  ],
  "foodcake": [
    "GRABBABLE",
    "EATABLE",
    "CUTTABLE",
    "MOVABLE"
  ],
  "foodcarrot": [
    "GRABBABLE",
    "EATABLE",
    "CUTTABLE",
    "MOVABLE"
  ],
  "carrot": [
    "GRABBABLE",
    "EATABLE",
    "CUTTABLE",
    "MOVABLE"
  ],
  "foodcereal": [
    "GRABBABLE",
    "EATABLE",
    "POURABLE",
    "CAN_OPEN",
    "MOVABLE"
  ],
  "cereal": [
    "GRABBABLE",
    "EATABLE",
    "MOVABLE"
  ],
  "foodcheese": [
    "GRABBABLE",
    "EATABLE",
    "CUTTABLE",
    "MOVABLE",
    "CREAM"
  ],
  "foodchicken": [
    "GRABBABLE",
    "EATABLE",
    "CUTTABLE",
    "MOVABLE"
  ],
  "chicken": [
    "GRABBABLE",
    "MOVABLE"
  ],
  "fooddessert": [
    "GRABBABLE",
    "EATABLE",
    "CUTTABLE",
    "MOVABLE"
  ],
  "cupcake": [
    "GRABBABLE",
    "EATABLE",
    "MOVABLE"
  ],
  "milkshake": [
    "GRABBABLE",
    "EATABLE",
    "CUTTABLE",
    "MOVABLE"
  ],
  "pancake": [
    "GRABBABLE",
    "EATABLE",
    "CUTTABLE",
    "MOVABLE"
  ],
  "pie": [
    "GRABBABLE",
    "EATABLE",
    "CUTTABLE",
    "MOVABLE"
  ],
  "poundcake": [
    "GRABBABLE",
    "EATABLE",
    "MOVABLE"
  ],
  "pudding": [
    "GRABBABLE",
    "EATABLE",
    "CUTTABLE",
    "MOVABLE"
  ],
  "sundae": [
    "GRABBABLE",
    "EATABLE",
    "CUTTABLE",
    "MOVABLE"
  ],
  "watermelon": [
    "GRABBABLE",
    "MOVABLE"
  ],
  "creamybuns": [
    "GRABBABLE",
    "EATABLE",
    "CUTTABLE",
    "MOVABLE"
  ],
  "fooddonut": [
    "GRABBABLE",
    "MOVABLE"
  ],
  "foodegg": [
    "GRABBABLE",
    "EATABLE",
    "CUTTABLE",
    "MOVABLE"
  ],
  "foodfish": [
    "GRABBABLE",
    "EATABLE",
    "CUTTABLE",
    "MOVABLE"
  ],
  "salmon": [
    "GRABBABLE",
    "MOVABLE"
  ],
  "foodfood": [
    "GRABBABLE",
    "EATABLE",
    "CUTTABLE",
    "MOVABLE"
  ],
  "potato": [
    "GRABBABLE",
    "MOVABLE"
  ],
  "bellpepper": [
    "GRABBABLE",
    "MOVABLE"
  ],
  "chocolatesyrup": [
    "GRABBABLE",
    "EATABLE",
    "CUTTABLE",
    "MOVABLE"
  ],
  "whippedcream": [
    "GRABBABLE",
    "EATABLE",
    "CUTTABLE",
    "MOVABLE"
  ],
  "bananas": [
    "GRABBABLE",
    "MOVABLE"
  ],
  "cutlets": [
    "GRABBABLE",
    "MOVABLE"
  ],
  "chinesefood": [
    "GRABBABLE",
    "EATABLE",
    "CUTTABLE",
    "MOVABLE"
  ],
  "pear": [
    "GRABBABLE",
    "MOVABLE"
  ],
  "plum": [
    "GRABBABLE",
    "MOVABLE"
  ],
  "orange": [
    "GRABBABLE",
    "MOVABLE"
  ],
  "cucumber": [
    "GRABBABLE",
    "MOVABLE"
  ],
  "tomato": [
    "GRABBABLE",
    "MOVABLE"
  ],
  "foodfruit": [
    "GRABBABLE",
    "EATABLE",
    "CUTTABLE",
    "MOVABLE"
  ],
  "foodhamburger": [
    "GRABBABLE",
    "EATABLE",
    "CUTTABLE",
    "MOVABLE"
  ],
  "mincedmeat": [
    "GRABBABLE",
    "MOVABLE"
  ],
  "foodicecream": [
    "GRABBABLE",
    "MOVABLE",
    "CREAM"
  ],
  "foodjam": [
    "GRABBABLE",
    "EATABLE",
    "CAN_OPEN",
    "MOVABLE",
    "CREAM"
  ],
  "foodkiwi": [
    "GRABBABLE",
    "EATABLE",
    "CUTTABLE",
    "MOVABLE"
  ],
  "foodlemon": [
    "GRABBABLE",
    "EATABLE",
    "CUTTABLE",
    "MOVABLE"
  ],
  "lemon": [
    "GRABBABLE",
    "MOVABLE"
  ],
  "foodnoodles": [
    "GRABBABLE",
    "EATABLE",
    "MOVABLE"
  ],
  "foodoatmeal": [
    "GRABBABLE",
    "EATABLE",
    "MOVABLE"
  ],
  "foodonion": [
    "GRABBABLE",
    "EATABLE",
    "CUTTABLE",
    "MOVABLE"
  ],
  "foodorange": [
    "GRABBABLE",
    "EATABLE",
    "CUTTABLE",
    "MOVABLE"
  ],
  "foodpeanutbutter": [
    "GRABBABLE",
    "EATABLE",
    "MOVABLE",
    "CREAM"
  ],
  "foodpizza": [
    "GRABBABLE",
    "EATABLE",
    "CUTTABLE",
    "MOVABLE"
  ],
  "foodpotato": [
    "GRABBABLE",
    "EATABLE",
    "CUTTABLE",
    "MOVABLE"
  ],
  "foodrice": [
    "GRABBABLE",
    "EATABLE",
    "POURABLE",
    "MOVABLE"
  ],
  "foodsalt": [
    "GRABBABLE",
    "EATABLE",
    "POURABLE",
    "MOVABLE"
  ],
  "condimentshaker": [
    "GRABBABLE",
    "EATABLE",
    "POURABLE",
    "MOVABLE"
  ],
  "foodsnack": [
    "GRABBABLE",
    "EATABLE",
    "MOVABLE"
  ],
  "candybar": [
    "GRABBABLE",
    "EATABLE",
    "MOVABLE"
  ],
  "saltcrackers": [
    "GRABBABLE",
    "MOVABLE"
  ],
  "chips": [
    "GRABBABLE",
    "MOVABLE"
  ],
  "foodsteak": [
    "GRABBABLE",
    "EATABLE",
    "CUTTABLE",
    "MOVABLE"
  ],
  "foodsugar": [
    "GRABBABLE",
    "EATABLE",
    "POURABLE",
    "MOVABLE"
  ],
  "foodturkey": [
    "GRABBABLE",
    "EATABLE",
    "CUTTABLE",
    "MOVABLE"
  ],
  "foodvegetable": [
    "GRABBABLE",
    "EATABLE",
    "CUTTABLE",
    "MOVABLE"
  ],
  "salad": [
    "GRABBABLE",
    "EATABLE",
    "CUTTABLE",
    "MOVABLE"
  ],
  "fork": [
    "GRABBABLE",
    "MOVABLE"
  ],
  "cutleryfork": [
    "GRABBABLE",
    "MOVABLE"
  ],
  "form": [
    "GRABBABLE",
    "HAS_PAPER",
    "MOVABLE"
  ],
  "foundation": [
    "GRABBABLE",
    "MOVABLE",
    "CREAM"
  ],
  "freezer": [
    "CAN_OPEN",
    "HAS_SWITCH",
    "CONTAINERS",
    "HAS_PLUG"
  ],
  "fridge": [
    "CAN_OPEN",
    "HAS_SWITCH",
    "CONTAINERS",
    "HAS_PLUG"
  ],
  "fryingpan": [
    "SURFACES",
    "GRABBABLE",
    "RECIPIENT",
    "CONTAINERS",
    "MOVABLE"
  ],
  "garbagecan": [
    "CAN_OPEN",
    "CONTAINERS",
    "MOVABLE"
  ],
  "glass": [
    "GRABBABLE",
    "RECIPIENT",
    "POURABLE",
    "MOVABLE"
  ],
  "glue": [
    "GRABBABLE",
    "MOVABLE",
    "CREAM"
  ],
  "groceries": [
    "GRABBABLE",
    "MOVABLE"
  ],
  "lime": [
    "GRABBABLE",
    "MOVABLE"
  ],
  "groundcoffee": [
    "GRABBABLE",
    "CAN_OPEN",
    "MOVABLE"
  ],
  "hair": [
    "GRABBABLE",
    "CUTTABLE",
    "BODY_PART",
    "MOVABLE"
  ],
  "hairbrush": [
    "GRABBABLE",
    "MOVABLE"
  ],
  "hairdryer": [
    "GRABBABLE",
    "HAS_SWITCH",
    "HAS_PLUG",
    "MOVABLE"
  ],
  "handsboth": [
    "BODY_PART"
  ],
  "handsleft": [
    "BODY_PART"
  ],
  "handsright": [
    "BODY_PART"
  ],
  "hanger": [
    "GRABBABLE",
    "HANGABLE",
    "MOVABLE"
  ],
  "coatrack": [
    "GRABBABLE",
    "HANGABLE",
    "MOVABLE"
  ],
  "headset": [
    "GRABBABLE",
    "CLOTHES",
    "MOVABLE"
  ],
  "highlighter": [
    "GRABBABLE",
    "MOVABLE"
  ],
  "pen": [
    "GRABBABLE",
    "MOVABLE"
  ],
  "homework": [
    "GRABBABLE",
    "READABLE",
    "HAS_PAPER",
    "MOVABLE"
  ],
  "ice": [
    "GRABBABLE",
    "MOVABLE"
  ],
  "instrumentguitar": [
    "GRABBABLE",
    "HAS_SWITCH",
    "MOVABLE"
  ],
  "instrumentpiano": [],
  "instrumentviolin": [
    "GRABBABLE",
    "MOVABLE"
  ],
  "iron": [
    "GRABBABLE",
    "HAS_SWITCH",
    "HAS_PLUG",
    "MOVABLE"
  ],
  "ironingboard": [
    "SURFACES",
    "MOVABLE"
  ],
  "jelly": [
    "GRABBABLE",
    "CAN_OPEN",
    "MOVABLE",
    "CREAM"
  ],
  "juice": [
    "GRABBABLE",
    "DRINKABLE",
    "POURABLE",
    "MOVABLE"
  ],
  "kettle": [
    "GRABBABLE",
    "RECIPIENT",
    "HAS_SWITCH",
    "HAS_PLUG",
    "MOVABLE"
  ],
  "keyboard": [
    "GRABBABLE",
    "HAS_PLUG",
    "MOVABLE"
  ],
  "keys": [
    "GRABBABLE",
    "MOVABLE"
  ],
  "kitchencabinet": [
    "SURFACES",
    "CAN_OPEN",
    "CONTAINERS"
  ],
  "kitchencounter": [
    "SURFACES"
  ],
  "kitchencounterdrawers": [
    "CONTAINERS",
    "CAN_OPEN"
  ],
  "knife": [
    "GRABBABLE",
    "MOVABLE"
  ],
  "cutleryknife": [
    "GRABBABLE",
    "MOVABLE"
  ],
  "laptop": [
    "GRABBABLE",
    "HAS_SWITCH",
    "LOOKABLE",
    "HAS_PLUG",
    "MOVABLE"
  ],
  "laserpointer": [
    "GRABBABLE",
    "HAS_SWITCH",
    "MOVABLE"
  ],
  "laundrydetergent": [
    "GRABBABLE",
    "POURABLE",
    "MOVABLE"
  ],
  "legsboth": [
    "BODY_PART"
  ],
  "legsleft": [
    "BODY_PART"
  ],
  "legsright": [
    "BODY_PART"
  ],
  "light": [
    "HAS_SWITCH",
    "HAS_PLUG"
  ],
  "lightswitch": [
    "HAS_SWITCH",
    "HAS_PLUG"
  ],
  "lightbulb": [
    "GRABBABLE",
    "HAS_SWITCH",
    "MOVABLE"
  ],
  "lighter": [
    "GRABBABLE",
    "HAS_SWITCH",
    "MOVABLE"
  ],
  "lighting": [
    "HAS_SWITCH",
    "HAS_PLUG"
  ],
  "loveseat": [
    "SURFACES",
    "SITTABLE",
    "LIEABLE",
    "MOVABLE"
  ],
  "magazine": [
    "GRABBABLE",
    "CAN_OPEN",
    "READABLE",
    "COVER_OBJECT",
    "HAS_PAPER",
    "MOVABLE"
  ],
  "mail": [
    "GRABBABLE",
    "CAN_OPEN",
    "READABLE",
    "HAS_PAPER",
    "MOVABLE"
  ],
  "man": [
    "PERSON"
  ],
  "mat": [
    "SURFACES",
    "GRABBABLE",
    "SITTABLE",
    "LIEABLE",
    "MOVABLE"
  ],
  "measuringcup": [
    "GRABBABLE",
    "RECIPIENT",
    "POURABLE",
    "MOVABLE"
  ],
  "mechanicalpencil": [
    "GRABBABLE",
    "MOVABLE"
  ],
  "microphone": [
    "GRABBABLE",
    "HAS_SWITCH",
    "HAS_PLUG",
    "MOVABLE"
  ],
  "microwave": [
    "CAN_OPEN",
    "HAS_SWITCH",
    "CONTAINERS",
    "HAS_PLUG"
  ],
  "milk": [
    "GRABBABLE",
    "DRINKABLE",
    "POURABLE",
    "CAN_OPEN",
    "MOVABLE"
  ],
  "mini-fridge": [
    "CAN_OPEN",
    "HAS_SWITCH",
    "CONTAINERS",
    "HAS_PLUG"
  ],
  "mirror": [],
  "mop": [
    "GRABBABLE",
    "MOVABLE"
  ],
  "mopbucket": [
    "GRABBABLE",
    "RECIPIENT",
    "MOVABLE"
  ],
  "bucket": [
    "GRABBABLE",
    "RECIPIENT",
    "MOVABLE"
  ],
  "mouse": [
    "GRABBABLE",
    "HAS_PLUG",
    "MOVABLE"
  ],
  "mousepad": [
    "SURFACES",
    "MOVABLE"
  ],
  "mousemat": [
    "SURFACES",
    "MOVABLE"
  ],
  "mouthwash": [
    "GRABBABLE",
    "DRINKABLE",
    "POURABLE",
    "MOVABLE"
  ],
  "musicstand": [
    "SURFACES",
    "CAN_OPEN",
    "CONTAINERS",
    "MOVABLE"
  ],
  "nailpolish": [
    "GRABBABLE",
    "CAN_OPEN",
    "MOVABLE",
    "CREAM"
  ],
  "needle": [
    "GRABBABLE",
    "MOVABLE"
  ],
  "newspaper": [
    "GRABBABLE",
    "CAN_OPEN",
    "READABLE",
    "COVER_OBJECT",
    "HAS_PAPER",
    "MOVABLE"
  ],
  "nightstand": [
    "SURFACES",
    "CAN_OPEN",
    "CONTAINERS"
  ],
  "notepad": [
    "GRABBABLE",
    "CUTTABLE",
    "CAN_OPEN",
    "READABLE",
    "HAS_PAPER",
    "MOVABLE"
  ],
  "notebook": [
    "GRABBABLE",
    "CUTTABLE",
    "CAN_OPEN",
    "READABLE",
    "HAS_PAPER",
    "MOVABLE"
  ],
  "novel": [
    "GRABBABLE",
    "CUTTABLE",
    "CAN_OPEN",
    "READABLE",
    "HAS_PAPER",
    "MOVABLE"
  ],
  "oil": [
    "GRABBABLE",
    "DRINKABLE",
    "POURABLE",
    "MOVABLE"
  ],
  "condimentbottle": [
    "GRABBABLE",
    "POURABLE",
    "MOVABLE",
    "CREAM"
  ],
  "oven": [
    "CAN_OPEN",
    "HAS_SWITCH",
    "CONTAINERS",
    "HAS_PLUG"
  ],
  "stove": [
    "SURFACES",
    "CAN_OPEN",
    "HAS_SWITCH",
    "CONTAINERS"
  ],
  "ovenmitts": [
    "GRABBABLE",
    "HANGABLE",
    "CLOTHES",
    "MOVABLE"
  ],
  "painting": [
    "GRABBABLE",
    "HANGABLE",
    "HAS_PAPER",
    "MOVABLE"
  ],
  "pajamas": [
    "GRABBABLE",
    "CLOTHES",
    "MOVABLE"
  ],
  "pantry": [
    "SURFACES"
  ],
  "pasta": [
    "GRABBABLE",
    "POURABLE",
    "MOVABLE"
  ],
  "pencil": [
    "GRABBABLE",
    "MOVABLE"
  ],
  "phone": [
    "GRABBABLE",
    "HAS_SWITCH",
    "HAS_PLUG",
    "MOVABLE"
  ],
  "wallphone": [
    "GRABBABLE",
    "HAS_SWITCH",
    "HAS_PLUG",
    "MOVABLE"
  ],
  "pianobench": [
    "SURFACES",
    "GRABBABLE",
    "SITTABLE",
    "MOVABLE"
  ],
  "picture": [
    "GRABBABLE",
    "HANGABLE",
    "HAS_PAPER",
    "MOVABLE"
  ],
  "pillow": [
    "GRABBABLE",
    "MOVABLE"
  ],
  "placemat": [
    "SURFACES",
    "GRABBABLE",
    "COVER_OBJECT",
    "MOVABLE"
  ],
  "plate": [
    "SURFACES",
    "GRABBABLE",
    "RECIPIENT",
    "MOVABLE"
  ],
  "pot": [
    "GRABBABLE",
    "RECIPIENT",
    "CAN_OPEN",
    "MOVABLE"
  ],
  "printingpaper": [
    "GRABBABLE",
    "CUTTABLE",
    "HAS_PAPER",
    "MOVABLE"
  ],
  "purse": [
    "GRABBABLE",
    "RECIPIENT",
    "CAN_OPEN",
    "CONTAINERS",
    "COVER_OBJECT",
    "MOVABLE"
  ],
  "rag": [
    "GRABBABLE",
    "RECIPIENT",
    "COVER_OBJECT",
    "MOVABLE"
  ],
  "razor": [
    "GRABBABLE",
    "MOVABLE"
  ],
  "receipt": [
    "GRABBABLE",
    "READABLE",
    "HAS_PAPER",
    "MOVABLE"
  ],
  "sauce": [
    "GRABBABLE",
    "POURABLE",
    "MOVABLE",
    "CREAM"
  ],
  "saucepan": [
    "SURFACES",
    "GRABBABLE",
    "RECIPIENT",
    "CONTAINERS",
    "MOVABLE"
  ],
  "scissors": [
    "GRABBABLE",
    "MOVABLE"
  ],
  "scrabble": [
    "GRABBABLE",
    "MOVABLE"
  ],
  "shampoo": [
    "GRABBABLE",
    "POURABLE",
    "CAN_OPEN",
    "MOVABLE"
  ],
  "lotionbottle": [
    "GRABBABLE",
    "POURABLE",
    "CAN_OPEN",
    "MOVABLE"
  ],
  "hairproduct": [
    "GRABBABLE",
    "POURABLE",
    "CAN_OPEN",
    "MOVABLE"
  ],
  "shavingcream": [
    "GRABBABLE",
    "POURABLE",
    "MOVABLE",
    "CREAM"
  ],
  "sheets": [
    "GRABBABLE",
    "COVER_OBJECT",
    "MOVABLE"
  ],
  "shoe-shinekit": [
    "GRABBABLE",
    "CAN_OPEN",
    "MOVABLE"
  ],
  "shoerack": [
    "SURFACES",
    "GRABBABLE",
    "MOVABLE"
  ],
  "shoes": [
    "GRABBABLE",
    "CLOTHES",
    "MOVABLE"
  ],
  "slippers": [
    "GRABBABLE",
    "HANGABLE",
    "CLOTHES",
    "MOVABLE"
  ],
  "shower": [],
  "stall": [],
  "shredder": [
    "HAS_SWITCH",
    "CONTAINERS",
    "HAS_PLUG"
  ],
  "sink": [
    "RECIPIENT",
    "CONTAINERS"
  ],
  "soap": [
    "GRABBABLE",
    "MOVABLE",
    "CREAM"
  ],
  "spectacles": [
    "GRABBABLE",
    "CLOTHES",
    "MOVABLE"
  ],
  "glasses": [
    "GRABBABLE",
    "CLOTHES",
    "MOVABLE"
  ],
  "sponge": [
    "GRABBABLE",
    "MOVABLE"
  ],
  "washingsponge": [
    "GRABBABLE",
    "MOVABLE"
  ],
  "spoon": [
    "GRABBABLE",
    "MOVABLE"
  ],
  "stamp": [
    "GRABBABLE",
    "MOVABLE"
  ],
  "stereo": [
    "SURFACES",
    "GRABBABLE",
    "CAN_OPEN",
    "HAS_SWITCH",
    "HAS_PLUG",
    "MOVABLE"
  ],
  "table": [
    "SURFACES",
    "MOVABLE"
  ],
  "diningtable": [
    "SURFACES",
    "MOVABLE"
  ],
  "kitchentable": [
    "SURFACES",
    "MOVABLE"
  ],
  "tablecloth": [
    "SURFACES",
    "GRABBABLE",
    "COVER_OBJECT",
    "MOVABLE"
  ],
  "tape": [
    "GRABBABLE",
    "MOVABLE"
  ],
  "tea": [
    "GRABBABLE",
    "DRINKABLE",
    "POURABLE",
    "MOVABLE"
  ],
  "teabag": [
    "GRABBABLE",
    "MOVABLE"
  ],
  "teeth": [
    "GRABBABLE",
    "MOVABLE"
  ],
  "telephone": [
    "GRABBABLE",
    "HAS_SWITCH",
    "HAS_PLUG",
    "MOVABLE"
  ],
  "television": [
    "HAS_SWITCH",
    "LOOKABLE",
    "HAS_PLUG"
  ],
  "tv": [
    "HAS_SWITCH",
    "LOOKABLE",
    "HAS_PLUG"
  ],
  "walltv": [
    "HAS_SWITCH",
    "LOOKABLE",
    "HAS_PLUG"
  ],
  "textbook": [
    "GRABBABLE",
    "CUTTABLE",
    "CAN_OPEN",
    "READABLE",
    "HAS_PAPER",
    "MOVABLE"
  ],
  "thread": [
    "GRABBABLE",
    "CUTTABLE",
    "MOVABLE"
  ],
  "toaster": [
    "HAS_SWITCH",
    "CONTAINERS",
    "HAS_PLUG",
    "MOVABLE"
  ],
  "toilet": [
    "SITTABLE",
    "CAN_OPEN",
    "CONTAINERS"
  ],
  "toiletpaper": [
    "GRABBABLE",
    "HANGABLE",
    "CUTTABLE",
    "COVER_OBJECT",
    "HAS_PAPER",
    "MOVABLE"
  ],
  "toothpaste": [
    "GRABBABLE",
    "POURABLE",
    "CAN_OPEN",
    "MOVABLE",
    "CREAM"
  ],
  "toothbrush": [
    "GRABBABLE",
    "RECIPIENT",
    "MOVABLE"
  ],
  "toothbrushholder": [
    "GRABBABLE",
    "CONTAINERS",
    "MOVABLE"
  ],
  "towel": [
    "GRABBABLE",
    "COVER_OBJECT",
    "MOVABLE"
  ],
  "towelrack": [
    "SURFACES",
    "GRABBABLE",
    "MOVABLE"
  ],
  "toy": [
    "GRABBABLE",
    "MOVABLE"
  ],
  "sportsball": [
    "GRABBABLE",
    "MOVABLE"
  ],
  "teddybear": [
    "GRABBABLE",
    "MOVABLE"
  ],
  "globe": [
    "GRABBABLE",
    "MOVABLE"
  ],
  "trashcan": [
    "CAN_OPEN",
    "CONTAINERS",
    "MOVABLE"
  ],
  "tray": [
    "SURFACES",
    "GRABBABLE",
    "MOVABLE"
  ],
  "oventray": [
    "SURFACES",
    "GRABBABLE",
    "MOVABLE"
  ],
  "vacuumcleaner": [
    "GRABBABLE",
    "HAS_SWITCH",
    "HAS_PLUG",
    "MOVABLE"
  ],
  "videogameconsole": [
    "CAN_OPEN",
    "HAS_SWITCH",
    "HAS_PLUG",
    "MOVABLE"
  ],
  "videogamecontroller": [
    "GRABBABLE",
    "HAS_SWITCH",
    "HAS_PLUG",
    "MOVABLE"
  ],
  "wall": [],
  "wallclock": [
    "GRABBABLE",
    "HAS_SWITCH",
    "LOOKABLE",
    "HAS_PLUG",
    "MOVABLE"
  ],
  "washingmachine": [
    "RECIPIENT",
    "CAN_OPEN",
    "HAS_SWITCH",
    "CONTAINERS",
    "HAS_PLUG"
  ],
  "water": [
    "DRINKABLE",
    "POURABLE"
  ],
  "bottlewater": [
    "DRINKABLE",
    "POURABLE"
  ],
  "window": [
    "CAN_OPEN"
  ],
  "wine": [
    "GRABBABLE",
    "DRINKABLE",
    "POURABLE",
    "MOVABLE"
  ],
  "woman": [
    "PERSON"
  ],
  "woodenspoon": [
    "GRABBABLE",
    "MOVABLE"
  ]
}"""
}
