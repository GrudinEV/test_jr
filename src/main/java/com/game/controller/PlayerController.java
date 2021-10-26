package com.game.controller;

import com.game.entity.Player;
import com.game.entity.PlayerCreate;
import com.game.service.PlayerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/rest/players")
public class PlayerController {
    private final PlayerService playerService;

    @Autowired
    public PlayerController(PlayerService playerService) {
        this.playerService = playerService;
    }

    @GetMapping("")
    public List<Player> show(@RequestParam Map<String, String> allRequestParams) {
        return playerService.getPlayersWithFilterAndPaging(allRequestParams);
    }

    @GetMapping("/count")
    public Integer count(@RequestParam Map<String, String> allRequestParams) {
        return playerService.getCountFindPlayers(allRequestParams);
    }

    @GetMapping("/{id}")
    public Player showById(@Validated @PathVariable("id") long id, HttpServletResponse response) {
        if (id < 1) {
            response.setStatus(HttpStatus.BAD_REQUEST.value());
            return null;
        }
        Player player = playerService.getById(id);
        if (player == null) {
            response.setStatus(HttpStatus.NOT_FOUND.value());
            return null;
        }
        return player;
    }

    @PostMapping("")
    public Player create(@Validated @RequestBody PlayerCreate playerCreate, HttpServletResponse response) {
        if (playerCreate.getName() == null
                || playerCreate.getName().length() == 0
                || playerCreate.getName().length() > 12
                || playerCreate.getTitle().length() == 0
                || playerCreate.getTitle().length() > 30
                || playerCreate.getExperience() < 0
                || playerCreate.getExperience() > 10_000_000
                || playerCreate.getBirthday() < 0
                || new Date(playerCreate.getBirthday()).before(new GregorianCalendar(2000, 0, 1).getTime())
                || new Date(playerCreate.getBirthday()).after(new GregorianCalendar(3000, 11, 31).getTime())) {
            response.setStatus(HttpStatus.BAD_REQUEST.value());
            return null;
        }
        return playerService.create(playerCreate);
    }

    @PostMapping("/{id}")
    public Player update(@Validated @PathVariable("id") long id,
                         @Validated @RequestBody Map<String, String> params, HttpServletResponse response) {
        if (id < 1) {
            response.setStatus(HttpStatus.BAD_REQUEST.value());
            return null;
        }
        if (params.get("name") != null
                && (params.get("name").length() == 0
                || params.get("name").length() > 12)) {
            response.setStatus(HttpStatus.BAD_REQUEST.value());
            return null;
        }
        if (params.get("title") != null
                && (params.get("title").length() == 0
                || params.get("title").length() > 30)) {
            response.setStatus(HttpStatus.BAD_REQUEST.value());
            return null;
        }
        if (params.get("experience") != null
                && (Integer.parseInt(params.get("experience")) < 0
                || Integer.parseInt(params.get("experience")) > 10_000_000)) {
            response.setStatus(HttpStatus.BAD_REQUEST.value());
            return null;
        }
        if (params.get("birthday") != null
                && (Long.parseLong(params.get("birthday")) < 0
                || new Date(Long.parseLong(params.get("birthday"))).before(new GregorianCalendar(2000, 0, 1).getTime())
                || new Date(Long.parseLong(params.get("birthday"))).after(new GregorianCalendar(3000, 11, 31).getTime()))) {
            response.setStatus(HttpStatus.BAD_REQUEST.value());
            return null;
        }
        Player player = playerService.update(id, params);
        if (player == null) {
            response.setStatus(HttpStatus.NOT_FOUND.value());
            return null;
        }
        return player;
    }

    @DeleteMapping("/{id}")
    public void delete(@PathVariable("id") long id, HttpServletResponse response) {
        if (id < 1) {
            response.setStatus(HttpStatus.BAD_REQUEST.value());
            return;
        }
        Player player = playerService.delete(id);
        if (player == null) {
            response.setStatus(HttpStatus.NOT_FOUND.value());
        }
    }
}
