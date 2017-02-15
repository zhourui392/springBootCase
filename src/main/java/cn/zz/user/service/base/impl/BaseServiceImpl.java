package cn.zz.user.service.base.impl;

import cn.zz.user.mapper.base.BaseMapper;
import cn.zz.user.service.base.BaseService;

public abstract class BaseServiceImpl<T> implements BaseService<T> {

	public abstract BaseMapper<T> getBaseMapper();

	public T getById(Long id) {
		return getBaseMapper().selectByPrimaryKey(id);
	}

	public T getById(Integer id) {
		return getBaseMapper().selectByPrimaryKey(id);
	}

	public T getById(String id) {
		return getBaseMapper().selectByPrimaryKey(id);
	}

	public Boolean update(T record) {
		return getBaseMapper().updateByPrimaryKeySelective(record) > -1;
	}

	public Boolean deleteById(Long id) {
		return getBaseMapper().deleteByPrimaryKey(id) > -1;
	}

	public Boolean deleteById(Integer id) {
		return getBaseMapper().deleteByPrimaryKey(id) > -1;
	}

	public Boolean deleteById(String id) {
		return getBaseMapper().deleteByPrimaryKey(id) > -1;
	}

	public Boolean add(T record) {
		return getBaseMapper().insertSelective(record) > 0;
	}
	
}