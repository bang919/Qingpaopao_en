package com.wopin.qingpaopao.bean.response;

import java.util.List;

public class ProductBanner {

    /**
     * id : 16
     * name : 以旧换新
     * slug : exchange
     * parent : 0
     * description : https://wifi.h2popo.com/wp-content/uploads/2018/07/exchangePage1.png;
     https://wifi.h2popo.com/wp-content/uploads/2018/07/C款主图-2.jpg

     * display : default
     * image : {"id":103,"date_created":"2018-07-23T02:08:49","date_created_gmt":"2018-07-23T02:08:49","date_modified":"2018-07-23T02:08:49","date_modified_gmt":"2018-07-23T02:08:49","src":"https://wifi.h2popo.com/wp-content/uploads/2018/07/exchangePage1.png","title":"exchangePage1","alt":""}
     * menu_order : 0
     * count : 3
     * _links : {"self":[{"href":"https://wifi.h2popo.com/wp-json/wc/v2/products/categories/16"}],"collection":[{"href":"https://wifi.h2popo.com/wp-json/wc/v2/products/categories"}]}
     */

    private int id;
    private String name;
    private String slug;
    private int parent;
    private String description;
    private String display;
    private ImageBean image;
    private int menu_order;
    private int count;
    private LinksBean _links;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getSlug() {
        return slug;
    }

    public void setSlug(String slug) {
        this.slug = slug;
    }

    public int getParent() {
        return parent;
    }

    public void setParent(int parent) {
        this.parent = parent;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getDisplay() {
        return display;
    }

    public void setDisplay(String display) {
        this.display = display;
    }

    public ImageBean getImage() {
        return image;
    }

    public void setImage(ImageBean image) {
        this.image = image;
    }

    public int getMenu_order() {
        return menu_order;
    }

    public void setMenu_order(int menu_order) {
        this.menu_order = menu_order;
    }

    public int getCount() {
        return count;
    }

    public void setCount(int count) {
        this.count = count;
    }

    public LinksBean get_links() {
        return _links;
    }

    public void set_links(LinksBean _links) {
        this._links = _links;
    }

    public static class ImageBean {
        /**
         * id : 103
         * date_created : 2018-07-23T02:08:49
         * date_created_gmt : 2018-07-23T02:08:49
         * date_modified : 2018-07-23T02:08:49
         * date_modified_gmt : 2018-07-23T02:08:49
         * src : https://wifi.h2popo.com/wp-content/uploads/2018/07/exchangePage1.png
         * title : exchangePage1
         * alt :
         */

        private int id;
        private String date_created;
        private String date_created_gmt;
        private String date_modified;
        private String date_modified_gmt;
        private String src;
        private String title;
        private String alt;

        public int getId() {
            return id;
        }

        public void setId(int id) {
            this.id = id;
        }

        public String getDate_created() {
            return date_created;
        }

        public void setDate_created(String date_created) {
            this.date_created = date_created;
        }

        public String getDate_created_gmt() {
            return date_created_gmt;
        }

        public void setDate_created_gmt(String date_created_gmt) {
            this.date_created_gmt = date_created_gmt;
        }

        public String getDate_modified() {
            return date_modified;
        }

        public void setDate_modified(String date_modified) {
            this.date_modified = date_modified;
        }

        public String getDate_modified_gmt() {
            return date_modified_gmt;
        }

        public void setDate_modified_gmt(String date_modified_gmt) {
            this.date_modified_gmt = date_modified_gmt;
        }

        public String getSrc() {
            return src;
        }

        public void setSrc(String src) {
            this.src = src;
        }

        public String getTitle() {
            return title;
        }

        public void setTitle(String title) {
            this.title = title;
        }

        public String getAlt() {
            return alt;
        }

        public void setAlt(String alt) {
            this.alt = alt;
        }
    }

    public static class LinksBean {
        private List<SelfBean> self;
        private List<CollectionBean> collection;

        public List<SelfBean> getSelf() {
            return self;
        }

        public void setSelf(List<SelfBean> self) {
            this.self = self;
        }

        public List<CollectionBean> getCollection() {
            return collection;
        }

        public void setCollection(List<CollectionBean> collection) {
            this.collection = collection;
        }

        public static class SelfBean {
            /**
             * href : https://wifi.h2popo.com/wp-json/wc/v2/products/categories/16
             */

            private String href;

            public String getHref() {
                return href;
            }

            public void setHref(String href) {
                this.href = href;
            }
        }

        public static class CollectionBean {
            /**
             * href : https://wifi.h2popo.com/wp-json/wc/v2/products/categories
             */

            private String href;

            public String getHref() {
                return href;
            }

            public void setHref(String href) {
                this.href = href;
            }
        }
    }
}
