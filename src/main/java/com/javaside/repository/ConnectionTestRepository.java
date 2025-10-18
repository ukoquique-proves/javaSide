package com.javaside.repository;

import com.javaside.entity.ConnectionTest;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ConnectionTestRepository extends JpaRepository<ConnectionTest, Long> {
}
