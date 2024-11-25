package Zabook.services.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;

import Zabook.models.Page;
import Zabook.models.User;
import Zabook.repository.PageRepository;
import Zabook.services.IPageService;

public class PageService implements IPageService {

    @Autowired
    PageRepository pageRepository;

    @Override
    public void createFanPage(Page page) {
        pageRepository.save(page);
    }

    @Override
    public void removeFanPage(Integer id_FanPage) {
        pageRepository.deleteById(id_FanPage);
    }

    @Override
    public void updateInformationFanPage(Page page) {
        this.createFanPage(page);
    }

    public List<Page> findbyUserAdmin(User admin){
        return pageRepository.findByAdmin(admin);
    }
}
