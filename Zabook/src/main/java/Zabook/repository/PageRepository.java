package Zabook.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import Zabook.models.Page;
import Zabook.models.User;

public interface PageRepository extends JpaRepository<Page, Integer> {

    List<Page> findByAdmin(User admin);
    
}
