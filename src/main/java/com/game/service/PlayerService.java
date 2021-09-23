package com.game.service;

import com.game.entity.Player;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface PlayerService {
    List<Player> getAll();
    List<Player> getAll(Pageable pageable);
}
