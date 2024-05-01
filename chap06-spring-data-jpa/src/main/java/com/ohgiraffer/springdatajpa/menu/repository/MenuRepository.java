package com.ohgiraffer.springdatajpa.menu.repository;

import java.util.List;
import com.ohgiraffer.springdatajpa.menu.entity.Menu;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.JpaRepository;

public interface MenuRepository extends JpaRepository<Menu, Integer> {
	
	/* JpaRepository를 상속 받아서 사용하는 메소드 외의 메소드는 직접 정의한다. */
	
	/* 전달 받은 가격을 초과하는 메뉴의 목록을 조회하는 메소드 */
	List<Menu> findByMenuPriceGreaterThan(Integer menuPrice);
	
	/* 전달 받은 가격을 초과하는 메뉴의 목록을 가격 순으로 조회하는 메소드 */
	List<Menu> findByMenuPriceGreaterThanOrderByMenuPrice(Integer menuPrice);
	
	/* 전달 받은 가격을 초과하는 메뉴의 목록을 전달 받는 정렬 기준으로 조회하는 메소드 */
	List<Menu> findByMenuPriceGreaterThan(Integer menuPrice, Sort sort);
	
	
	
}
