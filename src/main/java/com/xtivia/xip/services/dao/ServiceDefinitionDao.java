package com.xtivia.xip.services.dao;

import com.xtivia.xip.entity.ServiceDefinition;

public interface ServiceDefinitionDao {
	ServiceDefinition getServiceDefinition(String serviceName);
	void upsertServiceDefinition(ServiceDefinition sd);
	void deleteServiceDefinition(String serviceName);
}
