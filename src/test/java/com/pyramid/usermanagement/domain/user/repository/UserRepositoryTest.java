package com.pyramid.usermanagement.domain.user.repository;

import com.pyramid.usermanagement.domain.user.model.AppUser;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest
class UserRepositoryTest {

    @Autowired
    private TestEntityManager entityManager;
    @Autowired
    private UserRepository userRepository;

    @Test
    void testSaveUser() {

        AppUser appUser = new AppUser();
        appUser.setLogin("Lukaku");
        appUser.setName("Lukaku momba");
        appUser.setEmail("lukaku@yahoo.fr");

        AppUser saveUser = userRepository.save(appUser);

        assertNotNull(saveUser.getId());
        assertEquals("Lukaku", saveUser.getLogin());
        assertEquals("lukaku@yahoo.fr", saveUser.getEmail());

    }

    @Test
    void testFindById() {

        AppUser appUser = new AppUser();
        appUser.setLogin("Lukaku");
        appUser.setName("Lukaku momba");
        appUser.setEmail("lukaku@yahoo.fr");

        AppUser user = entityManager.persistAndFlush(appUser);
        Optional<AppUser> foundUser = userRepository.findById(user.getId());

        assertTrue(foundUser.isPresent());
        assertEquals(user.getId(), foundUser.get().getId());
        assertEquals("lukaku@yahoo.fr", foundUser.get().getEmail());

    }

    @Test
    void testFindByEmail() {

        AppUser appUser = new AppUser();
        appUser.setLogin("Lukaku");
        appUser.setName("Lukaku momba");
        appUser.setEmail("lukaku@yahoo.fr");

        AppUser user = entityManager.persistAndFlush(appUser);
        Optional<AppUser> foundUser = userRepository.findByEmail("lukaku@yahoo.fr");

        assertTrue(foundUser.isPresent());
        assertEquals(user.getId(), foundUser.get().getId());
        assertEquals("lukaku@yahoo.fr", foundUser.get().getEmail());

    }

    @Test
    void testFindUserByEmailNotFound() {

        Optional<AppUser> foundUser = userRepository.findByEmail("misternew@yaoo.co");

        assertFalse(foundUser.isPresent());

    }

    @Test
    void testFindAll() {

        AppUser appUser = new AppUser();
        appUser.setLogin("Lukaku");
        appUser.setName("Lukaku momba");
        appUser.setEmail("lukaku@yahoo.fr");

        AppUser appUser1 = new AppUser();
        appUser1.setLogin("washington");
        appUser1.setName("washington barry");
        appUser1.setEmail("washington@yahoo.fr");

        AppUser appUser2 = new AppUser();
        appUser2.setLogin("duncan");
        appUser2.setName("duncan pitt");
        appUser2.setEmail("duncan@gmail.com");

        entityManager.persist(appUser1);
        entityManager.persist(appUser2);
        entityManager.persist(appUser);
        entityManager.flush();

        List<AppUser> userList = userRepository.findAll();

        assertNotNull(userList);

        assertNotEquals(0, userList.size());

    }

    @Test
    void testFindAllPaginated() {

        for (int i = 1; i <= 20; i++) {

            AppUser appUser = new AppUser();
            appUser.setLogin("Lukaku" + i);
            appUser.setName("Lukaku" + i + " momba");
            appUser.setEmail("lukaku" + i + "@yahoo.fr");

            entityManager.persist(appUser);
        }
        entityManager.flush();

        Pageable pageable = PageRequest.of(0, 5);
        Page<AppUser> userPage = userRepository.findAll(pageable);

        assertEquals(5, userPage.getContent().size());
        assertEquals(20, userPage.getTotalElements());
        assertEquals(4, userPage.getTotalPages());
        assertTrue(userPage.isFirst());
        assertFalse(userPage.isLast());

        pageable = PageRequest.of(1, 5);
        userPage = userRepository.findAll(pageable);

        assertEquals(5, userPage.getContent().size());
        assertFalse(userPage.isFirst());
        assertFalse(userPage.isLast());

    }

    @Test
    void testUpdateUser() {

        AppUser appUser1 = new AppUser();
        appUser1.setLogin("washington");
        appUser1.setName("washington barry");
        appUser1.setEmail("washington@yahoo.fr");

        AppUser persistedUser = entityManager.persistAndFlush(appUser1);

        persistedUser.setLogin("brown");
        persistedUser.setEmail("barry@roro.in");
        AppUser updatedUser = userRepository.save(persistedUser);

        assertEquals(persistedUser.getId(), updatedUser.getId());
        assertEquals("brown", updatedUser.getLogin());
        assertEquals("barry@roro.in", updatedUser.getEmail());

        AppUser retrieveUser = entityManager.find(AppUser.class, persistedUser.getId());
        assertEquals("brown", retrieveUser.getLogin());
        assertEquals("barry@roro.in", retrieveUser.getEmail());
    }
    
    @Test
    void testDeleteUser() {

        AppUser appUser1 = new AppUser();
        appUser1.setLogin("washington");
        appUser1.setName("washington barry");
        appUser1.setEmail("washington@yahoo.fr");

        AppUser persisterUser = entityManager.persistAndFlush(appUser1);
        Long userId = persisterUser.getId();

        userRepository.deleteById(userId);

        AppUser deletedUser = entityManager.find(AppUser.class, userId);
        assertNull(deletedUser);

    }
}