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
//		String value = getMirProperties().getProperty(ENTITY_INDEXING_ENABLED);
//		return Boolean.valueOf(value);
//	}

	public Properties getMirProperties() {
		return mirProperties;
	}

	public void setMirProperties(Properties mirProperties) {
		this.mirProperties = mirProperties;
	}

	@Override
	public boolean isProductionEnvironment() {
		return VALUE_ENVIRONMENT_PRODUCTION.equals(getEnvironment());
	}

	@Override
	public String getEnvironment() {
		return getMirProperties().getProperty(ENTITY_ENVIRONMENT);
	}

	@Override
	public String getDataUrl() {
		return getMirProperties().getProperty(DATA_URL);
	}

}
