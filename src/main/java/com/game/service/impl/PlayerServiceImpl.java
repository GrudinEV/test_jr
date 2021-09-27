package com.game.service.impl;

import com.game.entity.Player;
import com.game.repository.PlayerRepository;
import com.game.service.PlayerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

@Service
public class PlayerServiceImpl implements PlayerService {
    private final PlayerRepository playerRepository;

    @Autowired
    public PlayerServiceImpl(PlayerRepository playerRepository) {
        this.playerRepository = playerRepository;
    }

    @Override
    public List<Player> getAll() {
        return playerRepository.findAll();
    }

    @Override
    public List<Player> getAll(Pageable pageable) {
        Page<Player> page = playerRepository.findAll(pageable);
        return page.getContent();
    }

    @Override
    public int getCountFindPlayers() {
        return playerRepository.getCountFindPlayers();
    }

    @Override
    public List<Player> getPlayersWithFilterAndPaging(Map<String, String> allRequestParams) {
        return playerRepository.getPlayersWithFilterAndPaging(allRequestParams);
    }
}
