package com.atomdrops.user.repository;
import com.atomdrops.user.model.UserAgreement;
import org.springframework.data.jpa.repository.JpaRepository;
public interface UserAgreementRepository extends JpaRepository<UserAgreement, Long> {
    boolean existsByUserIdAndAgreementType(Long userId, String agreementType);
}
