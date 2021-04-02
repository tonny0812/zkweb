package com.keepgulp.zkweb.model;

import lombok.Data;

import java.util.ArrayList;
import java.util.List;

/**
 * @ClassName ZKNode
 * @Description zk节点
 * @Author guodongqing
 * @Date 2020/12/21 17:13
 * @Version 1.0
 **/
@Data
public class ZKNode {

    long createTime;
    String path;
    String parentPath;
    String name;
    String value;
    List<ZKNode> childrenNodeList = new ArrayList<>();

}
