
package com.example.FinTech_Digital_Banking.Repository;


import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;
import java.util.UUID;
import com.example.FinTech_Digital_Banking.Entity.Portfolio;
public interface PortfolioRepository extends JpaRepository<Portfolio, UUID> {
    List<Portfolio> findByUserId(String userId);
}
