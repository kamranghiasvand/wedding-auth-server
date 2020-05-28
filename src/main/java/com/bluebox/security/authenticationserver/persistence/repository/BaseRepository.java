package com.bluebox.security.authenticationserver.persistence.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.repository.NoRepositoryBean;

/**
 * @author Yaser(amin) Sadeghi
 */
@NoRepositoryBean
public interface BaseRepository<T, I> extends JpaSpecificationExecutor<T>, JpaRepository<T, I> {
}
