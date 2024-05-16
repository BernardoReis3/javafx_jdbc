package model.services;

import java.util.ArrayList;
import java.util.List;

import model.entities.Department;

public class DepartmentService {

	
	public List<Department> findAll(){
		List<Department> listDeps = new ArrayList<>();
		//Mock the data of list department for now
		listDeps.add(new Department(1, "Books"));
		listDeps.add(new Department(2, "Computers"));
		listDeps.add(new Department(3, "Phones and gadgets"));
		
		return listDeps;
		
	}
}
