package com.xtivia.xip.services.dao;

import com.xtivia.xip.entity.ServiceDefinition;

import java.sql.ResultSet;
import java.sql.SQLException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;


@SuppressWarnings("unchecked")
@Repository("serviceDefinitionDao")
public class JdbcServiceDefinitionDao implements ServiceDefinitionDao{
	@Autowired
	@Qualifier("jdbcTemplate")
	private JdbcTemplate jdbcTemplate;

	
	@Override
	public ServiceDefinition getServiceDefinition(String serviceName){
		String sql = "select * from service_definition where serviceName=?";

		ServiceDefinition serviceDefinition = (ServiceDefinition)jdbcTemplate.queryForObject(
				sql, new Object[] { serviceName }, new CustomerRowMapper());
	 
		return serviceDefinition;
	}

	@Override
	public void upsertServiceDefinition(ServiceDefinition sd) {
		ServiceDefinition existing=null;
		try{
			existing = getServiceDefinition(sd.getServiceName());
		} catch(Exception e){};
		if(null==existing){
			jdbcTemplate.update("insert into service_definition (" +
					"serviceAction," +
					"serviceInputMappings," +
					"serviceName," +
					"serviceOutputMappings," +
					"serviceProvider," +
					"serviceRoles," +
					"serviceText," +
					"serviceType" +
					") values (?,?,?,?,?,?,?,?)", new Object[] {
					sd.getServiceAction(),
					sd.getServiceInputMappings(),
					sd.getServiceName(),
					sd.getServiceOutputMappings(),
					sd.getServiceProvider(),
					sd.getServiceRoles(),
					sd.getServiceText(),
					sd.getServiceType()
			});
		} else {
			jdbcTemplate.update("update service_definition set " +
					"serviceAction=?," +
					"serviceInputMappings=?," +
					"serviceName=?," +
					"serviceOutputMappings=?," +
					"serviceProvider=?," +
					"serviceRoles=?," +
					"serviceText=?," +
					"serviceType=?" +
					"  where serviceName = ?", new Object[] {
					sd.getServiceAction(),
					sd.getServiceInputMappings(),
					sd.getServiceName(),
					sd.getServiceOutputMappings(),
					sd.getServiceProvider(),
					sd.getServiceRoles(),
					sd.getServiceText(),
					sd.getServiceType(),
					sd.getServiceName()
			});
		}
	}
	
	public void deleteServiceDefinition(String serviceName){
		jdbcTemplate.update("delete from service_definition where serviceName=?", new Object[]{serviceName});
	}
	
	@SuppressWarnings("rawtypes")
	private class CustomerRowMapper implements RowMapper {
		public Object mapRow(ResultSet rs, int rowNum) throws SQLException {
			ServiceDefinition serviceDefinition = new ServiceDefinition();
			serviceDefinition.setServiceAction(rs.getString("serviceAction"));
			serviceDefinition.setServiceInputMappings(rs.getString("serviceInputMappings"));
			serviceDefinition.setServiceName(rs.getString("serviceName"));
			serviceDefinition.setServiceOutputMappings(rs.getString("serviceOutputMappings"));
			serviceDefinition.setServiceProvider(rs.getString("serviceProvider"));
			serviceDefinition.setServiceRoles(rs.getString("serviceRoles"));
			serviceDefinition.setServiceText(rs.getString("serviceText"));
			serviceDefinition.setServiceType(rs.getString("serviceType"));
			return serviceDefinition;
		}
	 
	}

}