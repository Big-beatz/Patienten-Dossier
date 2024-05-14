package com.kalma.Patienten.Dossier.security;

import com.kalma.Patienten.Dossier.models.Employee;
import com.kalma.Patienten.Dossier.repository.EmployeeRepository;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

import java.util.Optional;

public class MyUserDetailsService implements UserDetailsService {

    private final EmployeeRepository employeeRepository;

    public MyUserDetailsService(EmployeeRepository repos) {
        this.employeeRepository = repos;
    }
    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        Optional<Employee> optionalEmployee = employeeRepository.findByUsername(username);
        if (optionalEmployee.isPresent()) {
            Employee employee = optionalEmployee.get();
            return new MyUserDetails(employee);
        }
        else {
            throw new UsernameNotFoundException(username);
        }
    }
}
