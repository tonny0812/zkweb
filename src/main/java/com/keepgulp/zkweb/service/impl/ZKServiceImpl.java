package com.keepgulp.zkweb.service.impl;

import com.keepgulp.zkweb.model.ZKNode;
import com.keepgulp.zkweb.service.IZKService;
import com.keepgulp.zkweb.util.ZKCuratorUtil;
import lombok.extern.slf4j.Slf4j;
import org.apache.curator.framework.CuratorFramework;
import org.apache.curator.framework.imps.CuratorFrameworkState;
import org.apache.curator.utils.ZKPaths;
import org.apache.zookeeper.data.Stat;
import org.springframework.stereotype.Service;
import org.thymeleaf.util.StringUtils;

import java.util.ArrayList;
import java.util.List;

/**
 * @ClassName ZKServiceImpl
 * @Description TODO
 * @Author guodongqing
 * @Date 2020/12/26 16:56
 * @Version 1.0
 **/
@Slf4j
@Service
public class ZKServiceImpl implements IZKService {

    @Override
    public boolean isHealth(CuratorFramework client) {
        return false;
    }

    @Override
    public boolean checkExists(String path, CuratorFramework client) {
        return false;
    }

    @Override
    public boolean checkExists(String path, String name, CuratorFramework client) {
        return false;
    }

    @Override
    public void createNode(String path, String name, String value, CuratorFramework client) {

    }

    @Override
    public ZKNode getZKNode(String path, CuratorFramework client) {
        log.debug(path);
        if (null == client.getState()
                || CuratorFrameworkState.LATENT.equals(client.getState())
                || CuratorFrameworkState.STOPPED.equals(client.getState())) {
            client.start();
        }
        ZKNode zkNode = new ZKNode();
        String vaule = null;
        try {
            vaule = ZKCuratorUtil.INSTANCE.getNodeData(path, client);
        } catch (Exception e) {
            log.error("获取节点值出错：{}，{}",path,e.getMessage());
        }
        Stat stat = null;
        try {
            stat = ZKCuratorUtil.INSTANCE.getStat(path, client);
        } catch (Exception e) {
            log.error("获取状态出错：{}，{}",path,e.getMessage());
        }

        List<String> childrenPathList = null;
        try {
            childrenPathList = ZKCuratorUtil.INSTANCE.getChildrenWithFullPath(path, client);
        } catch (Exception e) {
            log.error("获取子节点全路径出错：{}，{}",path,e.getMessage());
        }
        ZKPaths.PathAndNode node = ZKPaths.getPathAndNode(path);
        zkNode.setPath(path);
        zkNode.setParentPath(node.getPath());
        zkNode.setName(node.getNode());
        zkNode.setValue(vaule);
        if (null != stat) {
            zkNode.setCreateTime(stat.getCtime());
        }
        List<ZKNode> childrenNodeList = new ArrayList<>();
        if (null != childrenPathList && childrenPathList.size() > 0) {
            for (String cPath : childrenPathList) {
                if (!StringUtils.isEmpty(cPath)) {
                    ZKNode zkCNode = getZKNode(cPath, client);
                    childrenNodeList.add(zkCNode);
                }
            }
        }
        zkNode.setChildrenNodeList(childrenNodeList);
        return zkNode;
    }
}
