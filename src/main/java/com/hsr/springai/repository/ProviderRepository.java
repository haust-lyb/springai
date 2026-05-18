package com.hsr.springai.repository;

import com.hsr.springai.entity.Provider;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ProviderRepository extends JpaRepository<Provider, Long> {

    Optional<Provider> findByName(String name);

    List<Provider> findByIsBuiltinTrue();

    boolean existsByName(String name);
}
