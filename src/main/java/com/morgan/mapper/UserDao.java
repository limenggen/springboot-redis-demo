package com.morgan.mapper;

import com.morgan.entity.User;
import org.apache.ibatis.annotations.*;

import java.util.List;

@Mapper
public interface UserDao {

    @Select("select * from users")
    List<User> queryAll();

    @Select("select * from users where uid = #{id}")
    User findUserById(int id);

    @Update("update users set username = #{user.userName},password = #{user.password},salary = #{user.salary} where uid = #{user.uid}")
    int updateUser(@Param("user") User user);

    @Delete("delete from users where uid = #{id}")
    int deleteUserById(int id);
}
