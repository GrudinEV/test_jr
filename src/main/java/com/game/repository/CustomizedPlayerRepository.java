package com.game.repository;

import com.game.entity.Player;

import java.util.List;
import java.util.Map;

public interface CustomizedPlayerRepository<T> {
    List<Player> getPlayersWithFilter(Map<String, String> allRequestParams);

    int getCountFindPlayers();

    List<Player> getPlayersWithFilterAndPaging(Map<String, String> allRequestParams);
}
