package com.ohgiraffers.section02.named;

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

public class NamedNativeQueryTests {

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
	
	/* NamedNativeQuery
	 * 네이티브 SQL도 JPQL처럼 @NamedNativeQuery를 사용해 정적 SQL을 만들어두고 쓸 수 있다.
	 * */
	
	@Test
	public void NamedNativeQuery를_이용한_조회_테스트() {
		
		//when
		Query nativeQuery = entityManager.createNamedQuery("Category.menuCountOfCategory");
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
