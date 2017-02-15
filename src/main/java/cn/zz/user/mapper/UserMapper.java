package cn.zz.user.mapper;

import cn.zz.user.common.util.page.PageQuery;
import cn.zz.user.entity.User;
import cn.zz.user.mapper.base.BaseUserMapper;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface UserMapper extends BaseUserMapper {

	User getUserByUsernameAndPassword(@Param("username") String username,
									  @Param("password") String password);

	User getUserByUsername(@Param("username") String username);


	List<User> getListByConditions(@Param("pageQuery") PageQuery pageQuery);

	int getCountByConditions();
}
