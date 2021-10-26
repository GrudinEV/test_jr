package com.game.service.impl;

import com.game.entity.Player;
import com.game.entity.PlayerCreate;
import com.game.entity.Profession;
import com.game.entity.Race;
import com.game.repository.PlayerRepository;
import com.game.service.PlayerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
public class PlayerServiceImpl implements PlayerService {
    private final PlayerRepository playerRepository;
    private int playersFound = 0;

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
    public int getCountFindPlayers(Map<String, String> allRequestParams) {

        return playerRepository.getPlayersWithFilter(allRequestParams).size();
    }

    @Override
    public List<Player> getPlayersWithFilterAndPaging(Map<String, String> allRequestParams) {
        String pageNumber = allRequestParams.remove("pageNumber");
        int pageNum = pageNumber == null ? 0 : Integer.parseInt(pageNumber);
        String pageSize = allRequestParams.remove("pageSize");
        int pageSizeInt = pageSize == null ? 3 : Integer.parseInt(pageSize);
        List<Player> players = playerRepository.getPlayersWithFilter(allRequestParams);
        playersFound = players.size();
        int startIndex = pageNum * pageSizeInt;
        int endIndex = Math.min((pageNum + 1) * pageSizeInt, playersFound);
        List<Player> resultPlayers = new ArrayList<>();
        for (int i = startIndex; i < endIndex; i++) {
            resultPlayers.add(players.get(i));
        }
        return resultPlayers;
    }

    @Override
    public Player create(PlayerCreate playerCreate) {
        Player newPlayer = new Player();
        newPlayer.setName(playerCreate.getName());
        newPlayer.setTitle(playerCreate.getTitle());
        newPlayer.setRace(playerCreate.getRace());
        newPlayer.setProfession(playerCreate.getProfession());
        newPlayer.setBirthday(new Date(playerCreate.getBirthday()));
        newPlayer.setBanned(playerCreate.isBanned());
        newPlayer.setExperience(playerCreate.getExperience());
        newPlayer.setLevel(calculateLevel(playerCreate.getExperience()));
        newPlayer.setUntilNextLevel(calculateExpUntilNextLvl(playerCreate.getExperience()));
        System.out.println(newPlayer);
        return playerRepository.save(newPlayer);
    }

    @Override
    public Player getById(long id) {
        return Optional.of(playerRepository.findById(id)).get().orElse(null);
    }

    @Override
    public Player update(long id, Map<String, String> params) {
        Player player = getById(id);
        if (player == null) {
            return player;
        }
        if (params.get("name") != null) {
            player.setName(params.get("name"));
        }
        if (params.get("title") != null) {
            player.setTitle(params.get("title"));
        }
        if (params.get("race") != null) {
            player.setRace(Race.valueOf(params.get("race").toUpperCase()));
        }
        if (params.get("profession") != null) {
            player.setProfession(Profession.valueOf(params.get("profession").toUpperCase()));
        }
        if (params.get("birthday") != null) {
            player.setBirthday(new Date(Long.parseLong(params.get("birthday"))));
        }
        if (params.get("banned") != null) {
            if (params.get("banned").equals("true")) {
                player.setBanned(true);
            } else {
                if (params.get("banned").equals("false")) {
                    player.setBanned(false);
                }
            }

        }
        if (params.get("experience") != null) {
            int exp = Integer.parseInt(params.get("experience"));
            player.setExperience(exp);
            player.setLevel(calculateLevel(player.getExperience()));
            player.setUntilNextLevel(calculateExpUntilNextLvl(player.getExperience()));
        }
        return playerRepository.save(player);
    }

    @Override
    public Player delete(long id) {
        Player player = getById(id);
        if (player == null) {
            return null;
        }
        playerRepository.delete(player);
        return player;
    }

    private int calculateExpUntilNextLvl(Integer experience) {
        return 50 * (calculateLevel(experience) + 1) * (calculateLevel(experience) + 2) - experience;
    }

    private int calculateLevel(Integer experience) {
        return (int) ((Math.sqrt(2500.0 + 200.0 * experience) - 50) / 100);
    }
}
