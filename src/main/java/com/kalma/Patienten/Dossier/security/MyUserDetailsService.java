package com.kalma.Patienten.Dossier.security;

import com.kalma.Patienten.Dossier.models.Employee;
import com.kalma.Patienten.Dossier.models.User;
import com.kalma.Patienten.Dossier.repository.EmployeeRepository;
import com.kalma.Patienten.Dossier.repository.UserRepository;
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
    public UserDetails loadUserByUsername(String userName) throws UsernameNotFoundException {
        Optional<Employee> optionalEmployee = employeeRepository.findByUserName(userName);
        if (optionalEmployee.isPresent()) {
            Employee employee = optionalEmployee.get();
            return new MyUserDetails(employee);
        }
        else {
            throw new UsernameNotFoundException(userName);
        }
    }
}
