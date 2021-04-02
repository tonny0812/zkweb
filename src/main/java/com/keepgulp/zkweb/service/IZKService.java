package com.keepgulp.zkweb.service;

import com.keepgulp.zkweb.model.ZKNode;
import org.apache.curator.framework.CuratorFramework;

/**
 * @InterfaceName IZKService
 * @Description ZK service
 * @Author guodongqing
 * @Date 2020/12/21 19:05
 * @Version 1.0
 **/
public interface IZKService {


    /**
     * 检查zk是否可用
     *
     * @return
     */
    boolean isHealth(CuratorFramework client);

    /**
     * 检查路径是否存在
     *
     * @param path
     * @return
     */
    boolean checkExists(String path, CuratorFramework client);

    /**
     * 检查节点是否存在
     *
     * @param path
     * @param name
     * @return
     */
    boolean checkExists(String path, String name, CuratorFramework client);

    /**
     * 创建节点
     *
     * @param path
     * @param name
     * @param value
     */
    void createNode(String path, String name, String value, CuratorFramework client);

    /**
     * 获取zk节点
     *
     * @param path
     * @return
     */
    ZKNode getZKNode(String path, CuratorFramework client);
}
