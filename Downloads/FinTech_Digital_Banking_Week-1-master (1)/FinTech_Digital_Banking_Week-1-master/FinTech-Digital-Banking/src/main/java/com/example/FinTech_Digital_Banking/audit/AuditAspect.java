
package com.example.FinTech_Digital_Banking.audit;






import com.example.FinTech_Digital_Banking.Entity.AuditLog;
import com.example.FinTech_Digital_Banking.Repository.AuditLogRepository;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.*;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.Arrays;

@Aspect
@Component
public class AuditAspect {

    private final AuditLogRepository auditRepo;

    public AuditAspect(AuditLogRepository auditRepo) {
        this.auditRepo = auditRepo;
    }

    @Around("execution(* com.vaultcore.service.*.*(..))")
    public Object auditServiceMethods(ProceedingJoinPoint joinPoint) throws Throwable {

        Object result = null;

        try {
            result = joinPoint.proceed();
            return result;
        } finally {

            AuditLog log = new AuditLog();
            log.setAction("SERVICE_CALL");
            log.setMethodName(joinPoint.getSignature().toShortString());
            log.setParameters(Arrays.toString(joinPoint.getArgs()));
            log.setReturnValue(result != null ? result.toString() : "void");
            log.setCreatedAt(LocalDateTime.now());

            auditRepo.save(log);
        }
    }
}
