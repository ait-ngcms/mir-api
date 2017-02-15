package eu.europeana.mir.vocabulary;

public enum MirTypes {

	Mir("skos", "Document", "https://www.w3.org/2009/08/skos-reference/skos.html#Concept"), 
	All("*", "All", "http://www.europeana.eu/schemas/edm/Entity");
	
	
	private String modelNameSpace;
	private String internalType;
	private String httpUri;
	
	
	public String getModelNameSpace() {
		return modelNameSpace;
	}

	
	public String getInternalType() {
		return internalType;
	}

	MirTypes(String modelNameSpace, String internalType, String uri){
		this.modelNameSpace = modelNameSpace;
		this.internalType = internalType;
		this.httpUri = uri;
	}
	
	
	public static boolean contains(String modelType) {

	    for (MirTypes field : MirTypes.values()) {
	        if (field.getInternalType().equalsIgnoreCase(modelType)) {
	            return true;
	        }
	    }

	    return false;
	}	
	
	

	/**
	 * Check if an array of EntityTypes contains an Entity type
	 * @param entityTypes Array of EntityTypes 
	 * @param entityType Single EntityTypes for which it is verified if it is contained in the EntityTypes array
	 * @return True if the EntityTypes object is contained in the EntityTypes array
	 */
	public static boolean arrayHasValue(MirTypes[] entityTypes, MirTypes entityType) {
		for (MirTypes entType : entityTypes) {
	        if (entType.equals(entityType)) {
	            return true;
	        }
	    }
	    return false;
	}
	

	/**
	 * Convert an array of EntityTypes into an array of strings
	 * @param entityTypes array of EntityTypes 
	 * @return array of strings
	 */
	public static String[] toStringArray(MirTypes[] entityTypes) {
		String[] internalEntityTypes = new String[entityTypes.length]; 
		for (int i = 0; i < entityTypes.length; i++) {
			internalEntityTypes[i] = entityTypes[i].getInternalType();
	    }
	    return internalEntityTypes;
	}
	

	/**
	 * Identifying agent type by the json value.
	 * For user friendliness the the comparison is case insensitive  
	 * @param jsonValue
	 * @return
	 */
	public static MirTypes getByJsonValue(String jsonValue){
		
		String[] values = jsonValue.split(":", 2);
		//last token
		String ignoreNamespace  = values[values.length -1];
		
		for(MirTypes agentType : MirTypes.values()){
			if(agentType.getJsonValue().equalsIgnoreCase(ignoreNamespace))
				return agentType;
		}
		return null;
	}
	
	/**
	 * Identifying agent type by the HTTP URI value.
	 * For user friendliness the the comparison is case insensitive  
	 * @param jsonValue
	 * @return
	 */
	public static MirTypes getByHttpUri(String uri){
		
		
		for(MirTypes agentType : MirTypes.values()){
			if(agentType.getHttpUri().equalsIgnoreCase(uri))
				return agentType;
		}
		return null;
	}
	
	public static String getInternalTypeByModel(String jsonValue){

		String[] values = jsonValue.split(":", 2);
		//last token
		String ignoreNamespace  = values[values.length -1];
		
		for(MirTypes agentType : MirTypes.values()){
			if(agentType.getJsonValue().equalsIgnoreCase(ignoreNamespace))
				return agentType.getInternalType();
		}
		return null;
	}	
		
	public static MirTypes getByInternalType(String internalType){

		for(MirTypes agentType : MirTypes.values()){
			if(agentType.getInternalType().equalsIgnoreCase(internalType))
				return agentType;
		}
		return null;
	}	
	

	public String getJsonValue() {
		return getModelNameSpace() + ":" + getInternalType();
	}


	public String getHttpUri() {
		return httpUri;
	}
	
	
}
