import java.util.Arrays;

public interface TypedItem {

	
	
	public boolean hasType(String type);
	public String[] getTypes();
	
	public static boolean isValidType(String type) {
		return Arrays.asList("Normal", "Fire", "Water", "Electric", "Grass").contains(type);
	}
	
	public static double getEffectiveness (String attackType, String defendType) {
		double superE = 2;
		double notVeryE = 0.5;
		double normalE = 1;
		
		if (attackType.equals("Normal") || defendType.equals("Normal")) 
			return normalE;
		
		if (attackType.equals(defendType))
			return notVeryE;
	
		if (attackType.equals("Fire")) {
			
			if (defendType.equals("Water"))
				return notVeryE;
			
			if(defendType.equals("Grass"))
				return superE;
		}
		
		if (attackType.equals("Water")) {
			if (defendType.equals("Fire"))
				return superE;
			
			if (defendType.equals("Grass"))
				return notVeryE;
		}
		
		if (attackType.equals("Electric")) {
			if (defendType.equals("Water"))
				return superE;
			
			if (defendType.equals("Grass"))
				return notVeryE;
		}
		
		if (attackType.equals("Grass")) {
			if (defendType.equals("Water"))
				return superE;
			
			if (defendType.equals("Fire"))
				return notVeryE;
		}
		
		
		
		
		return normalE;
	}
	
	
	
}
