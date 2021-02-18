package com.keepgulp.zkweb.util;

import com.keepgulp.zkweb.model.ZKNode;
import org.apache.curator.framework.CuratorFramework;
import org.apache.curator.utils.ZKPaths;
import org.apache.zookeeper.data.Stat;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.util.List;

import static org.junit.Assert.*;

public class ZKCuratorUtilTest {

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

    @Before
    public void setUp() throws Exception {
        //创建 CuratorFrameworkImpl实例
//        client = ZKCuratorUtil.INSTANCE.createZKConnection(SERVERS, NAMESPACE, SESSION_TIMEOUT, CONNECTION_TIMEOUT, BASE_SLEEP_TIME, CONNECTION_RETRY);
        client = ZKCuratorUtil.INSTANCE.createZKConnection(SERVERS, SESSION_TIMEOUT, CONNECTION_TIMEOUT, BASE_SLEEP_TIME, CONNECTION_RETRY);
        //启动
        client.start();
    }

    @After
    public void close() {
        client.close();
    }

    @Test
    public void getNodeData() throws Exception {

        String value = ZKCuratorUtil.INSTANCE.getNodeData("/", client);
        Stat stat = ZKCuratorUtil.INSTANCE.getStat("/", client);
        System.out.println("##### " + value + " @@@@@ " + stat.getCtime());
        List<String> list = ZKCuratorUtil.INSTANCE.getChildren("/", client);
        for (String s : list) {
            System.out.println(s);
        }
    }

    @Test
    public void testPath() {
        ZKPaths.PathAndNode node = ZKPaths.getPathAndNode("/郭东清");
        System.out.println(node.getPath());
        System.out.println(node.getNode());
    }

}