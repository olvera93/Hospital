package com.olvera.hospital.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ConsultingRoom extends JpaRepository<com.olvera.hospital.entity.ConsultingRoom, Long> {
}
