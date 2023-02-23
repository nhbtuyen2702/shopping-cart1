package com.shoppingcart.admin.category;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.List;
import java.util.Set;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase.Replace;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.data.domain.Sort;
import org.springframework.test.annotation.Rollback;

import com.shoppingcart.admin.entity.Category;

@DataJpaTest(showSql = false)
@AutoConfigureTestDatabase(replace = Replace.NONE)
@Rollback(false)
public class CategoryRepositoryTests {

	@Autowired
	private CategoryRepository repo;
	
	/*
	 Computer
	 	--Computer Components
		 	----CPU Processors Unit
		 	----Graphic Cards
		 	----Internal Hard Drives
		 	----Internal Optical Drives
		 	----Memory
		 	----Motherboard
		 	----Network Cards
		 	----Power Supplies
		 	----Solid State Drives
		 	----Sound Cards
		 --Desktops
		 --Laptops
		 --Tablets
	 Electronics
	 	--Camera & Photo
	 		----Bags & Cases
	 		----Digital Cameras
	 */
	
	/*
 	Computers
 		Desktops
 		Laptops
 			Laptops 1
 			Laptops 2
 				Laptops 21
 					Laptop 211
 			Laptops 3
 		Computer components
 			Memory a
 				a1
 					a2
 						a3
 							a4
 			Memory b
 				b1
 					b2
	*/ 					
	
	@Test
	public void testCreateRootCategory() {
		Category computers = new Category("Computers");//id=1
		
		Category desktops = new Category("Desktops", computers);
		
		Category laptops = new Category("Laptops", computers);
		Category laptop1 = new Category("Laptop 1", laptops);
		Category laptop2 = new Category("Laptop 2", laptops);
		Category laptop21 = new Category("Laptop 21", laptop2);
		Category laptop211 = new Category("Laptop 211", laptop21);
		Category laptop3 = new Category("Laptop 3", laptops);
		
		Category computerComponents = new Category("Computer components", computers);
		Category memoryA = new Category("Memory a", computerComponents);
		Category a1 = new Category("a1", memoryA);
		Category a2 = new Category("a2", a1);
		Category a3 = new Category("a3", a2);
		Category a4 = new Category("a4", a3);
		Category memoryB = new Category("Memory b", computerComponents);
		Category b1 = new Category("b1", memoryB);
		Category b2 = new Category("b2", b1);
		
		repo.saveAll(List.of(computers, desktops, laptops, laptop1, laptop2, laptop21, laptop211, laptop3,
				computerComponents, memoryA, a1, a2, a3, a4, memoryB, b1, b2));
		
	}
	
	@Test
	public void testCreateSubCategory() {
		Category parent = new Category(7);
		Category subCategory = new Category("iPhone", parent);
		Category savedCategory = repo.save(subCategory);
		
		assertThat(savedCategory.getId()).isGreaterThan(0);
	}
	
	@Test
	public void testGetCategory() {
		Category category = repo.findById(2).get();
		System.out.println(category.getName());
		
		Set<Category> children = category.getChildren();
		
		for (Category subCategory : children) {
			System.out.println(subCategory.getName());	
		}
		
		assertThat(children.size()).isGreaterThan(0);
	}
	
	@Test
	public void testPrintHierarchicalCategories() {
		Iterable<Category> categories = repo.findAll();
		
		for (Category category : categories) {
			if (category.getParent() == null) {
				System.out.println(category.getName());
				
				Set<Category> children = category.getChildren();
				
				for (Category subCategory : children) {
					System.out.println("--" + subCategory.getName());
					printChildren(subCategory, 1);
				}
			}
		}
	}
	 				
	private void printChildren(Category parent, int subLevel) {
		int newSubLevel = subLevel + 1;
		Set<Category> children = parent.getChildren();
		
		for (Category subCategory : children) {
			for (int i = 0; i < newSubLevel; i++) {				
				System.out.print("--");
			}
			
			System.out.println(subCategory.getName());
			
			printChildren(subCategory, newSubLevel);
		}		
	}
	
	@Test
	public void testListRootCategories() {
		List<Category> rootCategories = repo.findRootCategories(Sort.by("name").ascending());
		rootCategories.forEach(cat -> System.out.println(cat.getName()));
	}
	
	@Test
	public void testFindByName() {
		String name = "Computers";
		Category category = repo.findByName(name);
		
		assertThat(category).isNotNull();
		assertThat(category.getName()).isEqualTo(name);
	}
	
	
	@Test
	public void testFindByAlias() {
		String alias = "electronics";
		Category category = repo.findByAlias(alias);
		
		assertThat(category).isNotNull();
		assertThat(category.getAlias()).isEqualTo(alias);
	}
}
