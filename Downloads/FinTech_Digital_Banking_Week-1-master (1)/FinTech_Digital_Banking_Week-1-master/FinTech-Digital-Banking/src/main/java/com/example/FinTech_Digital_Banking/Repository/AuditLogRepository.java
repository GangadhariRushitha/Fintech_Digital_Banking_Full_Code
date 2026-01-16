
package com.example.FinTech_Digital_Banking.Repository;




import com.example.FinTech_Digital_Banking.Entity.AuditLog;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AuditLogRepository extends JpaRepository<AuditLog, Long> {
}
