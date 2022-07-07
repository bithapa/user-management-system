package io.bikash.employee.services;

import com.fasterxml.jackson.databind.util.BeanUtil;
import io.bikash.employee.entity.EmployeeEntity;
import io.bikash.employee.model.Employee;
import io.bikash.employee.repository.EmployeeRepository;
import org.springframework.beans.BeanUtils;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class EmployeeServiceImpl implements EmployeeService{
    private EmployeeRepository employeeRepository;

    public EmployeeServiceImpl(EmployeeRepository employeeRepository) {
        this.employeeRepository = employeeRepository;
    }
    @Override
    public Employee createEmployee(Employee employee) {
        // object to entity, to save in the database
        EmployeeEntity employeeEntity = new EmployeeEntity();

        // object source and destination
        BeanUtils.copyProperties(employee, employeeEntity);

        employeeRepository.save(employeeEntity);
        return employee;
    }

    @Override
    public List<Employee> getAllEmployees() {
        // entity from the repository to a model for UI
        List<EmployeeEntity> employeeEntities = employeeRepository.findAll();
        List<Employee> employees = employeeEntities
                .stream()
                .map(employee -> new Employee(
                        employee.getId(),
                        employee.getFirstName(),
                        employee.getLastName(),
                        employee.getEmailId()))
                .collect(Collectors.toList());
        return employees;
    }

    @Override
    public boolean deleteEmployee(Long id) {
        EmployeeEntity employee = employeeRepository.findById(id).get();
        employeeRepository.delete(employee);
        return true ;
    }

    @Override
    public Employee getEmployeeById(Long id) {
        EmployeeEntity employeeEntity =
                employeeRepository.findById(id).get();
        Employee employee = new Employee();
        BeanUtils.copyProperties(employeeEntity, employee);
        return employee;
    }

    @Override
    public Employee updateEmployee(Long id, Employee employee) {
        EmployeeEntity employeeEntity =
                employeeRepository.findById(id).get();
        employeeEntity.setEmailId(employee.getEmailId());
        employeeEntity.setFirstName(employee.getFirstName());
        employeeEntity.setLastName((employee.getLastName()));

        employeeRepository.save(employeeEntity);
        return employee;
    }
}
