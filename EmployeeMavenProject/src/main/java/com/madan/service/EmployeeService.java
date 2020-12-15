package com.madan.service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.madan.dao.EmployeeDAO;
import com.madan.model.Employee;


@Service
public class EmployeeService {
	
	@Autowired
	EmployeeDAO employeeDAO;
	
	//to update the employee information
	public Map<String, Object> employeeUpdate(Employee employee, HttpServletRequest request) {
		Map<String, Object> response = new HashMap<String, Object>();
		int id = employeeDAO.employeeUpdate(employee, "", request.getSession());
		if(id != 0) {
			response.put("status", "Success");
			response.put("id", String.valueOf(id));
			response.put("message", "Employee information updated successfully");
		} else {
			response.put("status", "Failed");
			response.put("message", "Not able to add into the database");
		}
		return response;
	}
	
	//to get the employee list information 
	public Map<String, Object> getEmployeeList(HttpServletRequest request) {
		Map<String, Object> response =new HashMap<String, Object>();
		HttpSession session = request.getSession();
		List<Employee> employeeList = employeeDAO.getEmployeeList("", "", "", session);
		if (employeeList != null) {
			response.put("status", "Success");
			response.put("data", employeeList);
		} else {
			response.put("status", "Failed");
		}
		return response;
	}
	
	//to get the employee details by id
	public Map<String, Object> getEmployeeDetails(int employeeId, HttpServletRequest request) {
		Map<String, Object> response =new HashMap<String, Object>();
		HttpSession session = request.getSession();
		Employee employeeDetails =  employeeDAO.getEmployeeDetails(employeeId, session);
		if (employeeDetails != null) {
			response.put("status", "Success");
			response.put("data", employeeDetails);
		} else {
			response.put("status", "Failed");
		}
		return response;
	}

	//to delete the employee details by id
	public Map<String, Object> deleteEmployeeDetails(int employeeId, HttpServletRequest request) {
		Map<String, Object> response =new HashMap<String, Object>();
		HttpSession session = request.getSession();
		int id =  employeeDAO.deleteEmployeeDetails(employeeId);
		if (id != 0) {
			response.put("status", "Success");
			response.put("message", "Employee details deleted successfully");
		} else {
			response.put("status", "Failed");
		}
		return response;
	}

	//building the update query
	public String buildUpdateQuery(String tableName, Map<String, Object> fieldInfo, Map<String, Object> condition) {
		String updateQuery = "";
		String queryParam = "";
		String conditionParam = "";
		updateQuery = "update " + tableName + " set ";
		//Builing Data
		for (String key : fieldInfo.keySet()) {
			if (! condition.containsKey(key)) {
				if (!"".equals(queryParam)) {queryParam += ", ";}
				queryParam += key + " = :" + key + "";
			}
		}
		//Building condition (only and will work)
		for (String key : condition.keySet()) {
			if (!"".equals(conditionParam)) {conditionParam += " and ";}
			conditionParam += key + " = :" + key + "";
		}
		if (conditionParam != "") {conditionParam = " where " + conditionParam;}
		return updateQuery + queryParam + conditionParam;
	}
	
	//building where condition
	public String buildWhereData(Map<String, String> whereMap) {
		String whereData = "";
		for (Map.Entry<String, String> entry : whereMap.entrySet()) {
			if (whereData != "") {whereData += " and ";}
			String firstCharKey = Character.toString(entry.getKey().charAt(0));
			if ("#".equals(firstCharKey)) {whereData += entry.getValue();} else {whereData += entry.getKey() + " = '" + entry.getValue() + "'";}
		}
		if (whereData != "") {whereData = " where " + whereData;}
		return whereData;
	}

}
