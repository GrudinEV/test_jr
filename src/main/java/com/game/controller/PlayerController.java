package com.game.controller;

import com.game.dao.PlayerDao;
import com.game.entity.Player;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.List;

@Controller
@RequestMapping("/rest/players")
public class PlayerController {
    private PlayerDao playerDao;

    @Autowired
    public PlayerController(PlayerDao playerDao) {
        this.playerDao = playerDao;
    }

    @GetMapping("")
    public String index(Model model) {
        List<Player> players = playerDao.index();
        players.forEach(System.out::println);
        model.addAttribute("mainTable", players);
        return "index";
    }
}
