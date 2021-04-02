package com.keepgulp.zkweb.service;

import com.keepgulp.zkweb.model.ZKNode;
import com.keepgulp.zkweb.util.ZKCuratorUtil;
import org.apache.curator.framework.CuratorFramework;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

@RunWith(SpringRunner.class)
@SpringBootTest
public class ZKServiceTest {

    //会话超时时间
    private final int SESSION_TIMEOUT = 30 * 1000;

    //连接超时时间
    private final int CONNECTION_TIMEOUT = 3 * 1000;

    //重试次数
    private final int CONNECTION_RETRY = 3;

    //重试次数
    private final int BASE_SLEEP_TIME = 1000;

    //ZooKeeper服务地址
    private static final String SERVERS = "10.144.123.104:2181";

    //创建连接实例
    private CuratorFramework client = null;

    @Autowired
    private IZKService zkService;

    @Before
    public void setUp() throws Exception {
        //创建 CuratorFrameworkImpl实例
//        client = ZKCuratorUtil.INSTANCE.createZKConnection(SERVERS, NAMESPACE, SESSION_TIMEOUT, CONNECTION_TIMEOUT, BASE_SLEEP_TIME, CONNECTION_RETRY);
        client = ZKCuratorUtil.INSTANCE.createZKConnection(SERVERS, SESSION_TIMEOUT, CONNECTION_TIMEOUT, BASE_SLEEP_TIME, CONNECTION_RETRY);
        //启动
        client.start();
    }

    @After
    public void tearDown() throws Exception {
        client.close();
    }

    @Test
    public void testgetZKNode() throws Exception {
        ZKNode zkNode = zkService.getZKNode("/distributed_gid", client);
        System.out.println(zkNode);
    }
}