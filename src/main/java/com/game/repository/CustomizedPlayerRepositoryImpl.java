package com.game.repository;

import com.game.controller.PlayerOrder;
import com.game.entity.Player;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.sql.Date;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class CustomizedPlayerRepositoryImpl implements CustomizedPlayerRepository<Player> {
    @PersistenceContext
    private EntityManager em;
    private int playersFound = 0;

    @Override
    public List<Player> getPlayersWithFilter(Map<String, String> allRequestParams) {
        String order = allRequestParams.remove("order");
        order = order == null ? "id" : PlayerOrder.valueOf(order).getFieldName();
        StringBuilder sb = new StringBuilder();
        sb.append("from Player");
        if (!allRequestParams.isEmpty()) {
            sb.append(" where");
            if (allRequestParams.containsKey("name")) {
                sb.append(" lower(name) like '%");
                sb.append(allRequestParams.get("name").toLowerCase());
                sb.append("%'");
            }
            if (allRequestParams.containsKey("title")) {
                if (!String.valueOf(sb).endsWith(" where")) {
                    sb.append(" and");
                }
                sb.append(" lower(title) like '%");
                sb.append(allRequestParams.get("title").toLowerCase());
                sb.append("%'");
            }
            if (allRequestParams.containsKey("race")) {
                if (!String.valueOf(sb).endsWith(" where")) {
                    sb.append(" and");
                }
                sb.append(" lower(race) = '");
                sb.append(allRequestParams.get("race").toLowerCase());
                sb.append("'");
            }
            if (allRequestParams.containsKey("profession")) {
                if (!String.valueOf(sb).endsWith(" where")) {
                    sb.append(" and");
                }
                sb.append(" lower(profession) = '");
                sb.append(allRequestParams.get("profession").toLowerCase());
                sb.append("'");
            }
            if (allRequestParams.containsKey("after")) {
                if (!String.valueOf(sb).endsWith(" where")) {
                    sb.append(" and");
                }
                sb.append(" birthday >= '");
                sb.append(new Date(Long.parseLong(allRequestParams.get("after"))));
                sb.append("'");
            }
            if (allRequestParams.containsKey("before")) {
                if (!String.valueOf(sb).endsWith(" where")) {
                    sb.append(" and");
                }
                sb.append(" birthday <= '");
                sb.append(new Date(Long.parseLong(allRequestParams.get("before"))));
                sb.append("'");
            }
            if (allRequestParams.containsKey("banned")) {
                if (!String.valueOf(sb).endsWith(" where")) {
                    sb.append(" and");
                }
                boolean isBanned = Boolean.parseBoolean(allRequestParams.get("banned"));
                if (isBanned) {
                    sb.append(" banned = true");
                } else {
                    sb.append(" banned = false");
                }
            }
            if (allRequestParams.containsKey("minExperience")) {
                if (!String.valueOf(sb).endsWith(" where")) {
                    sb.append(" and");
                }
                sb.append(" experience >= ");
                sb.append(Integer.parseInt(allRequestParams.get("minExperience")));
            }
            if (allRequestParams.containsKey("maxExperience")) {
                if (!String.valueOf(sb).endsWith(" where")) {
                    sb.append(" and");
                }
                sb.append(" experience <= ");
                sb.append(Integer.parseInt(allRequestParams.get("maxExperience")));
            }
            if (allRequestParams.containsKey("minLevel")) {
                if (!String.valueOf(sb).endsWith(" where")) {
                    sb.append(" and");
                }
                sb.append(" level >= ");
                sb.append(Integer.parseInt(allRequestParams.get("minLevel")));
            }
            if (allRequestParams.containsKey("maxLevel")) {
                if (!String.valueOf(sb).endsWith(" where")) {
                    sb.append(" and");
                }
                sb.append(" level <= ");
                sb.append(Integer.parseInt(allRequestParams.get("maxLevel")));
            }
        }
        sb.append(" order by ");
        sb.append(order);
        String query = String.valueOf(sb);
        List<Player> players = em.createQuery(query, Player.class).getResultList();
        playersFound = players.size();
        return players;
    }

    @Override
    public int getCountFindPlayers() {
        return playersFound;
    }

    @Override
    public List<Player> getPlayersWithFilterAndPaging(Map<String, String> allRequestParams) {
//        allRequestParams.forEach((k, v) -> System.out.println(k + " : " + v));
//        System.out.println("");

        String pageNumber = allRequestParams.remove("pageNumber");
        int pageNum = pageNumber == null ? 0 : Integer.parseInt(pageNumber);
        String pageSize = allRequestParams.remove("pageSize");
        int pageSizeInt = pageSize == null ? 3 : Integer.parseInt(pageSize);
        List<Player> players = getPlayersWithFilter(allRequestParams);
        int startIndex = pageNum * pageSizeInt;
        int endIndex = Math.min((pageNum + 1) * pageSizeInt, playersFound);
        List<Player> resultPlayers = new ArrayList<>();
        for (int i = startIndex; i < endIndex; i++) {
            resultPlayers.add(players.get(i));
        }
        return resultPlayers;
    }
}
