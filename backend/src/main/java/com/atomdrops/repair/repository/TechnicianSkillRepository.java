package com.atomdrops.repair.repository;
import com.atomdrops.repair.model.TechnicianSkill;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;
public interface TechnicianSkillRepository extends JpaRepository<TechnicianSkill, Long> {
    List<TechnicianSkill> findByTechnicianId(Long technicianId);
}
