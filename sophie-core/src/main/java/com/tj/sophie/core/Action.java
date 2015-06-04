package com.tj.sophie.core; /**
 * Created by mbp on 6/2/15.
 */

/**
 * 标识消息Id.
 */
public final class Action {
    /**
     * 标识消息Id的类型.
     */
    private String category;

    /**
     * 表示消息Id的Id.
     */
    private String id;

    public String getCategory() {
        return category;
    }

    public String getId() {
        return id;
    }

    public Action(String category, String id) {
        if (category == null || category.trim().isEmpty()) {
            throw ExceptionHelper.ArgumentIsNullOrEmpty("category");
        }
        if (id == null || id.trim().isEmpty()) {
            throw ExceptionHelper.ArgumentIsNullOrEmpty("id");
        }
        this.category = category.trim();
        this.id = id.trim();
    }


    /**
     * 创建Action实例.
     *
     * @param category
     * @param id
     * @return 返回VerbId实例
     */
    public static Action create(String category, String id) {
        return new Action(category, id);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || this.getClass() != o.getClass()) {
            return false;
        }
        Action that = (Action) o;
        if (this.category != that.category) {
            return false;
        }
        if (this.id != that.id) {
            return false;
        }
        return true;
    }

    @Override
    public int hashCode() {
        int result = 255;
        result = 31 * result + (this.category != null ? this.category.hashCode() : 0);
        result = 31 * result + (this.id != null ? this.id.hashCode() : 0);
        return result;
    }
}
