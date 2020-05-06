package com.itheima.health.job;

import com.itheima.health.constant.RedisConstant;
import com.itheima.health.utils.QiniuUtils;
import org.springframework.beans.factory.annotation.Autowired;
import redis.clients.jedis.JedisPool;

import java.util.Iterator;
import java.util.Set;

/**
 * @author ${dong}
 * @date 2020/1/3 20:00
 */
//定时任务 清理垃圾图片
public class ClearImgJob {
    //注入jedispool对象
    @Autowired
    private JedisPool jedisPool;

    public void clearImg(){
        //计算redis两个集合的差值，获取垃圾图片的名称

        Set<String> set=jedisPool.getResource().sdiff(RedisConstant.SETMEAL_PIC_DB_RESOURCES,RedisConstant.SETMEAL_PIC_RESOURCES);
        //获取set集合的迭代器
        Iterator<String> iterator = set.iterator();

        while (iterator.hasNext()) {
           String pic=iterator.next();
            System.out.println("删除的图片名称是"+pic);
            //删除图片服务器中的图片
            QiniuUtils.deleteFileFromQiniu(pic);
            //删除redis中的数据
            jedisPool.getResource().srem(RedisConstant.SETMEAL_PIC_RESOURCES,pic);
        }



    }


}
