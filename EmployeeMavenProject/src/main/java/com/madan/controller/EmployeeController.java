package com.madan.controller;

import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import com.madan.model.Employee;
import com.madan.service.EmployeeService;

@Controller
public class EmployeeController {
	
	@Autowired
	EmployeeService employeeService;
	
	//updating the employee information
	@RequestMapping(value="/api/employeeUpdate", method=RequestMethod.POST, headers = "Accept=application/json")
	public ResponseEntity<Object> employeeUpdate(@RequestBody Employee employee, HttpServletRequest request) {
		Map<String, Object> details = employeeService.employeeUpdate(employee, request);
		return new ResponseEntity<Object>(details, HttpStatus.OK);
	}
	
	//get the employee list 
	@RequestMapping(value = "/api/getEmployeeList", method = RequestMethod.POST, headers = "Accept=application/json")
	public ResponseEntity<Object> getEmployeeList(HttpServletRequest request) {
		Map<String, Object> details = employeeService.getEmployeeList(request);
		return new ResponseEntity<Object>(details, HttpStatus.OK);
	}
	
	//get the employee details by employee id 
	 @RequestMapping(value = "/api/getEmployeeDetails/{employeeId}", method=RequestMethod.POST, headers = "Accept=application/json") 
	 public ResponseEntity<Object> getEmployeeDetails(@PathVariable int employeeId, HttpServletRequest request) { 
		 Map<String, Object> details = employeeService.getEmployeeDetails(employeeId, request);
		return new ResponseEntity<Object>(details, HttpStatus.OK);
	}

	//delete the employee details by employee id 
	 @RequestMapping(value = "/api/deleteEmployeeDetails/{employeeId}", method=RequestMethod.POST, headers = "Accept=application/json") 
	 public ResponseEntity<Object> deleteEmployeeDetails(@PathVariable int employeeId, HttpServletRequest request) { 
		 Map<String, Object> details = employeeService.deleteEmployeeDetails(employeeId, request);
		return new ResponseEntity<Object>(details, HttpStatus.OK);
	} 

}