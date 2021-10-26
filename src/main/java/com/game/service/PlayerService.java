package com.game.service;

import com.game.entity.Player;
import com.game.entity.PlayerCreate;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.Map;

public interface PlayerService {
    List<Player> getAll();
    List<Player> getAll(Pageable pageable);

    int getCountFindPlayers(Map<String, String> allRequestParams);

    List<Player> getPlayersWithFilterAndPaging(Map<String, String> allRequestParams);

    Player create(PlayerCreate playerCreate);

    Player getById(long id);

    Player update(long id, Map<String, String> params);

    Player delete(long id);
}
