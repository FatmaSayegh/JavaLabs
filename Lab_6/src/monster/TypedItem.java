package monster;

public interface TypedItem {
	public boolean hasType (String type);
	public String[] getTypes();
	
	public static boolean isValidType (String type) {
		if (type == null) return false;
		
		switch (type) {
		case "Normal":
		case "Fire":
		case "Water":
		case "Electric":
		case "Grass":
			return true;
		}
		return false;
	}
	
	public static double NOT_EFFECTIVE = 0.5;
	public static double SUPER_EFFECTIVE = 2.0;
	public static double NORMAL_EFFECTIVE = 1.0;
	
	public static double getEffectiveness (String attackType, String defendType) {
		double effectiveness = NORMAL_EFFECTIVE;
		
		switch (attackType) {
		case "Fire":
		 	if (defendType.equals("Fire") || defendType.equals("Water")) {
		 		effectiveness = NOT_EFFECTIVE;
			} else if (defendType.equals("Grass")) {
				effectiveness = SUPER_EFFECTIVE;
			}
		 	break;
			
		case "Water":
		 	if (defendType.equals("Water") || defendType.equals("Grass")) {
				effectiveness = NOT_EFFECTIVE;
			} else if (defendType.equals("Fire")) {
				effectiveness = SUPER_EFFECTIVE;
			}
		 	break;
			
		case "Electric":
		 	if (defendType.equals("Electric") || defendType.equals("Grass")) {
				effectiveness = NOT_EFFECTIVE;
			} else if (defendType.equals("Water")) {
				effectiveness = SUPER_EFFECTIVE;
			}
		 	break;
			
		case "Grass":
		 	if (defendType.equals("Fire") || defendType.equals("Grass")) {
				effectiveness = NOT_EFFECTIVE;
			} else if (defendType.equals("Water")) {
				effectiveness = SUPER_EFFECTIVE;
			}
		 	break;
		}

		return effectiveness;
	}
}
