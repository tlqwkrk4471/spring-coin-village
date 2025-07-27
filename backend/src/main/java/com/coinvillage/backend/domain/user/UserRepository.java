package com.coinvillage.backend.domain.user;

import java.util.List;
import java.util.Optional;

public interface UserRepository {

    User save(User save);
    Optional<User> findById(Long id);
    Optional<User> findByLoginId(String loginId);
    List<User> findAll();
    Boolean existLoginId(String loginId);
    void clear();
}
