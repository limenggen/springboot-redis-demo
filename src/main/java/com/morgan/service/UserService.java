package com.morgan.service;

import com.morgan.entity.User;
import com.morgan.mapper.UserDao;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.concurrent.TimeUnit;

@Service
public class UserService {
    @Autowired
    private UserDao userDao;

    @Autowired
    private RedisTemplate redisTemplate;

    public List<User> queryAll(){
        return userDao.queryAll();
    }

    /**
     * 获取用户测流，先从缓存中获取用户，没有则去数据表中数据，再将数据写入缓存
     */
    public User findUserById(int id){
        String key = "user_" + id;
        ValueOperations<String, User> operations = redisTemplate.opsForValue();

        // 判断redis中是否有键位key的缓存
        Boolean hasKey = redisTemplate.hasKey(key);

        if(hasKey){
            User user = operations.get(key);
            System.out.println("从缓存中获得数据：" + user.getUserName());
            return user;
        }else{
            User user = userDao.findUserById(id);
            System.out.println("查询数据库获得数据："+user.getUserName());
            operations.set(key, user, 5, TimeUnit.HOURS);
            return user;
        }
    }

    /**
     * 更新用户策略：先更新数据表，成功之后，删除原来的缓存，再更新缓存
     */
    public int updateUser(User user){
        ValueOperations<String, User> operations = redisTemplate.opsForValue();
        System.out.println("User:" + user.toString());
        int result = userDao.updateUser(user);
        System.out.println("result："+ result);
        if(result != 0){
            String key = "user_" + user.getUid();
            Boolean hasKey = redisTemplate.hasKey(key);
            if(hasKey){
                redisTemplate.delete(key);
                System.out.println("删除缓存中的key---" + key);
            }
            // 再将更新后的数据加入缓存
            User userById = userDao.findUserById(user.getUid());
            if(userById != null)
                operations.set(key, userById, 3, TimeUnit.HOURS);
        }
        return result;
    }

    /**
     * 删除用户策略：删除数据表中数据，然后删除缓存
     */
    public int deleteUserById(int id){
        int result = userDao.deleteUserById(id);
        String key = "user_" + id;
        if( result != 0){
            Boolean hasKey = redisTemplate.hasKey(id);
            if(hasKey){
                redisTemplate.delete(key);
                System.out.println("删除缓存中的key" + key);
            }
        }
        return result;
    }
}
