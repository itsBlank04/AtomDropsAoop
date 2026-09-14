package com.atomdrops.admin.repository;
import com.atomdrops.admin.model.AuditLog;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;
public interface AuditLogRepository extends JpaRepository<AuditLog, Long> {
    List<AuditLog> findByActorId(Long actorId);
    List<AuditLog> findByEntityTypeAndEntityId(String entityType, Long entityId);
}
