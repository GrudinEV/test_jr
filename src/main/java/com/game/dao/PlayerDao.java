package com.game.dao;

import com.game.entity.Player;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.TypedQuery;
import java.util.List;

@Component
@Transactional
public class PlayerDao {
    EntityManagerFactory emf;

    @Autowired
    public PlayerDao(EntityManagerFactory emf) {
        this.emf = emf;
    }

    public List<Player> index() {
        EntityManager em = emf.createEntityManager();
        TypedQuery<Player> namedQuery = em.createNamedQuery("Player.getAll", Player.class);
        List<Player> players = namedQuery.getResultList();
        return players;
    }
}
