package com.ohgiraffers.springjpa.menu.repository;

import com.ohgiraffers.springjpa.menu.entity.Menu;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface MenuRepository extends JpaRepository<Menu,Integer> {
    Menu findByMenuName(String name);  // 메소드 이름 기준으로 where 문을 만들어줌

    Menu findByMenuCode(Integer menuCode);
}
