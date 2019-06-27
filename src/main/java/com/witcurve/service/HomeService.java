package com.witcurve.service;

import com.witcurve.service.dto.HomeDTO;

public interface HomeService {

    /***
     * get content for home screen for admin
     * @param userId
     * @param schoolInfoId
     * @return
     */
    HomeDTO getCurrentUserHomeContentForAdmin(Long userId, Long schoolInfoId);

    /***
     * get content for home screen for staff
     * @param userId
     * @param schoolInfoId
     * @return
     */
    HomeDTO getCurrentUserHomeContentForStaff(Long userId, Long schoolInfoId);
}
