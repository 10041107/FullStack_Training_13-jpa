package com.ohgiraffers.springjpa.category.service;

import com.ohgiraffers.springjpa.category.entity.Category;
import com.ohgiraffers.springjpa.category.repository.CategoryRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Objects;

@Service
public class CategoryService {

    @Autowired
    private CategoryRepository categoryRepository;

    public Integer findByCategory(int categoryCode){

        /*if ((categoryCode < 0)){
            return null;   없으면 어차피 null
        }*/
/*        Category category = categoryRepository.findByCategoryCode(categoryCode);*/

        Category category = categoryRepository.findByCategoryCode(categoryCode);


        if (Objects.isNull(category.getCategoryCode())){
            return null;
        }


        return category.getCategoryCode();
    }


    public Category insertCategory(String categoryName) {

        // 이름 중복 여부
        Category foundCategory = categoryRepository.findByCategoryName(categoryName);

        if (!Objects.isNull(foundCategory)){
            return null;
        }

        Category insertCategory = new Category();
        insertCategory.setCategoryName(categoryName);

        Category result = categoryRepository.save(insertCategory);

       /* if(result){

        }*/
        return result;

    }
}
