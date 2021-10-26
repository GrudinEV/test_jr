package com.game.repository;

import com.game.controller.PlayerOrder;
import com.game.entity.Player;
import com.game.entity.Profession;
import com.game.entity.Race;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

public class CustomizedPlayerRepositoryImpl implements CustomizedPlayerRepository<Player> {
    @PersistenceContext
    private EntityManager em;

    @Override
    public List<Player> getPlayersWithFilter(Map<String, String> allRequestParams) {
        String order = allRequestParams.remove("order");
        order = order == null ? "id" : PlayerOrder.valueOf(order).getFieldName();
        CriteriaBuilder cb = em.getCriteriaBuilder();
        CriteriaQuery<Player> playerCriteria = cb.createQuery(Player.class);
        Root<Player> playerRoot = playerCriteria.from(Player.class);
        List<Predicate> predicateList = new ArrayList<>();

        String name = allRequestParams.get("name");
        if (name != null && name.length() > 0) {
            predicateList.add(cb.like(cb.lower(playerRoot.get("name")), "%" + name.toLowerCase() + "%"));
        }

        String title = allRequestParams.get("title");
        if (title != null && title.length() > 0) {
            predicateList.add(cb.like(cb.lower(playerRoot.get("title")), "%" + title.toLowerCase() + "%"));
        }

        String race = allRequestParams.get("race");
        if (race != null && race.length() > 0) {
            predicateList.add(cb.equal(playerRoot.get("race"), Race.valueOf(race.toUpperCase())));
        }

        String profession = allRequestParams.get("profession");
        if (profession != null && profession.length() > 0) {
            predicateList.add(cb.equal(playerRoot.get("profession"), Profession.valueOf(profession.toUpperCase())));
        }

        String after = allRequestParams.get("after");
        if (after != null && after.length() > 0) {
            predicateList.add(cb.greaterThanOrEqualTo(playerRoot.get("birthday"), new Date(Long.parseLong(after))));
        }

        String before = allRequestParams.get("before");
        if (before != null && before.length() > 0) {
            predicateList.add(cb.lessThanOrEqualTo(playerRoot.get("birthday"), new Date(Long.parseLong(before))));
        }

        String banned = allRequestParams.get("banned");
        if (banned != null && banned.length() > 0) {
            if (banned.equals("false")) {
                predicateList.add(cb.equal(playerRoot.get("banned"), false));
            } else {
                predicateList.add(cb.equal(playerRoot.get("banned"), true));
            }
        }

        String minExperience = allRequestParams.get("minExperience");
        if (minExperience != null && minExperience.length() > 0) {
            predicateList.add(cb.greaterThanOrEqualTo(playerRoot.get("experience"), Integer.parseInt(minExperience)));
        }

        String maxExperience = allRequestParams.get("maxExperience");
        if (maxExperience != null && maxExperience.length() > 0) {
            predicateList.add(cb.lessThanOrEqualTo(playerRoot.get("experience"), Integer.parseInt(maxExperience)));
        }

        String minLevel = allRequestParams.get("minLevel");
        if (minLevel != null && minLevel.length() > 0) {
            predicateList.add(cb.greaterThanOrEqualTo(playerRoot.get("level"), Integer.parseInt(minLevel)));
        }

        String maxLevel = allRequestParams.get("maxLevel");
        if (maxLevel != null && maxLevel.length() > 0) {
            predicateList.add(cb.lessThanOrEqualTo(playerRoot.get("level"), Integer.parseInt(maxLevel)));
        }

        Predicate[] predicates = predicateList.toArray(new Predicate[0]);
        playerCriteria.select(playerRoot).where(predicates).orderBy(cb.asc(playerRoot.get(order)));

        List<Player> list = em.createQuery(playerCriteria)
                .getResultList();
        return list;
    }
}
