package eu.europeana.mir.config;

public interface MirConfiguration {

	public static final String ENTITY_ENVIRONMENT = "mir.environment";
	
	//public static final String SUFFIX_BASEURL = "baseUrl";
	public static final String VALUE_ENVIRONMENT_PRODUCTION = "production";
	public static final String VALUE_ENVIRONMENT_TEST = "test";
	public static final String VALUE_ENVIRONMENT_DEVELOPMENT = "development";
	
	public static final String DATA_URL = "data.url";

	
	public String getComponentName();
	
	
	/**
	 * checks annotation.environment=production property
	 */
	public boolean isProductionEnvironment();
	
	/**
	 * uses annotation.environment property
	 */
	public String getEnvironment();
	
	/**
	 * uses data.url property
	 * @return URL to the external hard drive containing experimental data
	 */
	public String getDataUrl();
	
	
//	/**
//	 * uses annotation.environment.{$environment}.baseUrl property
//	 */
//	public String getAnnotationBaseUrl();
	
	
}
