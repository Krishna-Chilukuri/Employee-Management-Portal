package com.employee.portal.repository;

import com.employee.portal.model.Employee;
import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface EmployeeRepository extends JpaRepository<Employee, Long> {
    @Query("SELECT COUNT(*) FROM Employee WHERE employeeRank = :rank")
    long countByEmployeeRank(long rank);

    @Query("SELECT employeeId FROM Employee WHERE employeeRank = :rank")
    List<Long> getEmployeeIdsByRank(long rank);

    @Query("SELECT employeeId FROM Employee WHERE reportsTo = :reportsToId and employeeRank > :newSuperiorRank")
    List<Long> getEmployeesByReportsToId(long reportsToId, long newSuperiorRank);

    @Transactional
    @Modifying
    @Query("UPDATE Employee SET reportsTo = :newReportsTo WHERE employeeId = :empId")
    void updateReportsToID(long newReportsTo, long empId);
}
