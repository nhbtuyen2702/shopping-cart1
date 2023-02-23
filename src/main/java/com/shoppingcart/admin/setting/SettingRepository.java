package com.shoppingcart.admin.setting;

import java.util.List;

import org.springframework.data.repository.CrudRepository;

import com.shoppingcart.admin.entity.Setting;
import com.shoppingcart.admin.entity.SettingCategory;

public interface SettingRepository extends CrudRepository<Setting, String> {
	public List<Setting> findByCategory(SettingCategory category);
}
