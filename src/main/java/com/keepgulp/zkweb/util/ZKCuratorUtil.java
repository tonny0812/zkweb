package com.keepgulp.zkweb.util;

import lombok.extern.slf4j.Slf4j;
import org.apache.curator.framework.CuratorFramework;
import org.apache.curator.framework.CuratorFrameworkFactory;
import org.apache.curator.framework.api.CuratorListener;
import org.apache.curator.framework.imps.CuratorFrameworkState;
import org.apache.curator.retry.ExponentialBackoffRetry;
import org.apache.curator.utils.PathUtils;
import org.apache.curator.utils.ZKPaths;
import org.apache.zookeeper.CreateMode;
import org.apache.zookeeper.data.Stat;

import java.util.ArrayList;
import java.util.List;

/**
 * zk客户端工具类
 */
@Slf4j
public enum ZKCuratorUtil {
    INSTANCE;

    /**
     * 获取zk客户端
     *
     * @param servers
     * @param zkSessionTimeout
     * @param zkConnectionTimeout
     * @param zkBaseSleepTime
     * @param retryCount
     * @return
     */
    public CuratorFramework createZKConnection(String servers,
                                               Integer zkSessionTimeout,
                                               Integer zkConnectionTimeout,
                                               Integer zkBaseSleepTime,
                                               Integer retryCount) {
        return CuratorFrameworkFactory.builder()
                .connectString(servers)
                .sessionTimeoutMs(zkSessionTimeout)
                .connectionTimeoutMs(zkConnectionTimeout)
                .retryPolicy(new ExponentialBackoffRetry(zkBaseSleepTime, retryCount))
                .build();
    }

    /**
     * 获取zk客户端
     *
     * @param servers
     * @param namespace
     * @param zkSessionTimeout
     * @param zkConnectionTimeout
     * @param zkBaseSleepTime
     * @param retryCount
     * @return
     */
    public CuratorFramework createZKConnection(String servers,
                                               String namespace,
                                               Integer zkSessionTimeout,
                                               Integer zkConnectionTimeout,
                                               Integer zkBaseSleepTime,
                                               Integer retryCount) {
        return CuratorFrameworkFactory.builder()
                .connectString(servers)
                .sessionTimeoutMs(zkSessionTimeout)
                .connectionTimeoutMs(zkConnectionTimeout)
                .retryPolicy(new ExponentialBackoffRetry(zkBaseSleepTime, retryCount))
                .namespace(namespace)
                .build();
    }

    /**
     * 获取链接服务
     *
     * @param client
     * @return
     */
    public String getServers(CuratorFramework client) {
        if (null == client.getState()
                || CuratorFrameworkState.LATENT.equals(client.getState())
                || CuratorFrameworkState.STOPPED.equals(client.getState())) {
            client.start();
        }
        return client.getZookeeperClient().getCurrentConnectionString();
    }

    /**
     * 创建节点
     *
     * @param path
     * @param name
     * @param value
     * @param client
     * @throws Exception
     */
    public String createNodePersistent(String path, String name, String value, CuratorFramework client) throws Exception {
        if (null == client.getState()
                || CuratorFrameworkState.LATENT.equals(client.getState())
                || CuratorFrameworkState.STOPPED.equals(client.getState())) {
            client.start();
        }
        String nodePath = ZKPaths.makePath(path, name);
        log.info("Creating node: {}, with value: {}", nodePath, value);
        nodePath = PathUtils.validatePath(nodePath);
        return client.create().creatingParentContainersIfNeeded()//自动递归创建父节点
                .withMode(CreateMode.PERSISTENT).forPath(nodePath, null == value ? null : value.getBytes());
    }

    /**
     * 创建节点
     *
     * @param path
     * @param name
     * @param value
     * @param client
     * @return
     * @throws Exception
     */
    public String createNodePersistentSequential(String path, String name, String value, CuratorFramework client) throws Exception {
        if (null == client.getState()
                || CuratorFrameworkState.LATENT.equals(client.getState())
                || CuratorFrameworkState.STOPPED.equals(client.getState())) {
            client.start();
        }
        String nodePath = ZKPaths.makePath(path, name);
        log.info("Creating node: {}, with value: {}", nodePath, value);
        nodePath = PathUtils.validatePath(nodePath);
        return client.create().creatingParentContainersIfNeeded()
                .withMode(CreateMode.PERSISTENT_SEQUENTIAL).forPath(nodePath, null == value ? null : value.getBytes());
    }

    /**
     * 检查节点是否存在
     *
     * @param path
     * @param name
     * @param client
     * @return
     * @throws Exception
     */
    public boolean checkNodeExists(String path, String name, CuratorFramework client) throws Exception {
        if (null == client.getState()
                || CuratorFrameworkState.LATENT.equals(client.getState())
                || CuratorFrameworkState.STOPPED.equals(client.getState())) {
            client.start();
        }
        String nodePath = ZKPaths.makePath(path, name);
        Stat stat = client.checkExists().forPath(nodePath);
        log.info("Check node: {}, is exists: {}", nodePath, stat != null ? true : false);
        return stat != null ? true : false;
    }

    /**
     * 获取某个节点的所有子节点
     *
     * @param path
     * @param client
     * @return
     * @throws Exception
     */
    public List<String> getChildren(String path, CuratorFramework client) throws Exception {
        if (null == client.getState()
                || CuratorFrameworkState.LATENT.equals(client.getState())
                || CuratorFrameworkState.STOPPED.equals(client.getState())) {
            client.start();
        }
        return client.getChildren().forPath(path);
    }

    /**
     * 获取某个节点的所有子节点，全路径
     *
     * @param path
     * @param client
     * @return
     * @throws Exception
     */
    public List<String> getChildrenWithFullPath(String path, CuratorFramework client) throws Exception {
        if (null == client.getState()
                || CuratorFrameworkState.LATENT.equals(client.getState())
                || CuratorFrameworkState.STOPPED.equals(client.getState())) {
            client.start();
        }
        List<String> list = client.getChildren().forPath(path);
        List<String> fullPathList = new ArrayList<>();
        for (String name : list) {
            fullPathList.add(ZKPaths.makePath(path, name));
        }
        return fullPathList;
    }

    /**
     * 获取节点创建时间
     *
     * @param path
     * @param client
     * @return
     * @throws Exception
     */
    public Stat getStat(String path, CuratorFramework client) throws Exception {
        if (null == client.getState()
                || CuratorFrameworkState.LATENT.equals(client.getState())
                || CuratorFrameworkState.STOPPED.equals(client.getState())) {
            client.start();
        }
        return client.checkExists().forPath(path);
    }

    /**
     * 获取某个节点数据
     *
     * @param path
     * @param client
     * @return
     * @throws Exception
     */
    public String getNodeData(String path, CuratorFramework client) throws Exception {
        if (null == client.getState()
                || CuratorFrameworkState.LATENT.equals(client.getState())
                || CuratorFrameworkState.STOPPED.equals(client.getState())) {
            client.start();
        }
        byte[] data = client.getData().forPath(path);
        return null != data ? String.valueOf(data) : null;
    }

    /**
     * 设置某个节点数据
     *
     * @param path
     * @param value
     * @param client
     * @throws Exception
     */
    public void setNodeData(String path, String value, CuratorFramework client) throws Exception {
        if (null == client.getState()
                || CuratorFrameworkState.LATENT.equals(client.getState())
                || CuratorFrameworkState.STOPPED.equals(client.getState())) {
            client.start();
        }
        //创建监听器
        CuratorListener listener = (client1, event) -> log.info(event.getPath());
        //添加监听器
        client.getCuratorListenable().addListener(listener);
        client.setData().inBackground().forPath(path, null == value ? null : value.getBytes());
    }

    /**
     * 删除该节点
     *
     * @param path
     * @param client
     * @throws Exception
     */
    public void deleteNode(String path, CuratorFramework client) throws Exception {
        if (null == client.getState()
                || CuratorFrameworkState.LATENT.equals(client.getState())
                || CuratorFrameworkState.STOPPED.equals(client.getState())) {
            client.start();
        }
        ZKPaths.PathAndNode node = ZKPaths.getPathAndNode(path);
        boolean isExists = INSTANCE.checkNodeExists(node.getPath(), node.getNode(), client);
        if (isExists) {
            client.delete().forPath(path);
        }
    }

    /**
     * 级联删除子节点
     *
     * @param path
     * @param client
     * @throws Exception
     */
    public void deleteNodeAndChildren(String path, CuratorFramework client) throws Exception {
        if (null == client.getState()
                || CuratorFrameworkState.LATENT.equals(client.getState())
                || CuratorFrameworkState.STOPPED.equals(client.getState())) {
            client.start();
        }
        ZKPaths.PathAndNode node = ZKPaths.getPathAndNode(path);
        boolean isExists = INSTANCE.checkNodeExists(node.getPath(), node.getNode(), client);
        if (isExists) {
            client.delete().guaranteed().deletingChildrenIfNeeded().forPath(path);
        }
    }

}
