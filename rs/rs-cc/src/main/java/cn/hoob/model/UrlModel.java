package cn.hoob.model;

import cn.hoob.utils.StringUtils;

import java.io.Serializable;

/***url模型对象
 * @author zhuqinhe**/
public class UrlModel implements Serializable {
    String contentId;
    String userId;

    /***/
    public void UrlModel(){

    }
    public String getContentId() {
        return contentId;
    }

    public void setContentId(String contentId) {
        this.contentId = contentId;
    }

    public String getUserId() {

        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }
    public UrlModel(String contentId, String userId) {
        this.contentId = contentId;
        this.userId = userId;
    }
    /***/
    public static  UrlModel getUrlModel(String url) {
        if (StringUtils.isEmpty(url)) {
            return null;
        }
        String ursl[]=url.split("\\|")[5].split("&");
        if(ursl==null||ursl.length<4){
            return null;
        }
        String tcontentid=ursl[1];
        String tuserId=ursl[4];
        if(StringUtils.isEmpty(tcontentid)||StringUtils.isEmpty(tuserId)
                ||!tcontentid.contains("contentid")||!tuserId.contains("userid")){
            return null;
        }
        String[] cids=tcontentid.split("=");
        String[] uids=tuserId.split("=");
        if(cids!=null&&cids.length==2&&uids!=null&&uids.length==2){
            return new UrlModel(cids[1],uids[1]);
        }
        return null;
    }

    @Override
    public String toString() {
        return "UrlModel{" +
                "contentId='" + contentId + '\'' +
                ", userId='" + userId + '\'' +
                '}';
    }
}
