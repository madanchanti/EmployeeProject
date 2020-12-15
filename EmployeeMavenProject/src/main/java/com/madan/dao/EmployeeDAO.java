package com.madan.dao;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.servlet.http.HttpSession;
import javax.sql.DataSource;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.core.simple.SimpleJdbcInsert;
import org.springframework.stereotype.Repository;
import com.madan.model.Employee;
import com.madan.service.EmployeeService;

@Repository
public class EmployeeDAO {
	
	private String tableName = "tblemployee";
	@Autowired
	private static final Logger logger = Logger.getLogger(EmployeeDAO.class);
	@Autowired
	private JdbcTemplate jdbcTemplate;
	@Autowired
	private DataSource dataSource;
	@Autowired
	EmployeeService employeeService;
	
	//update the employee information
	public int employeeUpdate(Employee employee, String whereData, HttpSession session) {
		int returnValue = 0;
		try {
			String sqlQuery = "";
			Map<String, Object> fieldInfo = new HashMap<String, Object>();
			Map<String, Object> conditionInfo = new HashMap<String, Object>();
			if (employee.getFirstName() != null) {fieldInfo.put("first_name", employee.getFirstName());}
			if (employee.getLastName() != null) {fieldInfo.put("last_name", employee.getLastName());}
			if (employee.getEmailId() != null) {fieldInfo.put("email_id", employee.getEmailId());}
			if (employee.getAge() != 0) {fieldInfo.put("age", employee.getAge());}
			if (employee.getGender() != null) {fieldInfo.put("gender", employee.getGender());}
			if (employee.getAddress() != null) {fieldInfo.put("address", employee.getAddress());}
			
			//update query
			if (employee.getId() != 0) {
				fieldInfo.put("id", employee.getId());
				conditionInfo.put("id", employee.getId());
				sqlQuery = employeeService.buildUpdateQuery(tableName, fieldInfo, conditionInfo);
				logger.info("New SQL query" + sqlQuery);				
				final NamedParameterJdbcTemplate jdbcTemplate = new NamedParameterJdbcTemplate(dataSource);
				returnValue = jdbcTemplate.update(sqlQuery, fieldInfo);
			}
			else {
				//insert query
				SimpleJdbcInsert insert = new SimpleJdbcInsert(dataSource).withTableName(tableName).usingGeneratedKeyColumns("id");
				Number id = insert.executeAndReturnKey(fieldInfo);
				returnValue = id.intValue();
				employee.setId(returnValue);
			}
		}
		catch (Exception e) {
			logger.error(e);
		}
		return returnValue;
	}
	
	//get the employee list information
	public List<Employee> getEmployeeList(String whereData, String orderBy, String limit, HttpSession session) {
		String sql = " select employee.id, employee.first_name, employee.last_name, employee.email_id, employee.age, employee.gender, employee.address "
				+ " from tblemployee as employee "	
				+ whereData + orderBy + limit;
		logger.info("sql query  - " + sql);
		List<Employee> employeeList = jdbcTemplate.query(sql, new RowMapper<Employee>() {
			public Employee mapRow(ResultSet rs, int rowNum) throws SQLException {
				Employee employee = new Employee();
				employee.setId(rs.getInt("id"));
				employee.setFirstName(rs.getString("first_name"));
				employee.setLastName(rs.getString("last_name"));
				employee.setEmailId(rs.getString("email_id"));
				employee.setAge(rs.getInt("age"));
				employee.setGender(rs.getString("gender"));
				employee.setAddress(rs.getString("address"));
				return employee;
			}
		});
		return employeeList;
	}
	
	//get the employee count information
	public int getEmployeeCount(String whereData) {
		try {
			String sqlQuery = "select count(*) " + "from tblemployee as employee " + whereData;
			return jdbcTemplate.queryForObject(sqlQuery, int.class);
		} catch (Exception e) {
			logger.error("Exception e" + e);
			return 0;
		}
	}

	//delete the employee information
	public int deleteEmployeeDetails(int id) {
		try {
			String sqlQuery = " delete from tblemployee where id = " + id;
			return jdbcTemplate.queryForObject(sqlQuery, int.class);
		} catch (Exception e) {
			logger.error("Exception e" + e);
			return 0;
		}
	}
		
	//get the employee details by id
	public Employee getEmployeeDetails(int employeeId, HttpSession session) {
		Map<String, String> whereMap = new HashMap<String, String>();
		whereMap.put("id", Integer.toString(employeeId));
		String whereData = employeeService.buildWhereData(whereMap);
		List<Employee> employeeList = getEmployeeList(whereData, "", "", session);
		if (employeeList.size() > 0) {return employeeList.get(0);}{return null;}
	}
	
}