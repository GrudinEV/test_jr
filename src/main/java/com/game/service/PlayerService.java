package com.game.service;

import com.game.entity.Player;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.Map;

public interface PlayerService {
    List<Player> getAll();
    List<Player> getAll(Pageable pageable);

    int getCountFindPlayers();

    List<Player> getPlayersWithFilterAndPaging(Map<String, String> allRequestParams);
}
