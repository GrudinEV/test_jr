package com.game.controller;

import com.game.entity.Player;
import com.game.service.PlayerService;
import com.game.service.impl.PlayerServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/rest/players")
public class PlayerController {
    private final PlayerService playerService;

    @Autowired
    public PlayerController(PlayerService playerService) {
        this.playerService = playerService;
    }

    @GetMapping("")
    public List<Player> index(@RequestParam(name = "pageNumber", defaultValue = "0") int pageNumber,
                              @RequestParam(name = "pageSize", defaultValue = "3") int pageSize,
                              @RequestParam(name = "order", defaultValue = "ID") String order) {
        return playerService.getAll(PageRequest.of(pageNumber, pageSize, Sort.by(PlayerOrder.valueOf(order).getFieldName())));
    }

    @GetMapping("/count")
    public Integer count() {
        return playerService.getAll().size();
    }
}
