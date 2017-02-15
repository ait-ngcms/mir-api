package eu.europeana.mir.config;

import java.util.Properties;

public class MirConfigurationImpl implements MirConfiguration {

	private Properties mirProperties;
	
	@Override
	public String getComponentName() {
		return "mir";
	}

//	@Override
//	public boolean isIndexingEnabled() {
//		String value = getAnnotationProperties().getProperty(ENTITY_INDEXING_ENABLED);
//		return Boolean.valueOf(value);
//	}

	public Properties getAnnotationProperties() {
		return mirProperties;
	}

	public void setAnnotationProperties(Properties annotationProperties) {
		this.mirProperties = annotationProperties;
	}

	@Override
	public boolean isProductionEnvironment() {
		return VALUE_ENVIRONMENT_PRODUCTION.equals(getEnvironment());
	}

	@Override
	public String getEnvironment() {
		return getAnnotationProperties().getProperty(ENTITY_ENVIRONMENT);
	}

}
