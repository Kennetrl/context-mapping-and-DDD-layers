package com.veritrabajo.backend.reputation.infrastructure.persistence;

import com.veritrabajo.backend.reputation.domain.model.TradeReputation;
import com.veritrabajo.backend.reputation.domain.port.TradeReputationRepository;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Repository;

import java.util.Map;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;

/**
 * In-memory repository used by the in-memory profile.
 */
@Repository
@Profile("in-memory")
public class InMemoryTradeReputationRepository implements TradeReputationRepository {

    private final Map<String, TradeReputation> storeByProfileId = new ConcurrentHashMap<>();

    @Override
    public Optional<TradeReputation> findByProfileId(String profileId) {
        return Optional.ofNullable(storeByProfileId.get(profileId));
    }

    @Override
    public TradeReputation save(TradeReputation tradeReputation) {
        storeByProfileId.put(tradeReputation.profileId(), tradeReputation);
        return tradeReputation;
    }
}
