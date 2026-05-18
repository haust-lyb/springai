package com.hsr.springai.repository;

import com.hsr.springai.entity.Model;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ModelRepository extends JpaRepository<Model, Long> {

    List<Model> findByProviderId(Long providerId);

    void deleteByProviderId(Long providerId);

    List<Model> findByProvider_Status(String status);
}
