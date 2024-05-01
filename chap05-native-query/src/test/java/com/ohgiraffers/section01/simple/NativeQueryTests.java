package com.ohgiraffers.section01.simple;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.List;
import java.util.stream.Stream;

import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.Persistence;
import jakarta.persistence.Query;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

public class NativeQueryTests {
	
    private static EntityManagerFactory entityManagerFactory;
    private EntityManager entityManager;

    @BeforeAll
    public static void initFactory() {
        entityManagerFactory = Persistence.createEntityManagerFactory("jpatest");
    }

    @BeforeEach
    public void initManager() {
        entityManager = entityManagerFactory.createEntityManager();
    }

    @AfterAll
    public static void closeFactory() {
        entityManagerFactory.close();
    }

    @AfterEach
    public void closeManager() {
        entityManager.close();
    }

    /* Native query란 SQL 쿼리를 그대로 사용하는 것을 말한다.
    이를 사용하면 ORM의 기능을 이용하면서 SQL 쿼리도 활용할 수 있어서 더욱 강력한 데이터베이스 접근이 가능하다.
    따라서 복잡한 쿼리를 작성할 때나, 특정 데이터베이스에서만 사용 가능한 기능을 사용해야 할 때 등에 Native query를 사용한다.
     * 네이티브 쿼리 API는 다음의 3가지가 있다.
     *   1. 결과 타입 정의
     *   public Query createNativeQuery(String sqlString, Class resultClass);
     *
     *   2. 결과 타입을 정의할 수 없을 때
     *   public Query createNativeQuery(String sqlString);
     *
     *   3. 결과 매핑 사용
     *   public Query createNativeQuery(String sqlString, String resultSetMapping);
     */
    
    /* 1. 결과 타입 정의
    * Menu 로 결과 타입을 정의한 네이티브 쿼리를 테스트 한다.
    * 유의할 점은 모든 컬럼값을 매핑하는 경우에만 타입을 특정할 수 있다.
    * 만약 일부 컬럼만 조회하고 싶은 경우는 Object[] 또는 스칼라 값을 별도로 담을 클래스를 정의해서 사용해야 한다.
    * */
    @Test
    public void 결과_타입을_정의한_네이티브_쿼리_사용_테스트() {

        //given
        int menuCodeParameter = 15;

        //when
        /* DBMS의 고유한 SQL문법을 작성한다. 위치 기반 파라미터로만 사용이 가능하다. */
        String query = "SELECT menu_code, menu_name, menu_price, category_code, orderable_status FROM tbl_menu WHERE menu_code = ?";

        /* 일부 컬럼만 조회하는 것은 불가능하다. */
        //String query = "SELECT menu_code, menu_name, menu_price FROM tbl_menu WHERE menu_code = ?";

        Query nativeQuery = entityManager.createNativeQuery(query, Menu.class).setParameter(1, menuCodeParameter);
        Menu foundMenu = (Menu) nativeQuery.getSingleResult();

        //then
        assertNotNull(foundMenu);
        //영속성 컨텍스트에서 관리하는 객체임을 알 수 있다.
        assertTrue(entityManager.contains(foundMenu));
        System.out.println(foundMenu);

    }

    /* 2. 결과 타입을 정의할 수 없는 경우 */
    @Test
    public void 결과_타입을_정의할_수_없는_경우_조회_테스트() {
    	
    	//when
    	String query = "SELECT menu_name, menu_price FROM tbl_menu";
    	List<Object[]> menuList = entityManager.createNativeQuery(query).getResultList();
    	
    	/* 결과 타입을 특정하는 것 자체가 불가능하다. 엔티티로 매핑 시킬 경우에만 클래스 타입을 입력한다.*/
//    	List<Object[]> menuList = entityManager.createNativeQuery(query, Object[].class).getResultList();
    	
    	//then
    	assertNotNull(menuList);
    	menuList.forEach(row -> {
    		Stream.of(row).forEach(col -> System.out.print(col + " "));
    		System.out.println();
    	});
    	
    }

    /* 3. 자동 결과 매핑 */
    @Test
    public void 자동_결과_매핑을_사용한_조회_테스트() {
    	
        //when
        String query = "SELECT\n"
                    + "       a.category_code, a.category_name, a.ref_category_code, COALESCE(v.menu_count, 0) menu_count\n"
                    + "  FROM tbl_category a\n"
                    + "  LEFT JOIN (SELECT COUNT(*) AS menu_count, b.category_code \n"
                    + "               FROM tbl_menu b\n"
                    + "              GROUP BY B.category_code) v ON (a.category_code = v.category_code)\n"
                    + " ORDER BY 1";
    	
    	Query nativeQuery = entityManager.createNativeQuery(query, "categoryCountAutoMapping");
    	List<Object[]> categoryList = nativeQuery.getResultList();
    	
    	//then
    	assertTrue(entityManager.contains(categoryList.get(0)[0]));
    	assertNotNull(categoryList);
    	categoryList.forEach(row -> {
    		Stream.of(row).forEach(col -> System.out.print(col + " "));
    		System.out.println();
    	});
    	
    }
    
    @Test
    public void 수동_결과_매핑을_사용한_조회_테스트() {
    	
        //when
        String query = "SELECT\n"
                    + "       a.category_code, a.category_name, a.ref_category_code, COALESCE(v.menu_count, 0) menu_count\n"
                    + "  FROM tbl_category a\n"
                    + "  LEFT JOIN (SELECT COUNT(*) AS menu_count, b.category_code \n"
                    + "               FROM tbl_menu b\n"
                    + "              GROUP BY b.category_code) v ON (a.category_code = v.category_code)\n"
                    + " ORDER BY 1";
    	
    	Query nativeQuery = entityManager.createNativeQuery(query, "categoryCountManualMapping");
    	List<Object[]> categoryList = nativeQuery.getResultList();
    	
    	//then
    	assertTrue(entityManager.contains(categoryList.get(0)[0]));
    	assertNotNull(categoryList);
    	categoryList.forEach(row -> {
    		Stream.of(row).forEach(col -> System.out.print(col + " "));
    		System.out.println();
    	});
    	
    }
    
    
    
    
    
    
    
    
    
    
    
    
    
    
    
    
    
    
    
    
    
    
    
    
    
    
    
    
    
    
    
    
    
    
    
    
    
    
    
    
    
    
    
    

}
