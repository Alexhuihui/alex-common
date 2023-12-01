package top.alexmmd.common.base.tree;

import io.swagger.v3.oas.annotations.media.Schema;
import java.util.ArrayList;
import java.util.List;

@Schema(description = "树节点")
public class TreeNode<T> {

    @Schema(description = "编码或ID")
    protected Object id;

    @Schema(description = "名称")
    protected String name;

    @Schema(description = "父编码或ID")
    protected Object parentId;

    @Schema(description = "子节点")
    protected List<T> children = new ArrayList<T>();

    public List<T> getChildren() {
        return children;
    }

    public void setChildren(List<T> children) {
        this.children = children;
    }


    public Object getId() {
        return id;
    }

    public void setId(Object id) {
        this.id = id;
    }

    public Object getParentId() {
        return parentId;
    }

    public void setParentId(Object parentId) {
        this.parentId = parentId;
    }

    public void add(T node) {
        children.add(node);
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }


}
