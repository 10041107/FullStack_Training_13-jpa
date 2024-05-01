package com.ohgiraffer.springdatajpa.menu.service;

import java.util.List;
import java.util.stream.Collectors;

import com.ohgiraffer.springdatajpa.menu.dto.CategoryDTO;
import com.ohgiraffer.springdatajpa.menu.dto.MenuDTO;
import com.ohgiraffer.springdatajpa.menu.entity.Category;
import com.ohgiraffer.springdatajpa.menu.entity.Menu;
import com.ohgiraffer.springdatajpa.menu.repository.CategoryRepository;
import com.ohgiraffer.springdatajpa.menu.repository.MenuRepository;
import org.modelmapper.ModelMapper;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


@Service
public class MenuService {

	private final MenuRepository menuRepository;
	private final CategoryRepository categoryRepository;
	private final ModelMapper modelMapper;
	
	public MenuService(MenuRepository menuRepository, CategoryRepository categoryRepository, ModelMapper modelMapper) {
		this.menuRepository = menuRepository;
		this.categoryRepository = categoryRepository;
		this.modelMapper = modelMapper;
	}
	
	/* 1. findById */
	public MenuDTO findMenuByCode(int menuCode) {
		
		/* findById 메소드는 이미 구현되어 있으므로 인터페이스에 따로 정의할 필요가 없다.
		 * findById 의 반환값은 Optional 타입이다. Optional 타입은 NPE을 방지하기 위한 다양한 기능이 존재한다.
		 * 해당 id로 조회되지 못했을 경우 IllegalArgumentException을 발생시킨다. */
		Menu menu = menuRepository.findById(menuCode).orElseThrow(IllegalArgumentException::new);
		
		/* modelMapper를 이용하여 entity를 DTO로 변환해서 반환 */
		return modelMapper.map(menu, MenuDTO.class);
	}
	
	/* 2. findAll -> 페이징 처리 전 */
	public List<MenuDTO> findMenuList() {
		
		/* findAll 메소드는 이미 구현 되어 있으므로 인터페이스에 따로 정의할 필요가 없다. 
		 * Sort(정렬) 기준을 전달하며 조회할 수도 있다. */
		List<Menu> menuList = menuRepository.findAll(Sort.by("menuCode").descending());
		
		return menuList.stream().map(menu -> modelMapper.map(menu, MenuDTO.class)).collect(Collectors.toList());
	}

	/* 3. Page -> 페이징 처리 후 */
	public Page<MenuDTO> findMenuList(Pageable pageable) {
		
		/* page 파라미터가 Pageable의 number 값으로 넘어오는데 해당 값이 조회시에는 인덱스 기준이 되어야 해서 -1 처리가 필요하다. */
		pageable = PageRequest.of(pageable.getPageNumber() <= 0 ? 0 : pageable.getPageNumber() - 1,
					pageable.getPageSize(),
					Sort.by("menuCode").descending());
		
		Page<Menu> menuList = menuRepository.findAll(pageable);
		
		return menuList.map(menu -> modelMapper.map(menu, MenuDTO.class));
	}

	
	/* 4. QueryMethod */
	public List<MenuDTO> findByMenuPrice(Integer menuPrice) {
		
		//List<Menu> menuList = menuRepository.findByMenuPriceGreaterThan(menuPrice);
		//List<Menu> menuList = menuRepository.findByMenuPriceGreaterThanOrderByMenuPrice(menuPrice);
		List<Menu> menuList = menuRepository.findByMenuPriceGreaterThan(menuPrice, Sort.by("menuPrice").descending());
		
		return menuList.stream().map(menu -> modelMapper.map(menu, MenuDTO.class)).collect(Collectors.toList());
	}

	/* 5. JPQL or native query */
	public List<CategoryDTO> findAllCategory() {
		
		List<Category> categoryList = categoryRepository.findAllCategory();
		
		return categoryList.stream().map(category -> modelMapper.map(category, CategoryDTO.class)).collect(Collectors.toList());
	}

	/* 6. save */
	@Transactional
	public void registNewMenu(MenuDTO newMenu) {
		
		/* CrudRepository에 미리 정의 되어 있는 save() 메소드를 통해 하나의 엔티티를 저장할 수 있다. */
		menuRepository.save(modelMapper.map(newMenu, Menu.class));
		
	}

	/* 7. 수정하기 - 엔티티 조회 후 값 변경 */
	@Transactional
	public void modifyMenu(MenuDTO modifyMenu) {
		
		Menu foundMenu = menuRepository.findById(modifyMenu.getMenuCode()).orElseThrow(IllegalArgumentException::new);
		foundMenu.setMenuName(modifyMenu.getMenuName());
		
	}

	/* 8. delete */
	@Transactional
	public void deleteMenu(Integer menuCode) {
		
		menuRepository.deleteById(menuCode);
	
	}
	
	
}
