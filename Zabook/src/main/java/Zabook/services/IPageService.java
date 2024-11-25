package Zabook.services;

import Zabook.models.Page;

public interface IPageService {
    public void createFanPage(Page page);
    public void removeFanPage(Integer id);
    public void updateInformationFanPage(Page page);
    
}
