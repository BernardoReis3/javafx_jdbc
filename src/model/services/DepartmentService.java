package model.services;

import java.util.List;

import model.dao.DaoFactory;
import model.dao.DepartmentDao;
import model.entities.Department;

public class DepartmentService {

	private DepartmentDao departmentDAO = DaoFactory.createDepartmentDao();
	
	public List<Department> findAll(){
		/*
		List<Department> listDeps = new ArrayList<>();
		//Mock the data of list department for now
		listDeps.add(new Department(1, "Books"));
		listDeps.add(new Department(2, "Computers"));
		listDeps.add(new Department(3, "Phones and gadgets"));
		*/
		return departmentDAO.findAll();
		
	}
	
	public void saveOrUpdate(Department department) {
		if(department.getId() == null) {
			departmentDAO.insert(department);
		}
		departmentDAO.update(department);
	}
}
