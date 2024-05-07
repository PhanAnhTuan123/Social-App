package com.shopme.admin.user;

import com.shopme.common.entity.Role;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.Rollback;

import java.util.List;

@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@Rollback(value = false)
public class RoleRepositoryTests {
    @Autowired
    private RoleRepository repository;

    @Test
    public void testCreateFirstRole(){
        Role roleAdmin = new Role("Admin","manager everything");
        Role savedRole = repository.save(roleAdmin);
        Assertions.assertThat(savedRole.getId()).isGreaterThan(0);
    }
    @Test
    public void testCreateRestRoles(){
        Role roleSalesperson = new Role("Salesperson","manage product price, customers, " +
                "shipping, orders and sales report");

        Role roleEditor = new Role("Editor","manage categories, brands, " +
                "products, articles and menus");
        Role roleShipper = new Role("Shipper","view products, view orders " +
                "and update order status");
        Role roleAssistant = new Role("Assistant","manage question and reviews");

        repository.saveAll(List.of(roleAssistant,roleEditor,roleShipper,roleSalesperson));


    }

}
