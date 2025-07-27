package com.coinvillage.backend.domain.user;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;

@Slf4j
@Repository
public class MemoryUserRepository implements UserRepository {

    private final Map<Long, User> store = new ConcurrentHashMap<>();
    private long sequence = 0L;

    @Override
    public User save(User user) {
        user.setId(++sequence);
        store.put(user.getId(), user);
        log.info("save: user={}", user);
        return user;
    }

    @Override
    public Optional<User> findById(Long id) {
        return Optional.ofNullable(store.get(id));
    }

    @Override
    public Optional<User> findByLoginId(String loginId) {
        return store.values().stream()
                .filter(user -> user.getLoginId().equals(loginId))
                .findFirst();
    }

    @Override
    public List<User> findAll() {
        return new ArrayList<>(store.values());
    }

    @Override
    public Boolean existLoginId(String loginId) {
        log.debug("existLoginId: loginId={}", loginId);
        return store.values().stream()
                .anyMatch(user -> user.getLoginId().equals(loginId));
    }

    @Override
    public void clear() {
        store.clear();
        sequence = 0L;
    }
}
