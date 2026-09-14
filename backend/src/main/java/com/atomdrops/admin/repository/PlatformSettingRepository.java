package com.atomdrops.admin.repository;
import com.atomdrops.admin.model.PlatformSetting;
import org.springframework.data.jpa.repository.JpaRepository;
public interface PlatformSettingRepository extends JpaRepository<PlatformSetting, String> {
}
